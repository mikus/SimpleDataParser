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

import java.io.*;

public class Lexer {

    public static final int INVALID = -2;
    public static final int END = -1;

    public static final int NONE = 0;

    public static final int VARIABLE = 1;
    public static final int NUMBER = 2;

    public static final int LEFT = 10;
    public static final int RIGHT = 11;

    public static final int AND = 21;
    public static final int OR = 22;

    public static final int EQUAL = 31;
    public static final int NOTEQUAL = 32;

    public static final int LESS = 41;
    public static final int LESSOREQUAL = 42;

    public static final int GREATER = 51;
    public static final int GREATEROREQUAL = 52;

    public static final String AND_LITERAL = "AND";
    public static final String OR_LITERAL = "OR";

    private final StreamTokenizer tokenizer;

    private int prevToken = NONE;

    private Object value;

    public Lexer(InputStream inputStream) {
        tokenizer = new StreamTokenizer(new BufferedReader(new InputStreamReader(inputStream)));

        tokenizer.resetSyntax();
        tokenizer.wordChars('a', 'z');
        tokenizer.wordChars('A', 'Z');
        tokenizer.whitespaceChars('\u0000', ' ');

        char[] ordinaryChars = new char[] { '(', ')', '<', '=', '>', '!' };
        for (char ordinaryChar : ordinaryChars)
            tokenizer.ordinaryChar(ordinaryChar);

        tokenizer.parseNumbers();
        tokenizer.eolIsSignificant(true);
    }

    public int nextToken() {
        value = null;
        try {
            switch (tokenizer.nextToken()) {
                case StreamTokenizer.TT_EOF:
                case StreamTokenizer.TT_EOL:
                    prevToken = END;
                    break;
                case StreamTokenizer.TT_WORD:
                    if (tokenizer.sval.equalsIgnoreCase(AND_LITERAL)) {
                        prevToken = AND;
                    } else if (tokenizer.sval.equalsIgnoreCase(OR_LITERAL)) {
                        prevToken = OR;
                    } else {
                        prevToken = VARIABLE;
                        value = tokenizer.sval;
                    }
                    break;
                case StreamTokenizer.TT_NUMBER:
                    prevToken = NUMBER;
                    value = tokenizer.nval;
                    break;
                case '(':
                    prevToken = LEFT;
                    break;
                case ')':
                    prevToken = RIGHT;
                    break;
                case '=':
                    prevToken = EQUAL;
                    break;
                case '!':
                    prevToken = nextTokenIs('=', NOTEQUAL, INVALID);
                    break;
                case '>':
                    prevToken = nextTokenIs('=', GREATEROREQUAL, GREATER);
                    break;
                case '<':
                    prevToken = nextTokenIs('=', LESSOREQUAL, LESS);
                    break;
                default:
                    prevToken = INVALID;
            }
        } catch (IOException e) {
            prevToken = END;
        }
        return prevToken;
    }

    private int nextTokenIs(int tokenToCompare, int compoundTokenIfTrue, int tokenIfFalse) throws IOException {
        if (tokenizer.nextToken() == tokenToCompare)
            return compoundTokenIfTrue;
        tokenizer.pushBack();
        return tokenIfFalse;
    }

    public void pushBack() {
        tokenizer.pushBack();
    }

    public Object getValue() {
        return value;
    }
}
