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

import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.VowelLexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.VowelLexicalTokenTerm;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.IVowelLexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.IVowelLexicalTokenTerm;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * Vowel lexical token analyzer class to operate on lexical tokens stream
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 * @param <E>
 * @param <T>
 *
 */
public class VowelLexicalTokenAnalyzer<E extends CharSequence, T extends IVowelLexicalToken<E>, U extends IVowelLexicalTokenTerm<E, T>> extends BaseLexicalTokenAnalyzer<E, T, U> implements IVowelLexicalTokenAnalyzer<E, T, U> {

    /**
     * Default token filter pattern
     */
    public static final String DEFAULT_TOKEN_FILTER_PATTERN = "[^a-zA-Z]";

    public VowelLexicalTokenAnalyzer() {
    }

    @Override
    public List<T> getLexicalTokenList(final Stream<E> stream) {
        return this.getLexicalTokenList(stream, this.getDefaultFilterFunction(), BaseLexicalTokenAnalyzer.DEFAULT_TOKEN_DELIMITER);
    }

    @Override
    public Function<CharSequence, CharSequence> getFilterFunction(final String filterPattern) {
        return (word -> String.valueOf(word).replaceAll(filterPattern, StringUtils.EMPTY).toLowerCase().trim());
    }

    protected Function<CharSequence, CharSequence> getDefaultFilterFunction() {
        return this.getFilterFunction(VowelLexicalTokenAnalyzer.DEFAULT_TOKEN_FILTER_PATTERN);
    }

    @Override
    public Map<Integer, List<T>> getTokenMapByLength(final Stream<E> stream) {
        return this.getTokenMapByLength(stream, this.getDefaultFilterFunction(), BaseLexicalTokenAnalyzer.DEFAULT_TOKEN_DELIMITER);
    }

    @Override
    public Map<T, Integer> getTokenVowelCountMapByVowelString(final List<T> tokenList) {
        return tokenList.parallelStream().collect(Collectors.toMap(token -> token, token -> token.getVowelCount(), Integer::sum));
    }

    protected Map<String, Integer> getTokenVowelCountMap(final List<T> tokenList) {
        return tokenList.parallelStream().collect(Collectors.toMap(token -> token.getId().toString(), token -> token.getVowelCount(), Integer::sum));
    }

    @Override
    public Map<Integer, List<T>> getSortedTokenMapByKey(final Stream<E> stream) {
        return this.getReversedSortedTokenMapByKey(stream, this.getDefaultFilterFunction(), BaseLexicalTokenAnalyzer.DEFAULT_TOKEN_DELIMITER);
    }

    @Override
    public Map<Integer, List<T>> getSortedTokenMapByKey(final Stream<E> stream, final Comparator<? super Integer> comparator) {
        return this.getSortedTokenMapByKey(stream, this.getDefaultFilterFunction(), BaseLexicalTokenAnalyzer.DEFAULT_TOKEN_DELIMITER, comparator);
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
