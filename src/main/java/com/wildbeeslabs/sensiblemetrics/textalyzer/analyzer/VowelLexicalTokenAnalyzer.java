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
package com.wildbeeslabs.sensiblemetrics.textalyzer.analyzer;

import com.wildbeeslabs.sensiblemetrics.textalyzer.analyzer.interfaces.IVowelLexicalTokenAnalyzer;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.VowelLexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.VowelLexicalTokenTerm;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.IVowelLexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.IVowelLexicalTokenTerm;
import com.wildbeeslabs.sensiblemetrics.textalyzer.utils.ConverterUtils;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import org.apache.commons.lang3.StringUtils;

/**
 * Vowel lexical token analyzer class to operate on lexical tokens stream
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 * @param <E>
 * @param <T>
 * @param <U>
 *
 */
@Value(staticConstructor = "getInstance")
@EqualsAndHashCode(callSuper = true)
@ToString
public class VowelLexicalTokenAnalyzer<E extends CharSequence, T extends IVowelLexicalToken<E>, U extends IVowelLexicalTokenTerm<E, T>> extends BaseLexicalTokenAnalyzer<E, T, U> implements IVowelLexicalTokenAnalyzer<E, T, U> {

    /**
     * Default token filter pattern
     */
    public static final String DEFAULT_TOKEN_FILTER_PATTERN = "[^a-zA-Z]";

    public VowelLexicalTokenAnalyzer() {
        getLogger().debug("Initializing vowel lexical token analyzer...");
    }

    @Override
    public Map<T, Integer> getVowelCountMapByLexicalToken(final List<T> tokenList) {
        return ConverterUtils.getMapSumBy(tokenList.stream(), Function.identity(), token -> token.vowelCount());
    }

    protected Map<String, Integer> getLexicalTokenVowelCountMap(final List<T> tokenList) {
        return ConverterUtils.getMapSumBy(tokenList.stream(), (token) -> token.getId().toString(), token -> token.vowelCount());
    }

    public Map<Integer, IntSummaryStatistics> getVowelLexicalTokenStatisticsByLength(final List<T> tokenList) {
        return this.getLexicalTokenStatistics(tokenList.stream(), (token) -> token.length(), mapper -> mapper.vowelCount());
    }

    @Override
    protected Function<CharSequence, CharSequence> getDefaultFilter() {
        return ((word) -> String.valueOf(word).replaceAll(VowelLexicalTokenAnalyzer.DEFAULT_TOKEN_FILTER_PATTERN, StringUtils.EMPTY).toLowerCase().trim());
    }

    @Override
    protected T createLexicalToken(final E value) {
        return (T) new VowelLexicalToken(value);
    }

    @Override
    protected U createLexicalTokenTerm() {
        return (U) new VowelLexicalTokenTerm<>();
    }
}
