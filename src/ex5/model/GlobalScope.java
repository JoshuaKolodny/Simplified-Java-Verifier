package ex5.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the global scope in SJava.
 * The global scope serves as the top-level scope containing all methods and global variables.
 * It extends the Scope class and manages method declarations.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class GlobalScope extends Scope {
    private final List<Method> methods; // List of declared methods in the global scope

    /**
     * Constructs a new GlobalScope instance.
     *
     * @param parent The parent scope (null if this is the root scope).
     */
    public GlobalScope(Scope parent) {
        super(parent);
        this.methods = new ArrayList<>();
    }

    /**
     * Retrieves the list of methods declared in the global scope.
     *
     * @return A list of Method objects.
     */
    public List<Method> getMethods() {
        return methods;
    }

    /**
     * Adds a method to the global scope.
     *
     * @param method The method to be added.
     */
    public void addMethod(Method method) {
        methods.add(method);
    }

    /**
     * Searches for a method by its name in the global scope.
     *
     * @param methodName The name of the method to find.
     * @return The Method object if found, otherwise null.
     */
    public Method findMethod(String methodName) {
        for (Method method : methods) {
            if (method.getMethodName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
