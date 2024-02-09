package zbasic.compiler.function;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class MidParser extends FunctionParser {
    public MidParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        checkAndSkipSymbol(Symbol.LEFT_PARENTHESIS);
        ts.skipEnd();
        final Node string = (new ExpressionParser(ts, context)).parse();
        validateString(string);
        ts.skipEnd();
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node index = (new ExpressionParser(ts, context)).parse();
        validateNumber(index);
        ts.skipEnd();
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node count = (new ExpressionParser(ts, context)).parse();
        validateNumber(count);
        ts.skipEnd();
        checkAndSkipSymbol(Symbol.RIGHT_PARENTHESIS);
        return (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.MID_, string, index, count);
    }
}
