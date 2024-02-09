package zbasic.compiler;

import zbasic.Keyword;
import zbasic.Symbol;

import java.util.List;

public final class NodeFactory {
    private static NodeFactory instance = null;

    private NodeFactory() {}

    public Node createKeywordAtom(Keyword keyword) {
        validateKeyword(keyword);
        return new Node.KeywordAtom(keyword);
    }

    public Node createNameAtom(String name) {
        validateName(name);
        return new Node.NameAtom(name);
    }

    public Node createStringAtom(String string) {
        validateString(string);
        return new Node.StringAtom(string);
    }

    public Node createNumberAtom(int number) {
        return new Node.NumberAtom(number);
    }

    public Node createSymbolTree(Symbol symbol, Node... children) {
        validateSymbol(symbol);
        final Node.Tree tree = new Node.SymbolTree(symbol);
        addChildren(tree, children);
        return tree;
    }

    public Node createKeywordTree(Keyword keyword, Node... children) {
        validateKeyword(keyword);
        final Node.Tree tree = new Node.KeywordTree(keyword);
        addChildren(tree, children);
        return tree;
    }

    public Node createNameTree(String name, Node... children) {
        validateName(name);
        final Node.Tree tree = new Node.NameTree(name);
        addChildren(tree, children);
        return tree;
    }

    public boolean isAtom(Node node) { return node instanceof Node.Atom; }

    public boolean isTree(Node node) { return node instanceof Node.Tree; }

    public boolean isKeywordAtom(Node node) { return node instanceof Node.KeywordAtom; }

    public boolean isNameAtom(Node node) { return node instanceof Node.NameAtom; }

    public boolean isStringAtom(Node node) { return node instanceof Node.StringAtom; }

    public boolean isNumberAtom(Node node) { return node instanceof Node.NumberAtom; }

    public boolean isSymbolTree(Node node) { return node instanceof Node.SymbolTree; }

    public boolean isKeywordTree(Node node) { return node instanceof Node.KeywordTree; }

    public boolean isNameTree(Node node) { return node instanceof Node.NameTree; }

    public Symbol getSymbol(Node node) {
        if (node instanceof Node.SymbolTree) {
            return ((Node.SymbolTree)node).getSymbol();
        } else {
            throw new IllegalArgumentException("node");
        }
    }

    public Keyword getKeyword(Node node) {
        if (node instanceof Node.KeywordAtom) {
            return ((Node.KeywordAtom)node).getKeyword();
        } else if (node instanceof Node.KeywordTree) {
            return ((Node.KeywordTree)node).getKeyword();
        } else {
            throw new IllegalArgumentException("node");
        }
    }

    public String getName(Node node) {
        if (node instanceof Node.NameAtom) {
            return ((Node.NameAtom)node).getName();
        } else if (node instanceof Node.NameTree) {
            return ((Node.NameTree)node).getName();
        } else {
            throw new IllegalArgumentException("node");
        }
    }

    public String getString(Node node) {
        if (node instanceof Node.StringAtom) {
            return ((Node.StringAtom)node).getString();
        } else {
            throw new IllegalArgumentException("node");
        }
    }

    public int getNumber(Node node) {
        if (node instanceof Node.NumberAtom) {
            return ((Node.NumberAtom)node).getNumber();
        } else {
            throw new IllegalArgumentException("node");
        }
    }

    public List<Node> getChildren(Node node) {
        if (node instanceof Node.Tree) {
            return ((Node.Tree)node).getChildren();
        } else {
            throw new IllegalArgumentException("node");
        }
    }

    private static void addChildren(Node.Tree tree, Node[] children) {
        for (Node node : children) {
            tree.add(node);
        }
    }

    private static void validateSymbol(Symbol symbol) {
        if (symbol == null) { throw new IllegalArgumentException("symbol"); }
    }

    private static void validateKeyword(Keyword keyword) {
        if (keyword == null) { throw new IllegalArgumentException("keyword"); }
    }

    private static void validateName(String name) {
        if (name == null) { throw new IllegalArgumentException("name"); }
    }

    private static void validateString(String string) {
        if (string == null) { throw new IllegalArgumentException("string"); }
    }

    public static NodeFactory getInstance() {
        if (instance == null) {
            synchronized (NodeFactory.class) {
                instance = new NodeFactory();
            }
        }
        return instance;
    }
}
