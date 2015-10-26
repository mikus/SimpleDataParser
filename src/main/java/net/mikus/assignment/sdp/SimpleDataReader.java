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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleDataReader implements Closeable {

    private final static String HEADER_PATTERN = "\\p{Alpha}\\w*(\\s+\\p{Alpha}\\w*)*";
    private final static String SPLIT_PATTERN = "\\s+";

    private final BufferedReader reader;

    private String line;

    private String[] attributes;

    private int row = 0;

    public SimpleDataReader(File file) throws IOException {
        reader = new BufferedReader(new FileReader(file));
        retrieveHeader();
    }

    private void nextLine() throws IOException {
        if (line == null) {
            ++row;
            line = reader.readLine();
            if (line != null)
                line = line.trim();
        }
    }

    private void retrieveHeader() throws IOException {
        nextLine();
        String[] data = line.split(SPLIT_PATTERN);
        if (Pattern.matches(HEADER_PATTERN, line)) {
            attributes = data;
            line = null;
        } else {
            attributes = IntStream.range(0, data.length)
                    .boxed()
                    .map(this::createHeaderNameFor)
                    .toArray(String[]::new);
        }
    }

    private String createHeaderNameFor(int nr) {
        String name = "";
        do {
            name = ((char)('a' + (nr % 26))) + name;
            nr = nr / 26 - 1;
        } while (nr >= 0);
        return name;
    }

    public Map<String, Double> readLine() throws IOException {
        nextLine();
        if (line == null)
            return null;

        String[] columns = line.split(SPLIT_PATTERN);
        if (columns.length != attributes.length)
            throw new IOException(
                    String.format("Unexpected number of columns %d instead of %d in row %d",
                            columns.length, attributes.length, row));

        Map<String, Double> result = IntStream.range(0, columns.length)
                .boxed()
                .collect(Collectors.toMap(
                        i -> attributes[i],
                        i -> Double.parseDouble(columns[i])
                ));

        line = null;

        return result;
    }

    public List<String> getAttributes() {
        return Arrays.asList(attributes);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
