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

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Helper class to handle stream converter operations
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public class ConverterUtils {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(ConverterUtils.class);

    private ConverterUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T, K, V> Map<K, V> convertTokenListToMap(final List<T> list, final Function<T, K> function1, final Function<T, V> function2) {
        return list.stream().collect(Collectors.toMap(function1, function2));
    }

    public static <K, T> Map<K, List<T>> getSortedTokenMapByKey(final Map<K, List<T>> map, final Comparator<? super K> comparator) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(comparator))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public static <T> Map<Integer, IntSummaryStatistics> getTokenStatisticsBy(final List<T> list, final Function<T, Integer> function, final ToIntFunction<? super T> mapper) {
        return list.stream().collect(Collectors.groupingBy(function, Collectors.summarizingInt(mapper)));
    }

    public static <T, K> Map<K, Optional<T>> getTokenBy(final List<T> list, final Function<T, K> function, final Comparator<? super T> cmp) {
        return list.stream().collect(Collectors.groupingBy(function, Collectors.maxBy(cmp)));
    }

    public static <E> Map<Integer, Long> getCountMapBy(final Stream<E> stream, final Function<E, Integer> function) {
        return stream.collect(Collectors.groupingBy(function, Collectors.counting()));
    }

    public static <E, K, U> Map<K, List<U>> getMapListBy(final Stream<E> stream, final Function<E, K> function, final Function<E, U> mapper) {
        return stream.collect(Collectors.groupingBy(function, Collectors.mapping(mapper, Collectors.toList())));
    }

    public static <E, K, U> Map<K, Set<U>> getMapSetBy(final Stream<E> stream, final Function<E, K> function, final Function<E, U> mapper) {
        return stream.collect(Collectors.groupingBy(function, Collectors.mapping(mapper, Collectors.toSet())));
    }

    public static <E, U> List<U> convertToList(final Stream<E> stream, final Function<E, U> mapper) {
        return stream.collect(Collectors.mapping(mapper, Collectors.toList()));
    }

    public static <T, K> Map<K, Integer> getCountSumBy(final List<T> tokenList, final Function<T, K> function1, final Function<T, Integer> function2) {
        return tokenList.stream().collect(Collectors.toMap(function1, function2, Integer::sum));
    }
}