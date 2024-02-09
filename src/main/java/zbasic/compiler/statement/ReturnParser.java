package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.SyntaxErrorException;
import zbasic.compiler.TokenScanner;

public final class ReturnParser extends StatementParser {
    public ReturnParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        final Node.KeywordTree statement =
            (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.RETURN);
        if (!ts.isEnd()) {
            if (!context.isFunctionContext()) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            final Node result = (new ExpressionParser(ts, context)).parse();
            if (context.isStringResult()) {
                validateString(result);
            } else {
                validateNumber(result);
            }
            statement.add(result);
            checkAndSkipEnd();
        } else {
            ts.nextToken();
        }
        return statement;
    }
}
