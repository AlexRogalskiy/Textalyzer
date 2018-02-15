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
package com.wildbeeslabs.sensiblemetrics.textalyzer.analyzer.interfaces;

import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalTokenTerm;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 * Lexical token analyzer interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-12-12
 * @param <E>
 * @param <T>
 * @param <U>
 */
public interface ILexicalTokenAnalyzer<E extends CharSequence, T extends ILexicalToken<E>, U extends ILexicalTokenTerm<E, T>> {

    /**
     * Returns collection of tokens collected from stream
     *
     * @param stream - input text stream
     * @return collection of tokens collected from stream
     */
    List<T> getLexicalTokenList(final Stream<E> stream);

    /**
     * Returns tokens map grouped by length
     *
     * @param stream - input text stream
     * @return tokens map grouped by length
     */
    Map<Integer, List<T>> getLexicalTokenMapByLength(final Stream<E> stream);

    /**
     * Returns tokens unique map grouped by length
     *
     * @param stream - input text stream
     * @return tokens unique map grouped by length
     */
    Map<Integer, Set<T>> getUniqueLexicalTokenMapByLength(final Stream<E> stream);

    /**
     * Returns tokens count map grouped by length
     *
     * @param stream - input text stream
     * @return tokens count map grouped by length
     */
    Map<Integer, Long> getCountMapByLength(final Stream<E> stream);

    /**
     * Returns tokens map grouped by token ID
     *
     * @param stream - input token stream
     * @return tokens map grouped by token ID
     */
    Map<String, T> getLexicalTokenMapById(final Stream<T> stream);

    /**
     * Returns tokens map grouped by key in a sorted order
     *
     * @param stream - input text stream
     * @param comparator - comparator instance for sort ordering
     * @return tokens map grouped by key in a sorted order
     */
    Map<Integer, List<T>> getSortedLexicalTokenMapByKey(final Stream<E> stream, final Comparator<? super Integer> comparator);

    /**
     * Returns collection of token terms from the current stream
     *
     * @param stream - input text stream
     * @param comparator - comparator instance for sort ordering
     * @return collection of token terms
     */
    List<U> getLexicalTokenTermList(final Stream<E> stream, final Comparator<? super Integer> comparator);
}
