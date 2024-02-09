package zbasic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import zbasic.Keyword;
import zbasic.Symbol;
import zbasic.compiler.Node;
import zbasic.compiler.NodeFactory;

import org.junit.Test;

public class MainTest {

    @Test
    public void testKeywordsAndSymbols() {
        final Symbol leSymbol = Symbol.getSymbol("<=");
        assertEquals(leSymbol, Symbol.LESS_OR_EQUALS_SIGN);
        assertEquals(leSymbol.getText(), "<=");

        final Keyword endKeyword = Keyword.getKeyword("END");
        assertEquals(endKeyword, Keyword.END);
        assertEquals(endKeyword.getText(), "END");
        assertEquals(endKeyword.getName(), "END");
        assertFalse(endKeyword.isString());

        final Keyword midKeyword = Keyword.getKeyword("MID$");
        assertEquals(midKeyword, Keyword.MID_);
        assertEquals(midKeyword.getText(), "MID$");
        assertEquals(midKeyword.getName(), "MID");
        assertTrue(midKeyword.isString());
    }

    @Test
    public void testNodes() {
        final NodeFactory nf = NodeFactory.getInstance();

        final Node andAtom = nf.createKeywordAtom(Keyword.AND);
        assertTrue(nf.isKeywordAtom(andAtom));
        assertEquals(nf.getKeyword(andAtom), Keyword.AND);

        final Node a1Atom = nf.createNameAtom("A1");
        assertTrue(nf.isNameAtom(a1Atom));
        assertEquals(nf.getName(a1Atom), "A1");

        final Node helloAtom = nf.createStringAtom("Hello World!");
        assertTrue(nf.isStringAtom(helloAtom));
        assertEquals(nf.getString(helloAtom), "Hello World!");

        final Node zeroAtom = nf.createNumberAtom(0);
        assertTrue(nf.isNumberAtom(zeroAtom));
        assertEquals(nf.getNumber(zeroAtom), 0);

        final Node plusTree = nf.createSymbolTree(
            Symbol.PLUS_SIGN,
            nf.createNumberAtom(2),
            nf.createNumberAtom(3)
        );
        assertTrue(nf.isSymbolTree(plusTree));
        assertEquals(nf.getSymbol(plusTree), Symbol.PLUS_SIGN);
        assertEquals(nf.getChildren(plusTree).size(), 2);
        assertEquals(nf.getNumber(nf.getChildren(plusTree).iterator().next()), 2);

        final Node lenTree = nf.createKeywordTree(
            Keyword.LEN,
            nf.createStringAtom("A")
        );
        assertTrue(nf.isKeywordTree(lenTree));
        assertEquals(nf.getKeyword(lenTree), Keyword.LEN);
        assertEquals(nf.getChildren(lenTree).size(), 1);
        assertEquals(nf.getString(nf.getChildren(lenTree).iterator().next()), "A");

        final Node qTree = nf.createNameTree("Q");
        assertTrue(nf.isNameTree(qTree));
        assertEquals(nf.getName(qTree), "Q");
        assertEquals(nf.getChildren(qTree).size(), 0);
    }
}
