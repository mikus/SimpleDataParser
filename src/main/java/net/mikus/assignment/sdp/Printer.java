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
import net.mikus.assignment.sdp.node.NonTerminal;

import java.io.PrintStream;

public class Printer {

    public static void print(ASTNode node) {
        print(node, System.out);
    }

    public static void print(ASTNode node, PrintStream printStream) {
        print(node, "", true, printStream);
    }

    private static void print(ASTNode node, String prefix, boolean isRight, PrintStream printStream) {
        printStream.println(prefix + (isRight ? "└── " : "├── ") + node.getSymbol());
        if (node instanceof NonTerminal) {
            NonTerminal nonTerminal = (NonTerminal)node;
            String childPrefix = prefix + (isRight ? "    " : "│   ");
            print(nonTerminal.getLeft(), childPrefix, false, printStream);
            print(nonTerminal.getRight(), childPrefix, true, printStream);
        }
    }
}
