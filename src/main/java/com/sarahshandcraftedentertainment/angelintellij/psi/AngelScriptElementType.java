package com.sarahshandcraftedentertainment.angelintellij.psi;

import com.sarahshandcraftedentertainment.angelintellij.language.AngelScriptLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class AngelScriptElementType extends IElementType {
    public AngelScriptElementType(@NotNull @NonNls String debugName) {
        super(debugName, AngelScriptLanguage.INSTANCE);
    }
}
