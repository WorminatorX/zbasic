package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class CopyParser extends StatementParser {
    public CopyParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node srcArr = (new ExpressionParser(ts, context)).parseLValue();
        validateArray(srcArr);
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node srcIndex = (new ExpressionParser(ts, context)).parse();
        validateNumber(srcIndex);
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node destArr = (new ExpressionParser(ts, context)).parseLValue();
        validateArray(destArr);
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node destIndex = (new ExpressionParser(ts, context)).parse();
        validateNumber(destIndex);
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node count = (new ExpressionParser(ts, context)).parse();
        validateNumber(count);
        checkAndSkipEnd();
        return (Node.KeywordTree)nf.createKeywordTree(Keyword.COPY, srcArr, srcIndex, destArr, destIndex, count);
    }
}
