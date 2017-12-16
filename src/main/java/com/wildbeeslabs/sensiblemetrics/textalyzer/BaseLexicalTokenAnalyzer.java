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
package com.wildbeeslabs.sensiblemetrics.textalyzer;

import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.VowelLexicalTokenTerm;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalTokenTerm;
import com.wildbeeslabs.sensiblemetrics.textalyzer.utils.LexicalUtils;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Base lexical token analyzer class to operate on lexical tokens stream
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 * @param <E>
 * @param <T>
 *
 */
public abstract class BaseLexicalTokenAnalyzer<E extends CharSequence, T extends ILexicalToken<E>> {

    /**
     * Default logger instance
     */
    private final Logger LOGGER = LogManager.getLogger(getClass());

    public BaseLexicalTokenAnalyzer() {
    }

    protected Stream<E> getFilteredStream(final Stream<E> stream, final Function<CharSequence, CharSequence> tokenFilter) {
        return stream.flatMap(line -> Arrays.stream(String.valueOf(line).trim().split(LexicalUtils.DEFAULT_TOKEN_DELIMITER)))
                .map(word -> tokenFilter.apply(word))
                .filter(StringUtils::isNotBlank)
                .map(word -> (E) word);
    }

    protected List<T> getLexicalTokenList(final Stream<E> stream, final Function<CharSequence, CharSequence> tokenFilter) {
        final List<T> tokenList = this.getFilteredStream(stream, tokenFilter)
                .collect(Collectors.mapping(word -> createLexicalToken((E) word), Collectors.toList()));
        return tokenList;
    }

    protected Map<Integer, List<T>> getTokenMapByLength(final Stream<E> stream, final Function<CharSequence, CharSequence> tokenFilter) {
        final Stream<E> tokenStream = this.getFilteredStream(stream, tokenFilter);
        final Map<Integer, List<T>> tokenMap = tokenStream.collect(Collectors.groupingBy(word -> word.length(), Collectors.mapping(word -> createLexicalToken((E) word), Collectors.toList())));
        return tokenMap;
    }

    protected Map<Integer, List<T>> getReversedSortedTokenMapByKey(final Stream<E> stream, final Function<CharSequence, CharSequence> tokenFilter) {
        return this.getSortedTokenMapByKey(stream, tokenFilter, Comparator.reverseOrder());
    }

    protected Map<Integer, List<T>> getSortedTokenMapByKey(final Stream<E> stream, final Function<CharSequence, CharSequence> tokenFilter, final Comparator<? super Integer> comparator) {
        final Map<Integer, List<T>> tokenMap = this.getTokenMapByLength(stream, tokenFilter);
        return tokenMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(comparator))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public <U extends ILexicalTokenTerm<E, T>> List<U> getLexicalTokenTermList(final Map<Integer, List<T>> tokenMap) {
        final List<U> tokenTermList = new ArrayList<>(tokenMap.size());
        for (final Map.Entry<Integer, List<T>> tokenEntry : tokenMap.entrySet()) {
            final U tokenTerm = this.createLexicalTokenTerm();
            tokenTerm.setTokens(tokenEntry.getValue());
            tokenTerm.setTokenLength(tokenEntry.getKey());
            tokenTermList.add(tokenTerm);
        }
        return tokenTermList;
    }

    protected abstract <U extends ILexicalTokenTerm<E, T>> U createLexicalTokenTerm();

    protected abstract T createLexicalToken(final E value);
}
