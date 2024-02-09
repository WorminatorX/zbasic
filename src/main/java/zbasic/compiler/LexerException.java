package zbasic.compiler;

public class LexerException extends Exception {
    private final int lineNumber;
    private final int positionNumber;

    public LexerException(int lineNumber, int positionNumber) {
        super("Lexer exception at " + lineNumber + ", " + positionNumber);
        this.lineNumber = lineNumber;
        this.positionNumber = positionNumber;
    }

    public int getLineNumber() { return lineNumber; }

    public int getPositionNumber() { return positionNumber; }
}