package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class WhileParser extends StatementParser {
    public WhileParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node condition = (new ExpressionParser(ts, context)).parse();
        validateNumber(condition);
        checkAndSkipEnd();
        final Node.KeywordTree body = (Node.KeywordTree)nf.createKeywordTree(Keyword.CODE);
        ts.skipEnd();
        while (!ts.isKeyword(Keyword.END)) {
            body.add((new StatementParser(ts, context)).parse());
            ts.skipEnd();
        }
        ts.nextToken();
        checkAndSkipKeyword(Keyword.WHILE);
        checkAndSkipEnd();
        return (Node.KeywordTree)nf.createKeywordTree(Keyword.WHILE, condition, body);
    }
}
