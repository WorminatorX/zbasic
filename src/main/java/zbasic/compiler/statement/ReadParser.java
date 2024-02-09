package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class ReadParser extends StatementParser {
    public ReadParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node.KeywordTree statement = (Node.KeywordTree)nf.createKeywordTree(Keyword.READ); 
        final Node firstLValue = (new ExpressionParser(ts, context)).parseLValue();
        validateNumber(firstLValue);
        statement.add(firstLValue);
        while (ts.isSymbol(Symbol.COMMA)) {
            ts.nextToken();
            ts.skipEnd();
            final Node nextLValue = (new ExpressionParser(ts, context)).parseLValue();
            validateNumber(nextLValue);
            statement.add(nextLValue);
        }
        checkAndSkipEnd();
        return statement;
    }
}
