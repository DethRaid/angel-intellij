package com.sarahshandcraftedentertainment.angelintellij.lsp.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.redhat.devtools.lsp4ij.commands.LSPCommand;
import com.redhat.devtools.lsp4ij.commands.LSPCommandAction;
import org.jetbrains.annotations.NotNull;

public class TriggerParameterHintsAction extends LSPCommandAction {
    @Override
    protected void commandPerformed(@NotNull LSPCommand lspCommand, @NotNull AnActionEvent event) {
        System.out.println(lspCommand);

        // TODO
    }
}
