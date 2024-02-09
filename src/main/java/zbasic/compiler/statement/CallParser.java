package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.SyntaxErrorException;
import zbasic.compiler.TokenScanner;

public final class CallParser extends StatementParser {
    public CallParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        if (ts.isKeyword(Keyword.CALL)) {
            ts.nextToken();
        }
        final Node.KeywordTree statement = (Node.KeywordTree)nf.createKeywordTree(Keyword.CALL);
        statement.add(parseSubroutineName());
        final Node.KeywordTree arguments = (Node.KeywordTree)nf.createKeywordTree(Keyword.INPUT);
        statement.add(arguments);
        if (!ts.isEnd()) {
            final Node firstArgument = (new ExpressionParser(ts, context)).parse();
            arguments.add(firstArgument);
            while (ts.isSymbol(Symbol.COMMA)) {
                ts.nextToken();
                ts.skipEnd();
                final Node nextArgument = (new ExpressionParser(ts, context)).parse();
                arguments.add(nextArgument);
            }
            checkAndSkipEnd();
        } else {
            ts.nextToken();
        }
        return statement;
    }

    private Node parseSubroutineName() throws ParserException {
        if (!ts.isName()) { throw new SyntaxErrorException(ts.getLine(), ts.getPosition()); }
        final String name = ts.getName();
        ts.nextToken();
        if (!context.isSubroutine(name)) { throw new SyntaxErrorException(ts.getLine(), ts.getPosition()); }
        return NodeFactory.getInstance().createNameAtom(name);
    }
}
