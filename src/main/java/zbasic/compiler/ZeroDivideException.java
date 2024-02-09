package zbasic.compiler;

public class ZeroDivideException extends ParserException {
    public ZeroDivideException(int line, int position) {
        super("Divide by zero", line, position);
    }
}
