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


abstract class BooleanExpression<N extends ASTNode<? extends T>, T> extends NonTerminal<Boolean, N> {

    private final BooleanComparator<T> comparator;

    protected BooleanExpression(String symbol, BooleanComparator<T> comparator) {
        super(symbol);
        this.comparator = comparator;
    }

    @Override
    public Boolean evaluate(EvaluationContext ctx) {
        T leftValue = left.evaluate(ctx);
        T rightValue = right.evaluate(ctx);
        return comparator.compare(leftValue, rightValue);
    }
}
