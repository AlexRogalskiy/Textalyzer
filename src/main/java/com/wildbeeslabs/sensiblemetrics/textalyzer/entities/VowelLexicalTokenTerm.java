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

import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.IVowelLexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.IVowelLexicalTokenTerm;
import com.wildbeeslabs.sensiblemetrics.textalyzer.utils.NumberUtils;

import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Vowel lexical token term class to store information on lexical tokens
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 * @param <E>
 * @param <T>
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class VowelLexicalTokenTerm<E extends CharSequence, T extends IVowelLexicalToken<E>> extends BaseLexicalTokenTerm<E, T> implements IVowelLexicalTokenTerm<E, T> {

    public VowelLexicalTokenTerm() {
        super();
    }

    public double getAvgVowelCounterPerToken() {
        return this.getTokenList().stream().mapToInt((token) -> token.vowelCount()).average().getAsDouble();
    }

    @Override
    public IntSummaryStatistics getStatistics() {
        return this.getTokenList().stream().collect(Collectors.summarizingInt((token) -> token.vowelCount()));
    }

    public int count() {
        return this.count((token) -> token.vowelCount());
    }

    @Override
    public Set<Character> getUniqueVowelSet() {
        final Set<Character> uniqueVowelSet = new HashSet<>();
        this.getTokenList().stream().forEach((token) -> {
            uniqueVowelSet.addAll(token.vowelCharacterSet());
        });
        return uniqueVowelSet;
    }

    @Override
    public String toFormatString() {
        final StringBuilder sBuffer = new StringBuilder();
        sBuffer.append("(");
        sBuffer.append("{");
        for (final Iterator<Character> it = this.getUniqueVowelSet().iterator(); it.hasNext();) {
            sBuffer.append(it.next());
            if (it.hasNext()) {
                sBuffer.append(", ");
            }
        }
        sBuffer.append("}").append(", ").append(Double.valueOf(this.getAvgTokenLength()).intValue()).append(")");
        sBuffer.append(" -> ").append(NumberUtils.format(this.getAvgVowelCounterPerToken()));
        return sBuffer.toString();
    }
}
