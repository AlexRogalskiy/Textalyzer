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
import com.wildbeeslabs.sensiblemetrics.textalyzer.utils.ConverterUtils;
import com.wildbeeslabs.sensiblemetrics.textalyzer.utils.NumberUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

    private int tokenLength;

    public VowelLexicalTokenTerm() {
        super();
    }

    @Override
    public double getVowelCounterPerToken() {
        double result = 0.0;
        if (!this.getTokenList().isEmpty()) {
            result = (double) this.getVowelCounter() / this.getTokenList().size();
        }
        return result;
    }

    private int getVowelCounter() {
        return ConverterUtils.reduceStreamBy(this.tokenList.stream().map((token) -> { return token.getVowelCount(); }), 0, (i1, i2) -> i1 + i2);
    }

    @Override
    public Set<Character> getUniqueVowelSet() {
        final Set<Character> uniqueVowelSet = new HashSet<>();
        this.tokenList.stream().forEach((token) -> {
            uniqueVowelSet.addAll(token.getVowelCharacterSet());
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
        sBuffer.append("}").append(", ").append(this.getTokenLength()).append(")");
        sBuffer.append(" -> ").append(NumberUtils.format(this.getVowelCounterPerToken()));
        return sBuffer.toString();
    }
}
