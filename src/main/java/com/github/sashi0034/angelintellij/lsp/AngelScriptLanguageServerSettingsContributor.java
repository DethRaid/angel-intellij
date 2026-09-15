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
                      "angelscript.language-server.ls-path": {
                        "type": "string",
                        "title": "Language Server: Path",
                        "description": "Path to the language server executable. You won\\u0027t need this in most cases, set this only when needing a specific version of the language server"
                      },
                      "angelscript.language-server.runtime": {
                        "type": "string",
                        "title": "Language Server: Runtime",
                        "description": "Path to the node executable used to execute the language server. You won\\u0027t need this in most cases"
                      },
                      "angelscript.trace.server": {
                        "type": "string",
                        "enum": [
                          "off",
                          "messages",
                          "verbose"
                        ],
                        "default": "off",
                        "description": "Traces the communication between VS Code and the language server."
                      },
                      "angelscript.content-intellisense": {
                        "type": "boolean",
                        "default": false,
                        "description": "Enable experimental support for content collection intellisense inside Markdown, MDX and Markdoc. Note that this require also enabling the feature in your angelscript config (experimental.contentCollectionIntellisense) (angelscript 4.14+)"
                      },
                      "angelscript.updateImportsOnFileMove.enabled": {
                        "type": "boolean",
                        "default": false,
                        "description": "Controls whether the extension updates imports when a file is moved to a new location. In most cases, you\\u0027ll want to keep this disabled as TypeScript and the angelscript TypeScript plugin already handles this for you. Having multiple tools updating imports at the same time can lead to corrupted files."
                      }
                    }
                  }""";
        }
    }
}
