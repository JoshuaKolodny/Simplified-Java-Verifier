package ex5.parser;

import ex5.Constants.Constants;
import ex5.model.*;
import ex5.model.statements.*;
import ex5.model.VariableType;
import ex5.validator.SemanticException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Parses an SJava source file and constructs the corresponding global scope representation.
 * This class validates file syntax, method structure, and statements while constructing
 * nested scopes and method declarations.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class SJavaFileParser {

    private static final String FILE_SUFFIX = ".sjava";
    private static final String FILE_SUFFIX_MESSAGE = "File must end with '.sjava'";
    private static final String MISSING_RETURN_MESSAGE = "Missing return statement in one of the methods.";
    private static final String EXTRA_CURLY_BRACKET_MESSAGE = "Extra '}' in global scope";
    private static final String UNRECOGNIZED_SYNTAX_MESSAGE = "Unrecognized syntax: %s";
    private static final String CANNOT_PERFORM_LINE_MESSAGE = "Cannot perform line: '%s' in global scope";
    private static final String CANNOT_DECLARE_METHOD_MESSAGE = "Cannot declare method: '%s' in nested scope";
    private static final String DUPLICATE_NAMED_METHODS_MESSAGE = "Duplicate named methods: '%s' and '%s'";
    private static final String UNCLOSED_BLOCK_MESSAGE = "Unclosed block at end of file";
    private static final String FINAL = "final";
    private static final String DUPLICATE_PARAMETER_NAME_MESSAGE =
            "Cannot have two parameters with the same name in method declaration with argument '%s'";

    /**
     * Parses an SJava file and constructs the global scope representation.
     *
     * @param filePath The path to the SJava source file.
     * @return The parsed global scope containing all methods and declarations.
     * @throws IOException If there is an issue reading the file.
     * @throws SyntaxException If there is a syntax error in the file.
     * @throws SemanticException If a semantic validation error occurs.
     */
    public static GlobalScope parseFile(String filePath) throws IOException, SyntaxException,
            SemanticException {
        validateFileSuffix(filePath);
        RegexPatterns.init();
        GlobalScope globalScope = new GlobalScope(null);
        Stack<Scope> scopeStack = new Stack<>();
        scopeStack.push(globalScope);
        processFile(filePath, globalScope, scopeStack);
        validateUnclosedBlocks(scopeStack);
        return globalScope;
    }

    /**
     * Validates that the file has the correct '.sjava' suffix.
     *
     * @param filePath The file path to validate.
     * @throws IOException If the file does not have the correct suffix.
     */
    private static void validateFileSuffix(String filePath) throws IOException {
        if (!filePath.endsWith(FILE_SUFFIX)) {
            throw new IOException(FILE_SUFFIX_MESSAGE);
        }
    }

    /**
     * Reads and processes the SJava file line by line, handling different statement types.
     *
     * @param filePath The path to the file.
     * @param globalScope The global scope being constructed.
     * @param scopeStack The stack representing nested scopes.
     * @throws IOException If an error occurs while reading the file.
     * @throws SyntaxException If a syntax error is detected.
     * @throws SemanticException If a semantic validation error occurs.
     */
    private static void processFile(String filePath, GlobalScope globalScope, Stack<Scope> scopeStack)
            throws IOException, SyntaxException, SemanticException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line, prevPatternType = "";
            while ((line = reader.readLine()) != null) {
                if (line.matches("\\s*")) continue;
                if (handleClosingBracket(line, scopeStack, prevPatternType)) {
                    prevPatternType = "}";
                    continue;
                }
                String patternType = getPatternType(line);
                processLine(line, patternType, scopeStack, globalScope);
                prevPatternType = patternType;
            }
        }
    }

    /**
     * Validates and processes the closing bracket ('}') in the source file.
     *
     * @param line The line containing the bracket.
     * @param scopeStack The scope stack tracking nested scopes.
     * @param prevPatternType The previous pattern type encountered.
     * @return True if a closing bracket was handled, otherwise false.
     * @throws SyntaxException If there is an unmatched or misplaced closing bracket.
     */
    private static boolean handleClosingBracket(String line, Stack<Scope> scopeStack, String prevPatternType)
            throws SyntaxException {
        if (line.trim().equals("}")) {
            if (scopeStack.size() == 2 && !prevPatternType.equals(Constants.RETURN_STATEMENT)) {
                throw new SyntaxException(MISSING_RETURN_MESSAGE);
            }
            if (scopeStack.size() == 1) {
                throw new SyntaxException(EXTRA_CURLY_BRACKET_MESSAGE);
            }
            scopeStack.pop();
            return true;
        }
        return false;
    }

    /**
     * Identifies the pattern type of a given line using regex matching.
     *
     * @param line The line to analyze.
     * @return The identified pattern type.
     * @throws SyntaxException If the syntax is unrecognized.
     */
    private static String getPatternType(String line) throws SyntaxException {
        String patternType = RegexPatterns.findMatchingPattern(line);
        if (patternType == null) {
            throw new SyntaxException(String.format(UNRECOGNIZED_SYNTAX_MESSAGE, line));
        }
        return patternType;
    }

    /**
     * Ensures that all opened scopes are properly closed.
     *
     * @param scopeStack The scope stack.
     * @throws SyntaxException If there are unclosed blocks.
     */
    private static void validateUnclosedBlocks(Stack<Scope> scopeStack) throws SyntaxException {
        if (scopeStack.size() > 1) {
            throw new SyntaxException(UNCLOSED_BLOCK_MESSAGE);
        }
    }

    /**
     * Processes a single line of SJava code, identifies its statement type, and adds it
     * to the current scope. Ensures that statements are handled according to their type
     * and belong to a valid scope.
     *
     * @param line The line of SJava code to be processed.
     * @param patternType The identified pattern type of the line (e.g., variable declaration, method call).
     * @param scopeStack The stack tracking nested scopes in the program.
     * @param globalScope The global scope containing all method declarations.
     * @throws SemanticException If the statement is not valid in the current scope.
     */
    private static void processLine(String line, String patternType, Stack<Scope> scopeStack,
                                    GlobalScope globalScope) throws SemanticException {
        // Retrieve the current active scope
        Scope currentScope = scopeStack.peek();

        // Ensure the statement is allowed in the current scope
        validateScope(patternType, scopeStack, line);

        // Process the statement based on its pattern type
        switch (patternType) {
            case Constants.VAR_DECLARATION -> currentScope.addStatement(parseVarDeclaration(line));
            case Constants.ASSIGNMENT -> currentScope.addStatement(parseAssignment(line));
            case Constants.METHOD_CALL -> currentScope.addStatement(parseMethodCall(line));
            case Constants.IF_WHILE_STATEMENT -> handleIfWhileStatement(line, scopeStack, currentScope);
            case Constants.METHOD_DECLARATION ->
                    handleMethodDeclaration(line, globalScope, scopeStack, currentScope);
        }
    }

    /**
     * Validates whether a statement is allowed in the current scope.
     * Ensures that statements such as method declarations are not placed inside other methods,
     * and that control structures (e.g., if/while) do not appear in the global scope.
     *
     * @param patternType The identified pattern type of the statement.
     * @param scopeStack The stack tracking nested scopes in the program.
     * @param line The original line of code being validated.
     * @throws SemanticException If the statement is placed in an invalid scope.
     */
    private static void validateScope(String patternType, Stack<Scope> scopeStack, String line)
            throws SemanticException {
        // Statements that are valid in the global scope
        Set<String> excludedPatternTypes = Set.of(Constants.COMMENT, Constants.VAR_DECLARATION,
                Constants.METHOD_DECLARATION, Constants.ASSIGNMENT);

        // Ensure statements such as if/while are not declared in the global scope
        if (scopeStack.size() == 1 && !excludedPatternTypes.contains(patternType)) {
            throw new SemanticException(String.format(CANNOT_PERFORM_LINE_MESSAGE, line));
        }

        // Prevent method declarations inside other scopes (methods or if/while blocks)
        if (scopeStack.size() > 1 && patternType.equals(Constants.METHOD_DECLARATION)) {
            throw new SemanticException(String.format(CANNOT_DECLARE_METHOD_MESSAGE, line));
        }
    }


    /**
     * Handles the parsing and scope management for an if/while statement.
     *
     * @param line The if/while statement line.
     * @param scopeStack The stack tracking active scopes.
     * @param currentScope The current scope where the statement appears.
     */
    private static void handleIfWhileStatement(String line, Stack<Scope> scopeStack, Scope currentScope) {
        Scope ifWhileScope = new Scope(currentScope);
        IfWhileStatement stmt = parseIfWhileStatement(line, ifWhileScope);
        currentScope.addStatement(stmt);
        scopeStack.push(ifWhileScope);
    }

    /**
     * Handles the parsing and scope management for a method declaration.
     *
     * @param line The method declaration line.
     * @param globalScope The global scope of the program.
     * @param scopeStack The stack tracking active scopes.
     * @param currentScope The current scope where the method is declared.
     * @throws SemanticException If a duplicate method name is found.
     */
    private static void handleMethodDeclaration(String line, GlobalScope globalScope, Stack<Scope> scopeStack,
                                                Scope currentScope) throws SemanticException {
        Method method = parseMethod(line, currentScope);
        for (Method m : globalScope.getMethods()) {
            if (m.getMethodName().equals(method.getMethodName())) {
                throw new SemanticException(String.format(DUPLICATE_NAMED_METHODS_MESSAGE,
                        m.getMethodName(), method.getMethodName()));
            }
        }
        globalScope.addMethod(method);
        scopeStack.push(method.getBodyScope());
    }

    /**
     * Parses an if/while statement from the given line and extracts its conditions.
     *
     * @param line The if/while statement line containing the condition.
     * @param ifWhileScope The new scope for the if/while statement.
     * @return An IfWhileStatement object representing the parsed statement.
     */
    private static IfWhileStatement parseIfWhileStatement(String line, Scope ifWhileScope) {
        // Remove trailing '{' and trim spaces
        String withoutBrace = line.replaceAll("\\{\\s*$", "").trim();

        // Extract condition within parentheses
        int parenIndex = withoutBrace.indexOf('(');
        int closeParen = withoutBrace.indexOf(')', parenIndex);
        String argsPart = withoutBrace.substring(parenIndex + 1, closeParen).strip();

        // Split conditions by logical operators (&&, ||)
        String[] conditions = argsPart.split("\\s*(?:\\|\\||&&)\\s*");

        return new IfWhileStatement(ifWhileScope, conditions);
    }

    /**
     * Parses a variable declaration statement from the given line.
     *
     * @param line The line containing the variable declaration.
     * @return A VarDeclarationStatement representing the parsed declaration.
     */
    private static VarDeclarationStatement parseVarDeclaration(String line) {
        // Trim and remove trailing semicolon
        String content = line.trim().substring(0, line.trim().length() - 1);

        // Check if the declaration contains 'final'
        boolean isFinal = content.contains(FINAL);
        if (isFinal) {
            content = content.replaceAll("final\\s+", "");
        }

        // Split declaration into type and variable assignments
        String[] parts = content.split("\\s+", 2);
        int idx = 0;
        VariableType type = VariableType.fromString(parts[idx++]);
        String varPart = parts[idx];

        // Extract multiple variable declarations separated by commas
        String[] declarations = varPart.split(",");

        return new VarDeclarationStatement(isFinal, type, declarations);
    }

    /**
     * Parses an assignment statement from the given line.
     *
     * @param line The assignment statement line.
     * @return An AssignmentStatement representing the parsed assignment.
     */
    private static AssignmentStatement parseAssignment(String line) {
        // Trim and remove trailing semicolon
        String content = line.trim().substring(0, line.trim().length() - 1);

        // Split assignment into variable name and value
        String[] eqSplit = content.split("=", 2);
        String varName = eqSplit[0].strip();
        String value = eqSplit[1].strip();

        return new AssignmentStatement(varName, value);
    }

    /**
     * Parses a method call statement from the given line.
     *
     * @param line The method call statement line.
     * @return A MethodCallStatement representing the parsed method call.
     */
    private static MethodCallStatement parseMethodCall(String line) {
        // Trim and remove trailing semicolon
        String content = line.trim().substring(0, line.trim().length() - 1);

        // Extract method name and arguments
        int parenIndex = content.indexOf('(');
        String name = content.substring(0, parenIndex).strip();
        int closeParen = content.indexOf(')', parenIndex);
        String argsPart = content.substring(parenIndex + 1, closeParen).strip();

        // Extract arguments as a list
        List<String> argsList = new ArrayList<>();
        if (!argsPart.isEmpty()) {
            String[] splitted = argsPart.split("\\s*,\\s*");
            argsList.addAll(Arrays.asList(splitted));
        }

        return new MethodCallStatement(name, argsList);
    }

    /**
     * Parses a method declaration from the given line, extracting its name and parameters.
     *
     * @param line The method declaration line.
     * @param currentScope The current scope where the method is being declared.
     * @return A Method object representing the parsed method.
     * @throws SemanticException If the method contains duplicate parameter names.
     */
    private static Method parseMethod(String line, Scope currentScope) throws SemanticException {
        // Remove trailing '{' and trim spaces
        String withoutBrace = line.replaceAll("\\{\\s*$", "").trim();

        // Remove "void" keyword
        String afterVoid = withoutBrace.replaceFirst("^void\\s+", "");
        int parenIndex = afterVoid.indexOf('(');
        String methodName = afterVoid.substring(0, parenIndex).strip();
        int closeParen = afterVoid.indexOf(')', parenIndex);
        String paramsPart = afterVoid.substring(parenIndex + 1, closeParen).strip();

        // Parse method parameters
        List<Variable> params = parseMethodParams(paramsPart);

        // Create a new scope for the method
        Scope methodScope = new Scope(currentScope);
        Method method = new Method(methodName, params, methodScope);

        // Add parameters as declared variables in method scope
        for (Variable v : params) {
            methodScope.addVariable(v);
        }

        checkDuplicateParameters(params);
        return method;
    }

    /**
     * Parses method parameters from a given parameter string.
     *
     * @param paramsPart The parameter string extracted from a method declaration.
     * @return A list of Variable objects representing the method parameters.
     * @throws SemanticException If duplicate parameter names are found.
     */
    private static List<Variable> parseMethodParams(String paramsPart) throws SemanticException {
        if (paramsPart.isEmpty()) {
            return new ArrayList<>();
        }

        // Extract multiple parameters separated by commas
        List<Variable> result = new ArrayList<>();
        String[] splitted = paramsPart.split("\\s*,\\s*");

        for (String raw : splitted) {
            // Extract type and name from parameter definition
            String[] tokens = raw.split("\\s+");
            int idx = 0;
            boolean isFinal = false;

            if (tokens[idx].equals(FINAL)) {
                isFinal = true;
                idx++;
            }

            VariableType type = VariableType.fromString(tokens[idx++]);
            String paramName = tokens[idx];

            // Method parameters are always considered initialized
            result.add(new Variable(paramName, type, isFinal, type));
        }

        checkDuplicateParameters(result);
        return result;
    }

    /**
     * Checks for duplicate parameter names in a method declaration.
     *
     * @param result The list of parameters to check.
     * @throws SemanticException If duplicate parameter names are found.
     */
    private static void checkDuplicateParameters(List<Variable> result) throws SemanticException {
        Set<String> seen = new HashSet<>();
        for (Variable var : result) {
            if (!seen.add(var.getName())) { // add() returns false if the element already exists
                throw new SemanticException(String.format(DUPLICATE_PARAMETER_NAME_MESSAGE, var.getName()));
            }
        }
    }
}
