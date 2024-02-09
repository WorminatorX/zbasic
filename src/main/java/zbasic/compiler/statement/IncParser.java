package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class IncParser extends StatementParser {
    public IncParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node lvalue = (new ExpressionParser(ts, context)).parseLValue();
        validateNumber(lvalue);
        final Node.KeywordTree statement = (Node.KeywordTree)nf.createKeywordTree(Keyword.INC, lvalue);
        if (ts.isSymbol(Symbol.COMMA)) {
            ts.nextToken();
            ts.skipEnd();
            final Node delta = (new ExpressionParser(ts, context)).parse();
            validateNumber(delta);
            statement.add(delta);
        }
        checkAndSkipEnd();
        return statement;
    }
}
