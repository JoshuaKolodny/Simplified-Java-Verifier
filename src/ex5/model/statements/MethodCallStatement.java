package ex5.model.statements;

import ex5.Constants.Constants;
import ex5.model.*;
import ex5.parser.RegexPatterns;
import ex5.validator.SemanticException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a method call statement in SJava.
 * This class validates method calls, ensuring compatibility between arguments
 * and the method's parameters.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class MethodCallStatement implements Statement {
    private static final String ARGUMENT_TYPE_MISMATCH_MESSAGE =
            "Argument %s is not compatible with '%s'";
    private static final String METHOD_NOT_FOUND_MESSAGE = "Method %s not found";
    private static final String METHOD_ARGUMENT_MISMATCH_MESSAGE =
            "Method %s expected %d args, but got %d";

    private final String methodName; // The name of the method being called
    private final List<String> arguments; // List of argument names or literals

    /**
     * Constructs a MethodCallStatement with a method name and arguments.
     *
     * @param methodName The name of the method being called.
     * @param arguments The arguments provided in the method call.
     */
    public MethodCallStatement(String methodName, List<String> arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
    }

    /**
     * Validates the method call by ensuring the method exists,
     * has the correct number of arguments, and their types match.
     *
     * @param scope The current scope in which the method call occurs.
     * @throws SemanticException If the method call is invalid.
     */
    @Override
    public void validate(Scope scope) throws SemanticException {
        List<Variable> paramList = getVariables(scope);
        for (int i = 0; i < arguments.size(); i++) {
            validateArgument(arguments.get(i), paramList.get(i), scope);
        }
    }

    /**
     * Validates an individual argument by checking its type compatibility with the corresponding parameter.
     *
     * @param arg The argument name or literal.
     * @param param The expected parameter variable.
     * @param scope The current scope.
     * @throws SemanticException If the argument type is incompatible.
     */
    private void validateArgument(String arg, Variable param, Scope scope) throws SemanticException {
        VariableType assignedVariableType = determineVariableType(arg, scope);
        checkTypeCompatibility(param, assignedVariableType, arg);
    }

    /**
     * Determines the type of a given argument.
     *
     * @param arg The argument value.
     * @param scope The current scope.
     * @return The determined variable type of the argument.
     * @throws SemanticException If the argument is not declared.
     */
    private VariableType determineVariableType(String arg, Scope scope) throws SemanticException {
        String assignedVarTypeString = RegexPatterns.findValueTypePattern(arg);
        if (assignedVarTypeString.equals(Constants.IDENTIFIER)) {
            return getIdentifierVariableType(arg, scope);
        } else {
            return VariableType.fromString(assignedVarTypeString);
        }
    }

    /**
     * Retrieves the type of an identifier argument from the scope.
     *
     * @param arg The identifier representing a variable.
     * @param scope The current scope.
     * @return The type of the identifier variable.
     * @throws SemanticException If the variable is not declared.
     */
    private VariableType getIdentifierVariableType(String arg, Scope scope) throws SemanticException {
        Variable variable = scope.findVariable(arg);
        if (variable == null || variable.getValueType() == null) {
            throw new SemanticException(String.format(Constants.INCOMPATIBLE_ASSIGNMENT_MESSAGE, arg));
        }
        return variable.getValueType();
    }

    /**
     * Ensures the argument type matches the expected parameter type.
     *
     * @param param The expected parameter variable.
     * @param assignedVariableType The determined argument type.
     * @param arg The argument name or value.
     * @throws SemanticException If the types are incompatible.
     */
    private void checkTypeCompatibility(Variable param, VariableType assignedVariableType, String arg) throws SemanticException {
        if (VariableType.isTypeIncompatible(param.getType(), assignedVariableType)) {
            throw new SemanticException(String.format(ARGUMENT_TYPE_MISMATCH_MESSAGE, arg, param.getType()));
        }
    }

    /**
     * Retrieves the expected parameters of the method from the global scope.
     *
     * @param scope The current scope.
     * @return The list of expected parameters.
     * @throws SemanticException If the method does not exist or has incorrect arguments.
     */
    private List<Variable> getVariables(Scope scope) throws SemanticException {
        GlobalScope globalScope = getGlobalScope(scope);
        Method method = globalScope.findMethod(methodName);
        if (method == null) {
            throw new SemanticException(String.format(METHOD_NOT_FOUND_MESSAGE, methodName));
        }
        validateArgumentCount(method);
        return new ArrayList<>(method.getParameters());
    }

    /**
     * Retrieves the global scope from a nested scope.
     *
     * @param scope The current scope.
     * @return The global scope.
     */
    private GlobalScope getGlobalScope(Scope scope) {
        while (scope.getParent() != null) {
            scope = scope.getParent();
        }
        return (GlobalScope) scope;
    }

    /**
     * Ensures the method is called with the correct number of arguments.
     *
     * @param method The method being called.
     * @throws SemanticException If the argument count does not match the method's parameters.
     */
    private void validateArgumentCount(Method method) throws SemanticException {
        if (arguments.size() != method.getParameters().size()) {
            throw new SemanticException(String.format(METHOD_ARGUMENT_MISMATCH_MESSAGE, methodName, method.getParameters().size(), arguments.size()));
        }
    }
}
