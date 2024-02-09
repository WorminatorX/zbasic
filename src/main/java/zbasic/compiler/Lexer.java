package zbasic.compiler;

import static zbasic.compiler.TextUtils.isDigit;
import static zbasic.compiler.TextUtils.isFirstIdentifierCharacter;
import static zbasic.compiler.TextUtils.isFirstSymbolCharacter;
import static zbasic.compiler.TextUtils.isIdentifierCharacter;
import static zbasic.compiler.TextUtils.isSpace;

import zbasic.Keyword;
import zbasic.Symbol;

import java.util.ArrayList;
import java.util.List;

public final class Lexer {
    private static enum State {
        UNKNOWN,
        ERROR,
        COMMENT,
        SYMBOL,
        NAME,
        STRING,
        END_STRING,
        NUMBER,
        HEX_NUMBER,
        STRING_SUFFIX
    }

    private final List<Token> tokens;
    private StringBuilder textBuffer;
    private int numberBuffer;
    private int tokenPosition;
    private int lineNumber;

    public Lexer() {
        this.tokens = new ArrayList<>();
        this.textBuffer = null;
        this.numberBuffer = 0;
        this.tokenPosition = 0;
        this.lineNumber = 0;
    }

    public List<Token> getTokens() { return tokens; }

    public void extractTokens(String line) throws LexerException {
        if (line == null) { throw new IllegalArgumentException("line"); }
        tokenPosition = 0;
        if (line.isEmpty()) {
            addEndToken();
            lineNumber++;
            return;
        }
        final char[] chars = line.toCharArray();
        State state = State.UNKNOWN;
        int position = 0;
        for (char c : chars) {
            switch (state) {
                case UNKNOWN:
                    state = whenUnknown(c, position);
                    break;
                case SYMBOL:
                    state = whenSymbol(c, position);
                    break;
                case NAME:
                    state = whenName(c, position);
                    if (state == State.COMMENT) {
                        tokenPosition = position;
                        addEndToken();
                        lineNumber++;
                        return;
                    }
                    break;
                case STRING:
                    state = whenString(c, position);
                    break;
                case END_STRING:
                    state = whenEndString(c, position);
                    break;
                case NUMBER:
                    state = whenNumber(c, position);
                    break;
                case HEX_NUMBER:
                    state = whenHexNumber(c, position);
                    break;
                case STRING_SUFFIX:
                    state = whenStringSuffix(c, position);
                    break;
            }
            if (state == State.ERROR) { throw new LexerException(lineNumber, position); }
            position++;
        }
        if (state == State.STRING) { throw new LexerException(lineNumber, position); }
        switch (state) {
            case SYMBOL:
                addSymbolToken();
                break;
            case NAME:
                addNameOrKeywordToken();
                break;
            case END_STRING:
                addStringToken();
                break;
            case NUMBER:
            case HEX_NUMBER:
                addNumberToken();
                break;
            case STRING_SUFFIX:
                appendCharacter(Keyword.STRING_SUFFIX);
                addNameOrKeywordToken();
                break;
        }
        addEndToken();
        lineNumber++;
    }

    private State whenUnknown(char c, int position) {
        if (isSpace(c)) {
            return State.UNKNOWN;
        } else if (c == '\"') {
            initTextBuffer();
            tokenPosition = position;
            return State.STRING;
        } else if (c == '#') {
            initNumberBuffer(0);
            tokenPosition = position;
            return State.HEX_NUMBER;
        } else if (isDigit(c)) {
            initNumberBuffer((int)c - (int)'0');
            tokenPosition = position;
            return State.NUMBER;
        } else if (isFirstIdentifierCharacter(c)) {
            initTextBuffer(c);
            tokenPosition = position;
            return State.NAME;
        } else if (isFirstSymbolCharacter(c)) {
            initTextBuffer(c);
            tokenPosition = position;
            return State.SYMBOL;
        } else {
            return State.ERROR;
        }
    }

    private State whenSymbol(char c, int position) {
        if (isPartOfSymbol(c)) {
            appendCharacter(c);
            return State.SYMBOL;
        } else if (isSpace(c)) {
            addSymbolToken();
            return State.UNKNOWN;
        } else if (c == '\"') {
            addSymbolToken();
            initTextBuffer();
            tokenPosition = position;
            return State.STRING;
        } else if (c == '#') {
            addSymbolToken();
            initNumberBuffer(0);
            tokenPosition = position;
            return State.HEX_NUMBER;
        } else if (isDigit(c)) {
            addSymbolToken();
            initNumberBuffer((int)c - (int)'0');
            tokenPosition = position;
            return State.NUMBER;
        } else if (isFirstIdentifierCharacter(c)) {
            addSymbolToken();
            initTextBuffer(c);
            tokenPosition = position;
            return State.NAME;
        } else if (isFirstSymbolCharacter(c)) {
            addSymbolToken();
            initTextBuffer(c);
            tokenPosition = position;
            return State.SYMBOL;
        } else {
            return State.ERROR;
        }
    }

