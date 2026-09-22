package com.sarahshandcraftedentertainment.angelintellij.language;

import com.intellij.lexer.FlexAdapter;

public class AngelScriptLexerAdapter extends FlexAdapter {
    public AngelScriptLexerAdapter() {
        super(new AngelScriptLexer(null));
    }
}
