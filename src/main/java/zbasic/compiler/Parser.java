package zbasic.compiler;

import static zbasic.Keyword.isStringName;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.statement.StatementParser;

import java.util.List;

public final class Parser extends BaseParser {
    private final Module module;

    private Parser(TokenScanner ts, Module module) {
        super(ts, module.getContext());
        this.module = module;
    }

    public Parser(TokenScanner ts, String moduleName) {
        this(ts, new Module(moduleName));
    }

    public Parser(TokenScanner ts) {
        this(ts, new Module(""));
    }

    @Override
    public Module parse() throws ParserException {
        boolean flagRepeat = true;
        while (flagRepeat) {
            ts.skipEnd();
            if (ts.isKeyword(Keyword.CONST)) {
                parseConst(module);
            } else if (ts.isKeyword(Keyword.VAR)) {
                parseVar(module);
            } else if (ts.isKeyword(Keyword.DIM)) {
                parseDim(module);
            } else if (ts.isKeyword(Keyword.DEF)) {
                parseDef(module);
            } else if (ts.isKeyword(Keyword.FUNCTION)) {
                parseFunction(module);
            } else if (ts.isKeyword(Keyword.SUB)) {
                parseSub(module);
            } else {
                flagRepeat = false;
            }
        }
        if (ts.isKeyword(Keyword.BEGIN)) {
            parseBegin(module);
        }
        if (ts.getCurrentToken() != null) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
        return module;
    }

