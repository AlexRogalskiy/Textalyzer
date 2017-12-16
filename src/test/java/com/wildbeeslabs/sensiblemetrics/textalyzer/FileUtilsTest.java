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

import com.wildbeeslabs.sensiblemetrics.textalyzer.analyzer.ILexicalTokenAnalyzer;
import com.wildbeeslabs.sensiblemetrics.textalyzer.analyzer.VowelLexicalTokenAnalyzer;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalTokenTerm;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.IVowelLexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * Unit test case for FileUtils class
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-12-12
 */
public class FileUtilsTest {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(FileUtilsTest.class);

    private ILexicalTokenAnalyzer<String, IVowelLexicalToken<String>> analyzer;

    @Before
    public void setUp() {
        LOGGER.info("Initializing vowel lexical token analyzer...");
        this.analyzer = new VowelLexicalTokenAnalyzer<>();
    }

    @Test
    public void testReadFile() {
        String inputFile = "src/main/resources/INPUT.txt";
        List<ILexicalTokenTerm<String, ILexicalToken<String>>> list = FileUtils.readFile(new File(inputFile), this.analyzer);
        Assert.assertEquals("Checking the size of token list: ", 3, list.size());

        inputFile = "src/main/resources/INPUT2.txt";
        list = FileUtils.readFile(new File(inputFile), this.analyzer);
        Assert.assertEquals("Checking the size of token list:", 0, list.size());

        inputFile = "src/main/resources/INPUT3.txt";
        list = FileUtils.readFile(new File(inputFile), this.analyzer);
        Assert.assertEquals("Checking the size of token list:", 4, list.size());
    }

    @Test
    public void testWriteFileSortedDesc() {
        String inputString = "asffsa sadfas fsad asdffsda ";
        Map<Integer, List<IVowelLexicalToken<String>>> map = this.analyzer.getTokenMapByLength(Stream.of(inputString));
        Assert.assertEquals("Checking the size of token list: ", 3, map.size());

        String outputFile = "src/main/e/OUTPUT.txt";
        List<ILexicalTokenTerm<String, IVowelLexicalToken<String>>> list = this.analyzer.getLexicalTokenTermList(map);
        Assert.assertEquals("Checking the size of output token list: ", 3, list.size());
        FileUtils.writeFile(new File(outputFile), list);

        try (final Stream<String> stream = Files.lines(Paths.get(outputFile), FileUtils.DEFAULT_FILE_CHARACTER_ENCODING)) {
            final Optional<String> firstLine = stream.findFirst();
            Assert.assertTrue(firstLine.isPresent());
            Assert.assertEquals("Checking the first line of output token list: ", "({a}, 4) -> 1", firstLine.get());
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot read from input file=%s, message=%s", String.valueOf(outputFile), ex.getMessage()));
        }
    }

    @Test
    public void testWriteFileSortedAsc() {
        String inputString = "asffsa sadfas fsad asdffsda ";
        Map<Integer, List<IVowelLexicalToken<String>>> map = this.analyzer.getSortedTokenMapByKey(Stream.of(inputString));
        Assert.assertEquals("Checking the size of token list: ", 3, map.size());

        String outputFile = "src/main/resources/OUTPUT.txt";
        List<ILexicalTokenTerm<String, IVowelLexicalToken<String>>> list = this.analyzer.getLexicalTokenTermList(map);
        Assert.assertEquals("Checking the size of output token list: ", 3, list.size());
        FileUtils.writeFile(new File(outputFile), list);

        try (final Stream<String> stream = Files.lines(Paths.get(outputFile), FileUtils.DEFAULT_FILE_CHARACTER_ENCODING)) {
            final Optional<String> firstLine = stream.findFirst();
            Assert.assertTrue(firstLine.isPresent());
            Assert.assertEquals("Checking the first line of output token list: ", "({a}, 8) -> 2", firstLine.get());
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot read from input file=%s, message=%s", String.valueOf(outputFile), ex.getMessage()));
        }
    }

    @After
    public void tearDown() {
    }
}
