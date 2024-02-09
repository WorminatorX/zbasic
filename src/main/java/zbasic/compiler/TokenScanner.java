package zbasic.compiler;

import zbasic.Keyword;
import zbasic.Symbol;

import java.util.Iterator;

public final class TokenScanner {
    private final Iterator<Token> tokenIterator;
    private Token currentToken;
    private int lineCounter;

    public TokenScanner(Iterable<Token> tokens) {
        if (tokens == null) { throw new IllegalArgumentException("tokens"); }
        this.tokenIterator = tokens.iterator();
        this.currentToken = (this.tokenIterator.hasNext()) ? this.tokenIterator.next() : null;
        this.lineCounter = 1;
    }

    public Token getCurrentToken() { return currentToken; }

    public int getLine() { return lineCounter; }

    public int getPosition() { return (currentToken != null) ? currentToken.getPosition() : 0; }

    public void nextToken() {
        if (currentToken != null) {
            if (tokenIterator.hasNext()) {
                if (isEnd()) {
                    lineCounter++;
                }
                currentToken = tokenIterator.next();
            } else {
                currentToken = null;
            }
        }
    }

    public void skipEnd() {
        final TokenFactory tf = TokenFactory.getInstance();
        while (tf.isEndToken(currentToken)) {
            nextToken();
        }
    }

    public boolean isEnd() {
        return TokenFactory.getInstance().isEndToken(currentToken);
    }

    public boolean isSymbol(Symbol symbol) {
        final TokenFactory tf = TokenFactory.getInstance();
        return tf.isSymbolToken(currentToken) && tf.getSymbol(currentToken) == symbol;
    }

    public boolean isKeyword() {
        return TokenFactory.getInstance().isKeywordToken(currentToken);
    }

    public boolean isKeyword(Keyword keyword) {
        final TokenFactory tf = TokenFactory.getInstance();
        return tf.isKeywordToken(currentToken) && tf.getKeyword(currentToken) == keyword;
    }

    public boolean isName() {
        return TokenFactory.getInstance().isNameToken(currentToken);
    }

    public boolean isString() {
        return TokenFactory.getInstance().isStringToken(currentToken);
    }

    public boolean isNumber() {
        return TokenFactory.getInstance().isNumberToken(currentToken);
    }

    public boolean isNumber(int number) {
        final TokenFactory tf = TokenFactory.getInstance();
        return tf.isNumberToken(currentToken) && tf.getNumber(currentToken) == number;
    }

    public Symbol getSymbol() {
        return TokenFactory.getInstance().getSymbol(currentToken);
    }

    public Keyword getKeyword() {
        return TokenFactory.getInstance().getKeyword(currentToken);
    }

    public String getName() {
        return TokenFactory.getInstance().getName(currentToken);
    }

    public String getString() {
        return TokenFactory.getInstance().getString(currentToken);
    }

    public int getNumber() {
        return TokenFactory.getInstance().getNumber(currentToken);
    }
}
