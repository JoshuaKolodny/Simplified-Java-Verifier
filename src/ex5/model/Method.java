package ex5.model;


import java.util.List;

/** A method with a unique name, parameters, and a dedicated Scope for its body. */
public class Method {
    private final String methodName;
    private final List<Variable> parameters;
    private final Scope bodyScope; // all statements (and sub-scopes) go here

    public Method(String methodName, List<Variable> parameters, Scope bodyScope) {
        this.methodName = methodName;
        this.parameters = parameters;
        this.bodyScope = bodyScope;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Variable> getParameters() {
        return parameters;
    }

    public Scope getBodyScope() {
        return bodyScope;
    }

}
