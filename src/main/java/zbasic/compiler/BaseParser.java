package zbasic.compiler;

import static zbasic.Keyword.isStringName;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.SyntaxErrorException;
import zbasic.compiler.TokenScanner;

import java.util.Iterator;

public abstract class BaseParser {
    private static final int MAX_BIT_COUNT = 15;
    private static final int MAX_BIT_VALUE = 32768;

    protected final TokenScanner ts;
    protected final Context context;

    public BaseParser(TokenScanner ts, Context context) {
        if (ts == null) { throw new IllegalArgumentException("ts"); }
        if (context == null) { throw new IllegalArgumentException("context"); }
        this.ts = ts;
        this.context = context;
    }

    public abstract Node parse() throws ParserException;

    protected void checkAndSkipEnd() throws ParserException {
        if (!ts.isEnd()) { throw new SyntaxErrorException(ts.getLine(), ts.getPosition()); }
        ts.nextToken();
    }

    protected void checkAndSkipKeyword(Keyword keyword) throws ParserException {
        if (!ts.isKeyword(keyword)) { throw new SyntaxErrorException(ts.getLine(), ts.getPosition()); }
        ts.nextToken();
    }

    protected void checkAndSkipSymbol(Symbol symbol) throws ParserException {
        if (!ts.isSymbol(symbol)) { throw new SyntaxErrorException(ts.getLine(), ts.getPosition()); }
        ts.nextToken();
    }

    protected void validateArray(Node array) throws ParserException {
        if (!NodeFactory.getInstance().isNameAtom(array) || isStringValue(array)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
    }

    protected void validateBitCount(Node bitCount) throws ParserException {
        if (!NodeFactory.getInstance().isNumberAtom(bitCount)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
        final int number = NodeFactory.getInstance().getNumber(bitCount);
        if (number <= 0 || number > MAX_BIT_COUNT) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
    }

    protected void validateBitValue(Node bitValue) throws ParserException {
        if (!NodeFactory.getInstance().isNumberAtom(bitValue)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
        final int number = NodeFactory.getInstance().getNumber(bitValue);
        if (number <= 0 || number > MAX_BIT_VALUE) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
    }

    protected void validateConstantNumber(Node number) throws ParserException {
        if (!NodeFactory.getInstance().isNumberAtom(number)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
    }

    protected void validateConstantString(Node string) throws ParserException {
        if (!NodeFactory.getInstance().isStringAtom(string)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
    }

    protected void validateName(Node name) throws ParserException {
        if (!NodeFactory.getInstance().isNameAtom(name)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
    }

    protected void validateNumber(Node number) throws ParserException {
        if (isStringValue(number)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
    }

    protected void validateString(Node string) throws ParserException {
        if (!isStringValue(string)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
    }

    protected void validateNotString(Node string) throws ParserException {
        if (isStringValue(string)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
    }

    protected static boolean isStringValue(Node value) {
        final NodeFactory nf = NodeFactory.getInstance();
        if (nf.isStringAtom(value)) { return true; }
        if (nf.isSymbolTree(value)) {
            if (nf.getSymbol(value) == Symbol.AMPERSAND) { return true; }
        }
        if (nf.isKeywordTree(value)) {
            return nf.getKeyword(value).isString();
        }
        if (nf.isNameTree(value)) {
            final Iterator<Node> arguments = nf.getChildren(value).iterator();
            final Node firstArgument = arguments.next();
            if (!arguments.hasNext() && nf.isSymbolTree(firstArgument)) {
                if (nf.getSymbol(firstArgument) == Symbol.LEFT_SQUARE_BRACKET) {
                    if (nf.getChildren(firstArgument).size() == 1) return false;                    
                }
            }
        }
        if (nf.isNameTree(value) || nf.isNameAtom(value)) {
            final String name = nf.getName(value);
            if (isStringName(name)) return true;
        }
        return false;
    }
}
