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


import net.mikus.assignment.sdp.node.*;

import java.util.Collection;

public class Parser {

    private final Lexer lexer;

    private int token;
    private ASTNode root;
    private Collection<String> allowedIdentifiers;

    public Parser(Lexer lexer) {
        this(lexer, null);
    }

    public Parser(Lexer lexer, Collection<String> allowedIdentifiers) {
        this.lexer = lexer;
        this.allowedIdentifiers = allowedIdentifiers;
    }

    @SuppressWarnings("unchecked")
    public ASTNode<Boolean> build() {
        expression();
        return root;
    }

    @SuppressWarnings("unchecked")
    private void expression() {
        term();
        while (token == Lexer.OR) {
            NonTerminal or = NodeFactory.createNonTerminal(token);
            or.setLeft(root);
            term();
            or.setRight(root);
            root = or;
        }
    }

    @SuppressWarnings("unchecked")
    private void term() {
        factor();
        while (token == Lexer.AND) {
            NonTerminal and = NodeFactory.createNonTerminal(token);
            and.setLeft(root);
            factor();
            and.setRight(root);
            root = and;
        }
    }

    private void factor() {
        token = lexer.nextToken();
        if (token == Lexer.LEFT) {
            expression();
            if (token != Lexer.RIGHT) {
                throw new MalformedExpressionException(String.format("')' instead of <%s> expected.", token));
            } else {
                token = lexer.nextToken();
            }
        } else {
            condition();
        }
    }

    @SuppressWarnings("unchecked")
    private void condition() {
        value();
        if (token == Lexer.GREATER ||
                token == Lexer.GREATEROREQUAL ||
                token == Lexer.LESS ||
                token == Lexer.LESSOREQUAL ||
                token == Lexer.EQUAL ||
                token == Lexer.NOTEQUAL) {
            NonTerminal condition = NodeFactory.createNonTerminal(token);
            condition.setLeft(root);
            token = lexer.nextToken();
            value();
            condition.setRight(root);
            root = condition;
        } else {
            throw new MalformedExpressionException(String.format("Conditional operator instead of <%s> expected.", token));
        }
    }

    private void value() {
        if (token == Lexer.VARIABLE ||
            token == Lexer.NUMBER) {
            root = NodeFactory.createTerminal(token, lexer.getValue());
            if (token == Lexer.VARIABLE && allowedIdentifiers != null) {
                if (!allowedIdentifiers.contains(root.getSymbol()))
                    throw new MalformedExpressionException(String.format("Unknown identifier '%s'", root.getSymbol()));
            }
            token = lexer.nextToken();
        } else {
            throw new MalformedExpressionException(String.format("Value instead of <%s> expected.", token));
        }
    }
}
