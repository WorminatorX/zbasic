package zbasic.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public final class Function extends Subroutine {
    private final boolean string;

    public Function(boolean string) {
        super();
        this.string = string;
    }

    public boolean isString() { return string; }
}
