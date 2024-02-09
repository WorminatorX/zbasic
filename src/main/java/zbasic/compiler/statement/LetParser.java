package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class LetParser extends StatementParser {
    public LetParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        if (ts.isKeyword(Keyword.LET)) {
            ts.nextToken();
        }
        final Node lvalue = (new ExpressionParser(ts, context)).parseLValue();
        checkAndSkipSymbol(Symbol.EQUALS_SIGN);
        ts.skipEnd();
        final Node rvalue = (new ExpressionParser(ts, context)).parse();
        if (isStringValue(lvalue)) {
            validateString(rvalue);
        } else {
            validateNumber(rvalue);
        }
        checkAndSkipEnd();
        return (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.LET, lvalue, rvalue);
    }
}
