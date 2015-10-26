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

import net.mikus.assignment.sdp.node.ASTNode
import net.mikus.assignment.sdp.node.NonTerminal
import net.mikus.assignment.sdp.node.Terminal
import spock.lang.Specification


class PrinterTest extends Specification {

    PrintStream printStream

    def setup() {
        printStream = Mock(PrintStream)
    }

    def "Single node should be properly printed"() {
        given:
        def root = Stub(ASTNode)
        root.symbol >> 'ROOT'

        when:
        Printer.print(root, printStream)

        then:
        1 * printStream.println('└── ROOT')
        0 * _
    }

    def "One level tree should be properly printed"() {
        given:
        def root = Stub(NonTerminal)
        root.symbol >> 'ROOT'
        root.left >> Stub(Terminal)
        root.left.symbol >> 'LEFT'
        root.right >> Stub(Terminal)
        root.right.symbol >> 'RIGHT'

        when:
        Printer.print(root, printStream)

        then:
        1 * printStream.println('└── ROOT')
        1 * printStream.println('    ├── LEFT')
        1 * printStream.println('    └── RIGHT')
        0 * _
    }

    def "Full two level tree should be properly printed"() {
        given:
        def root = Stub(NonTerminal)
        root.symbol >> 'ROOT'
        root.left >> Stub(NonTerminal)
        root.left.symbol >> 'LEFT'
        root.left.left >> Stub(Terminal)
        root.left.left.symbol >> 'L LEFT'
        root.left.right >> Stub(Terminal)
        root.left.right.symbol >> 'L RIGHT'
        root.right >> Stub(NonTerminal)
        root.right.symbol >> 'RIGHT'
        root.right.left >> Stub(Terminal)
        root.right.left.symbol >> 'R LEFT'
        root.right.right >> Stub(Terminal)
        root.right.right.symbol >> 'R RIGHT'

        when:
        Printer.print(root, printStream)

        then:
        1 * printStream.println('└── ROOT')
        1 * printStream.println('    ├── LEFT')
        1 * printStream.println('    │   ├── L LEFT')
        1 * printStream.println('    │   └── L RIGHT')
        1 * printStream.println('    └── RIGHT')
        1 * printStream.println('        ├── R LEFT')
        1 * printStream.println('        └── R RIGHT')
        0 * _
    }

    def "Two level tree slanted to the left should be properly printed"() {
        given:
        def root = Stub(NonTerminal)
        root.symbol >> 'ROOT'
        root.left >> Stub(NonTerminal)
        root.left.symbol >> 'LEFT'
        root.left.left >> Stub(Terminal)
        root.left.left.symbol >> 'L LEFT'
        root.left.right >> Stub(Terminal)
        root.left.right.symbol >> 'L RIGHT'
        root.right >> Stub(Terminal)
        root.right.symbol >> 'RIGHT'

        when:
        Printer.print(root, printStream)

        then:
        1 * printStream.println('└── ROOT')
        1 * printStream.println('    ├── LEFT')
        1 * printStream.println('    │   ├── L LEFT')
        1 * printStream.println('    │   └── L RIGHT')
        1 * printStream.println('    └── RIGHT')
        0 * _
    }

    def "Full two level tree slanted to the right should be properly printed"() {
        given:
        def root = Stub(NonTerminal)
        root.symbol >> 'ROOT'
        root.left >> Stub(Terminal)
        root.left.symbol >> 'LEFT'
        root.right >> Stub(NonTerminal)
        root.right.symbol >> 'RIGHT'
        root.right.left >> Stub(Terminal)
        root.right.left.symbol >> 'R LEFT'
        root.right.right >> Stub(Terminal)
        root.right.right.symbol >> 'R RIGHT'

        when:
        Printer.print(root, printStream)

        then:
        1 * printStream.println('└── ROOT')
        1 * printStream.println('    ├── LEFT')
        1 * printStream.println('    └── RIGHT')
        1 * printStream.println('        ├── R LEFT')
        1 * printStream.println('        └── R RIGHT')
        0 * _
    }
}
