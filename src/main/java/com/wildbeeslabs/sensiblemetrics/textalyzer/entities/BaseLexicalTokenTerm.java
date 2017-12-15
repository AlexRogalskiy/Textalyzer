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
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalTokenTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

/**
 * Base abstract lexical token term class to store information on lexical tokens
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 * @param <E>
 * @param <T>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class BaseLexicalTokenTerm<E extends CharSequence, T extends ILexicalToken<E>> implements ILexicalTokenTerm<E, T> {

    @Setter(AccessLevel.NONE)
    protected final List<T> tokenList;

    public BaseLexicalTokenTerm() {
        this.tokenList = new ArrayList<>();
    }

    @Override
    public void setTokens(final Collection<? extends T> tokenCollection) {
        this.tokenList.clear();
        if (Objects.nonNull(tokenCollection)) {
            this.tokenList.addAll(tokenCollection);
        }
    }

    @Override
    public void addToken(final T token) {
        if (Objects.nonNull(token)) {
            this.tokenList.add(token);
        }
    }

    @Override
    public void removeToken(final T token) {
        if (Objects.nonNull(token)) {
            this.tokenList.remove(token);
        }
    }

    @Override
    public String toFormatString() {
        return StringUtils.join(this.tokenList, ", ");
    }
}
