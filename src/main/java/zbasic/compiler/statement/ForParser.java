package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class ForParser extends StatementParser {
    public ForParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node counter = (new ExpressionParser(ts, context)).parseLValue();
        validateNumber(counter);
        final Node.KeywordTree forStatement = (Node.KeywordTree)nf.createKeywordTree(Keyword.FOR, counter);
        checkAndSkipSymbol(Symbol.EQUALS_SIGN);
        ts.skipEnd();
        final Node startValue = (new ExpressionParser(ts, context)).parse();
        validateNumber(startValue);
        forStatement.add(startValue);
        checkAndSkipKeyword(Keyword.TO);
        ts.skipEnd();
        final Node limitValue = (new ExpressionParser(ts, context)).parse();
        validateNumber(limitValue);
        forStatement.add(limitValue);
        if (ts.isKeyword(Keyword.STEP)) {
            ts.nextToken();
            ts.skipEnd();
            final Node stepValue = (new ExpressionParser(ts, context)).parse();
            validateNumber(stepValue);
            forStatement.add(stepValue);
        }
        checkAndSkipEnd();
        final Node.KeywordTree body = (Node.KeywordTree)nf.createKeywordTree(Keyword.CODE);
        ts.skipEnd();
        while (!ts.isKeyword(Keyword.END)) {
            body.add((new StatementParser(ts, context)).parse());
            ts.skipEnd();
        }
        ts.nextToken();
        checkAndSkipKeyword(Keyword.FOR);
        checkAndSkipEnd();
        forStatement.add(body);
        return forStatement;
    }
}
