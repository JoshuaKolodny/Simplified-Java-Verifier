package ex5.parser;

import java.util.regex.Pattern;


public class RegexPatterns {
    private static final String IDENTIFIER_NAME = "_?[A-Za-z]\\w* *";
    private static final String INTEGER_PATTERN = "[+-]?\\d+ *";
    private static final String FINAL = "(?:final +)? *";
    private static final String DOUBLE_PATTERN = "[+-]?\\.\\d+|\\d+\\.\\d* *";
    private static final String STRING_PATTERN = "\".*\" *";
    private static final String CHAR_PATTERN = "'.' *";
    private static final String BOOLEAN_PATTERN = "\btrue\b$|^\bfalse\b *";
    private static final String CONDITION_PARAM = IDENTIFIER_NAME + "|" + INTEGER_PATTERN + "|" + DOUBLE_PATTERN + "|" + BOOLEAN_PATTERN;
    private static final String CONDITION_PATTERN = " *" + CONDITION_PARAM +
            "(?:\\|\\||&&" + CONDITION_PARAM + ")*";
    private static final String ASSIGNMENT_PATTERN = IDENTIFIER_NAME + "(?:= *" + DOUBLE_PATTERN
            + "|" + IDENTIFIER_NAME + "|" + INTEGER_PATTERN + "|" + STRING_PATTERN + "|"
            + CHAR_PATTERN + "|" + BOOLEAN_PATTERN + ")";
    private static final String VARIABLE_TYPES = "int|double|char|String|boolean +";
    private static final String METHOD_PARAM = FINAL + VARIABLE_TYPES + IDENTIFIER_NAME;
    Pattern commentPattern = Pattern.compile("^//.*");
    Pattern varDecPattern = Pattern.compile(
            "^" + FINAL + VARIABLE_TYPES + ASSIGNMENT_PATTERN + "? *(?:, *" + ASSIGNMENT_PATTERN + "? *)*;$"
    );
    Pattern assignmentPattern = Pattern.compile(ASSIGNMENT_PATTERN);
    Pattern methodDecPattern = Pattern.compile("^void +" + IDENTIFIER_NAME + "\\("
            + " *(?:" + METHOD_PARAM + "(?:, *" + METHOD_PARAM + ")*)?" + "\\) *\\{$");
    Pattern methodCallPattern = Pattern.compile(IDENTIFIER_NAME + "\\( *"
            + "(?:" + IDENTIFIER_NAME + "(?:, *" + IDENTIFIER_NAME + ")*)?" + "\\) *;$");
    Pattern returnPattern = Pattern.compile("^return *;$");
    Pattern ifPattern = Pattern.compile("^if *\\(" + CONDITION_PATTERN + "\\) *\\{$");
    Pattern whilePattern = Pattern.compile("^while *\\(" + CONDITION_PATTERN + "\\) *\\{$");
    Pattern test = Pattern.compile("^(?:final +)?(?:" + VARIABLE_TYPES + ") +_?[A-Za-z]\\w*( *= *(?:[+-]?\\.\\d+|\\d+\\.\\d*|_?[A-Za-z]\\w*|[+-]?\\d+|\".*\"|'.'|\btrue\b|\bfalse\b))?(?: *, *_?[A-Za-z]\\w*( *= *(?:[+-]?\\d+|_?[A-Za-z]\\w*|[+-]?\\.\\d+|\\d+\\.\\d*|\".*\"|'.?'\btrue\b|\bfalse\b))?)*;$");

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
