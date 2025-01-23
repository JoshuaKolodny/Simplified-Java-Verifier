package ex5.model;

import ex5.model.statements.Statement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A single scope, containing:
 *   - A pointer to its parent scope (null if global).
 *   - A set of variables declared in this scope.
 *   - A list of statements in this scope.
 */
public class Scope {
    private final Scope parent;
    private final Set<Variable> localVariables;
    private final List<Statement> statements;

    public Scope(Scope parent) {
        this.parent = parent;
        this.localVariables = new HashSet<>();
        this.statements = new ArrayList<>();
    }

    public Scope getParent() {
        return parent;
    }

    public Set<Variable> getLocalVariables() {
        return localVariables;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void addVariable(Variable variable) {
        localVariables.removeIf(var -> var.getName().equals(variable.getName()));
        localVariables.add(variable);
    }

    private void removeVariable(Variable variable) {
        localVariables.remove(variable);
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    public Variable findVariableInCurrentScope(String varName) {
        for (Variable v : localVariables) {
            if (v.getName().equals(varName)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Find a variable (by name) in this scope or any ancestor.
     * Return null if not found.
     */
    public Variable findVariable(String varName) {
        for (Variable v : localVariables) {
            if (v.getName().equals(varName)) {
                return v;
            }
        }
        if (parent != null) {
            return parent.findVariable(varName);
        }
        return null;
    }
}
