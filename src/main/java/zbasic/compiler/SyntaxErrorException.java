package zbasic.compiler;

public class SyntaxErrorException extends ParserException {
    public SyntaxErrorException(int line, int position) {
        super("Syntax error", line, position);
    }
}
