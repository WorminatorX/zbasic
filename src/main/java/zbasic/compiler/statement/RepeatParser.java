package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class RepeatParser extends StatementParser {
    public RepeatParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node.KeywordTree body = (Node.KeywordTree)nf.createKeywordTree(Keyword.CODE);
        if (ts.isEnd()) {
            ts.nextToken();
            ts.skipEnd();
            while (!ts.isKeyword(Keyword.UNTIL)) {
                body.add((new StatementParser(ts, context)).parse());
                ts.skipEnd();
            }
        } else if (!ts.isKeyword(Keyword.UNTIL)) {
            body.add((new StatementParser(ts, context)).parse());
        }
        checkAndSkipKeyword(Keyword.UNTIL);
        final Node exitCondition = (new ExpressionParser(ts, context)).parse();
        validateNumber(exitCondition);
        checkAndSkipEnd();
        return (Node.KeywordTree)nf.createKeywordTree(Keyword.REPEAT, body, exitCondition);
    }
}
