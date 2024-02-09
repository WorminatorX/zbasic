package zbasic.compiler.function;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class BNotParser extends FunctionParser {
    public BNotParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        final Node.KeywordTree function =
            (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.BNOT);
        if (ts.isSymbol(Symbol.LEFT_PARENTHESIS)) {
            ts.nextToken();
            ts.skipEnd();
            final Node number = (new ExpressionParser(ts, context)).parse();
            validateNumber(number);
            function.add(number);
            ts.skipEnd();
            checkAndSkipSymbol(Symbol.RIGHT_PARENTHESIS);
        } else {
            final Node number = (new ExpressionParser(ts, context)).parse();
            validateNumber(number);
            function.add(number);
        }
        return function;
    }
}
