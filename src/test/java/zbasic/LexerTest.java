package zbasic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Lexer;
import zbasic.compiler.LexerException;
import zbasic.compiler.Token;
import zbasic.compiler.TokenFactory;

import org.junit.Test;

public class LexerTest {

    @Test
    public void testTokens() {
        final TokenFactory tf = TokenFactory.getInstance();

        final Token endToken = tf.createEndToken(1);
        assertTrue(tf.isEndToken(endToken));
        assertEquals(endToken.getPosition(), 1);

        final Token commaToken = tf.createSymbolToken(2, Symbol.COMMA);
        assertTrue(tf.isSymbolToken(commaToken));
        assertEquals(tf.getSymbol(commaToken), Symbol.COMMA);
        assertEquals(commaToken.getPosition(), 2);

        final Token ifToken = tf.createNameOrKeywordToken(3, "IF");
        assertTrue(tf.isKeywordToken(ifToken));
        assertEquals(tf.getKeyword(ifToken), Keyword.IF);
        assertEquals(ifToken.getPosition(), 3);

        final Token strToken = tf.createNameOrKeywordToken(4, "STR$");
        assertTrue(tf.isKeywordToken(strToken));
        assertEquals(tf.getKeyword(strToken), Keyword.STR_);
        assertEquals(strToken.getPosition(), 4);

        final Token abcToken = tf.createNameOrKeywordToken(5, "ABC");
        assertTrue(tf.isNameToken(abcToken));
        assertEquals(tf.getName(abcToken), "ABC");
        assertEquals(abcToken.getPosition(), 5);

        final Token helloToken = tf.createStringToken(6, "Hello World!");
        assertTrue(tf.isStringToken(helloToken));
        assertEquals(tf.getString(helloToken), "Hello World!");
        assertEquals(helloToken.getPosition(), 6);

        final Token number42Token = tf.createNumberToken(7, 42);
        assertTrue(tf.isNumberToken(number42Token));
        assertEquals(tf.getNumber(number42Token), 42);
        assertEquals(number42Token.getPosition(), 7);
    }

    @Test
    public void testLexer() {
        final Lexer lexer = new Lexer();
        try {
            lexer.extractTokens("REM Code example");
            lexer.extractTokens("PRINT \"Hello World!\"");
            lexer.extractTokens("");
            lexer.extractTokens("PRINT XYZ*(123+LEN(\"ABC\")-1)");
            lexer.extractTokens("PRINT A$;B[1]");
            lexer.extractTokens("QUIT");
            assertEquals(lexer.getTokens().size(), 29);
        } catch (LexerException ex) {
            fail(ex.getMessage());
        }
    }
}
