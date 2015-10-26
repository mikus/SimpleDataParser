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

package net.mikus.assignment.sdp

import spock.lang.Specification

class LexerTest extends Specification {

    def 'Empty input should give END'() {
        given:
        def input = ''

        when:
        def lexer = new Lexer(input.toStream())

        then:
        lexer.nextToken() == Lexer.END
    }

    def 'Fully whitespace input should give END'() {
        given:
        def input = '   \t\r  \n  \t \r  \t '

        when:
        def lexer = new Lexer(input.toStream())

        then:
        lexer.nextToken() == Lexer.END
    }

    def 'End of line should finish lexer work'() {
        given:
        def input = ' OR\n'

        when:
        def lexer = new Lexer(input.toStream())

        then:
        lexer.nextToken() == Lexer.OR
        lexer.nextToken() == Lexer.END
    }

    def 'Variable values should be properly recognized'() {
        given:
        def input = ' o REty'

        when:
        def lexer = new Lexer(input.toStream())

        then:
        lexer.nextToken() == Lexer.VARIABLE
        lexer.getValue() == 'o'
        lexer.nextToken() == Lexer.VARIABLE
        lexer.getValue() == 'REty'
    }

    def 'Number values should be properly recognized'() {
        given:
        def input = ' 3 123 45.67'

        when:
        def lexer = new Lexer(input.toStream())

        then:
        lexer.nextToken() == Lexer.NUMBER
        lexer.getValue() == 3
        lexer.nextToken() == Lexer.NUMBER
        lexer.getValue() == 123
        lexer.nextToken() == Lexer.NUMBER
        lexer.getValue() == 45.67
    }

    def 'Compound operators should be recognized as one token'() {
        expect:
        new Lexer(input.toStream()).nextToken() == token

        where:
        input  || token
        ' >= ' || Lexer.GREATEROREQUAL
        ' <='  || Lexer.LESSOREQUAL
        '!= '  || Lexer.NOTEQUAL
    }

    def 'Longer text should be recognized as several tokens'() {
        given:
        def input = ' a> b AND ( 3=d)'

        when:
        def lexer = new Lexer(input.toStream())

        then:
        lexer.nextToken() == Lexer.VARIABLE
        lexer.nextToken() == Lexer.GREATER
        lexer.nextToken() == Lexer.VARIABLE
        lexer.nextToken() == Lexer.AND
        lexer.nextToken() == Lexer.LEFT
        lexer.nextToken() == Lexer.NUMBER
        lexer.nextToken() == Lexer.EQUAL
        lexer.nextToken() == Lexer.VARIABLE
        lexer.nextToken() == Lexer.RIGHT
        lexer.nextToken() == Lexer.END
    }
}