package zbasic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import zbasic.Keyword;
import zbasic.compiler.Context;
import zbasic.compiler.ExpressionParser;
import zbasic.compiler.Lexer;
import zbasic.compiler.LexerException;
import zbasic.compiler.Module;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;
import zbasic.compiler.Parser;
import zbasic.compiler.ParserException;
import zbasic.compiler.TokenScanner;
import zbasic.compiler.statement.StatementParser;

import org.junit.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ParserTest {
    @Test
    public void testExpressionParser() {
        final Lexer lexer = new Lexer();
        try {
            lexer.extractTokens("1+2*(3+4)");
            lexer.extractTokens("\"A\" & \"B\" & \"C\"");
            lexer.extractTokens("\"A\" < \"AB\" AND NOT 0 >= 1");
            lexer.extractTokens("A$");
        } catch (LexerException ex) {
            fail(ex.getMessage());
        }

        try {
            final NodeFactory nf = NodeFactory.getInstance();
            final TokenScanner ts = new TokenScanner(lexer.getTokens());

            final Context context = new Context();
            context.addStringConstant("A$", "A");

            final Node numberResult = (new ExpressionParser(ts, context)).parse();
            assertTrue(nf.isNumberAtom(numberResult));
            assertEquals(nf.getNumber(numberResult), 1 + 2 * (3 + 4));
            assertTrue(ts.isEnd());
            ts.nextToken();

            final Node stringResult = (new ExpressionParser(ts, context)).parse();
            assertTrue(nf.isStringAtom(stringResult));
            assertEquals(nf.getString(stringResult), "ABC");
            assertTrue(ts.isEnd());
            ts.nextToken();

            final Node compResult = (new ExpressionParser(ts, context)).parse();
            assertTrue(nf.isNumberAtom(compResult));
            assertEquals(nf.getNumber(compResult), -1);
            assertTrue(ts.isEnd());
            ts.nextToken();

            final Node constantResult = (new ExpressionParser(ts, context)).parse();
            assertTrue(nf.isStringAtom(constantResult));
            assertEquals(nf.getString(constantResult), "A");
            assertTrue(ts.isEnd());
            ts.nextToken();

            assertTrue(ts.getCurrentToken() == null);
        } catch (ParserException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testStatements() {
        final Lexer lexer = new Lexer();
        try {
            lexer.extractTokens("CLS");
            lexer.extractTokens("PRINT 1;2, \"Hello\"");
            lexer.extractTokens("INPUT Z");
            lexer.extractTokens("PAUSE A$[Z]");
            lexer.extractTokens("WHILE FALSE");
            lexer.extractTokens("INC X, 10");
            lexer.extractTokens("DEC Y");
            lexer.extractTokens("SWAP X, Y");
            lexer.extractTokens("END WHILE");
            lexer.extractTokens("RESTORE A$");
            lexer.extractTokens("FOR I = 1 TO 100 STEP 10");
            lexer.extractTokens("READ C");
            lexer.extractTokens("PRINT C");
            lexer.extractTokens("END FOR");
            lexer.extractTokens("QUIT");
        } catch (LexerException ex) {
            fail(ex.getMessage());
        }

        try {
            final NodeFactory nf = NodeFactory.getInstance();
            final TokenScanner ts = new TokenScanner(lexer.getTokens());

            final Context context = new Context();
            context.addNumberVariable("C");
            context.addNumberVariable("I");
            context.addNumberVariable("X");
            context.addNumberVariable("Y");
            context.addNumberVariable("Z");
            context.addStringVariable("A$");

            final Node clsStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(clsStatement));
            assertEquals(nf.getKeyword(clsStatement), Keyword.CLS);
            assertEquals(nf.getChildren(clsStatement).size(), 0);

            final Node printStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(printStatement));
            assertEquals(nf.getKeyword(printStatement), Keyword.PRINT);
            assertEquals(nf.getChildren(printStatement).size(), 5);

            final Node inputStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(inputStatement));
            assertEquals(nf.getKeyword(inputStatement), Keyword.INPUT);
            assertEquals(nf.getChildren(inputStatement).size(), 1);

            final Node pauseStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(pauseStatement));
            assertEquals(nf.getKeyword(pauseStatement), Keyword.PAUSE);
            assertEquals(nf.getChildren(pauseStatement).size(), 1);

            final Node whileStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(whileStatement));
            assertEquals(nf.getKeyword(whileStatement), Keyword.WHILE);
            assertEquals(nf.getChildren(whileStatement).size(), 2);
            final Iterator<Node> whileIterator = nf.getChildren(whileStatement).iterator();
            assertTrue(whileIterator.hasNext());
            whileIterator.next();
            assertTrue(whileIterator.hasNext());
            final Node whileBody = whileIterator.next();
            assertFalse(whileIterator.hasNext());
            assertTrue(nf.isKeywordTree(whileBody));
            assertEquals(nf.getKeyword(whileBody), Keyword.CODE);
            assertEquals(nf.getChildren(whileBody).size(), 3);

            final Node restoreStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(restoreStatement));
            assertEquals(nf.getKeyword(restoreStatement), Keyword.RESTORE);
            assertEquals(nf.getChildren(restoreStatement).size(), 1);

            final Node forStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(forStatement));
            assertEquals(nf.getKeyword(forStatement), Keyword.FOR);
            assertEquals(nf.getChildren(forStatement).size(), 5);

            final Node quitStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(quitStatement));
            assertEquals(nf.getKeyword(quitStatement), Keyword.QUIT);
            assertEquals(nf.getChildren(quitStatement).size(), 0);

            assertTrue(ts.getCurrentToken() == null);
        } catch (ParserException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testIfStatement() {
        final Lexer lexer = new Lexer();
        try {
            lexer.extractTokens("IF 0 <> 1 THEN PRINT");
            lexer.extractTokens("IF 0 THEN");
            lexer.extractTokens("END IF");
            lexer.extractTokens("IF K THEN");
            lexer.extractTokens("  PRINT \"THEN WISE\"");
            lexer.extractTokens("ELSE");
            lexer.extractTokens("  PRINT \"ELSE WISE\"");
            lexer.extractTokens("END IF");
            lexer.extractTokens("IF I < J THEN");
            lexer.extractTokens("  PRINT \"I < J\"");
            lexer.extractTokens("ELSE IF I > J THEN");
            lexer.extractTokens("  PRINT \"I > J\"");
            lexer.extractTokens("ELSE IF I = J THEN");
            lexer.extractTokens("  PRINT \"I = J\"");
            lexer.extractTokens("ELSE");
            lexer.extractTokens("  PRINT \"???\"");
            lexer.extractTokens("END IF");
        } catch (LexerException ex) {
            fail(ex.getMessage());
        }

        try {
            final NodeFactory nf = NodeFactory.getInstance();
            final TokenScanner ts = new TokenScanner(lexer.getTokens());

            final Context context = new Context();
            context.addNumberVariable("I");
            context.addNumberVariable("J");
            context.addNumberVariable("K");

            final Node shortIfStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(shortIfStatement));
            assertEquals(nf.getKeyword(shortIfStatement), Keyword.IF);
            assertEquals(nf.getChildren(shortIfStatement).size(), 2);

            final Node longIfStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(longIfStatement));
            assertEquals(nf.getKeyword(longIfStatement), Keyword.IF);
            assertEquals(nf.getChildren(longIfStatement).size(), 2);

            final Node ifElseStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(ifElseStatement));
            assertEquals(nf.getKeyword(ifElseStatement), Keyword.IF);
            assertEquals(nf.getChildren(ifElseStatement).size(), 3);

            final Node multiwiseIfStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(multiwiseIfStatement));
            assertEquals(nf.getKeyword(multiwiseIfStatement), Keyword.IF);
            assertEquals(nf.getChildren(multiwiseIfStatement).size(), 3);

            assertTrue(ts.getCurrentToken() == null);
        } catch (ParserException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testSelectStatement() {
        final Lexer lexer = new Lexer();
        try {
            lexer.extractTokens("SELECT CASE N");
            lexer.extractTokens("CASE 1, 2");
            lexer.extractTokens("  QUIT");
            lexer.extractTokens("DEFAULT");
            lexer.extractTokens("END SELECT");
        } catch (LexerException ex) {
            fail(ex.getMessage());
        }

        try {
            final NodeFactory nf = NodeFactory.getInstance();
            final TokenScanner ts = new TokenScanner(lexer.getTokens());

            final Context context = new Context();
            context.addNumberVariable("N");

            final Node selectStatement = (new StatementParser(ts, context)).parse();
            assertTrue(nf.isKeywordTree(selectStatement));
            assertEquals(nf.getKeyword(selectStatement), Keyword.SELECT);

            final Iterator<Node> selectIterator = nf.getChildren(selectStatement).iterator();

            assertTrue(selectIterator.hasNext());
            final Node selectedValue = selectIterator.next();
            assertTrue(nf.isNameAtom(selectedValue));
            assertEquals(nf.getName(selectedValue), "N");

            assertTrue(selectIterator.hasNext());
            final Node caseWise = selectIterator.next();
            assertTrue(nf.isKeywordTree(caseWise));
            assertEquals(nf.getKeyword(caseWise), Keyword.CASE);
            final Iterator<Node> caseIterator = nf.getChildren(caseWise).iterator();
            assertTrue(caseIterator.hasNext());

            final Node caseData = caseIterator.next();
            assertTrue(nf.isKeywordTree(caseData));
            assertEquals(nf.getKeyword(caseData), Keyword.DATA);
            assertEquals(nf.getChildren(caseData).size(), 2);
            assertTrue(caseIterator.hasNext());

            final Node caseCode = caseIterator.next();
            assertTrue(nf.isKeywordTree(caseCode));
            assertEquals(nf.getKeyword(caseCode), Keyword.CODE);
            assertEquals(nf.getChildren(caseCode).size(), 1);

            assertFalse(caseIterator.hasNext());

            assertTrue(selectIterator.hasNext());
            final Node defaultWise = selectIterator.next();
            assertTrue(nf.isKeywordTree(defaultWise));
            assertEquals(nf.getKeyword(defaultWise), Keyword.CASE);
            final Iterator<Node> defaultIterator = nf.getChildren(defaultWise).iterator();
            assertTrue(defaultIterator.hasNext());

            final Node defaultData = defaultIterator.next();
            assertTrue(nf.isKeywordTree(defaultData));
            assertEquals(nf.getKeyword(defaultData), Keyword.DATA);
            assertEquals(nf.getChildren(defaultData).size(), 0);
            assertTrue(defaultIterator.hasNext());

            final Node defaultCode = defaultIterator.next();
            assertTrue(nf.isKeywordTree(defaultCode));
            assertEquals(nf.getKeyword(defaultCode), Keyword.CODE);
            assertEquals(nf.getChildren(defaultCode).size(), 0);

            assertFalse(defaultIterator.hasNext());

            assertFalse(selectIterator.hasNext());

            assertTrue(ts.getCurrentToken() == null);
        } catch (ParserException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testModule() {
        final Module module = new Module("");
        module.addDynamicArray("A");
        module.addStaticArray("S", 2);
        module.addNumberConstant("N", 1);
        module.addStringConstant("A$", "ABC");
        module.addNumberVariable("X");
        module.addNumberVariable("Y", -1);
        module.addStringVariable("Z$");
        module.addStringVariable("P$", ".");

        final Set<String> dynamicArrays = module.getDynamicArrays();
        final Map<String, Integer> staticArrays = module.getStaticArrays();
        final Map<String, Integer> numberConstants = module.getNumberConstants();
        final Map<String, Integer> numberVariables = module.getNumberVariables();
        final Map<String, String> stringConstants = module.getStringConstants();
        final Map<String, String> stringVariables = module.getStringVariables();

        assertTrue(dynamicArrays.contains("A"));
        assertEquals(staticArrays.get("S"), Integer.valueOf(2));
        assertEquals(numberConstants.get("N"), Integer.valueOf(1));
        assertEquals(stringConstants.get("A$"), "ABC");
        assertEquals(numberVariables.get("X"), Integer.valueOf(0));
        assertEquals(numberVariables.get("Y"), Integer.valueOf(-1));
        assertEquals(stringVariables.get("Z$"), "");
        assertEquals(stringVariables.get("P$"), ".");

        assertEquals(module.getInitialization().size(), 0);
    }

    @Test
    public void testEmptyProgram() {
        try {
            final NodeFactory nf = NodeFactory.getInstance();
            final Lexer lexer = new Lexer();
            final TokenScanner ts = new TokenScanner(lexer.getTokens());
            final Parser parser = new Parser(ts);
            final Module module = parser.parse();

            final Iterator<Node.KeywordTree> initialization = module.getInitialization().iterator();
            assertFalse(initialization.hasNext());
        } catch (ParserException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testHelloWorldProgram() {
        final Lexer lexer = new Lexer();
        try {
            lexer.extractTokens("BEGIN");
            lexer.extractTokens("PRINT \"Hello World!\"");
            lexer.extractTokens("END");
        } catch (LexerException ex) {
            fail(ex.getMessage());
        }

        try {
            final NodeFactory nf = NodeFactory.getInstance();
            final TokenScanner ts = new TokenScanner(lexer.getTokens());
            final Parser parser = new Parser(ts);
            final Module module = parser.parse();

            final Iterator<Node.KeywordTree> initialization = module.getInitialization().iterator();
            assertTrue(initialization.hasNext());
            final Node.KeywordTree statementPrint = initialization.next();
            assertEquals(nf.getKeyword(statementPrint), Keyword.PRINT);
            final Iterator<Node> printArguments = nf.getChildren(statementPrint).iterator();
            assertTrue(printArguments.hasNext());
            final Node printableString = printArguments.next();
            assertTrue(nf.isStringAtom(printableString));
            assertEquals(nf.getString(printableString), "Hello World!");
            assertTrue(printArguments.hasNext());
            final Node endLine = printArguments.next();
            assertTrue(nf.isKeywordTree(endLine));
            assertEquals(nf.getKeyword(endLine), Keyword.CHR_);
            final Iterator<Node> endLineArguments = nf.getChildren(endLine).iterator();
            assertTrue(endLineArguments.hasNext());
            final Node charCode = endLineArguments.next();
            assertTrue(nf.isNumberAtom(charCode));
            assertEquals(nf.getNumber(charCode), 0x0D);
            assertFalse(endLineArguments.hasNext());
            assertFalse(printArguments.hasNext());
            assertFalse(initialization.hasNext());
        } catch (ParserException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testSummProgram() {
        final Lexer lexer = new Lexer();
        try {
            lexer.extractTokens("VAR N, M");
            lexer.extractTokens("DEF Summ(x, y) = x + y");
            lexer.extractTokens("SUB Init()");
            lexer.extractTokens("N = 2");
            lexer.extractTokens("M = 3");
            lexer.extractTokens("END SUB");
            lexer.extractTokens("BEGIN");
            lexer.extractTokens("Init");
            lexer.extractTokens("PRINT Summ(N, M)");
            lexer.extractTokens("END");
        } catch (LexerException ex) {
            fail(ex.getMessage());
        }

        try {
            final NodeFactory nf = NodeFactory.getInstance();
            final TokenScanner ts = new TokenScanner(lexer.getTokens());
            final Parser parser = new Parser(ts);
            final Module module = parser.parse();

            assertEquals(module.getNumberVariables().get("N"), Integer.valueOf(0));
            assertEquals(module.getNumberVariables().get("M"), Integer.valueOf(0));
            assertEquals(module.getFunctions().get("Summ").getArguments().size(), 2);
            assertEquals(module.getFunctions().get("Summ").getStatements().size(), 1);
            assertEquals(module.getSubroutines().get("Init").getArguments().size(), 0);
            assertEquals(module.getSubroutines().get("Init").getStatements().size(), 2);
            assertEquals(module.getInitialization().size(), 2);
        } catch (ParserException ex) {
            fail(ex.getMessage());
        }
    }
}
