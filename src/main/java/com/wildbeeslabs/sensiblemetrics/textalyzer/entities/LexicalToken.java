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

import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalToken;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

/**
 * Entity class to store information on lexical token
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ToString
public class LexicalToken implements ILexicalToken {

    /**
     * Default lexical sort order comparator
     */
    public static final LexicalToken.LexicalComparator<String> DEFAULT_TOKEN_SORT_COMPARATOR = new LexicalToken.LexicalComparator<>();
    /**
     * Default vowels pattern
     */
    public static final String DEFAULT_TOKEN_VOWELS_PATTERN = "[^aeiouyAEIOUY]";
    /**
     * Default token filter pattern
     */
    public static final String DEFAULT_TOKEN_FILTER_PATTERN = "[^a-zA-Z]";

    private Comparator<? super String> comparator;
    private String value;

    public LexicalToken(final String value) {
        this(value, DEFAULT_TOKEN_SORT_COMPARATOR);
    }

    public LexicalToken(final String value, final Comparator<? super String> comparator) {
        this.value = value;
        this.comparator = comparator;
    }

    protected static class LexicalComparator<T extends Comparable<? super T>> implements Comparator<T> {

        @Override
        public int compare(final T first, final T last) {
            return Objects.compare(first, last, this);
        }
    }

    @Override
    public Set<Integer> getCharacterSet() {
        return Stream.of(this.value)
                .flatMapToInt(CharSequence::chars)
                .mapToObj(c -> Integer.valueOf(c))
                .collect(Collectors.toSet());
    }

    public Set<Character> getVowelCharacterSet() {
        return Stream.of(this.getVowelsAsString())
                .flatMapToInt(CharSequence::chars)
                .distinct()
                .mapToObj(c -> Character.valueOf((char) c))
                .collect(Collectors.toSet());
    }

    public int getVowelCount() {
        return this.getVowelsAsString().length();
    }

    @Override
    public int getLength() {
        return this.value.length();
    }

    public String getDistinctVowelString() {
        return Stream.of(this.getVowelsAsString()
                .split(StringUtils.EMPTY))
                .distinct()
                .sorted(LexicalToken.DEFAULT_TOKEN_SORT_COMPARATOR)
                .collect(Collectors.joining(StringUtils.EMPTY));
    }

    private String getVowelsAsString() {
        return this.value.replaceAll(DEFAULT_TOKEN_VOWELS_PATTERN, StringUtils.EMPTY);
    }
}
