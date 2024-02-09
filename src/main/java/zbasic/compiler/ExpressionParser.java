package zbasic.compiler;

import static zbasic.Keyword.isStringName;
import static zbasic.Keyword.isSystemConstant;
import static zbasic.Keyword.isSystemVariable;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.function.FunctionParser;

public final class ExpressionParser extends BaseParser {
    public ExpressionParser(TokenScanner ts, Context context) {
        super(ts, context);
    }

    @Override
    public Node parse() throws ParserException {
        return parseOr();
    }

    public Node parseLValue() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        final Node lvalue = parseOperand();
        if (nf.isNameTree(lvalue)) {
            final String name = nf.getName(lvalue);
            if (isStringName(name)) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            if (!context.isStaticArray(name) && !context.isDynamicArray(name)) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            if (nf.getChildren(lvalue).size() != 1) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            final Node argument = nf.getChildren(lvalue).iterator().next();
            if (!nf.isSymbolTree(argument)) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            if (nf.getSymbol(argument) != Symbol.LEFT_SQUARE_BRACKET) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            if (nf.getChildren(argument).size() != 1) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            if (isStringValue(nf.getChildren(argument).iterator().next())) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
        } else if (!nf.isNameAtom(lvalue)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
        return lvalue;
    }

    public Node parseNewName() throws ParserException {
        if (!ts.isName()) { throw new SyntaxErrorException(ts.getLine(), ts.getPosition()); }
        final String name = ts.getName();
        ts.nextToken();
        if (context.isNumberConstant(name) || context.isNumberVariable(name)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        } else if (context.isStringConstant(name) || context.isStringVariable(name)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        } else if (context.isStaticArray(name) || context.isDynamicArray(name)) {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
        return NodeFactory.getInstance().createNameAtom(name);
    }

    private Node parseOr() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        Node firstOperand = parseAnd();
        while (ts.isKeyword(Keyword.OR)) {
            ts.nextToken();
            ts.skipEnd();
            final Node secondOperand = parseAnd();
            boolean flagOptimization = false;
            if (isStringValue(firstOperand) || isStringValue(secondOperand)) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            if (nf.isNumberAtom(firstOperand)) {
                if (nf.getNumber(firstOperand) != 0) {
                    firstOperand = nf.createNumberAtom(-1);
                    flagOptimization = true;
                } else if (nf.isNumberAtom(secondOperand)) {
                    if (nf.getNumber(secondOperand) == 0) {
                        firstOperand = secondOperand;
                    } else {
                        firstOperand = nf.createNumberAtom(-1);
                    }
                    flagOptimization = true;
                }
            } else if (nf.isNumberAtom(secondOperand)) {
                if (nf.getNumber(secondOperand) == 0) {
                    flagOptimization = true;
                }
            }
            if (!flagOptimization) {
                firstOperand = nf.createKeywordTree(Keyword.OR, firstOperand, secondOperand);
            }
        }
        return firstOperand;
    }

