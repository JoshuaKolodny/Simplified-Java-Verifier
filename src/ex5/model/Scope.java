package ex5.model;

import ex5.model.statements.Statement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a scope in SJava.
 * A scope contains a reference to its parent, a set of local variables,
 * and a list of statements that belong to it.
 * Scopes allow for hierarchical variable resolution.
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class Scope {
    private final Scope parent; // Parent scope (null if global)
    private final Set<Variable> localVariables; // Variables declared in this scope
    private final List<Statement> statements; // Statements in this scope

    /**
     * Constructs a new Scope with an optional parent scope.
     *
     * @param parent The parent scope (null for the global scope).
     */
    public Scope(Scope parent) {
        this.parent = parent;
        this.localVariables = new HashSet<>();
        this.statements = new ArrayList<>();
    }

    /**
     * Retrieves the parent scope of the current scope.
     *
     * @return The parent Scope, or null if this is the global scope.
     */
    public Scope getParent() {
        return parent;
    }

    /**
     * Retrieves the list of statements within this scope.
     *
     * @return A list of Statement objects contained in this scope.
     */
    public List<Statement> getStatements() {
        return statements;
    }

    /**
     * Adds a variable to the scope, ensuring no duplicate declarations.
     *
     * @param variable The variable to add.
     */
    public void addVariable(Variable variable) {
        localVariables.removeIf(var -> var.getName().equals(variable.getName()));
        localVariables.add(variable);
    }

    /**
     * Adds a statement to the scope.
     *
     * @param statement The statement to add.
     */
    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    /**
     * Finds a variable declared in the current scope.
     *
     * @param varName The name of the variable.
     * @return The variable if found, otherwise null.
     */
    public Variable findVariableInCurrentScope(String varName) {
        for (Variable v : localVariables) {
            if (v.getName().equals(varName)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Recursively searches for a variable in the current and parent scopes.
     *
     * @param varName The name of the variable.
     * @return The variable if found, otherwise null.
     */
    public Variable findVariable(String varName) {
        for (Variable v : localVariables) {
            if (v.getName().equals(varName)) {
                return v;
            }
        }
        return (parent != null) ? parent.findVariable(varName) : null;
    }
}
