package com.sarahshandcraftedentertainment.angelintellij.lsp;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiFile;
import com.redhat.devtools.lsp4ij.features.semanticTokens.SemanticTokensColorsProvider;
import com.redhat.devtools.lsp4ij.features.semanticTokens.SemanticTokensHighlightingColors;
import org.eclipse.lsp4j.SemanticTokenModifiers;
import org.eclipse.lsp4j.SemanticTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// Reference: https://github.com/redhat-developer/lsp4ij/blob/main/src/main/java/com/redhat/devtools/lsp4ij/features/semanticTokens/DefaultSemanticTokensColorsProvider.java

public class AngelScriptSemanticTokensColorProvider implements SemanticTokensColorsProvider {

    // Rider's C++ colors, used when the current color scheme defines them
    private static final String CPP_NAMESPACE = "ReSharper.CPP_NAMESPACE_IDENTIFIER";
    private static final String CPP_CLASS = "ReSharper.CPP_CLASS_IDENTIFIER";
    private static final String CPP_STRUCT = "ReSharper.CPP_STRUCT_IDENTIFIER";
    private static final String CPP_TYPEDEF = "ReSharper.CPP_TYPEDEF_IDENTIFIER";
    private static final String CPP_TEMPLATE_PARAMETER = "ReSharper.CPP_TEMPLATE_PARAMETER_IDENTIFIER";
    private static final String CPP_ENUM = "ReSharper.CPP_ENUM_IDENTIFIER";
    private static final String CPP_ENUMERATOR = "ReSharper.CPP_ENUM_ENUMERATOR_IDENTIFIER";
    private static final String CPP_GLOBAL_FUNCTION = "ReSharper.CPP_GLOBAL_FUNCTION_IDENTIFIER";
    private static final String CPP_MEMBER_FUNCTION = "ReSharper.CPP_MEMBER_FUNCTION_IDENTIFIER";
    private static final String CPP_STATIC_MEMBER_FUNCTION = "ReSharper.CPP_STATIC_MEMBER_FUNCTION_IDENTIFIER";
    private static final String CPP_CLASS_FIELD = "ReSharper.CPP_CLASS_FIELD_IDENTIFIER";
    private static final String CPP_STATIC_FIELD = "ReSharper.CPP_STATIC_FIELD_IDENTIFIER";
    private static final String CPP_CONSTANT = "ReSharper.CPP_CONSTANT_IDENTIFIER";
    private static final String CPP_LOCAL_VARIABLE = "ReSharper.CPP_LOCAL_VARIABLE_IDENTIFIER";
    private static final String CPP_GLOBAL_VARIABLE = "ReSharper.CPP_GLOBAL_VARIABLE_IDENTIFIER";
    private static final String CPP_PARAMETER = "ReSharper.CPP_PARAMETER_VARIABLE_IDENTIFIER";
    private static final String CPP_MACRO = "ReSharper.CPP_PREPROCESSOR_MACRO_IDENTIFIER";
    private static final String CPP_CONTROL_FLOW_KEYWORD = "ReSharper.CPP_CONTROL_FLOW_KEYWORD";

