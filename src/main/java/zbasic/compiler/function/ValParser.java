package zbasic.compiler.function;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class ValParser extends FunctionParser {
    public ValParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        final Node.KeywordTree function =
            (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.VAL);
        if (ts.isSymbol(Symbol.LEFT_PARENTHESIS)) {
            ts.nextToken();
            ts.skipEnd();
            final Node string = (new ExpressionParser(ts, context)).parse();
            validateString(string);
            function.add(string);
            ts.skipEnd();
            checkAndSkipSymbol(Symbol.RIGHT_PARENTHESIS);
        } else {
            final Node string = (new ExpressionParser(ts, context)).parse();
            validateString(string);
            function.add(string);
        }
        return function;
    }
}
