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

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

/**
 * Vowel lexical token class to store information on lexical token
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 * @param <T>
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class VowelLexicalToken<T extends CharSequence> extends BaseLexicalToken<T> implements IVowelLexicalToken<T> {

    /**
     * Default vowels pattern
     */
    public static final String DEFAULT_TOKEN_VOWELS_PATTERN = "[^aeiouyAEIOUY]";

    public VowelLexicalToken() {
        this((T) StringUtils.EMPTY);
    }

    public VowelLexicalToken(final T value) {
        this(value, DEFAULT_TOKEN_SORT_COMPARATOR);
    }

    public VowelLexicalToken(final T value, final Comparator<? super String> comparator) {
        super(value, comparator);
    }

    @Override
    public Set<Character> vowelCharacterSet() {
        return Stream.of(this.toVowelString())
                .flatMapToInt(CharSequence::chars)
                .distinct()
                .mapToObj(c -> Character.valueOf((char) c))
                .collect(Collectors.toSet());
    }

    @Override
    public int vowelCount() {
        return this.toVowelString().length();
    }

    public String getDistinctVowelString() {
        return Stream.of(this.toVowelString()
                .split(StringUtils.EMPTY))
                .distinct()
                .sorted(VowelLexicalToken.DEFAULT_TOKEN_SORT_COMPARATOR)
                .collect(Collectors.joining(StringUtils.EMPTY));
    }

    private String toVowelString() {
        return String.valueOf(this.value).replaceAll(VowelLexicalToken.DEFAULT_TOKEN_VOWELS_PATTERN, StringUtils.EMPTY);
    }
}
