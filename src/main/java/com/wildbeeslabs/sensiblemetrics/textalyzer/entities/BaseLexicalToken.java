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
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * Base abstract lexical token class to store information on lexical token
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 * @param <T>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class BaseLexicalToken<T extends CharSequence> implements ILexicalToken<T> {

    /**
     * Default lexical token sort order comparator
     */
    public static final BaseLexicalToken.LexicalComparator<String> DEFAULT_TOKEN_SORT_COMPARATOR = new BaseLexicalToken.LexicalComparator<>();

    @Setter(AccessLevel.NONE)
    private final UUID id;

    protected Comparator<? super String> comparator;
    protected T value;

    protected static class LexicalComparator<T extends Comparable<? super T>> implements Comparator<T> {

        @Override
        public int compare(final T first, final T last) {
            return Objects.compare(first, last, this);
        }
    }

    public BaseLexicalToken(final T value, final Comparator<? super String> comparator) {
        this.value = value;
        this.comparator = comparator;
        this.id = UUID.randomUUID();
    }

    @Override
    public int getLength() {
        if (Objects.isNull(this.value)) {
            return 0;
        }
        return this.value.length();
    }

    @Override
    public Set<Integer> getCharacterSet() {
        return Stream.of(this.value)
                .flatMapToInt(CharSequence::chars)
                .mapToObj(c -> Integer.valueOf(c))
                .collect(Collectors.toSet());
    }
}
