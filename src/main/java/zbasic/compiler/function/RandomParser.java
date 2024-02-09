package zbasic.compiler.function;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class RandomParser extends FunctionParser {
    public RandomParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        checkAndSkipSymbol(Symbol.LEFT_PARENTHESIS);
        ts.skipEnd();
        checkAndSkipSymbol(Symbol.RIGHT_PARENTHESIS);
        return (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.RANDOM);
    }
}
