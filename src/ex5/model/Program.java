package ex5.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Represents an entire s-Java file: global variables + methods.
 */
public class Program {

    private final HashSet<Variable> globalVariables;
    private final HashSet<Method> methods;

    public Program() {
        this.globalVariables = new HashSet<>();
        this.methods = new HashSet<>();
    }

    public HashSet<Variable> getGlobalVariables() {
        return globalVariables;
    }

    public HashSet<Method> getMethods() {
        return methods;
    }

    public void addGlobalVariable(Variable var) {
        globalVariables.add(var);
    }

    public void addMethod(Method method) {
        methods.add(method);
    }
}

