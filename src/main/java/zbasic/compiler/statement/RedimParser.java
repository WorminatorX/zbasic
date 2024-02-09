package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class RedimParser extends StatementParser {
    public RedimParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        final Node array = (new ExpressionParser(ts, context)).parseLValue();
        validateArray(array);
        checkAndSkipSymbol(Symbol.LEFT_SQUARE_BRACKET);
        ts.skipEnd();
        final Node size = (new ExpressionParser(ts, context)).parse();
        validateNumber(size);
        ts.skipEnd();
        checkAndSkipSymbol(Symbol.RIGHT_SQUARE_BRACKET);
        checkAndSkipEnd();
        return (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.REDIM, array, size);
    }
}
