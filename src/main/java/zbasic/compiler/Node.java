package zbasic.compiler;

import zbasic.Keyword;
import zbasic.Symbol;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    public static abstract class Atom extends Node {}

    public static abstract class Tree extends Node {
        private final List<Node> children;

        protected Tree() {
            this.children = new ArrayList<>();
        }

        public List<Node> getChildren() { return children; }

        public void add(Node node) {
            if (node == null) { throw new IllegalArgumentException("node"); }
            children.add(node);
        }
    }

    public static final class KeywordAtom extends Atom {
        private final Keyword keyword;

        public KeywordAtom(Keyword keyword) {
            this.keyword = keyword;
        }

        public Keyword getKeyword() { return keyword; }
    }

    public static final class NameAtom extends Atom {
        private final String name;

        public NameAtom(String name) {
            this.name = name;
        }

        public String getName() { return name; }
    }

    public static final class StringAtom extends Atom {
        private final String string;

        public StringAtom(String string) {
            this.string = string;
        }

        public String getString() { return string; }
    }

    public static final class NumberAtom extends Atom {
        private final int number;

        public NumberAtom(int number) {
            this.number = number;
        }

        public int getNumber() { return number; }
    }

    public static final class SymbolTree extends Tree {
        private final Symbol symbol;

        public SymbolTree(Symbol symbol) {
            this.symbol = symbol;
        }

        public Symbol getSymbol() { return symbol; }
    }

    public static final class KeywordTree extends Tree {
        private final Keyword keyword;

        public KeywordTree(Keyword keyword) {
            this.keyword = keyword;
        }

        public Keyword getKeyword() { return keyword; }
    }

    public static final class NameTree extends Tree {
        private final String name;

        public NameTree(String name) {
            this.name = name;
        }

        public String getName() { return name; }
    }
}
