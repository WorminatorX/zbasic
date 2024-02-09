package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class BSetParser extends StatementParser {
    public BSetParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        final Node lvalue = (new ExpressionParser(ts, context)).parseLValue();
        validateNumber(lvalue);
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node bit = (new ExpressionParser(ts, context)).parse();
        validateBitValue(bit);
        checkAndSkipEnd();
        return (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.BSET, lvalue, bit);
    }
}
