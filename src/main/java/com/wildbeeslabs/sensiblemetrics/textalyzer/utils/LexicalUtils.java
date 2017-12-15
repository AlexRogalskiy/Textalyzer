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
package com.wildbeeslabs.sensiblemetrics.textalyzer.utils;

import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.LexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.LexicalTokenTerm;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalTokenTerm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * Helper class to handle lexical format operations
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public final class LexicalUtils {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(LexicalUtils.class);
    /**
     * Default token delimiter
     */
    public static final String DEFAULT_TOKEN_DELIMITER = "\\s+";

    private LexicalUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T extends ILexicalToken> Map<T, Integer> getCountMapByTokenVowelsString(final List<T> tokenList) {
        return tokenList.parallelStream().collect(Collectors.toMap(word -> word, word -> word.getVowelCount(), Integer::sum));
    }

    public static <T extends ILexicalToken> Map<Integer, List<T>> getTokenMapByWordLength(final Stream<String> stream) {
        final Map<Integer, List<T>> wordsMap = stream
                .flatMap(line -> Arrays.stream(line.trim().split(LexicalUtils.DEFAULT_TOKEN_DELIMITER)))
                .map(word -> word.replaceAll(LexicalToken.DEFAULT_TOKEN_FILTER_PATTERN, StringUtils.EMPTY).toLowerCase().trim())
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.groupingBy(String::length, Collectors.mapping(word -> (T) new LexicalToken(word), Collectors.toList())));
        return wordsMap;
    }

    public static <T extends ILexicalToken> Map<Integer, List<T>> getSortedTokenMapByWordLength(final Stream<String> stream) {
        return getSortedTokenMapByKeyLength(stream, Comparator.reverseOrder());
    }

    public static <T extends ILexicalToken> Map<Integer, List<T>> getSortedTokenMapByKeyLength(final Stream<String> stream, final Comparator<? super Integer> comparator) {
        final Map<Integer, List<T>> wordsMap = getTokenMapByWordLength(stream);
        return wordsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(comparator))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public static <T extends ILexicalToken, E extends ILexicalTokenTerm<T>> List<E> getLexicalTokenTermList(final Map<Integer, List<T>> tokenMap) {
        final List<E> tokenTermList = new ArrayList<>();
        for (final Map.Entry<Integer, List<T>> tokenEntry : tokenMap.entrySet()) {
            final E tokenTerm = (E) new LexicalTokenTerm<>();
            tokenEntry.getValue().stream().map((token) -> {
                tokenTerm.incrementSymbolCounter(token.getVowelCount());
                return token;
            }).forEach((token) -> {
                tokenTerm.addToken(token);
            });
            tokenTerm.setTokenLength(tokenEntry.getKey());
            tokenTermList.add(tokenTerm);
        }
        return tokenTermList;
    }
}
