package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class FreeParser extends StatementParser {
    public FreeParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        final Node array = (new ExpressionParser(ts, context)).parseLValue();
        validateArray(array);
        checkAndSkipEnd();
        return (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.FREE, array);
    }
}
