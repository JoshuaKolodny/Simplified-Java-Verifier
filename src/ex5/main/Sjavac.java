package ex5.main;

import ex5.model.GlobalScope;
import ex5.parser.SJavaFileParser;
import ex5.parser.SyntaxException;
import ex5.validator.SJavaValidator;
import ex5.validator.SemanticException;

import java.io.IOException;

/**
 * The main compiler class for the SJava language.
 * This class serves as the entry point for the SJava compiler,
 * handling parsing, validation, and error reporting.
 *
 * It processes a single SJava source file, checks for syntax and semantic errors,
 * and prints the appropriate exit code:
 * <ul>
 *     <li>0 - Compilation successful, no errors.</li>
 *     <li>1 - Compilation failed due to syntax or semantic errors.</li>
 *     <li>2 - Compilation failed due to incorrect usage or I/O errors.</li>
 * </ul>
 *
 * @author Joshua Kolodny, Itamar Lev Ari
 */
public class Sjavac {

    private static final String ARG_LENGTH_ERR = "Usage: java ex5.main.Sjavac <source_file.sjava>";
    private static final String IO_ERROR = "IO Error: ";
    private static final String COMPILATION_ERROR = "Compilation Error: ";

    /**
     * The main method for the SJava compiler.
     * It parses the given file, validates it, and prints the appropriate exit code.
     *
     * @param args Command-line arguments. Expected: a single file path.
     */
    public static void main(String[] args) {
        // Check if the correct number of arguments is provided
        if (args.length != 1) {
            System.err.println(ARG_LENGTH_ERR);
            System.out.println(2);
            return;
        }

        String filePath = args[0];

        try {
            // Parse the file and create a GlobalScope representation
            GlobalScope globalScope = SJavaFileParser.parseFile(filePath);

            // Validate the parsed file for syntax and semantic correctness
            SJavaValidator.validate(globalScope);

            // If no exceptions were thrown, the compilation is successful
            System.out.println(0);
        } catch (IOException e) {
            // Handle file reading errors and other I/O issues
            System.err.println(IO_ERROR + e.getMessage());
            System.out.println(2);
        } catch (SyntaxException | SemanticException e) {
            // Handle syntax or semantic validation errors
            System.err.println(COMPILATION_ERROR + e.getMessage());
            System.out.println(1);
        }
    }
}
