package zbasic.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Subroutine {
    private final Set<String> arguments;
    private final List<Node.KeywordTree> statements;

    public Subroutine() {
        this.arguments = new TreeSet<>();
        this.statements = new ArrayList<>();
    }

    public Set<String> getArguments() { return arguments; }

    public List<Node.KeywordTree> getStatements() { return statements; }

    public void addArgument(String name) {
        if (name != null) {
            if (name.isEmpty()) { throw new IllegalArgumentException("name"); }
            arguments.add(name);
        }
    }

    public void addStatement(Node statement) {
        if (statement instanceof Node.KeywordTree) {
            statements.add((Node.KeywordTree)statement);
        } else {
            throw new IllegalArgumentException("statement");
        }
    }
}
