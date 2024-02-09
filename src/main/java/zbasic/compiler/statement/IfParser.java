package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.SyntaxErrorException;
import zbasic.compiler.TokenScanner;

public final class IfParser extends StatementParser {
    private final boolean allowShortForm;

    public IfParser(TokenScanner ts, Context context, boolean allowShortForm) {
        super(ts, context);
        this.allowShortForm = allowShortForm;
    }

    public IfParser(TokenScanner ts, Context context) {
        this(ts, context, true);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node condition = (new ExpressionParser(ts, context)).parse();
        validateNumber(condition);
        final Node.KeywordTree ifStatement = (Node.KeywordTree)nf.createKeywordTree(Keyword.IF, condition);
        checkAndSkipKeyword(Keyword.THEN);
        if (ts.isEnd()) {
            ts.nextToken();
            final Node.KeywordTree thenStatement = (Node.KeywordTree)nf.createKeywordTree(Keyword.CODE);
            ifStatement.add(thenStatement);
            ts.skipEnd();
            while (!ts.isKeyword(Keyword.END) && !ts.isKeyword(Keyword.ELSE)) {
                thenStatement.add((new StatementParser(ts, context)).parse());
                ts.skipEnd();
            }
            if (ts.isKeyword(Keyword.ELSE)) {
                ts.nextToken();
                final Node.KeywordTree elseStatement = (Node.KeywordTree)nf.createKeywordTree(Keyword.CODE);
                ifStatement.add(elseStatement);
                if (ts.isKeyword(Keyword.IF)) {
                    final Node.KeywordTree innerIfStatement = (new IfParser(ts, context, false)).parse();
                    elseStatement.add(innerIfStatement);
                    return ifStatement;
                }
                checkAndSkipEnd();
                ts.skipEnd();
                while (!ts.isKeyword(Keyword.END)) {
                    elseStatement.add((new StatementParser(ts, context)).parse());
                    ts.skipEnd();
                }
            }
            ts.nextToken();
            checkAndSkipKeyword(Keyword.IF);
            checkAndSkipEnd();
        } else {
            if (!allowShortForm) { throw new SyntaxErrorException(ts.getLine(), ts.getPosition()); }
            ifStatement.add((new StatementParser(ts, context)).parse());
        }
        return ifStatement;
    }
}
