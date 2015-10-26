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


class SimpleDataReaderTest extends Specification {

    File file

    def setup() {
        file = File.createTempFile('test', '.data')
    }

    def cleanup() {
        file.delete()
    }

    def 'Attribute names should be properly read from file header'() {
        given:
        file.write('ala ma kota \n 1 2 3')
        def reader = new SimpleDataReader(file)

        when:
        def row = reader.readLine()

        then:
        row[name] == value

        where:
         name  | value
        'ala'  | 1d
        'ma'   | 2d
        'kota' | 3d
    }

    def 'Default attribute names should be generated for file without a header'() {
        given:
        file.write((1..100).join('\t'))
        def reader = new SimpleDataReader(file)

        when:
        def row = reader.readLine()

        then:
        row[name] == value

        where:
        name | value
        'a'  | 1d
        'z'  | 26d
        'aa' | 27d
        'az' | 52d
        'ba' | 53d
    }

    def 'Same number of lines should be readed as in file'() {
        given:
        file.write('1 \n 2 \n 3')
        def reader = new SimpleDataReader(file)

        when:
        def row1 = reader.readLine()
        def row2 = reader.readLine()
        def row3 = reader.readLine()
        def row4 = reader.readLine()

        then:
        row1 != null
        row2 != null
        row3 != null
        row4 == null
    }

}
