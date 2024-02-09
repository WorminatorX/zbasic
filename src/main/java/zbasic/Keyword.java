package zbasic;

public enum Keyword {
    ABS("ABS"),
    AND("AND"),
    BAND("BAND"),
    BIT("BIT"),
    BNOT("BNOT"),
    BOR("BOR"),
    BOUND("BOUND"),
    BXOR("BXOR"),
    BEGIN("BEGIN"),
    BRESET("BRESET"),
    BSET("BSET"),
    CALL("CALL"),
    CASE("CASE"),
    CHR_("CHR", true),
    CLS("CLS"),
    CODE("CODE"),
    CONST("CONST"),
    COPY("COPY"),
    DATA("DATA"),
    DEC("DEC"),
    DEF("DEF"),
    DEFAULT("DEFAULT"),
    DIM("DIM"),
    END("END"),
    ELSE("ELSE"),
    EXPORT("EXPORT"),
    FALSE("FALSE"),
    FILL("FILL"),
    FOR("FOR"),
    FREE("FREE"),
    FUNCTION("FUNCTION"),
    IF("IF"),
    INC("INC"),
    INPUT("INPUT"),
    INSTR("INSTR"),
    IMPORT("IMPORT"),
    LEFT_("LEFT", true),
    LEN("LEN"),
    LET("LET"),
    LSH("LSH"),
    MID_("MID", true),
    NOT("NOT"),
    OR("OR"),
    PAUSE("PAUSE"),
    PRINT("PRINT"),
    QUIT("QUIT"),
    RANDOM("RANDOM"),
    READ("READ"),
    REDIM("REDIM"),
    REM("REM"),
    REPEAT("REPEAT"),
    RESTORE("RESTORE"),
    RETURN("RETURN"),
    RIGHT_("RIGHT", true),
    RSH("RSH"),
    SELECT("SELECT"),
    STEP("STEP"),
    STR_("STR", true),
    SUB("SUB"),
    SWAP("SWAP"),
    THEN("THEN"),
    TIMER("TIMER"),
    TO("TO"),
    TRUE("TRUE"),
    UNTIL("UNTIL"),
    USH("USH"),
    VAL("VAL"),
    VAR("VAR"),
    WHILE("WHILE"),
    WITH("WITH");

    public static final char STRING_SUFFIX = '$';

    private final String name;
    private final boolean string;

    private Keyword(String name, boolean string) {
        this.name = name;
        this.string = string;
    }

    private Keyword(String name) {
        this(name, false);
    }

    public String getName() { return name; }

    public boolean isString() { return string; }

    public String getText() { return string ? (name + STRING_SUFFIX) : name; }

    public static Keyword getKeyword(String name) {
        if (name == null || name.isEmpty()) { return null; }
        final boolean string = name.charAt(name.length() - 1) == STRING_SUFFIX;
        final String pureName = string ? name.substring(0, name.length() - 1) : name;
        final Keyword[] keywords = Keyword.values();
        for (Keyword keyword : keywords) {
            if (keyword.string == string && keyword.name.equals(pureName)) { return keyword; }
        }
        return null;
    }

    public static boolean isSystemConstant(Keyword keyword) {
        return keyword == Keyword.FALSE || keyword == Keyword.TRUE;
    }

    public static boolean isSystemVariable(Keyword keyword) {
        return keyword == Keyword.TIMER;
    }

    public static boolean isStringName(String name) {
        return name.charAt(name.length() - 1) == Keyword.STRING_SUFFIX;
    }
}
