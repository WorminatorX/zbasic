package zbasic.compiler.function;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class BitParser extends FunctionParser {
    public BitParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        checkAndSkipSymbol(Symbol.LEFT_PARENTHESIS);
        ts.skipEnd();
        final Node number = (new ExpressionParser(ts, context)).parse();
        validateNumber(number);
        ts.skipEnd();
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node bit = (new ExpressionParser(ts, context)).parse();
        validateBitValue(bit);
        ts.skipEnd();
        checkAndSkipSymbol(Symbol.RIGHT_PARENTHESIS);
        return (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.BIT, number, bit);
    }
}
