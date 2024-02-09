package zbasic.compiler;

import zbasic.Keyword;
import zbasic.Symbol;

public abstract class Token {
    public enum TokenType {
        END,
        SYMBOL,
        KEYWORD,
        NAME,
        STRING,
        NUMBER
    }

    private final TokenType tokenType;
    private final int position;

    protected Token(TokenType tokenType, int position) {
        this.tokenType = tokenType;
        this.position = position;
    }

    public TokenType getTokenType() { return tokenType; }

    public int getPosition() { return position; }

    public static final class EndToken extends Token {
        public EndToken(int position) {
            super(TokenType.END, position);
        }
    }

    public static final class SymbolToken extends Token {
        private final Symbol symbol;

        public SymbolToken(int position, Symbol symbol) {
            super(TokenType.SYMBOL, position);
            this.symbol = symbol;
        }

        public Symbol getSymbol() { return symbol; }
    }

    public static final class KeywordToken extends Token {
        private final Keyword keyword;

        public KeywordToken(int position, Keyword keyword) {
            super(TokenType.KEYWORD, position);
            this.keyword = keyword;
        }

        public Keyword getKeyword() { return keyword; }
    }

    public static final class NameToken extends Token {
        private final String name;

        public NameToken(int position, String name) {
            super(TokenType.NAME, position);
            this.name = name;
        }

        public String getName() { return name; }
    }

    public static final class StringToken extends Token {
        private final String string;

        public StringToken(int position, String string) {
            super(TokenType.STRING, position);
            this.string = string;
        }

        public String getString() { return string; }
    }

    public static final class NumberToken extends Token {
        private final int number;

        public NumberToken(int position, int number) {
            super(TokenType.NUMBER, position);
            this.number = number;
        }

        public int getNumber() { return number; }
    }
}