package com.sarahshandcraftedentertainment.angelintellij.psi;

import com.sarahshandcraftedentertainment.angelintellij.language.AngelScriptLanguage;
import com.intellij.psi.tree.IElementType;

public class AngelScriptTokenType extends IElementType {
    public AngelScriptTokenType(String debugName) {
        super(debugName, AngelScriptLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "AngelScriptTokenType." + super.toString();
    }
}
