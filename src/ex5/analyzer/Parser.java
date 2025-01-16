package ex5.analyzer;

import java.util.regex.Pattern;

public class Parser {
    Pattern commentPattern = Pattern.compile("^//.*");
    Pattern intDecPattern = Pattern.compile(
            "^(final +)?int +(((_)?[A-Za-z]\\w*)( *= *([+-]?\\d+|((_)?[A-Za-z]\\w*)))?)"
                    + "( *, *((_)?[A-Za-z]\\w*)( *= *([+-]?\\d+|((_)?[A-Za-z]\\w*)))?)*;$"
    );



    public boolean compileFile() {
        return false;
    }

    private boolean compileMethod() {
        return false;
    }

    private boolean compileVarDeclaration() {
        return false;
    }
}
