package ex5.validator;

import ex5.model.Program;
import ex5.parser.SyntaxException;

public class SJavaValidator {

    /**
     * Validates the given Program object.
     *
     * @param program the Program to validate
     * @throws SemanticException if any semantic rules are violated
     * @throws SyntaxException if any leftover syntax checking is needed
     */
    public static void validate(Program program) throws SemanticException, SyntaxException {
        // Here you'd:
        //   1) Check global variables
        //   2) Check each method's signature and body
        //   3) Check for final reassignments, type mismatches, etc.
        throw new UnsupportedOperationException("validate not yet implemented");
    }
}
