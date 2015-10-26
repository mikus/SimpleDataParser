/* Copyright 2015 Mikołaj Olszewski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.mikus.assignment.sdp;


import net.mikus.assignment.sdp.node.ASTNode;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

public class Program {


    private void run(String filePath) {
        try (SimpleDataReader dataFileReader = createDataFileReader(filePath)) {
            ASTNode<Boolean> selector = acceptSelector(dataFileReader.getAttributes());
            evaluateData(dataFileReader, selector);
        } catch (Exception ex) {
            printError(ex);
        }
    }

    private static SimpleDataReader createDataFileReader(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists())
            throw new IOException(String.format("There is no such file %s.", filePath));
        return new SimpleDataReader(file);
    }

    private static ASTNode<Boolean> acceptSelector(Collection<String> attributes) {
        Scanner scanner = new Scanner(System.in);
        ASTNode<Boolean> selector = null;
        while (selector == null) {
            selector = grabSelector(attributes);
            printTree(selector);
            if (!isAccepted(scanner))
                selector = null;
        }
        return selector;
    }

    private static ASTNode<Boolean> grabSelector(Collection<String> attributes) {
        while (true) {
            try {
                printInput("Enter data selector for attributes %s", attributes);
                Lexer lexer = new Lexer(System.in);
                Parser parser = new Parser(lexer, attributes);
                return parser.build();
            } catch (MalformedExpressionException ex) {
                printError(ex);
            }
        }
    }

    private static boolean isAccepted(Scanner scanner) {
        printInput("\nDo you accept it? [y/n]");
        return scanner.next().equalsIgnoreCase("y");
    }


    private static void printTree(ASTNode root) {
        printMessage("\nAST for entered selector:");
        Printer.print(root);
    }

    private static void evaluateData(SimpleDataReader dataFileReader, ASTNode<Boolean> selector) throws IOException {
        Map<String, Double> row;
        int total = 0;
        int selected = 0;
        printMessage("\nSelected rows:");

        Instant start = Instant.now();
        while ((row = dataFileReader.readLine()) != null) {
            ++total;
            if (selector.evaluate(row::get)) {
                ++selected;
                System.out.println(row);
            }
        }
        Instant finish = Instant.now();
        printMessage("\nResult:  %d out of %d rows selected in %.3fs",
                selected, total, Duration.between(start, finish).toMillis() / 1000.0);
    }


    public static void main(String... args) {
        if (checkArguments(args)) {
            Program program = new Program();
            program.run(args[0]);
        }
    }

    private static boolean checkArguments(String... args) {
        if (args.length != 1) {
            usage();
            return false;
        }
        return true;
    }

    private static void usage() {
        try {
            CodeSource codeSource = Program.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            printMessage("usage:\tjava -jar %s <data file path>", jarFile.getName());
        } catch (URISyntaxException ex) {
            printError(ex);
        }
    }

    private static void printMessage(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    private static void printInput(String format, Object... args) {
        System.out.print(String.format(format + ":  ", args));
    }

    private static void printError(Exception ex) {
        System.err.println(String.format("ERROR: %s", ex.getMessage()));
    }

}
