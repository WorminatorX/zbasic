package zbasic.compiler.function;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class BoundParser extends FunctionParser {
    public BoundParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        final Node.KeywordTree function =
            (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.BOUND);
        if (ts.isSymbol(Symbol.LEFT_PARENTHESIS)) {
            ts.nextToken();
            ts.skipEnd();
            final Node array = (new ExpressionParser(ts, context)).parseLValue();
            validateArray(array);
            function.add(array);
            ts.skipEnd();
            checkAndSkipSymbol(Symbol.RIGHT_PARENTHESIS);
        } else {
            final Node array = (new ExpressionParser(ts, context)).parseLValue();
            validateArray(array);
            function.add(array);
        }
        return function;
    }
}
