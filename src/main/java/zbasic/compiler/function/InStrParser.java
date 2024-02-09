package zbasic.compiler.function;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class InStrParser extends FunctionParser {
    public InStrParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        final Node.KeywordTree function =
            (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.INSTR);
        checkAndSkipSymbol(Symbol.LEFT_PARENTHESIS);
        ts.skipEnd();
        final Node firstArgument = (new ExpressionParser(ts, context)).parse();
        function.add(firstArgument);
        ts.skipEnd();
        if (!isStringValue(firstArgument)) {
            checkAndSkipSymbol(Symbol.COMMA);
            ts.skipEnd();
            final Node string = (new ExpressionParser(ts, context)).parse();
            validateString(string);
            function.add(string);
            ts.skipEnd();
        }
        checkAndSkipSymbol(Symbol.COMMA);
        ts.skipEnd();
        final Node subString = (new ExpressionParser(ts, context)).parse();
        validateString(subString);
        function.add(subString);
        ts.skipEnd();
        checkAndSkipSymbol(Symbol.RIGHT_PARENTHESIS);
        return function;
    }
}
