package ex5.model.statements;

import ex5.Constants.Constants;
import ex5.model.Scope;
import ex5.model.Variable;
import ex5.model.VariableType;
import ex5.parser.RegexPatterns;
import ex5.validator.SemanticException;

import java.util.List;

/**
 * E.g.: "final int x = 5, y;"
 * We store the type, final-ness, plus the declared variables (names + isInitialized).
 */
public class VarDeclarationStatement implements Statement {

    private final boolean isFinal;
    private final VariableType type;
    private final String[] declarations; // each has name & isInitialized

    public VarDeclarationStatement(boolean isFinal,
                                   VariableType type,
                                   String[] declarations) {
        this.isFinal = isFinal;
        this.type = type;
        this.declarations = declarations;
    }

    @Override
    public void validate(Scope scope) throws SemanticException {
        // Check that no variable with same name is already in this scope
        // Also ensure final variables are indeed initialized
        for (String decl : declarations) {
            String[] eqSplit = decl.split("=", 2);
            String varValue;
            VariableType assignedVariableType = null;
            if (eqSplit.length == 2) {
                varValue = eqSplit[1].strip();
                assignedVariableType = getVariableType(scope, varValue);
            }
            String varName = eqSplit[0].strip();
            if (assignedVariableType != null && !VariableType.isTypeCompatible(type, assignedVariableType)) {
                throw new SemanticException(String.format("Incompatible Variable types in" +
                        " declaration %s with %s", type, assignedVariableType));
            }
            Variable var = new Variable(varName, type, isFinal, assignedVariableType);
            if (scope.findVariableInCurrentScope(var.getName()) != null) {
                // The current scope already has a variable with this name
                throw new SemanticException("Variable " + var.getName() + " already declared in this scope");
            }
            if (var.isFinal() && var.getValueType() == null) {
                throw new SemanticException("Final variable " + var.getName() + " not initialized");
            }
            scope.addVariable(var);
        }
    }

    private VariableType getVariableType(Scope currentScope, String varValue) throws SemanticException {
        VariableType variableType = null;
        String variableString = RegexPatterns.findValueTypePattern(varValue);
        if (variableString != null && variableString.equals(Constants.IDENTIFIER)) {
            Variable var = currentScope.findVariable(varValue);
            if (var == null || var.getValueType() == null) {
                throw new SemanticException("Incompatible assignment or unknown variable");
            }
            variableType = var.getType();
            //check literal and then check compatibility
        } else if (variableString != null) {
            variableType = VariableType.fromString(variableString);
        }
        return variableType;
    }
}
