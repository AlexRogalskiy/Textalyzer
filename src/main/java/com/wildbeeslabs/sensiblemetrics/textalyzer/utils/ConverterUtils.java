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

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
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

    public static <T, K, V> Map<K, V> convertToMap(final Stream<T> stream, final Function<T, K> keys, final Function<T, V> values) {
        return stream.collect(Collectors.toMap(keys, values));
    }

    public static <K, T> Map<K, List<T>> getSortedMapByKey(final Map<K, List<T>> map, final Comparator<? super K> comparator) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(comparator))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public static <T> Map<Integer, IntSummaryStatistics> getMapStatisticsBy(final Stream<T> stream, final Function<T, Integer> groupingBy, final ToIntFunction<? super T> mapper) {
        return stream.collect(Collectors.groupingBy(groupingBy, Collectors.summarizingInt(mapper)));
    }

    public static <T, K> Map<K, Optional<T>> getMapMaxBy(final Stream<T> stream, final Function<T, K> groupingBy, final Comparator<? super T> cmp) {
        return stream.collect(Collectors.groupingBy(groupingBy, Collectors.maxBy(cmp)));
    }

    public static <T, K> Map<K, Optional<T>> getMapMinBy(final Stream<T> stream, final Function<T, K> groupingBy, final Comparator<? super T> cmp) {
        return stream.collect(Collectors.groupingBy(groupingBy, Collectors.minBy(cmp)));
    }

    public static <T> Optional<T> getMaxBy(final Stream<T> stream, final Comparator<? super T> cmp) {
        return getMinMaxBy(stream, Collectors.maxBy(cmp));
    }

    public static <T> Optional<T> getMinBy(final Stream<T> stream, final Comparator<? super T> cmp) {
        return getMinMaxBy(stream, Collectors.minBy(cmp));
    }

    protected static <T> Optional<T> getMinMaxBy(final Stream<T> stream, final Collector<T, ?, Optional<T>> collector) {
        return stream.collect(collector);
    }

    public static <E> Map<Integer, Long> getMapCountBy(final Stream<E> stream, final Function<E, Integer> groupingBy) {
        return stream.collect(Collectors.groupingBy(groupingBy, Collectors.counting()));
    }

    public static <E, K, U> Map<K, List<U>> convertToMapList(final Stream<E> stream, final Function<E, K> groupingBy, final Function<E, U> mapper) {
        return stream.collect(Collectors.groupingBy(groupingBy, Collectors.mapping(mapper, Collectors.toList())));
    }

    public static <E, K, U> Map<K, Set<U>> convertToMapSet(final Stream<E> stream, final Function<E, K> groupingBy, final Function<E, U> mapper) {
        return stream.collect(Collectors.groupingBy(groupingBy, Collectors.mapping(mapper, Collectors.toSet())));
    }

    public static <E, U> List<U> convertToList(final Stream<E> stream, final Function<E, U> mapper) {
        return stream.collect(Collectors.mapping(mapper, Collectors.toList()));
    }

    public static <E, U> Stream<U> getStreamBy(final Stream<E> stream, final Function<E, U> mapper, final Predicate<U> predicate) {
        return stream.map(mapper).filter(predicate);
    }

    public static <T extends Number> T reduceStreamBy(final Stream<T> stream, final T identity, final BinaryOperator<T> accumulator) {
        return stream.reduce(identity, accumulator);
    }

    public static <E> Stream<E> getStreamSortedBy(final Stream<E> stream, final Comparator<? super E> cmp) {
        return stream.sorted(cmp);
    }

    public static <T, K> Map<K, Integer> getMapSumBy(final Stream<T> stream, final Function<T, K> keys, final Function<T, Integer> values) {
        return stream.collect(Collectors.toMap(keys, values, Integer::sum));
    }

    public static <A, B, C> Function<A, C> compose(Function<A, B> f1, Function<B, C> f2) {
        return f1.andThen(f2);
    }

    public static <T> Collection<T> join(final Collection<T> first, final Collection<T> second, final Predicate<? super T> predicate) {
        return Stream.concat(first.stream(), second.stream()).filter(predicate).collect(Collectors.toList());
    }

    public static String join(final Collection<String> collection, final String delimiter) {
        return collection.stream().collect(Collectors.joining(delimiter));
    }

    public static <K, V> String join(final Map<K, V> map, final String keyValueDelimiter, final String delimiter) {
        return map.entrySet().stream().map(entry -> entry.getKey() + keyValueDelimiter + entry.getValue()).collect(Collectors.joining(delimiter));
    }

    public static String[] join(final String[] first, final String[] second) {
        return Stream.concat(Arrays.stream(first), Arrays.stream(second)).toArray(String[]::new);
    }

    public static String join(final String[] array, final String delimiter) {
        return Arrays.stream(array).collect(Collectors.joining(delimiter));
    }

    public static Map<Integer, List<String>> getMapByLength(final String[] array) {
        return Arrays.stream(array).filter(Objects::nonNull).collect(Collectors.groupingBy(String::length));
    }

    public static String[] getArrayBy(final String[] array, final Predicate<? super String> predicate) {
        return Arrays.stream(array).filter(predicate).toArray(size -> new String[size]);
    }

    public static List<String> split(final String value, final String delimiter, final Predicate<? super String> predicate) {
        return Arrays.stream(String.valueOf(value).split(delimiter)).map(String::trim).filter(predicate).collect(Collectors.toList());
    }
}
