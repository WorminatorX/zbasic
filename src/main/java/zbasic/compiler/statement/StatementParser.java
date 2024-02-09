package zbasic.compiler.statement;

import zbasic.compiler.BaseParser;
import zbasic.compiler.Context;
import zbasic.compiler.Node;
import zbasic.compiler.ParserException;
import zbasic.compiler.SyntaxErrorException;
import zbasic.compiler.TokenScanner;

public class StatementParser extends BaseParser {
    public StatementParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node.KeywordTree parse() throws ParserException {
        if (ts.isName()) {
            final String name = ts.getName();
            if (context.isNumberVariable(name) || context.isStringVariable(name)) {
                return (new LetParser(ts, context)).parse();
            } else if (context.isSubroutine(name)) {
                return (new CallParser(ts, context)).parse();
            }
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
        if (!ts.isKeyword()) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
        Node.KeywordTree statement = null;
        switch (ts.getKeyword()) {
            case BRESET:
                statement = (new BResetParser(ts, context)).parse();
                break;
            case BSET:
                statement = (new BSetParser(ts, context)).parse();
                break;
            case CALL:
                statement = (new CallParser(ts, context)).parse();
                break;
            case CLS:
                statement = (new ClsParser(ts, context)).parse();
                break;
            case COPY:
                statement = (new CopyParser(ts, context)).parse();
                break;
            case DEC:
                statement = (new DecParser(ts, context)).parse();
                break;
            case FOR:
                statement = (new ForParser(ts, context)).parse();
                break;
            case FILL:
                statement = (new FillParser(ts, context)).parse();
                break;
            case FREE:
                statement = (new FreeParser(ts, context)).parse();
                break;
            case IF:
                statement = (new IfParser(ts, context)).parse();
                break;
            case INC:
                statement = (new IncParser(ts, context)).parse();
                break;
            case INPUT:
                statement = (new InputParser(ts, context)).parse();
                break;
            case LET:
                statement = (new LetParser(ts, context)).parse();
                break;
            case PAUSE:
                statement = (new PauseParser(ts, context)).parse();
                break;
            case PRINT:
                statement = (new PrintParser(ts, context)).parse();
                break;
            case QUIT:
                statement = (new QuitParser(ts, context)).parse();
                break;
            case READ:
                statement = (new ReadParser(ts, context)).parse();
                break;
            case REDIM:
                statement = (new RedimParser(ts, context)).parse();
                break;
            case REPEAT:
                statement = (new RepeatParser(ts, context)).parse();
                break;
            case RESTORE:
                statement = (new RestoreParser(ts, context)).parse();
                break;
            case RETURN:
                statement = (new ReturnParser(ts, context)).parse();
                break;
            case SELECT:
                statement = (new SelectParser(ts, context)).parse();
                break;
            case SWAP:
                statement = (new SwapParser(ts, context)).parse();
                break;
            case WHILE:
                statement = (new WhileParser(ts, context)).parse();
                break;
        }
        if (statement == null) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
        return statement;
    }
}
