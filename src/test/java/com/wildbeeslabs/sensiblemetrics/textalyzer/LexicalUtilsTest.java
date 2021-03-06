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

import com.wildbeeslabs.sensiblemetrics.textalyzer.analyzer.interfaces.IVowelLexicalTokenAnalyzer;
import com.wildbeeslabs.sensiblemetrics.textalyzer.analyzer.VowelLexicalTokenAnalyzer;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.IVowelLexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.IVowelLexicalTokenTerm;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * Unit test case for Vowel lexical token analyzer class
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-12-12
 */
public class LexicalUtilsTest {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(LexicalUtilsTest.class);

    private IVowelLexicalTokenAnalyzer<String, IVowelLexicalToken<String>, IVowelLexicalTokenTerm<String, IVowelLexicalToken<String>>> analyzer;

    @Before
    public void setUp() {
        LOGGER.info("Initializing vowel lexical token analyzer...");
        this.analyzer = new VowelLexicalTokenAnalyzer<>();
    }

    @Test
    public void testGetTokenMapByWordLength() {
        String inputString = "Base test simple string";
        Map<Integer, List<IVowelLexicalToken<String>>> map = this.analyzer.getLexicalTokenMapByLength(Stream.of(inputString));
        Assert.assertEquals(2, map.size());

        inputString = "Base test";
        map = this.analyzer.getLexicalTokenMapByLength(Stream.of(inputString));
        Assert.assertEquals(1, map.size());

        inputString = "";
        map = this.analyzer.getLexicalTokenMapByLength(Stream.of(inputString));
        Assert.assertEquals(0, map.size());
    }

    @Test
    public void testGetSortedTokenMapByWordLength() {
        String inputString = "Base test simple string a new one";
        Map<Integer, List<IVowelLexicalToken<String>>> list = this.analyzer.getSortedLexicalTokenMapByKey(Stream.of(inputString), Comparator.reverseOrder());
        Assert.assertEquals(4, list.size());
    }

    @Test
    public void testGetLexicalTokenTermList() {
        String inputString = "sfd saf sdf f asdfs dsf sdf sdf ass";
        Map<Integer, List<IVowelLexicalToken<String>>> map = this.analyzer.getLexicalTokenMapByLength(Stream.of(inputString));
        Assert.assertEquals(3, map.size());

        List<IVowelLexicalTokenTerm<String, IVowelLexicalToken<String>>> list = this.analyzer.getLexicalTokenTermList(Stream.of(inputString), Comparator.reverseOrder());
        Assert.assertEquals(3, list.size());
    }

    @After
    public void tearDown() {
    }
}
