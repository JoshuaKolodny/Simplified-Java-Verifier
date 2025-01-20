package ex5.model.statements;

/**
 * Base class for any statement in a method (e.g., assignment, return, if/while, etc.).
 */
public abstract class Statement {
    // Possibly store the raw text or line number
    private final String rawLine;

    public Statement(String rawLine) {
        this.rawLine = rawLine;
    }

    public String getRawLine() {
        return rawLine;
    }

    public abstract void validate() throws Exception;
}

