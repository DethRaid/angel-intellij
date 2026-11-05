package com.sarahshandcraftedentertainment.angelintellij.lsp;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.impl.DocumentMarkupModel;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.Alarm;
import com.redhat.devtools.lsp4ij.LSPFileSupport;
import com.redhat.devtools.lsp4ij.LanguageServersRegistry;
import com.redhat.devtools.lsp4ij.features.semanticTokens.SemanticTokensData;
import com.sarahshandcraftedentertainment.angelintellij.language.AngelScriptFileType;
import com.sarahshandcraftedentertainment.angelintellij.language.AngelScriptPredefinedFileType;
import org.eclipse.lsp4j.SemanticTokensParams;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

// Rider does not paint semantic tokens through the LSP4IJ highlight visitor for this language,
// so they are requested here and added to the document markup directly.
@Service(Service.Level.PROJECT)
public final class AngelScriptSemanticTokensApplier implements Disposable {
    private static final Key<List<RangeHighlighter>> HIGHLIGHTERS = Key.create("angelscript.semanticTokens.highlighters");
    private static final int DELAY_MS = 300;

    private final Project project;
    private final Alarm alarm;
    private final Set<Document> pending = ConcurrentHashMap.newKeySet();

    public AngelScriptSemanticTokensApplier(@NotNull Project project) {
        this.project = project;
        this.alarm = new Alarm(Alarm.ThreadToUse.POOLED_THREAD, this);

        EditorFactory.getInstance().getEventMulticaster().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                schedule(event.getDocument());
            }
        }, this);

        project.getMessageBus().connect(this).subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,
                new FileEditorManagerListener() {
                    @Override
                    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                        schedule(FileDocumentManager.getInstance().getDocument(file));
                    }
                });
    }

    public static AngelScriptSemanticTokensApplier getInstance(@NotNull Project project) {
        return project.getService(AngelScriptSemanticTokensApplier.class);
    }

    public void scheduleOpenFiles() {
        for (VirtualFile file : FileEditorManager.getInstance(project).getOpenFiles()) {
            schedule(FileDocumentManager.getInstance().getDocument(file));
        }
    }

    private void schedule(@Nullable Document document) {
        if (document == null || project.isDisposed()) {
            return;
        }
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
        if (file == null || !isAngelScript(file)) {
            return;
        }
        pending.add(document);
        alarm.cancelAllRequests();
        alarm.addRequest(this::requestPending, DELAY_MS);
    }

    private static boolean isAngelScript(@NotNull VirtualFile file) {
        var type = file.getFileType();
        return type == AngelScriptFileType.INSTANCE || type == AngelScriptPredefinedFileType.INSTANCE;
    }

    private void requestPending() {
        for (Document document : pending) {
            pending.remove(document);
            request(document);
        }
    }

    private record Request(PsiFile file, long stamp, CompletableFuture<SemanticTokensData> tokens) {
    }

    private void request(@NotNull Document document) {
        if (project.isDisposed()) {
            return;
        }
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        Request request = ReadAction.compute(() -> {
            if (!psiDocumentManager.isCommitted(document)) {
                return null;
            }
            PsiFile file = psiDocumentManager.getPsiFile(document);
            if (file == null || !LanguageServersRegistry.getInstance().isFileSupported(file)) {
                return null;
            }
            var params = new SemanticTokensParams(new TextDocumentIdentifier());
            var tokens = LSPFileSupport.getSupport(file).getSemanticTokensSupport().getSemanticTokens(params);
            return new Request(file, document.getModificationStamp(), tokens);
        });
        if (request == null) {
            if (!psiDocumentManager.isCommitted(document)) {
                schedule(document);
            }
            return;
        }
        request.tokens().thenAccept(data -> {
            if (data != null) {
                ApplicationManager.getApplication().invokeLater(() -> apply(document, request, data), project.getDisposed());
            }
        });
    }

    private void apply(@NotNull Document document, @NotNull Request request, @NotNull SemanticTokensData data) {
        if (document.getModificationStamp() != request.stamp() || !request.file().isValid()) {
            return;
        }
        MarkupModel markup = DocumentMarkupModel.forDocument(document, project, true);
        List<RangeHighlighter> previous = document.getUserData(HIGHLIGHTERS);
        if (previous != null) {
            for (RangeHighlighter highlighter : previous) {
                if (highlighter.isValid()) {
                    markup.removeHighlighter(highlighter);
                }
            }
        }
        List<RangeHighlighter> highlighters = new ArrayList<>();
        data.highlight(request.file(), document, (start, end, key) ->
                highlighters.add(markup.addRangeHighlighter(key, start, end,
                        HighlighterLayer.ADDITIONAL_SYNTAX, HighlighterTargetArea.EXACT_RANGE)));
        document.putUserData(HIGHLIGHTERS, highlighters);
    }

    @Override
    public void dispose() {
    }
}
