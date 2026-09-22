package com.sarahshandcraftedentertainment.angelintellij.language;

import com.intellij.lang.Language;

public class AngelScriptPredefinedLanguage extends Language {
    public static final AngelScriptPredefinedLanguage INSTANCE = new AngelScriptPredefinedLanguage();

    private AngelScriptPredefinedLanguage() {
        super("angelscript-predefined");
    }
}
