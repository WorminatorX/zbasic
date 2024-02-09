package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class SwapParser extends StatementParser {
    public SwapParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        final Node lvA = (new ExpressionParser(ts, context)).parseLValue();
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node lvB = (new ExpressionParser(ts, context)).parseLValue();
        if (isStringValue(lvA)) {
            validateString(lvB);
        } else {
            validateNumber(lvB);
        }
        checkAndSkipEnd();
        return (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.SWAP, lvA, lvB);
    }
}
