package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class SelectParser extends StatementParser {
    public SelectParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        checkAndSkipKeyword(Keyword.CASE);
        final Node selectedValue = (new ExpressionParser(ts, context)).parse();
        checkAndSkipEnd();
        final boolean flagStringValue = isStringValue(selectedValue);
        final Node.KeywordTree selectStatement = (Node.KeywordTree)nf.createKeywordTree(Keyword.SELECT);
        selectStatement.add(selectedValue);
        ts.skipEnd();
        while (ts.isKeyword(Keyword.CASE)) {
            ts.nextToken();
            final Node.KeywordTree caseWise = (Node.KeywordTree)nf.createKeywordTree(Keyword.CASE);
            selectStatement.add(caseWise);
            final Node.KeywordTree variants = (Node.KeywordTree)nf.createKeywordTree(Keyword.DATA);
            caseWise.add(variants);
            final Node firstValue = (new ExpressionParser(ts, context)).parse();
            if (flagStringValue) {
                validateString(firstValue);
            } else {
                validateNumber(firstValue);
            }
            variants.add(firstValue);
            while (ts.isSymbol(Symbol.COMMA)) {
                ts.nextToken();
                ts.skipEnd();
                final Node nextValue = (new ExpressionParser(ts, context)).parse();
                if (flagStringValue) {
                    validateString(nextValue);
                } else {
                    validateNumber(nextValue);
                }
                variants.add(nextValue);
            }
            checkAndSkipEnd();
            final Node.KeywordTree code = (Node.KeywordTree)nf.createKeywordTree(Keyword.CODE);
            caseWise.add(code);
            ts.skipEnd();
            while (
                !ts.isKeyword(Keyword.CASE) &&
                !ts.isKeyword(Keyword.DEFAULT) &&
                !ts.isKeyword(Keyword.END)
            ) {
                code.add((new StatementParser(ts, context)).parse());
                ts.skipEnd();
            }
        }
        if (ts.isKeyword(Keyword.DEFAULT)) {
            ts.nextToken();
            final Node.KeywordTree defaultWise = (Node.KeywordTree)nf.createKeywordTree(Keyword.CASE);
            selectStatement.add(defaultWise);
            defaultWise.add((Node.KeywordTree)nf.createKeywordTree(Keyword.DATA));
            checkAndSkipEnd();
            final Node.KeywordTree code = (Node.KeywordTree)nf.createKeywordTree(Keyword.CODE);
            defaultWise.add(code);
            ts.skipEnd();
            while (!ts.isKeyword(Keyword.END)) {
                code.add((new StatementParser(ts, context)).parse());
                ts.skipEnd();
            }
        }
        checkAndSkipKeyword(Keyword.END);
        checkAndSkipKeyword(Keyword.SELECT);
        checkAndSkipEnd();
        return selectStatement;
    }
}