    private Node parseAnd() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        Node firstOperand = parseNot();
        while (ts.isKeyword(Keyword.AND)) {
            ts.nextToken();
            ts.skipEnd();
            final Node secondOperand = parseNot();
            boolean flagOptimization = false;
            if (isStringValue(firstOperand) || isStringValue(secondOperand)) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            if (nf.isNumberAtom(firstOperand)) {
                if (nf.getNumber(firstOperand) == 0) {
                    flagOptimization = true;
                } else if (nf.isNumberAtom(secondOperand)) {
                    if (nf.getNumber(secondOperand) == 0) {
                        firstOperand = secondOperand;
                    } else {
                        firstOperand = nf.createNumberAtom(-1);
                    }
                    flagOptimization = true;
                }
            } else if (nf.isNumberAtom(secondOperand)) {
                if (nf.getNumber(secondOperand) != 0) {
                    flagOptimization = true;
                }
            }
            if (!flagOptimization) {
                firstOperand = nf.createKeywordTree(Keyword.AND, firstOperand, secondOperand);
            }
        }
        return firstOperand;
    }

    private Node parseNot() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        boolean flagNot = false;
        while (ts.isKeyword(Keyword.NOT)) {
            flagNot = !flagNot;
            ts.nextToken();
        }
        Node operand = parseComp();
        if (flagNot) {
            if (isStringValue(operand)) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            if (nf.isNumberAtom(operand)) {
                operand = nf.createNumberAtom((nf.getNumber(operand) == 0) ? -1 : 0);
            } else {
                operand = nf.createKeywordTree(Keyword.NOT, operand);
            }
        }
        return operand;
    }

    private Node parseComp() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        Node firstOperand = parseSumm();
        if (
            ts.isSymbol(Symbol.EQUALS_SIGN) ||
            ts.isSymbol(Symbol.LESS_THAN_SIGN) ||
            ts.isSymbol(Symbol.GREATER_THAN_SIGN) ||
            ts.isSymbol(Symbol.LESS_OR_EQUALS_SIGN) ||
            ts.isSymbol(Symbol.GREATER_OR_EQUALS_SIGN) ||
            ts.isSymbol(Symbol.NOT_EQUALS_SIGN)
        ) {
            final Symbol symbol = ts.getSymbol();
            ts.nextToken();
            ts.skipEnd();
            final Node secondOperand = parseSumm();
            boolean flagOptimization = false;
            if (isStringValue(firstOperand)) {
                if (!isStringValue(secondOperand)) {
                    throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                }
                if (nf.isStringAtom(firstOperand)) {
                    if (nf.isStringAtom(secondOperand)) {
                        int result = 0;
                        if (symbol == Symbol.EQUALS_SIGN) {
                            if (nf.getString(firstOperand).equals(nf.getString(secondOperand))) {
                                result = -1;
                            }
                        } else if (symbol == Symbol.LESS_THAN_SIGN) {
                            if (nf.getString(firstOperand).compareTo(nf.getString(secondOperand)) < 0) {
                                result = -1;
                            }
                        } else if (symbol == Symbol.GREATER_THAN_SIGN) {
                            if (nf.getString(firstOperand).compareTo(nf.getString(secondOperand)) > 0) {
                                result = -1;
                            }
                        } else if (symbol == Symbol.LESS_OR_EQUALS_SIGN) {
                            if (nf.getString(firstOperand).compareTo(nf.getString(secondOperand)) <= 0) {
                                result = -1;
                            }
                        } else if (symbol == Symbol.GREATER_OR_EQUALS_SIGN) {
                            if (nf.getString(firstOperand).compareTo(nf.getString(secondOperand)) >= 0) {
                                result = -1;
                            }
                        } else {
                            if (!nf.getString(firstOperand).equals(nf.getString(secondOperand))) {
                                result = -1;
                            }
                        }
                        firstOperand = nf.createNumberAtom(result);
                        flagOptimization = true;
                    }
                }
            } else {
                if (isStringValue(secondOperand)) {
                    throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                }
                if (nf.isNumberAtom(firstOperand)) {
                    if (nf.isNumberAtom(secondOperand)) {
                        int result = 0;
                        if (symbol == Symbol.EQUALS_SIGN) {
                            if (nf.getNumber(firstOperand) == nf.getNumber(secondOperand)) {
                                result = -1;
                            }
                        } else if (symbol == Symbol.LESS_THAN_SIGN) {
                            if (nf.getNumber(firstOperand) < nf.getNumber(secondOperand)) {
                                result = -1;
                            }
                        } else if (symbol == Symbol.GREATER_THAN_SIGN) {
                            if (nf.getNumber(firstOperand) > nf.getNumber(secondOperand)) {
                                result = -1;
                            }
                        } else if (symbol == Symbol.LESS_OR_EQUALS_SIGN) {
                            if (nf.getNumber(firstOperand) <= nf.getNumber(secondOperand)) {
                                result = -1;
                            }
                        } else if (symbol == Symbol.GREATER_OR_EQUALS_SIGN) {
                            if (nf.getNumber(firstOperand) >= nf.getNumber(secondOperand)) {
                                result = -1;
                            }
                        } else {
                            if (nf.getNumber(firstOperand) != nf.getNumber(secondOperand)) {
                                result = -1;
                            }
                        }
                        firstOperand = nf.createNumberAtom(result);
                        flagOptimization = true;
                    }
                }
            }
            if (!flagOptimization) {
                firstOperand = nf.createSymbolTree(symbol, firstOperand, secondOperand);
            }
        }
        return firstOperand;
    }

    private Node parseSumm() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        boolean flagNegative = false;
        while (ts.isSymbol(Symbol.MINUS_SIGN)) {
            flagNegative = !flagNegative;
            ts.nextToken();
        }
        Node firstOperand = parseMult();
        if (flagNegative) {
            if (isStringValue(firstOperand)) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            if (nf.isNumberAtom(firstOperand)) {
                firstOperand = nf.createNumberAtom(-nf.getNumber(firstOperand));
            } else {
                firstOperand = nf.createSymbolTree(Symbol.MINUS_SIGN, firstOperand);
            }
        }
        while (
            ts.isSymbol(Symbol.PLUS_SIGN) ||
            ts.isSymbol(Symbol.MINUS_SIGN) ||
            ts.isSymbol(Symbol.AMPERSAND)
        ) {
            final Symbol symbol = ts.getSymbol();
            ts.nextToken();
            ts.skipEnd();
            final Node secondOperand = parseMult();
            boolean flagOptimization = false;
            if (symbol == Symbol.PLUS_SIGN) {
                if (isStringValue(firstOperand) || isStringValue(secondOperand)) {
                    throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                }
                if (nf.isNumberAtom(firstOperand)) {
                    final int firstNumber = nf.getNumber(firstOperand);
                    if (nf.isNumberAtom(secondOperand)) {
                        firstOperand = nf.createNumberAtom(firstNumber + nf.getNumber(secondOperand));
                        flagOptimization = true;
                    } else if (firstNumber == 0) {
                        firstOperand = secondOperand;
                        flagOptimization = true;
                    } else if (firstNumber == 1) {
                        firstOperand = nf.createKeywordTree(Keyword.INC, secondOperand);
                        flagOptimization = true;
                    } else if (firstNumber == -1) {
                        firstOperand = nf.createKeywordTree(Keyword.DEC, secondOperand);
                        flagOptimization = true;
                    }
                } else if (nf.isNumberAtom(secondOperand)) {
                    final int secondNumber = nf.getNumber(secondOperand);
                    if (secondNumber == 0) {
                        flagOptimization = true;
                    } else if (secondNumber == 1) {
                        firstOperand = nf.createKeywordTree(Keyword.INC, firstOperand);
                        flagOptimization = true;
                    } else if (secondNumber == -1) {
                        firstOperand = nf.createKeywordTree(Keyword.DEC, firstOperand);
                        flagOptimization = true;
                    }
                }
            } else if (symbol == Symbol.MINUS_SIGN) {
                if (isStringValue(firstOperand) || isStringValue(secondOperand)) {
                    throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                }
                if (nf.isNumberAtom(firstOperand)) {
                    final int firstNumber = nf.getNumber(firstOperand);
                    if (nf.isNumberAtom(secondOperand)) {
                        firstOperand = nf.createNumberAtom(firstNumber - nf.getNumber(secondOperand));
                        flagOptimization = true;
                    } else if (firstNumber == 0) {
                        firstOperand = nf.createSymbolTree(Symbol.MINUS_SIGN, secondOperand);
                        flagOptimization = true;
                    }
                } else if (nf.isNumberAtom(secondOperand)) {
                    final int secondNumber = nf.getNumber(secondOperand);
                    if (secondNumber == 0) {
                        flagOptimization = true;
                    } else if (secondNumber == 1) {
                        firstOperand = nf.createKeywordTree(Keyword.DEC, firstOperand);
                        flagOptimization = true;
                    } else if (secondNumber == -1) {
                        firstOperand = nf.createKeywordTree(Keyword.INC, firstOperand);
                        flagOptimization = true;
                    }
                }
            } else {
                if (!isStringValue(firstOperand) || !isStringValue(secondOperand)) {
                    throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                }
                if (nf.isStringAtom(firstOperand)) {
                    final String firstString = nf.getString(firstOperand);
                    if (firstString.isEmpty()) {
                        firstOperand = secondOperand;
                        flagOptimization = true;
                    } else if (nf.isStringAtom(secondOperand)) {
                        final String secondString = nf.getString(secondOperand);
                        if (!secondString.isEmpty()) {
                            final String string = firstString + secondString;
                            context.addStringValue(string);
                            firstOperand = nf.createStringAtom(string);
                        }
                        flagOptimization = true;
                    }
                } else if (nf.isStringAtom(secondOperand)) {
                    if (nf.getString(secondOperand).isEmpty()) {
                        flagOptimization = true;
                    }
                }
            }
            if (!flagOptimization) {
                firstOperand = nf.createSymbolTree(symbol, firstOperand, secondOperand);
            }
        }
        return firstOperand;
    }

    private Node parseMult() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        Node firstOperand = parseOperand();
        while (
            ts.isSymbol(Symbol.ASTERISK) ||
            ts.isSymbol(Symbol.SOLIDUS) ||
            ts.isSymbol(Symbol.PERCENT_SIGN)
        ) {
            final Symbol symbol = ts.getSymbol();
            ts.nextToken();
            ts.skipEnd();
            final Node secondOperand = parseOperand();
            boolean flagOptimization = false;
            if (symbol == Symbol.ASTERISK) {
                if (isStringValue(firstOperand) || isStringValue(secondOperand)) {
                    throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                }
                if (nf.isNumberAtom(firstOperand)) {
                    final int firstNumber = nf.getNumber(firstOperand);
                    if (nf.isNumberAtom(secondOperand)) {
                        if (firstNumber != 0) {
                            firstOperand = nf.createNumberAtom(firstNumber * nf.getNumber(secondOperand));
                        }
                        flagOptimization = true;
                    } else if (firstNumber == 1) {
                        firstOperand = secondOperand;
                        flagOptimization = true;
                    } else if (firstNumber == -1) {
                        firstOperand = nf.createSymbolTree(Symbol.MINUS_SIGN, secondOperand);
                        flagOptimization = true;
                    } else if (firstNumber > 1 && isBinaryPower(firstNumber)) {
                        firstOperand = nf.createKeywordTree(
                            Keyword.LSH,
                            secondOperand,
                            nf.createNumberAtom(log2(firstNumber))
                        );
                        flagOptimization = true;
                    } else if (firstNumber < 1 && isBinaryPower(-firstNumber)) {
                        firstOperand = nf.createSymbolTree(
                            Symbol.MINUS_SIGN,
                            nf.createKeywordTree(
                                Keyword.LSH,
                                secondOperand,
                                nf.createNumberAtom(log2(-firstNumber))
                            )
                        );
                        flagOptimization = true;
                    }
                } else if (nf.isNumberAtom(secondOperand)) {
                    final int secondNumber = nf.getNumber(secondOperand);
                    if (secondNumber == 1) {
                        flagOptimization = true;
                    } else if (secondNumber == -1) {
                        firstOperand = nf.createSymbolTree(Symbol.MINUS_SIGN, firstOperand);
                        flagOptimization = true;
                    } else if (secondNumber > 1 && isBinaryPower(secondNumber)) {
                        firstOperand = nf.createKeywordTree(
                            Keyword.LSH,
                            firstOperand,
                            nf.createNumberAtom(log2(secondNumber))
                        );
                        flagOptimization = true;
                    } else if (secondNumber < -1 && isBinaryPower(-secondNumber)) {
                        firstOperand = nf.createSymbolTree(
                            Symbol.MINUS_SIGN,
                            nf.createKeywordTree(
                                Keyword.LSH,
                                firstOperand,
                                nf.createNumberAtom(log2(-secondNumber))
                            )
                        );
                        flagOptimization = true;
                    }
                }
            } else if (symbol == Symbol.SOLIDUS) {
                if (isStringValue(firstOperand) || isStringValue(secondOperand)) {
                    throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                }
                if (nf.isNumberAtom(secondOperand)) {
                    final int secondNumber = nf.getNumber(secondOperand);
                    if (secondNumber == 0) {
                        throw new ZeroDivideException(ts.getLine(), ts.getPosition());
                    } else if (secondNumber == 1) {
                        flagOptimization = true;
                    } else if (secondNumber == -1) {
                        if (nf.isNumberAtom(firstOperand)) {
                            firstOperand = nf.createNumberAtom(-nf.getNumber(firstOperand));
                        } else {
                            firstOperand = nf.createSymbolTree(Symbol.MINUS_SIGN, firstOperand);
                        }
                        flagOptimization = true;
                    } else if (nf.isNumberAtom(firstOperand)) {
                        firstOperand = nf.createNumberAtom(nf.getNumber(firstOperand) / secondNumber);
                        flagOptimization = true;
                    }
                }
            } else {
                if (isStringValue(firstOperand) || isStringValue(secondOperand)) {
                    throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                }
                if (nf.isNumberAtom(secondOperand)) {
                    final int secondNumber = nf.getNumber(secondOperand);
                    if (secondNumber == 0) {
                        throw new ZeroDivideException(ts.getLine(), ts.getPosition());
                    } else if (secondNumber == 1 || secondNumber == -1) {
                        firstOperand = nf.createNumberAtom(0);
                        flagOptimization = true;
                    } else if (nf.isNumberAtom(firstOperand)) {
                        firstOperand = nf.createNumberAtom(nf.getNumber(firstOperand) % secondNumber);
                        flagOptimization = true;
                    }
                }
            }
            if (!flagOptimization) {
                firstOperand = nf.createSymbolTree(symbol, firstOperand, secondOperand);
            }
        }
        return firstOperand;
    }

    private Node parseOperand() throws ParserException {
        final NodeFactory nf = NodeFactory.getInstance();
        if (ts.isSymbol(Symbol.LEFT_PARENTHESIS)) {
            ts.nextToken();
            ts.skipEnd();
            final Node innerExporession = parse();
            ts.skipEnd();
            if (!ts.isSymbol(Symbol.RIGHT_PARENTHESIS)) {
                throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
            }
            ts.nextToken();
            return innerExporession;
        } else if (ts.isName()) {
            final String name = ts.getName();
            ts.nextToken();
            if (ts.isSymbol(Symbol.LEFT_PARENTHESIS)) {
                if (!context.isFunction(name)) {
                    throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                }
                ts.nextToken();
                ts.skipEnd();
                final Node callFunction = nf.createNameTree(name);
                if (!ts.isSymbol(Symbol.RIGHT_PARENTHESIS)) {
                    final Node firstArgument = parse();
                    nf.getChildren(callFunction).add(firstArgument);
                    while (ts.isSymbol(Symbol.COMMA)) {
                        ts.nextToken();
                        ts.skipEnd();
                        final Node nextArgument = parse();
                        nf.getChildren(callFunction).add(nextArgument);
                    }
                    ts.skipEnd();
                    if (!ts.isSymbol(Symbol.RIGHT_PARENTHESIS)) {
                        throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                    }
                }
                ts.nextToken();
                return callFunction;
            } else if (ts.isSymbol(Symbol.LEFT_SQUARE_BRACKET)) {
                if (!isStringName(name)) {
                    if (!context.isStaticArray(name) && !context.isDynamicArray(name)) {
                        throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                    }
                }
                ts.nextToken();
                ts.skipEnd();
                final Node index = parse();
                if (isStringValue(index)) {
                    throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                }
                ts.skipEnd();
                if (!ts.isSymbol(Symbol.RIGHT_SQUARE_BRACKET)) {
                    throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                }
                ts.nextToken();
                return nf.createNameTree(name, nf.createSymbolTree(Symbol.LEFT_SQUARE_BRACKET, index));
            } else {
                if (context.isNumberConstant(name)) {
                    final Integer value = context.getNumberConstantValue(name);
                    return nf.createNumberAtom((value != null) ? value.intValue() : 0);
                } else if (context.isStringConstant(name)) {
                    final String value = context.getStringConstantValue(name);
                    return nf.createStringAtom((value != null) ? value : "");
                } else if (context.isNumberVariable(name) || context.isStringVariable(name)) {
                    return nf.createNameAtom(name);
                } else {
                    throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
                }
            }
        } else if (ts.isString()) {
            final String string = ts.getString();
            ts.nextToken();
            context.addStringValue(string);
            return nf.createStringAtom(string);
        } else if (ts.isNumber()) {
            final int number = ts.getNumber();
            ts.nextToken();
            return nf.createNumberAtom(number);
        } else if (ts.isKeyword()) {
            final Keyword keyword = ts.getKeyword();
            if (isSystemConstant(keyword) || isSystemVariable(keyword)) {
                ts.nextToken();
                return nf.createKeywordAtom(keyword);
            } else {
                return (new FunctionParser(ts, context)).parse();
            }
        } else {
            throw new SyntaxErrorException(ts.getLine(), ts.getPosition());
        }
    }

    private static boolean isBinaryPower(int x) {
        return ((x - 1) & x) == 0;
    }

    private static int log2(int x) {
        int k = 0;
        int y = 1;
        while (y < x) {
            y <<= 1;
            k++;
        }
        return k;
    }
}
