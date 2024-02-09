package zbasic.compiler.function;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class BOrParser extends FunctionParser {
    public BOrParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        checkAndSkipSymbol(Symbol.LEFT_PARENTHESIS);
        ts.skipEnd();
        final Node numberA = (new ExpressionParser(ts, context)).parse();
        validateNumber(numberA);
        ts.skipEnd();
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node numberB = (new ExpressionParser(ts, context)).parse();
        validateNumber(numberB);
        ts.skipEnd();
        checkAndSkipSymbol(Symbol.RIGHT_PARENTHESIS);
        return (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.BOR, numberA, numberB);
    }
}
