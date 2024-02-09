package zbasic.compiler;

public abstract class ParserException extends Exception {
    private final int line;
    private final int position;

    public ParserException(String title, int line, int position) {
        super(title + " at " + line + ", " + position);
        this.line = line;
        this.position = position;
    }

    public int getLine() { return line; }

    public int getPosition() { return position; }
}