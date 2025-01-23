package ex5.model;

import java.util.List;

/**
 * Represents a method in the SJava language.
 * A method has a unique name, parameters, and a dedicated scope for its body.
 * This class provides access to method metadata and parameters.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class Method {
    private final String methodName; // Unique method name
    private final List<Variable> parameters; // List of parameters
    private final Scope bodyScope; // Scope containing method statements

    /**
     * Constructs a Method with a name, parameters, and a body scope.
     *
     * @param methodName  The unique name of the method.
     * @param parameters  The list of parameters required by the method.
     * @param bodyScope   The scope containing the method's statements.
     */
    public Method(String methodName, List<Variable> parameters, Scope bodyScope) {
        this.methodName = methodName;
        this.parameters = parameters;
        this.bodyScope = bodyScope;
    }

    /**
     * Retrieves the name of the method.
     *
     * @return The method name as a string.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Retrieves the list of parameters associated with the method.
     *
     * @return A list of Variable objects representing the method's parameters.
     */
    public List<Variable> getParameters() {
        return parameters;
    }

    /**
     * Retrieves the body scope of the method, containing its statements.
     *
     * @return The Scope object representing the method's body.
     */
    public Scope getBodyScope() {
        return bodyScope;
    }
}
