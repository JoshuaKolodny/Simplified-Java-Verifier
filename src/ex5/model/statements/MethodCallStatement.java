package ex5.model.statements;

import ex5.Constants.Constants;
import ex5.model.*;
import ex5.parser.RegexPatterns;
import ex5.validator.SemanticException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * For "foo(x, y);"
 * We store methodName="foo" and argNames=["x","y"] or possibly literals.
 */
public class MethodCallStatement implements Statement {

    private final String methodName;
    private final List<String> arguments; // each is a var name or literal

    public MethodCallStatement(String methodName, List<String> arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
    }

    @Override
    public void validate(Scope scope) throws SemanticException {
        // 1) Find the method in the "global" program structure. Typically you'd store a reference
        //    to the Program. Or your scope might store a reference to it. We'll show a simplified approach
        //    calling a static method or so.
        Scope tempScope = scope;
        while (tempScope.getParent() != null){
            tempScope = tempScope.getParent();
        }
        GlobalScope globalScope = (GlobalScope) tempScope;
        Method method = globalScope.findMethod(methodName);
        if (method == null) {
            throw new SemanticException("Method " + methodName + " not found");
        }

        // 2) Check argument count
        if (arguments.size() != method.getParameters().size()) {
            throw new SemanticException("Method " + methodName + " expected "
                    + method.getParameters().size() + " args, but got " + arguments.size());
        }

        // 3) For each arg, check if it's a variable name or literal,
        //    then check type compatibility.
        // This is a simplified example:
        List<Variable> paramList = new ArrayList<>(method.getParameters());

        for (int i = 0; i < arguments.size(); i++) {
            String arg = arguments.get(i);
            Variable param = paramList.get(i);
            String assignedVarTypeString = RegexPatterns.findValueTypePattern(arg);
            VariableType assignedVariableType;
            if (assignedVarTypeString.equals(Constants.IDENTIFIER)) {
                Variable variable = scope.findVariable(arg);
                if (variable == null || variable.getValueType() == null) {
                    throw new SemanticException("Incompatible assignment or unknown variable " + arg);
                }
                assignedVariableType = variable.getValueType();
            } else {
                assignedVariableType = VariableType.fromString(assignedVarTypeString);
            }
                // It's presumably a literal => check if it's type-compatible
                // We'll do a naive approach:
                if (!VariableType.isTypeCompatible(param.getType(), assignedVariableType)) {
                    throw new SemanticException("argument" + arg + " is not compatible with '" +
                            param.getType() + "'");
                }
        }
    }


}
