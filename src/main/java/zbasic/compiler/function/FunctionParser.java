package zbasic.compiler.function;

import zbasic.compiler.BaseParser;
import zbasic.compiler.Context;
import zbasic.compiler.Node;
import zbasic.compiler.ParserException;
import zbasic.compiler.SyntaxErrorException;
import zbasic.compiler.TokenScanner;

public class FunctionParser extends BaseParser {
    public FunctionParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        if (!ts.isKeyword()) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
        Node.KeywordTree function = null;
        switch (ts.getKeyword()) {
            case ABS:
                function = (new AbsParser(ts, context)).parse();
                break;
            case BAND:
                function = (new BAndParser(ts, context)).parse();
                break;
            case BIT:
                function = (new BitParser(ts, context)).parse();
                break;
            case BNOT:
                function = (new BNotParser(ts, context)).parse();
                break;
            case BOR:
                function = (new BOrParser(ts, context)).parse();
                break;
            case BOUND:
                function = (new BoundParser(ts, context)).parse();
                break;
            case BXOR:
                function = (new BXorParser(ts, context)).parse();
                break;
            case CHR_:
                function = (new ChrParser(ts, context)).parse();
                break;
            case CODE:
                function = (new CodeParser(ts, context)).parse();
                break;
            case INSTR:
                function = (new InStrParser(ts, context)).parse();
                break;
            case LEFT_:
                function = (new LeftParser(ts, context)).parse();
                break;
            case LEN:
                function = (new LenParser(ts, context)).parse();
                break;
            case LSH:
                function = (new LshParser(ts, context)).parse();
                break;
            case MID_:
                function = (new MidParser(ts, context)).parse();
                break;
            case RANDOM:
                function = (new RandomParser(ts, context)).parse();
                break;
            case RIGHT_:
                function = (new RightParser(ts, context)).parse();
                break;
            case RSH:
                function = (new RshParser(ts, context)).parse();
                break;
            case STR_:
                function = (new StrParser(ts, context)).parse();
                break;
            case USH:
                function = (new UshParser(ts, context)).parse();
                break;
            case VAL:
                function = (new ValParser(ts, context)).parse();
                break;
        }
        if (function == null) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
        return function;
    }
}
