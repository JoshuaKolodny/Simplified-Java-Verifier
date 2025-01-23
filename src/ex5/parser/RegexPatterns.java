package ex5.parser;

import ex5.Constants.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class RegexPatterns {
    private static final String IDENTIFIER_NAME = "_?[A-Za-z]\\w*+\\s*+";
    private static final String METHOD_NAME = "[A-Za-z]\\w*+\\s*+";
    private static final String INTEGER_REGEX = "[+-]?+\\d++";
    private static final String DOUBLE_REGEX = "[+-]?+(?:\\.\\d++|\\d++\\.\\d*+)";
    private static final String STRING_REGEX = "\".*?\"";
    private static final String CHAR_REGEX = "'.'";
    private static final String BOOLEAN_REGEX = "true|false";
    private static final String FINAL = "(?:final\\s++)?+";
    private static final String CONDITION_PARAM = "(?:"
            + IDENTIFIER_NAME + "|"
            + INTEGER_REGEX + "|"
            + DOUBLE_REGEX + "|"
            + BOOLEAN_REGEX + ")";
    private static final String CONDITION_PATTERN = "\\s*+"
            + CONDITION_PARAM
            + "(?:\\s*+(?:\\|\\||&&)\\s*+" + CONDITION_PARAM + ")*\\s*+";
    private static final String VARIABLE_TYPES = "(int|double|char|String|boolean)\\s++";
    private static final String METHOD_PARAM = FINAL + VARIABLE_TYPES + IDENTIFIER_NAME;
    private static final String ALL_OPTIONS = DOUBLE_REGEX
            + "|" + BOOLEAN_REGEX + "|" + INTEGER_REGEX + "|" + STRING_REGEX + "|"
            + CHAR_REGEX + "|" + IDENTIFIER_NAME;
    private static final String ASSIGNMENT_REGEX = IDENTIFIER_NAME + "(=\\s*+" + "(?:" + ALL_OPTIONS + "))";


    public static final Map<String, Pattern> patterns = new HashMap<>();
    public static final Map<String, Pattern> valueTypePatterns = new HashMap<>();


    private static final Pattern COMMENT_PATTERN = Pattern.compile("^//.*+");
    static Pattern VAR_DEC_PATTERN = Pattern.compile(
            "^\\s*+" + FINAL + VARIABLE_TYPES + ASSIGNMENT_REGEX + "?+\\s*+(?:,\\s*+" + ASSIGNMENT_REGEX + "?\\s*+)*+;\\s*+$"
    );
    private static final Pattern ASSIGNMENT_PATTERN = Pattern.compile("\\s*+" + ASSIGNMENT_REGEX + "\\s*+;\\s*+$");
    private static final Pattern METHOD_DEC_PATTERN = Pattern.compile("^\\s*+void\\s++" + METHOD_NAME + "\\("
            + "\\s*+(?:" + METHOD_PARAM + "(?:,\\s*+" + METHOD_PARAM + ")*+)?+" + "\\)\\s*+\\{\\s*+$");
    private static final Pattern METHOD_CALL_PATTERN = Pattern.compile(
            "\\s*+" + IDENTIFIER_NAME + "\\(\\s*+"
                    + "(?:(?:" + ALL_OPTIONS + ")(?:\\s*+,\\s*+(?:" + ALL_OPTIONS + "))*+)?+"
                    + "\\s*+\\)\\s*+;\\s*+$"
    );

    private static final Pattern RETURN_PATTERN = Pattern.compile("^\\s*+return\\s*+;$");
    private static final Pattern IF_WHILE_PATTERN = Pattern.compile(
            "^\\s*+(?:if|while)\\s*+\\(\\s*+"
                    + CONDITION_PATTERN
                    + "\\s*+\\)\\s*+\\{\\s*+$"
    );
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile(IDENTIFIER_NAME);
    private static final Pattern INTEGER_PATTERN = Pattern.compile(INTEGER_REGEX);
    private static final Pattern DOUBLE_PATTERN = Pattern.compile(DOUBLE_REGEX);
    private static final Pattern STRING_PATTERN = Pattern.compile(STRING_REGEX);
    private static final Pattern CHAR_PATTERN = Pattern.compile(CHAR_REGEX);
    private static final Pattern BOOLEAN_PATTERN = Pattern.compile(BOOLEAN_REGEX);

    //TODO delete this row
//    Pattern test = Pattern.compile("^(?:final +)?(?:" + VARIABLE_TYPES + ") +_?[A-Za-z]\\w*(\\s*=\\s*(?:[+-]?\\.\\d+|\\d+\\.\\d*|_?[A-Za-z]\\w*|[+-]?\\d+|\".*\"|'.'|\btrue\b|\bfalse\b))?(?:\\s*,\\s*_?[A-Za-z]\\w*(\\s*=\\s*(?:[+-]?\\d+|_?[A-Za-z]\\w*|[+-]?\\.\\d+|\\d+\\.\\d*|\".*\"|'.?'\btrue\b|\bfalse\b))?)*;\\s*$");
    public static void init() {
        patterns.put(Constants.COMMENT, COMMENT_PATTERN);
        patterns.put(Constants.ASSIGNMENT, ASSIGNMENT_PATTERN);
        patterns.put(Constants.METHOD_CALL, METHOD_CALL_PATTERN);
        patterns.put(Constants.METHOD_DECLARATION, METHOD_DEC_PATTERN);
        patterns.put(Constants.VAR_DECLARATION, VAR_DEC_PATTERN);
        patterns.put(Constants.RETURN_STATEMENT, RETURN_PATTERN);
        patterns.put(Constants.IF_WHILE_STATEMENT, IF_WHILE_PATTERN);
        valueTypePatterns.put(Constants.INT_VAR, INTEGER_PATTERN);
        valueTypePatterns.put(Constants.DOUBLE_VAR, DOUBLE_PATTERN);
        valueTypePatterns.put(Constants.STRING_VAR, STRING_PATTERN);
        valueTypePatterns.put(Constants.CHAR_VAR, CHAR_PATTERN);
        valueTypePatterns.put(Constants.IDENTIFIER, IDENTIFIER_PATTERN);
        valueTypePatterns.put(Constants.BOOL_VAR, BOOLEAN_PATTERN);
    }

    public static String findMatchingPattern(String line) {
        for (Map.Entry<String, Pattern> entry : patterns.entrySet()) {
            if (entry.getValue().matcher(line).matches()) {
                return entry.getKey(); // Return the pattern name
            }
        }
        return null; // No match found
    }

    public static String findValueTypePattern(String line) {
        if (BOOLEAN_PATTERN.matcher(line).matches()){
            return Constants.BOOL_VAR;
        }
        for (Map.Entry<String, Pattern> entry : valueTypePatterns.entrySet()) {
            if (entry.getValue().matcher(line).matches()) {
                return entry.getKey(); // Return the pattern name
            }
        }
        return null; // No match found
    }
}
