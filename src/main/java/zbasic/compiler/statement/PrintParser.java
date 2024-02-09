package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class PrintParser extends StatementParser {
    private static final int CODE_TAB = 0x08;
    private static final int CODE_CR  = 0x0D;

    public PrintParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        final Node.KeywordTree statement = (Node.KeywordTree)nf.createKeywordTree(Keyword.PRINT);
        ts.nextToken();
        boolean flagEndLine = true;
        if (!ts.isEnd()) {
            statement.add((new ExpressionParser(ts, context)).parse());
            while (ts.isSymbol(Symbol.COMMA) || ts.isSymbol(Symbol.SEMICOLON)) {
                final Symbol symbol = ts.getSymbol();
                ts.nextToken();
                if (symbol == Symbol.COMMA) {
                    statement.add(nf.createKeywordTree(Keyword.CHR_, nf.createNumberAtom(CODE_TAB)));
                    ts.skipEnd();
                    statement.add((new ExpressionParser(ts, context)).parse());
                } else {
                    if (ts.isEnd()) {
                        flagEndLine = false;
                    } else {
                        statement.add((new ExpressionParser(ts, context)).parse());
                    }
                }
            }
        }
        checkAndSkipEnd();
        if (flagEndLine) {
            statement.add(nf.createKeywordTree(Keyword.CHR_, nf.createNumberAtom(CODE_CR)));
        }
        return statement;
    }
}
