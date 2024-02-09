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
import zbasic.generator.zvm.Generator;
import zbasic.generator.zvm.Opcode;

import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

public class GeneratorTest {
    @Test
    public void testEmptyByteCode() {
        final Generator generator = new Generator();
        final byte[] byteCode = generator.generate(new Module(""));
        final int sectionCount = 11;
        assertEquals(byteCode.length, Generator.SIGNATURE.length() + 1 + 1 + sectionCount);
        int i = 0;
        for (i = 0; i < Generator.SIGNATURE.length(); i++) {
            assertEquals((int)byteCode[i], (int)Generator.SIGNATURE.charAt(i));
        }
        assertEquals((int)byteCode[i++], Generator.VERSION);
        assertEquals((int)byteCode[i++], 0);
        for (int j = 0; j < sectionCount; j++) {
            assertEquals((int)byteCode[i++], 0);
        }
    }

    @Test
    public void testHelloWorldByteCode() {
        final Lexer lexer = new Lexer();
        try {
            lexer.extractTokens("BEGIN");
            lexer.extractTokens("PRINT \"Hello World!\"");
            lexer.extractTokens("END");
        } catch (LexerException ex) {
            fail(ex.getMessage());
        }

        try {
            final TokenScanner ts = new TokenScanner(lexer.getTokens());
            final Parser parser = new Parser(ts);
            final Module module = parser.parse();

            assertEquals(module.getStringValues().size(), 1);
            assertEquals(module.getInitialization().size(), 1);
            final String helloWorld = module.getStringValues().iterator().next();

            final Generator generator = new Generator();
            final byte[] byteCode = generator.generate(module);
            final int sectionCount = 11;
            assertEquals(
                byteCode.length,
                Generator.SIGNATURE.length() + 1 + 1 + sectionCount +
                1 + helloWorld.length()
            );
            int i = 0;
            for (i = 0; i < Generator.SIGNATURE.length(); i++) {
                assertEquals((int)byteCode[i], (int)Generator.SIGNATURE.charAt(i));
            }
            assertEquals((int)byteCode[i++], Generator.VERSION);
            assertEquals((int)byteCode[i++], 0);
            assertEquals((int)byteCode[i++], 0);
            assertEquals((int)byteCode[i++], 1);
            for (int j = 2; j < sectionCount; j++) {
                assertEquals((int)byteCode[i++], 0);
            }
            assertEquals((int)byteCode[i++], helloWorld.length());
            final char[] chars = helloWorld.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                assertEquals(byteCode[i++], chars[j]);
            }
        } catch (ParserException ex) {
            fail(ex.getMessage());
        }
    }
}
