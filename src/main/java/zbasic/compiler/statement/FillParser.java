package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class FillParser extends StatementParser {
    public FillParser(TokenScanner ts, Context context) {
        super(ts, context); 
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node arr = (new ExpressionParser(ts, context)).parseLValue();
        validateArray(arr);
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node index = (new ExpressionParser(ts, context)).parse();
        validateNumber(index);
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node count = (new ExpressionParser(ts, context)).parse();
        validateNumber(count);
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node value = (new ExpressionParser(ts, context)).parse();
        validateNumber(value);
        checkAndSkipEnd();
        return (Node.KeywordTree)nf.createKeywordTree(Keyword.FILL, arr, index, count, value);
    }
}
