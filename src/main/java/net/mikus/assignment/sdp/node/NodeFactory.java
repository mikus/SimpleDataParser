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

package net.mikus.assignment.sdp.node;

import net.mikus.assignment.sdp.Lexer;


public class NodeFactory {

    public static NonTerminal createNonTerminal(int token) {
        switch (token) {
            case Lexer.OR:
                return new LogicalExpression("OR", (l, r) -> l || r);
            case Lexer.AND:
                return new LogicalExpression("AND", (l, r) -> l && r);
            case Lexer.EQUAL:
                return new Comparison("=", Double::equals);
            case Lexer.NOTEQUAL:
                return new Comparison("!=", (l, r) -> !l.equals(r));
            case Lexer.LESS:
                return new Comparison("<", (l, r) -> l < r);
            case Lexer.LESSOREQUAL:
                return new Comparison("<=", (l, r) -> l <= r);
            case Lexer.GREATER:
                return new Comparison(">", (l, r) -> l > r);
            case Lexer.GREATEROREQUAL:
                return new Comparison(">=", (l, r) -> l >= r);
            default:
                throw new IllegalArgumentException(String.format("Unknown nonterminal token %s.", token));
        }
    }

    public static Terminal createTerminal(int token, Object value) {
        switch (token) {
            case Lexer.VARIABLE:
                if (!(value instanceof String))
                    throw new IllegalArgumentException(String.format("Terminal token %s requires string value.", token));
                return new Variable((String)value);
            case Lexer.NUMBER:
                if (!(value instanceof java.lang.Number))
                    throw new IllegalArgumentException(String.format("Terminal token %s requires number value.", token));
                return new Number((java.lang.Number)value);
            default:
                throw new IllegalArgumentException(String.format("Unknown terminal token %s.", token));
        }
    }

}
