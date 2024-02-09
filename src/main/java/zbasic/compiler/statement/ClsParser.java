package zbasic.compiler.statement;

import zbasic.Keyword;
import zbasic.compiler.Context;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;

public final class ClsParser extends StatementParser {
    public ClsParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        ts.nextToken();
        checkAndSkipEnd();
        return (Node.KeywordTree)NodeFactory.getInstance().createKeywordTree(Keyword.CLS);
    }
}
