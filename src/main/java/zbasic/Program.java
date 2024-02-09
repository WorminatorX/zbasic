package zbasic;

public class Program {
    public static void main(String[] args) {
        boolean flagQuit = false;
        while (!flagQuit) {
            System.out.print("> ");
            final String line = System.console().readLine();
            if (
                line != null && (
                    line.toUpperCase().equals("/QUIT") ||
                    line.toUpperCase().equals("/EXIT")
                )
            ) { flagQuit = true; }
        }
    }
}
