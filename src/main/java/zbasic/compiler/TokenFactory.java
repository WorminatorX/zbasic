package zbasic.compiler;

import static zbasic.Keyword.isStringName;
import static zbasic.compiler.TextUtils.isFirstIdentifierCharacter;
import static zbasic.compiler.TextUtils.isIdentifierCharacter;

import zbasic.Keyword;
import zbasic.Symbol;

public final class TokenFactory {
    private static TokenFactory instance = null;

    private TokenFactory() {}

    public Token createEndToken(int position) {
        validatePosition(position);
        return new Token.EndToken(position);
    }

    public Token createSymbolToken(int position, Symbol symbol) {
        validatePosition(position);
        validateSymbol(symbol);
        return new Token.SymbolToken(position, symbol);
    }

    public Token createNameOrKeywordToken(int position, String name) {
        validatePosition(position);
        validateName(name);
        final Keyword keyword = Keyword.getKeyword(name);
        return (keyword != null) ? new Token.KeywordToken(position, keyword) : new Token.NameToken(position, name);
    }

    public Token createStringToken(int position, String string) {
        validatePosition(position);
        validateString(string);
        return new Token.StringToken(position, string);
    }

    public Token createNumberToken(int position, int number) {
        validatePosition(position);
        return new Token.NumberToken(position, number);
    }

    public boolean isEndToken(Token token) { return token instanceof Token.EndToken; }

    public boolean isSymbolToken(Token token) { return token instanceof Token.SymbolToken; }

    public boolean isKeywordToken(Token token) { return token instanceof Token.KeywordToken; }

    public boolean isNameToken(Token token) { return token instanceof Token.NameToken; }

    public boolean isStringToken(Token token) { return token instanceof Token.StringToken; }

    public boolean isNumberToken(Token token) { return token instanceof Token.NumberToken; }

    public Symbol getSymbol(Token token) {
        if (!(token instanceof Token.SymbolToken)) { throw new IllegalArgumentException("token"); }
        return ((Token.SymbolToken)token).getSymbol();
    }

    public Keyword getKeyword(Token token) {
        if (!(token instanceof Token.KeywordToken)) { throw new IllegalArgumentException("token"); }
        return ((Token.KeywordToken)token).getKeyword();
    }

    public String getName(Token token) {
        if (!(token instanceof Token.NameToken)) { throw new IllegalArgumentException("token"); }
        return ((Token.NameToken)token).getName();
    }

    public String getString(Token token) {
        if (!(token instanceof Token.StringToken)) { throw new IllegalArgumentException("token"); }
        return ((Token.StringToken)token).getString();
    }

    public int getNumber(Token token) {
        if (!(token instanceof Token.NumberToken)) { throw new IllegalArgumentException("token"); }
        return ((Token.NumberToken)token).getNumber();
    }

    private static void validatePosition(int position) {
        if (position < 0) { throw new IllegalArgumentException("position"); }
    }

    private static void validateSymbol(Symbol symbol) {
        if (symbol == null) { throw new IllegalArgumentException("symbol"); }
    }

    private static void validateName(String name) {
        if (name == null || name.isEmpty()) { throw new IllegalArgumentException("name"); }
        final char[] chars = name.toCharArray();
        if (!isFirstIdentifierCharacter(chars[0])) { throw new IllegalArgumentException("name"); }
        final int length = (isStringName(name)) ? (chars.length - 1) : chars.length;
        for (int i = 1; i < length; i++) {
            if (!isIdentifierCharacter(chars[i])) { throw new IllegalArgumentException("name"); }
        }
    }

    private static void validateString(String string) {
        if (string == null) { throw new IllegalArgumentException("string"); }
    }

    public static TokenFactory getInstance() {
        if (instance == null) {
            synchronized (TokenFactory.class) {
                instance = new TokenFactory();
            }
        }
        return instance;
    }
}