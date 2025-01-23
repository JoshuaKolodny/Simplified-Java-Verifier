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

public class SJavaFileParser {

    public static GlobalScope parseFile(String filePath) throws IOException, SyntaxException, SemanticException {
        if (!filePath.endsWith(".sjava")) {
            throw new IOException("File must end with '.sjava'");
        }
        RegexPatterns.init();
        GlobalScope globalScope = new GlobalScope(null);

        // A stack of scopes to handle nesting.
        Stack<Scope> scopeStack = new Stack<>();
        // The global scope is program.getGlobalScope()

        // Start with the global scope at the top
        scopeStack.push(globalScope);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line, prevPatternType = "";
            while ((line = reader.readLine()) != null) {
                if (line.matches("\\s*")) {
                    continue; // skip empty lines
                }
                if (line.trim().equals("}")) {
                    // End of current scope
                    if (scopeStack.size() == 2 && !prevPatternType.equals(Constants.RETURN_STATEMENT)) {
                        throw new SyntaxException("Missing return statement.");
                    }
                    if (scopeStack.size() == 1) {
                        // can't pop the global scope
                        throw new SyntaxException("Extra '}' in global scope");
                    }
                    scopeStack.pop();
                    prevPatternType = "}";
                    continue;
                }

                // match line to a pattern
                String patternType = RegexPatterns.findMatchingPattern(line);
                if (patternType == null) {
                    throw new SyntaxException("Unrecognized syntax: " + line);
                }

                Scope currentScope = scopeStack.peek();
                Set<String> excludedPatternTypes = new HashSet<>();
                excludedPatternTypes.add(Constants.COMMENT);
                excludedPatternTypes.add(Constants.VAR_DECLARATION);
                excludedPatternTypes.add(Constants.METHOD_DECLARATION);
                excludedPatternTypes.add(Constants.ASSIGNMENT);

                // Check the conditions and throw exception if necessary
                if (scopeStack.size() == 1 && !excludedPatternTypes.contains(patternType)) {
                    throw new SemanticException("Cannot perform line: " + line + " in global scope");
                }

                if (scopeStack.size() > 1 && patternType.equals(Constants.METHOD_DECLARATION)){
                    throw new SemanticException("Cannot declare method: " + line + " in nested scope");
                }


                switch (patternType) {
                    case Constants.COMMENT -> {
                        // ignore
                    }
                    case Constants.VAR_DECLARATION -> {
                        VarDeclarationStatement stmt = parseVarDeclaration(line);
                        currentScope.addStatement(stmt);
                    }
                    case Constants.ASSIGNMENT -> {
                        AssignmentStatement stmt = parseAssignment(line);
                        currentScope.addStatement(stmt);
                    }
                    case Constants.METHOD_CALL -> {
                        MethodCallStatement stmt = parseMethodCall(line);
                        currentScope.addStatement(stmt);
                    }
                    case Constants.RETURN_STATEMENT -> {
                        currentScope.addStatement(new ReturnStatement());
                    }
                    case Constants.IF_WHILE_STATEMENT -> {
                        // create sub-scope
                        Scope ifWhileScope = new Scope(currentScope);
                        IfWhileStatement stmt = parseIfWhileStatement(line, ifWhileScope);
                        // create IfWhileStatement
                        currentScope.addStatement(stmt);
                        // push the ifWhileScope
                        scopeStack.push(ifWhileScope);
                    }
                    case Constants.METHOD_DECLARATION -> {
                        // parse method signature
                        Method method = parseMethod(line, currentScope);
                        for (Method m : globalScope.getMethods()) {
                            if (m.getMethodName().equals(method.getMethodName())) {
                                throw new SemanticException("Duplicate named methods");
                            }
                        }
                        // add method to program
                        globalScope.addMethod(method);
                        // push method's scope
                        scopeStack.push(method.getBodyScope());
                    }
                    default -> throw new SyntaxException("Unsupported pattern: " + patternType);
                }
                prevPatternType = patternType;
            }
            // If any scope remains besides global => missing '}'
            if (scopeStack.size() > 1) {
                throw new SyntaxException("Unclosed block at end of file");
            }
        }
        return globalScope;
    }

    private static IfWhileStatement parseIfWhileStatement(String line, Scope ifWhileScope) {
        String withoutBrace = line.replaceAll("\\{\\s*$", "").trim();
        int parenIndex = withoutBrace.indexOf('(');
        int closeParen = withoutBrace.indexOf(')', parenIndex);
        String argsPart = withoutBrace.substring(parenIndex + 1, closeParen).strip();
        String[] conditions = argsPart.split("\\s*(?:\\|\\||&&)\\s*");
        return new IfWhileStatement(ifWhileScope, conditions);
    }

    private static VarDeclarationStatement parseVarDeclaration(String line) {
        // example: "final int x = 5, y;"
        String content = line.trim().substring(0, line.trim().length() - 1); // remove trailing ';'
        // e.g. "final int x=5, y"
        boolean isFinal = content.contains("final");
        if (isFinal) {
            content = content.replaceAll("final\\s+", "");
        }
        String[] parts = content.split("\\s+", 2);
        int idx = 0;
        VariableType type = VariableType.fromString(parts[idx++]);
        String varPart = parts[idx];
        // split by commas
        String[] declarations = varPart.split(",");

        return new VarDeclarationStatement(isFinal, type, declarations);
    }


    private static AssignmentStatement parseAssignment(String line) {
        // e.g. "x=5;"
        String content = line.trim().substring(0, line.trim().length() - 1);
        String[] eqSplit = content.split("=", 2);
        String varName = eqSplit[0].strip();
        String value = eqSplit[1].strip();
        return new AssignmentStatement(varName, value);
    }

    private static MethodCallStatement parseMethodCall(String line) {
        // e.g. "foo(x, y);"
        // strip trailing ';'
        String content = line.trim().substring(0, line.trim().length() - 1);
        // "foo(x, y)"
        int parenIndex = content.indexOf('(');
        String name = content.substring(0, parenIndex).strip();
        int closeParen = content.indexOf(')', parenIndex);
        String argsPart = content.substring(parenIndex + 1, closeParen).strip();

        List<String> argsList = new ArrayList<>();
        if (!argsPart.isEmpty()) {
            String[] splitted = argsPart.split("\\s*,\\s*");
            argsList.addAll(Arrays.asList(splitted));
        }
        return new MethodCallStatement(name, argsList);
    }

    private static Method parseMethod(String line, Scope currentScope) throws SemanticException {
        // e.g. "void foo(final int x, double y){"
        // remove trailing '{'
        String withoutBrace = line.replaceAll("\\{\\s*$", "").trim();
        // "void foo(final int x, double y)"

        // remove "void"
        String afterVoid = withoutBrace.replaceFirst("^void\\s+", "");
        int parenIndex = afterVoid.indexOf('(');
        String methodName = afterVoid.substring(0, parenIndex).strip();
        int closeParen = afterVoid.indexOf(')', parenIndex);
        String paramsPart = afterVoid.substring(parenIndex + 1, closeParen).strip();

        // parse parameters
        List<Variable> params = parseMethodParams(paramsPart);

        // create a new scope for the method:
        Scope methodScope = new Scope(currentScope);

        Method method = new Method(methodName, params, methodScope);
        // add parameters as declared variables in methodScope
        for (Variable v : params) {
            methodScope.addVariable(v);
        }
        checkDuplicateParameters(params);
        return method;
    }

    private static List<Variable> parseMethodParams(String paramsPart) throws SemanticException {
        if (paramsPart.isEmpty()) {
            return new ArrayList<>();
        }
        // e.g. "final int x, double y"
        List<Variable> result = new ArrayList<>();
        String[] splitted = paramsPart.split("\\s*,\\s*");
        for (String raw : splitted) {
            // e.g. "final int x"
            String[] tokens = raw.split("\\s+");
            int idx = 0;
            boolean isFinal = false;
            if (tokens[idx].equals("final")) {
                isFinal = true;
                idx++;
            }
            VariableType type = VariableType.fromString(tokens[idx++]);
            String paramName = tokens[idx];
            // method param is always considered initialized
            result.add(new Variable(paramName, type, isFinal, type));
        }
        checkDuplicateParameters(result);
        return result;
    }

    private static void checkDuplicateParameters(List<Variable> result) throws SemanticException {
        Set<String> seen = new HashSet<>();
        for (Variable var : result) {
            if (!seen.add(var.getName())) { // add() returns false if the element already exists
                throw new SemanticException(String.format("Cannot have two parameters with the" +
                        " same name in method declaration with argument '%s'", var.getName()));
            }
        }
    }
}
