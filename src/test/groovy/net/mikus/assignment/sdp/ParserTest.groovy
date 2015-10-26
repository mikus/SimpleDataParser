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


class ParserTest extends Specification {

    def "Basic comparisions should be properly recognized"() {
        expect:
        new Parser(new Lexer(input.toStream())).build().toString() == output

        where:
          input     ||   output
        'a = 123'   || '(a = 123.0)'
        'ab!= bcd'  || '(ab != bcd)'
        'aaaa >bb'  || '(aaaa > bb)'
        'abc>=cde'  || '(abc >= cde)'
        'a < b'     || '(a < b)'
        '(c <= d)'  || '(c <= d)'
    }

    def "Logical expressions should be properly recognized"() {
        expect:
        new Parser(new Lexer(input.toStream())).build().toString() == output

        where:
                   input               ||            output
        'a = b AND b > c'              || '((a = b) AND (b > c))'
        'a != b or c <= d'             || '((a != b) OR (c <= d))'
        'a = b AND b > c and c!= d'    || '(((a = b) AND (b > c)) AND (c != d))'
        'a != b or c <= d or c!= d'    || '(((a != b) OR (c <= d)) OR (c != d))'
        'a = b AND b > c OR c != d'    || '(((a = b) AND (b > c)) OR (c != d))'
        'a = b OR b > c AND c != d'    || '((a = b) OR ((b > c) AND (c != d)))'
        'a = b AND (b > c OR 15 != d)' || '((a = b) AND ((b > c) OR (15.0 != d)))'
        '(a = b OR b > c) AND c != d'  || '(((a = b) OR (b > c)) AND (c != d))'
    }

    def "Incorrect expression should casue exception"() {
        when:
        new Parser(new Lexer(input.toStream())).build()

        then:
        thrown MalformedExpressionException

        where:
        input << ['a', '3', '(', ')', '()', 'a = ', '< 4', 'a + b',
                  '(a = 2 AND b = 3', '(a = 2 AND) b = 3', '(a = 2 (OR b = 3)', '(a = 2 AND (b = 3 OR c = 4)']
    }

    def "Parser should accept allowed identifiers"() {
        when:
        new Parser(new Lexer(input.toStream()), ['a', 'b', 'c', 'd']).build()

        then:
        noExceptionThrown()

        where:
        input << ['a = b AND b > c', 'a != b or c <= d']
    }


    def "Parser should not accept not allowed identifiers"() {
        when:
        new Parser(new Lexer(input.toStream()), ['a', 'b', 'c', 'd']).build()

        then:
        thrown MalformedExpressionException

        where:
        input << ['a = b AND b > e', 'f != b or c <= d']
    }
}
