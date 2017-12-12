/*
 * The MIT License
 *
 * Copyright 2017 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.sensiblemetrics.textalyzer.entities;

import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalTokenTerm;

import com.wildbeeslabs.sensiblemetrics.textalyzer.utils.NumberUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class to store information on lexical tokens
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 * @param <T>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class LexicalTokenTerm<T extends LexicalToken> implements ILexicalTokenTerm<T> {

    @Setter(AccessLevel.NONE)
    private final List<T> tokenList;

    @Setter(AccessLevel.NONE)
    private Set<Character> uniqueSymbolSet;

    @Setter(AccessLevel.NONE)
    private int symbolCounter;

    private int tokenLength;

    @Setter(AccessLevel.NONE)
    private double symbolAvgCounter;

    public LexicalTokenTerm() {
        this.tokenList = new ArrayList<>();
        this.symbolCounter = 0;
    }

    @Override
    public void setTokens(final Collection<? extends T> tokenCollection) {
        this.tokenList.clear();
        if (Objects.nonNull(tokenCollection)) {
            this.tokenList.addAll(tokenCollection);
        }
    }

    @Override
    public void addToken(final T token) {
        if (Objects.nonNull(token)) {
            this.tokenList.add(token);
        }
    }

    public void incrementSymbolCounter(int count) {
        assert (count >= 0);
        this.symbolCounter += count;
    }

    public void decrementSymbolCounter(int count) {
        assert (count >= 0);
        this.symbolCounter -= count;
    }

    @Override
    public double getSymbolAvgCounter() {
        this.symbolAvgCounter = calculateAvgSymbolDistribution();
        return this.symbolAvgCounter;
    }

    private double calculateAvgSymbolDistribution() {
        double result = 0.0;
        if (!this.tokenList.isEmpty()) {
            result = (double) this.symbolCounter / this.tokenList.size();
        }
        return result;
    }

    @Override
    public Set<Character> getUniqueSymbols() {
        this.uniqueSymbolSet = createUniqueSymbolSet();
        return this.uniqueSymbolSet;
    }

    private Set<Character> createUniqueSymbolSet() {
        final Set<Character> symbolSet = new HashSet<>();
        this.tokenList.stream().forEach((token) -> {
            symbolSet.addAll(token.getVowelCharacterSet());
        });
        return symbolSet;
    }

    public String toFormatString() {
        StringBuilder sBuffer = new StringBuilder();
        sBuffer.append("(");
        sBuffer.append("{");
        for (final Iterator<Character> it = this.getUniqueSymbols().iterator(); it.hasNext();) {
            sBuffer.append(it.next());
            if (it.hasNext()) {
                sBuffer.append(", ");
            }
        }
        sBuffer.append("}").append(", ").append(this.tokenLength).append(")");
        sBuffer.append(" -> ").append(NumberUtils.format(this.getSymbolAvgCounter()));
        return sBuffer.toString();
    }
}
