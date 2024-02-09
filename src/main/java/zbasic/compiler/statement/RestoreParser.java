package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class RestoreParser extends StatementParser {
    public RestoreParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        final Node arrayOrString = (new ExpressionParser(ts, context)).parseLValue();
        validateName(arrayOrString);
        checkAndSkipEnd();
        return (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.RESTORE, arrayOrString);
    }
}
