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

package net.mikus.assignment.sdp.node

import net.mikus.assignment.sdp.Lexer
import spock.lang.Specification


class NodeFactoryTest extends Specification {

    def 'Logic nodes should be properly evaluated'() {
        given:
        def node = NodeFactory.createNonTerminal(token)

        when:
        node.setLeft({ left } as ASTNode)
        node.setRight({ right } as ASTNode)

        then:
        node.evaluate(null) == result

        where:
          token   | left  | right || result
        Lexer.AND | true  | true  || true
        Lexer.AND | true  | false || false
        Lexer.AND | false | true  || false
        Lexer.AND | false | false || false
        Lexer.OR  | true  | true  || true
        Lexer.OR  | true  | false || true
        Lexer.OR  | false | true  || true
        Lexer.OR  | false | false || false
    }

    def 'Comparison nodes should be properly evaluated'() {
        given:
        def node = NodeFactory.createNonTerminal(token)

        when:
        node.setLeft({ left } as ASTNode)
        node.setRight({ right } as ASTNode)

        then:
        node.evaluate(null) == result

        where:
                token        | left | right || result
        Lexer.EQUAL          |  1d  |  1d   || true
        Lexer.EQUAL          |  1d  |  2d   || false
        Lexer.NOTEQUAL       |  1d  |  2d   || true
        Lexer.NOTEQUAL       |  1d  |  1d   || false
        Lexer.LESS           |  1d  |  2d   || true
        Lexer.LESS           |  1d  |  1d   || false
        Lexer.LESS           |  2d  |  1d   || false
        Lexer.LESSOREQUAL    |  1d  |  2d   || true
        Lexer.LESSOREQUAL    |  1d  |  1d   || true
        Lexer.LESSOREQUAL    |  2d  |  1d   || false
        Lexer.GREATER        |  2d  |  1d   || true
        Lexer.GREATER        |  1d  |  1d   || false
        Lexer.GREATER        |  1d  |  2d   || false
        Lexer.GREATEROREQUAL |  2d  |  1d   || true
        Lexer.GREATEROREQUAL |  1d  |  1d   || true
        Lexer.GREATEROREQUAL |  1d  |  2d   || false
    }

    def 'Number node should be evaluated as is'() {
        expect:
        NodeFactory.createTerminal(Lexer.NUMBER, 3).evaluate(null) == 3
    }

    def 'Variable node should be evaluated from context'() {
        expect:
        NodeFactory.createTerminal(Lexer.VARIABLE, 'a').evaluate({name -> 3d}) == 3d
    }
}
