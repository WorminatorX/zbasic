package zbasic;

public enum Symbol {
    COMMA(","),
    DOT("."),
    SEMICOLON(";"),
    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")"),
    LEFT_SQUARE_BRACKET("["),
    RIGHT_SQUARE_BRACKET("]"),
    PLUS_SIGN("+"),
    MINUS_SIGN("-"),
    AMPERSAND("&"),
    ASTERISK("*"),
    SOLIDUS("/"),
    PERCENT_SIGN("%"),
    EQUALS_SIGN("="),
    LESS_THAN_SIGN("<"),
    GREATER_THAN_SIGN(">"),
    LESS_OR_EQUALS_SIGN("<="),
    GREATER_OR_EQUALS_SIGN(">="),
    NOT_EQUALS_SIGN("<>");
    private final String text;

    private Symbol(String text) {
        this.text = text;
    }

    public String getText() { return text; }

    public static Symbol getSymbol(String text) {
        if (text == null || text.isEmpty()) { return null; }
        final Symbol[] symbols = Symbol.values();
        for (Symbol symbol : symbols) {
            if (symbol.text.equals(text)) { return symbol; }
        }
        return null;
    }

    public static boolean startsWith(char c) {
        final Symbol[] symbols = Symbol.values();
        for (Symbol symbol : symbols) {
            if (symbol.text.charAt(0) == c) { return true; }
        }
        return false;
    }

    public static boolean startsWith(String prefix) {
        final Symbol[] symbols = Symbol.values();
        for (Symbol symbol : symbols) {
            if (symbol.text.startsWith(prefix)) { return true; }
        }
        return false;
    }
}