    private void parseConst(Module module) throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node name = (new ExpressionParser(ts, context)).parseNewName();
        validateName(name);
        final String constName = nf.getName(name);
        checkAndSkipSymbol(Symbol.EQUALS_SIGN);
        ts.skipEnd();
        final Node value = (new ExpressionParser(ts, context)).parse();
        if (isStringName(constName)) {
            validateConstantString(value);
            context.addStringValue(nf.getString(value));
            module.addStringConstant(constName, nf.getString(value));
        } else {
            validateConstantNumber(value);
            module.addNumberConstant(constName, nf.getNumber(value));
        }
        checkAndSkipEnd();
    }

    private void parseVar(Module module) throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node name = (new ExpressionParser(ts, context)).parseNewName();
        validateName(name);
        if (ts.isSymbol(Symbol.EQUALS_SIGN)) {
            final String varName = nf.getName(name);
            ts.nextToken();
            ts.skipEnd();
            final Node value = (new ExpressionParser(ts, context)).parse();
            if (isStringName(varName)) {
                validateConstantString(value);
                context.addStringValue(nf.getString(value));
                module.addStringVariable(varName, nf.getString(value));
            } else {
                validateConstantNumber(value);
                module.addNumberVariable(varName, nf.getNumber(value));
            }
        } else {
            String varName = nf.getName(name);
            boolean flagRepeat = true;
            while (flagRepeat) {
                if (isStringName(varName)) {
                    module.addStringVariable(varName);
                } else {
                    module.addNumberVariable(varName);
                }
                if (ts.isSymbol(Symbol.COMMA)) {
                    ts.nextToken();
                    ts.skipEnd();
                    final Node nextName = (new ExpressionParser(ts, context)).parseNewName();
                    validateName(nextName);
                    varName = nf.getName(nextName);
                } else {
                    flagRepeat = false;
                }
            }
        }
        checkAndSkipEnd();
    }

    private void parseDim(Module module) throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node name = (new ExpressionParser(ts, context)).parseNewName();
        validateArray(name);
        checkAndSkipSymbol(Symbol.LEFT_SQUARE_BRACKET);
        ts.skipEnd();
        if (!ts.isSymbol(Symbol.RIGHT_SQUARE_BRACKET)) {
            final Node size = (new ExpressionParser(ts, context)).parse();
            validateConstantNumber(size);
            module.addStaticArray(nf.getName(name), nf.getNumber(size));
        } else {
            ts.nextToken();
            module.addDynamicArray(nf.getName(name));
        }
    }

    private void parseDef(Module module) throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node name = (new ExpressionParser(ts, context)).parseNewName();
        validateName(name);
        final boolean flagStringResult = isStringName(nf.getName(name));
        final Function function = new Function(flagStringResult);
        final Context functionContext = new Context(context, true, flagStringResult);
        parseArguments(function, functionContext);
        checkAndSkipSymbol(Symbol.EQUALS_SIGN);
        ts.skipEnd();
        final Node expression = (new ExpressionParser(ts, functionContext)).parse();
        if (flagStringResult) {
            validateString(expression);
        } else {
            validateNumber(expression);
        }
        checkAndSkipEnd();
        function.addStatement(nf.createKeywordTree(Keyword.RETURN, expression));
        copySubroutineValues(functionContext);
        module.addFunction(nf.getName(name), function);
    }

    private void parseFunction(Module module) throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node name = (new ExpressionParser(ts, context)).parseNewName();
        validateName(name);
        final boolean flagStringResult = isStringName(nf.getName(name));
        final Function function = new Function(flagStringResult);
        final Context functionContext = new Context(context, true, flagStringResult);
        parseArguments(function, functionContext);
        checkAndSkipEnd();
        parseSubroutineBody(function, functionContext);
        ts.nextToken();
        checkAndSkipKeyword(Keyword.FUNCTION);
        checkAndSkipEnd();
        copySubroutineValues(functionContext);
        module.addFunction(nf.getName(name), function);
    }

    private void parseSub(Module module) throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        ts.nextToken();
        final Node name = (new ExpressionParser(ts, context)).parseNewName();
        validateName(name);
        validateNotString(name);
        final Subroutine subroutine = new Subroutine();
        final Context subroutineContext = new Context(context, false);
        parseArguments(subroutine, subroutineContext);
        checkAndSkipEnd();
        parseSubroutineBody(subroutine, subroutineContext);
        ts.nextToken();
        checkAndSkipKeyword(Keyword.SUB);
        checkAndSkipEnd();
        copySubroutineValues(subroutineContext);
        module.addSubroutine(nf.getName(name), subroutine);
    }

    private void copySubroutineValues(Context subContext) {
        for (Integer numberValue : subContext.getNumberValues()) {
            context.addNumberValue(numberValue);
        }
        for (String stringValue : subContext.getStringValues()) {
            context.addStringValue(stringValue);
        }
    }

    private void parseSubroutineBody(Subroutine subroutine, Context subContext) throws ParserException {
        ts.skipEnd();
        while (!ts.isKeyword(Keyword.END)) {
            final Node statement = (new StatementParser(ts, subContext)).parse();
            subroutine.addStatement(statement);
            ts.skipEnd();
        }
    }

    private void parseArguments(Subroutine subroutine, Context subContext) throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        checkAndSkipSymbol(Symbol.LEFT_PARENTHESIS);
        ts.skipEnd();
        if (!ts.isSymbol(Symbol.RIGHT_PARENTHESIS)) {
            final Node firstArgument = (new ExpressionParser(ts, subContext)).parseNewName();
            validateName(firstArgument);
            final String firstArgumentName = nf.getName(firstArgument);
            subroutine.addArgument(firstArgumentName);
            if (isStringName(firstArgumentName)) {
                subContext.addStringVariable(firstArgumentName);
            } else {
                subContext.addNumberVariable(firstArgumentName);
            }
            while (ts.isSymbol(Symbol.COMMA)) {
                ts.skipEnd();
                ts.nextToken();
                final Node nextArgument = (new ExpressionParser(ts, subContext)).parseNewName();
                validateName(nextArgument);
                final String nextArgumentName = nf.getName(nextArgument);
                subroutine.addArgument(nextArgumentName);
                if (isStringName(nextArgumentName)) {
                    subContext.addStringVariable(nextArgumentName);
                } else {
                    subContext.addStringVariable(nextArgumentName);
                }
            }
            ts.skipEnd();
            checkAndSkipSymbol(Symbol.RIGHT_PARENTHESIS);
        } else {
            ts.nextToken();
        }
    }

    private void parseBegin(Module module) throws ParserException {
        final List<Node.KeywordTree> initializationBlock = module.getInitialization();
        ts.nextToken();
        checkAndSkipEnd();
        ts.skipEnd();
        while (!ts.isKeyword(Keyword.END)) {
            if (ts.isKeyword(Keyword.RETURN)) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            initializationBlock.add((new StatementParser(ts, context)).parse());
            ts.skipEnd();
        }
        ts.nextToken();
        checkAndSkipEnd();
        ts.skipEnd();
    }
}
