package ex5.model;

import ex5.model.statements.Statement;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a method (void).
 */
public class Method {

    private final String name;
    private final Set<Variable> parameters;
    private final Set<Statement> bodyStatements;

    public Method(String name, Set<Variable> parameters) {
        this.name = name;
        this.parameters = parameters;
        this.bodyStatements = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Set<Variable> getParameters() {
        return parameters;
    }

    public Set<Statement> getBodyStatements() {
        return bodyStatements;
    }

    public void addStatement(Statement stmt) {
        bodyStatements.add(stmt);
    }
}

