package com.sarahshandcraftedentertainment.angelintellij.lsp;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.util.PlatformUtils;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AngelScriptSemanticTokensStartupActivity implements ProjectActivity {
    @Override
    public @Nullable Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        if (PlatformUtils.isRider()) {
            AngelScriptSemanticTokensApplier.getInstance(project).scheduleOpenFiles();
        }
        return null;
    }
}
