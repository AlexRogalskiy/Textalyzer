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
package com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces;

import java.io.Serializable;
import java.util.Collection;
import java.util.IntSummaryStatistics;

/**
 *
 * Lexical token term interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-12-12
 * @param <E>
 * @param <T>
 */
public interface ILexicalTokenTerm<E extends CharSequence, T extends ILexicalToken<E>> extends Serializable {

    /**
     * Adds new token to the current term
     *
     * @param token - token to be stored
     */
    void addToken(final T token);

    /**
     * Removes token from the current term
     *
     * @param token - token to be removed
     */
    void removeToken(final T token);

    /**
     * Sets a new collection of tokens to the current term
     *
     * @param tokenCollection - collection of tokens to be stored
     */
    void setTokens(final Collection<? extends T> tokenCollection);

    /**
     * Returns number of tokens of the current term
     *
     * @return number of tokens of the current term
     */
    int size();

    /**
     * Returns formatted output representation of the current term
     *
     * @return - term as string
     */
    String toFormatString();

    /**
     * Returns formatted output statistics of the current term (default by
     * length)
     *
     * @return - term statistics
     */
    IntSummaryStatistics getStatistics();

    /**
     * Returns average token length of the current term
     *
     * @return - average token length
     */
    double getAvgTokenLength();
}
