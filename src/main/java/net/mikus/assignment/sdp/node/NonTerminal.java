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

public abstract class NonTerminal<V, N extends ASTNode> implements ASTNode<V> {

    protected N left;
    protected N right;

    private final String symbol;

    protected NonTerminal(String symbol) {
        this.symbol = symbol;
    }

    public void setLeft(N left) {
        this.left = left;
    }

    public void setRight(N right) {
        this.right = right;
    }

    public N getLeft() {
        return left;
    }

    public N getRight() {
        return right;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", left.toString(), symbol, right.toString());
    }
}
