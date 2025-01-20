package ex5.main;

import ex5.model.Program;
import ex5.parser.SJavaFileParser;
import ex5.parser.SyntaxException;
import ex5.validator.SJavaValidator;
import ex5.validator.SemanticException;

import java.io.IOException;

public class Sjavac {
    public static void main(String[] args) {
        if (args.length != 1) {
            // Not the correct number of arguments → exit code 2
            System.err.println("Usage: java ex5.main.Sjavac <source_file.sjava>");
            System.out.println(2);
            return;
        }

        String filePath = args[0];

        try {
            // 1) Parse the file into a Program object
            Program program = SJavaFileParser.parseFile(filePath);

            // 2) Validate the parsed Program
            SJavaValidator.validate(program);

            // If no exception → code is valid
            System.out.println(0);

        } catch (IOException e) {
            // I/O or file format issues
            System.err.println("IO Error: " + e.getMessage());
            System.out.println(2);

        } catch (SyntaxException | SemanticException e) {
            // Code is syntactically/semantically invalid
            System.err.println("Compilation Error: " + e.getMessage());
            System.out.println(1);

        } catch (Exception e) {
            // Any other unexpected errors
            System.err.println("Unexpected Error: " + e.getMessage());
            System.out.println(1);
        }
    }
}