    private State whenName(char c, int position) {
        if (c == Keyword.STRING_SUFFIX) {
            return State.STRING_SUFFIX;
        } else if (isIdentifierCharacter(c)) {
            appendCharacter(c);
            return State.NAME;
        } else if (isSpace(c)) {
            if (foundKeyword(Keyword.REM)) { return State.COMMENT; }
            addNameOrKeywordToken();
            return State.UNKNOWN;
        } else if (isFirstSymbolCharacter(c)) {
            addNameOrKeywordToken();
            initTextBuffer(c);
            tokenPosition = position;
            return State.SYMBOL;
        } else {
            return State.ERROR;
        }
    }

    private State whenString(char c, int position) {
        if (c < ' ') {
            return State.ERROR;
        } else if (c == '\"') {
            return State.END_STRING;
        } else {
            appendCharacter(c);
            return State.STRING;
        }
    }

    private State whenEndString(char c, int position) {
        if (c == '\"') {
            appendCharacter('\"');
            return State.STRING;
        } else if (isSpace(c)) {
            addStringToken();
            return State.UNKNOWN;
        } else if (isFirstSymbolCharacter(c)) {
            addStringToken();
            initTextBuffer(c);
            tokenPosition = position;
            return State.SYMBOL;
        } else {
            return State.ERROR;
        }
    }

    private State whenNumber(char c, int position) {
        if (isDigit(c)) {
            appendDigit((int)c - (int)'0');
            return State.NUMBER;
        } else if (isSpace(c)) {
            addNumberToken();
            return State.UNKNOWN;
        } else if (isFirstSymbolCharacter(c)) {
            addNumberToken();
            initTextBuffer(c);
            tokenPosition = position;
            return State.SYMBOL;
        } else {
            return State.ERROR;
        }
    }

    private State whenHexNumber(char c, int position) {
        if (isDigit(c)) {
            appendHexDigit((int)c - (int)'0');
            return State.NUMBER;
        } else if (c >= 'A' && c <= 'F') {
            appendHexDigit((int)c - (int)'A' + 10);
            return State.NUMBER;
        } else if (c >= 'a' && c <= 'f') {
            appendHexDigit((int)c - (int)'a' + 10);
            return State.NUMBER;
        } else if (isSpace(c)) {
            addNumberToken();
            return State.UNKNOWN;
        } else if (isFirstSymbolCharacter(c)) {
            addNumberToken();
            initTextBuffer(c);
            tokenPosition = position;
            return State.SYMBOL;
        } else {
            return State.ERROR;
        }
    }

    private State whenStringSuffix(char c, int position) {
        if (isSpace(c)) {
            appendCharacter(Keyword.STRING_SUFFIX);
            addNameOrKeywordToken();
            return State.UNKNOWN;
        } else if (isFirstSymbolCharacter(c)) {
            appendCharacter(Keyword.STRING_SUFFIX);
            addNameOrKeywordToken();
            initTextBuffer(c);
            tokenPosition = position;
            return State.SYMBOL;
        } else {
            return State.ERROR;
        }
    }

    private void addEndToken() {
        tokens.add(TokenFactory.getInstance().createEndToken(tokenPosition));
    }

    private void addSymbolToken() {
        final Symbol symbol = Symbol.getSymbol(textBuffer.toString());
        tokens.add(TokenFactory.getInstance().createSymbolToken(tokenPosition, symbol));
    }

    private void addNameOrKeywordToken() {
        tokens.add(TokenFactory.getInstance().createNameOrKeywordToken(tokenPosition, textBuffer.toString()));
    }

    private void addStringToken() {
        tokens.add(TokenFactory.getInstance().createStringToken(tokenPosition, textBuffer.toString()));
    }

    private void addNumberToken() {
        tokens.add(TokenFactory.getInstance().createNumberToken(tokenPosition, numberBuffer));
    }

    private void initTextBuffer() {
        textBuffer = new StringBuilder();
    }

    private void initTextBuffer(char c) {
        textBuffer = new StringBuilder();
        textBuffer.append(c);
    }

    private void initNumberBuffer(int digit) {
        numberBuffer = digit;
    }

    private void appendCharacter(char c) {
        textBuffer.append(c);
    }

    private void appendDigit(int digit) {
        numberBuffer *= 10;
        numberBuffer += digit;
    }

    private void appendHexDigit(int digit) {
        numberBuffer *= 16;
        numberBuffer += digit;
    }

    private boolean isPartOfSymbol(char c) {
        return Symbol.startsWith(textBuffer.toString() + c);
    }

    private boolean foundKeyword(Keyword keyword) {
        return keyword.getText().equals(textBuffer.toString());
    }
}