    @Override
    public @Nullable TextAttributesKey getTextAttributesKey(@NotNull String tokenType,
                                                            @NotNull List<String> tokenModifiers,
                                                            @NotNull PsiFile file) {
        switch (tokenType) {

            // namespace: for identifiers that declare or reference a namespace, module, or package.
            case SemanticTokenTypes.Namespace:
                if (hasTokenModifiers(tokenModifiers,
                        SemanticTokenModifiers.Declaration,
                        SemanticTokenModifiers.Definition)) {
                    // with declaration, definition modifiers
                    return cpp(CPP_NAMESPACE, SemanticTokensHighlightingColors.NAMESPACE_DECLARATION);
                }
                // with other modifiers
                return cpp(CPP_NAMESPACE, SemanticTokensHighlightingColors.NAMESPACE);

            // class: for identifiers that declare or reference a class type.
            case SemanticTokenTypes.Class:
                if (hasTokenModifiers(tokenModifiers,
                        SemanticTokenModifiers.Declaration,
                        SemanticTokenModifiers.Definition)) {
                    // with declaration, definition modifiers
                    return cpp(CPP_CLASS, SemanticTokensHighlightingColors.CLASS_DECLARATION);
                }
                // with other modifiers
                return cpp(CPP_CLASS, SemanticTokensHighlightingColors.CLASS);

            // enum: for identifiers that declare or reference an enumeration type.
            case SemanticTokenTypes.Enum:
                return cpp(CPP_ENUM, SemanticTokensHighlightingColors.ENUM);

            // interface: for identifiers that declare or reference an interface type.
            case SemanticTokenTypes.Interface:
                return cpp(CPP_CLASS, SemanticTokensHighlightingColors.INTERFACE);

            // struct: for identifiers that declare or reference a struct type.
            case SemanticTokenTypes.Struct:
                return cpp(CPP_STRUCT, SemanticTokensHighlightingColors.STRUCT);

            // typeParameter: for identifiers that declare or reference a type parameter.
            case SemanticTokenTypes.TypeParameter:
                return cpp(CPP_TEMPLATE_PARAMETER, SemanticTokensHighlightingColors.TYPE_PARAMETER);

            // type: for identifiers that declare or reference a type that is not covered above.
            case SemanticTokenTypes.Type:
                return cpp(CPP_TYPEDEF, SemanticTokensHighlightingColors.TYPE);

            // parameter: for identifiers that declare or reference a function or method parameters.
            case SemanticTokenTypes.Parameter:
                return cpp(CPP_PARAMETER, SemanticTokensHighlightingColors.PARAMETER);

            // variable: for identifiers that declare or reference a local or global variable.
            case SemanticTokenTypes.Variable:
                if (hasTokenModifiers(tokenModifiers, SemanticTokenModifiers.Static)) {
                    if (hasTokenModifiers(tokenModifiers, SemanticTokenModifiers.Readonly)) {
                        // with static, readonly modifiers
                        return cpp(CPP_CONSTANT, SemanticTokensHighlightingColors.STATIC_READONLY_VARIABLE);
                    }
                    // with static readonly modifiers
                    return cpp(CPP_GLOBAL_VARIABLE, SemanticTokensHighlightingColors.STATIC_VARIABLE);
                }
                if (hasTokenModifiers(tokenModifiers, SemanticTokenModifiers.Readonly)) {
                    // with readonly modifiers
                    return cpp(CPP_LOCAL_VARIABLE, SemanticTokensHighlightingColors.READONLY_VARIABLE);
                }
                // with other modifiers
                return cpp(CPP_LOCAL_VARIABLE, SemanticTokensHighlightingColors.VARIABLE);

            // property: for identifiers that declare or reference a member property, member field, or member variable.
            case SemanticTokenTypes.Property:
                if (hasTokenModifiers(tokenModifiers, SemanticTokenModifiers.Static)) {
                    if (hasTokenModifiers(tokenModifiers, SemanticTokenModifiers.Readonly)) {
                        // with static, readonly modifiers
                        return cpp(CPP_CONSTANT, SemanticTokensHighlightingColors.STATIC_READONLY_PROPERTY);
                    }
                    // with static readonly modifiers
                    return cpp(CPP_STATIC_FIELD, SemanticTokensHighlightingColors.STATIC_PROPERTY);
                }
                if (hasTokenModifiers(tokenModifiers, SemanticTokenModifiers.Readonly)) {
                    // with readonly modifiers
                    return cpp(CPP_CLASS_FIELD, SemanticTokensHighlightingColors.READONLY_PROPERTY);
                }
                // with other modifiers
                return cpp(CPP_CLASS_FIELD, SemanticTokensHighlightingColors.PROPERTY);

            // enum member: for identifiers that declare or reference an enumeration property, constant, or member.
            case SemanticTokenTypes.EnumMember:
                return cpp(CPP_ENUMERATOR, SemanticTokensHighlightingColors.ENUM_MEMBER);

            // decorator: for identifiers that declare or reference decorators and annotations.
            case SemanticTokenTypes.Decorator:
                return SemanticTokensHighlightingColors.DECORATOR;

            // event: for identifiers that declare an event property.
            case SemanticTokenTypes.Event:
                return SemanticTokensHighlightingColors.EVENT;

            // function: for identifiers that declare a function.
            case SemanticTokenTypes.Function:
                if (hasTokenModifiers(tokenModifiers,
                        SemanticTokenModifiers.DefaultLibrary)) {
                    // with defaultLibrary modifiers
                    return cpp(CPP_GLOBAL_FUNCTION, SemanticTokensHighlightingColors.DEFAULT_LIBRARY_FUNCTION);
                }
                if (hasTokenModifiers(tokenModifiers,
                        SemanticTokenModifiers.Declaration,
                        SemanticTokenModifiers.Definition)) {
                    // with declaration, definition modifiers
                    return cpp(CPP_GLOBAL_FUNCTION, SemanticTokensHighlightingColors.FUNCTION_DECLARATION);
                }
                // with other modifiers
                return cpp(CPP_GLOBAL_FUNCTION, SemanticTokensHighlightingColors.FUNCTION);

            // method: for identifiers that declare a member function or method.
            case SemanticTokenTypes.Method: {
                if (hasTokenModifiers(tokenModifiers,
                        SemanticTokenModifiers.Declaration,
                        SemanticTokenModifiers.Definition)) {
                    // with declaration, definition modifiers
                    return cpp(CPP_MEMBER_FUNCTION, SemanticTokensHighlightingColors.METHOD_DECLARATION);
                }
                if (hasTokenModifiers(tokenModifiers,
                        SemanticTokenModifiers.Static)) {
                    // with static modifiers
                    return cpp(CPP_STATIC_MEMBER_FUNCTION, SemanticTokensHighlightingColors.STATIC_METHOD);
                }
                // with other modifiers
                return cpp(CPP_MEMBER_FUNCTION, SemanticTokensHighlightingColors.METHOD);
            }

            // macro: for identifiers that declare a macro.
            case SemanticTokenTypes.Macro:
            case "directive": // unique to angel-lsp
                return cpp(CPP_MACRO, SemanticTokensHighlightingColors.MACRO);

            // label: for identifiers that declare a label.
            case "label":
                // LSP doesn't define "label", but vscode defines it
                // See https://code.visualstudio.com/api/language-extensions/semantic-highlight-guide#standard-token-types-and-modifiers
                return SemanticTokensHighlightingColors.LABEL;

            // comment: for tokens that represent a comment.
            case SemanticTokenTypes.Comment:
                return SemanticTokensHighlightingColors.COMMENT;

            // string: for tokens that represent a string literal.
            case SemanticTokenTypes.String:
                return SemanticTokensHighlightingColors.STRING;

            // keyword: for tokens that represent a language keyword.
            case SemanticTokenTypes.Keyword:
            case "builtin": // unique to angel-lsp
                return SemanticTokensHighlightingColors.KEYWORD;

            case "controlKeyword": // unique to angel-lsp
                return cpp(CPP_CONTROL_FLOW_KEYWORD, SemanticTokensHighlightingColors.KEYWORD);

            // number: for tokens that represent a number literal.
            case SemanticTokenTypes.Number:
                return SemanticTokensHighlightingColors.NUMBER;

            // regexp: for tokens that represent a regular expression literal.
            case SemanticTokenTypes.Regexp:
                return SemanticTokensHighlightingColors.REGEXP;

            // modifier
            case SemanticTokenTypes.Modifier:
                return SemanticTokensHighlightingColors.MODIFIER;

            // operator
            case SemanticTokenTypes.Operator:
                return SemanticTokensHighlightingColors.OPERATOR;

        }
        return null;
    }

    private static TextAttributesKey cpp(String cppKeyName, TextAttributesKey fallback) {
        TextAttributesKey cppKey = TextAttributesKey.find(cppKeyName);
        TextAttributes attributes = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(cppKey);
        return attributes != null && !attributes.isEmpty() ? cppKey : fallback;
    }

    protected boolean hasTokenModifiers(List<String> tokenModifiers, String... checkedTokenModifiers) {
        if (tokenModifiers.isEmpty()) {
            return false;
        }
        for (var modifier : checkedTokenModifiers) {
            if (tokenModifiers.contains(modifier)) {
                return true;
            }
        }
        return false;
    }

}