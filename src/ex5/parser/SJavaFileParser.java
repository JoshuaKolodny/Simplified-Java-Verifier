package ex5.parser;

import ex5.model.Program;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SJavaFileParser {

    /**
     * Reads and parses the s-Java file into a Program object.
     *
     * @param filePath The path to the .sjava source file
     * @return A Program object representing the parsed file
     * @throws IOException if there's any problem reading the file or invalid extension
     * @throws SyntaxException if the file is syntactically invalid
     */
    public static Program parseFile(String filePath) throws IOException, SyntaxException {
        // 1) Validate file extension, open with BufferedReader
        if (!filePath.endsWith(".sjava")) {
            throw new IOException("File must end with '.sjava': " + filePath);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            // 2) Read the file line by line, parse into Program structure
            //    (Stub: no actual implementation)
            throw new UnsupportedOperationException("parseFile not yet implemented");

        } catch (IOException e) {
            throw new IOException("Cannot read file: " + e.getMessage());
        }
    }
}
