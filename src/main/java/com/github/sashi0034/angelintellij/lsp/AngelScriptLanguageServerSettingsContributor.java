package com.github.sashi0034.angelintellij.lsp;

import com.redhat.devtools.lsp4ij.settings.contributors.LanguageServerSettingsContributorBase;
import com.redhat.devtools.lsp4ij.settings.contributors.ServerConfigurationContributor;

public class AngelScriptLanguageServerSettingsContributor extends LanguageServerSettingsContributorBase {
    public AngelScriptLanguageServerSettingsContributor() {
        super.setServerConfigurationContributor(new AngelScriptServerConfigurationContributor());
    }

    private static class AngelScriptServerConfigurationContributor implements ServerConfigurationContributor {

        @Override
        public String getDefaultConfigurationContent() {
            // language=json
            return """
                    {
                      "angelscript.trace.server": "off",
                      "angelscript.content-intellisense": false,
                      "angelscript.updateImportsOnFileMove.enabled": false
                    }""";
        }

        @Override
        public String getDefaultConfigurationSchemaContent() {
            // sheme generate by hand from `server/src/core/settings.js` in the language server
            // language=json
            return """                  
                  {
                    "$schema": "http://json-schema.org/draft-07/schema#",
                    "$id": "LSP4IJ/angelscript/settings.schema.json",
                    "title": "LSP4IJ angelscript server settings JSON schema",
                    "description": "JSON schema for angelscript server settings.",
                    "type": "object",
                    "additionalProperties": false,
                    "properties": {
                        "angelscript.suppressAnalyzerErrors": {
                            "type": "boolean",
                            "title": "AngelScript: Suppress analyzer errors"
                        },
                        "angelscript.includePath": {
                            "type": "string[]",
                            "title": "AngelScript: additional include paths",
                            "description": "places to search for files to include"
                        },
                        "angelscript.forceIncludePredefined": {
                            "type": "string[]",
                            "title": "AngelScript: files to force-include",
                            "description": "list of files to be included before compiling any other file"
                        },
                        "angelscript.implicitMutualInclusion": {
                            "type": "boolean",
                            "default": "false",
                            "description": "Tells the language server to combine all your files into one big file before compiling"
                        },
                      "angelscript.default-string-type": {
                        "type": "string",
                        "title": "AngelScript: Default string type",
                        "description": "Which type to use for strings. Should probably be `string` or `String`"
                      },
                      "angelscript.builtin-array-type": {
                        "type": "string",
                        "title": "AngelScript: Default array type",
                        "description": "Which type to use for builtin arrays"
                      },
                    }
                  }""";
        }
    }
}
