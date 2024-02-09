package zbasic.compiler;

import zbasic.Symbol;

public final class TextUtils {

    public static boolean isSpace(char c) {
        return c == '\t' || c == ' ' || c == '\u00A0';
    }

    public static boolean isAlpha(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isFirstIdentifierCharacter(char c) {
        return c == '_' || isAlpha(c);
    }

    public static boolean isIdentifierCharacter(char c) {
        return c == '_' || isDigit(c) || isAlpha(c);
    }

    public static boolean isFirstSymbolCharacter(char c) {
        return Symbol.startsWith(c);
    }
}
