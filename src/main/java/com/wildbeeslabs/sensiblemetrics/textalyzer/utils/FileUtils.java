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

import com.wildbeeslabs.sensiblemetrics.textalyzer.analyzer.ILexicalTokenAnalyzer;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.ILexicalTokenTerm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Helper class to handle file read / write operations
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public class FileUtils {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);
    /**
     * Default file character encoding
     */
    public static final Charset DEFAULT_FILE_CHARACTER_ENCODING = StandardCharsets.UTF_8;
    
    private FileUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }
    
    public static List<String> readAllLines(final File inputFile) {
        Objects.requireNonNull(inputFile);
        List<String> resultList = Collections.EMPTY_LIST;
        try {
            resultList = Files.readAllLines(inputFile.toPath(), FileUtils.DEFAULT_FILE_CHARACTER_ENCODING);
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot read from input file=%s, message=%s", String.valueOf(inputFile), ex.getMessage()));
        }
        return resultList;
    }
    
    public static List<String> readFileByFilter(final File inputFile, final Predicate<String> predicate) {
        Objects.requireNonNull(inputFile);
        List<String> resultList = Collections.EMPTY_LIST;
        try (final BufferedReader br = Files.newBufferedReader(inputFile.toPath(), FileUtils.DEFAULT_FILE_CHARACTER_ENCODING)) {
            resultList = br.lines().filter(predicate).collect(Collectors.toList());
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot read from input file=%s, message=%s", String.valueOf(inputFile), ex.getMessage()));
        }
        return resultList;
    }
    
    public static <U extends CharSequence, T extends ILexicalToken<U>, E extends ILexicalTokenTerm<U, T>> List<E> readFile(final File inputFile, final ILexicalTokenAnalyzer<U, T, E> analyzer) {
        Objects.requireNonNull(inputFile);
        final List<String> stringList = readAllLines(inputFile);
        final Map<Integer, List<T>> wordsMap = analyzer.getSortedTokenMapByKey(stringList.stream().map(word -> (U) word));
        return analyzer.getLexicalTokenTermList(wordsMap);
    }
    
    public static <U extends CharSequence, T extends ILexicalToken<U>, E extends ILexicalTokenTerm<U, T>> void writeFile(final File outputFile, final List<? extends E> output) {
        Objects.requireNonNull(outputFile);
        Objects.requireNonNull(output);
        try (final PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputFile.toPath(), FileUtils.DEFAULT_FILE_CHARACTER_ENCODING))) {
            output.stream().map(term -> term.toFormatString()).forEach(writer::println);
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            LOGGER.error(String.format("ERROR: cannot create output file=%s, message=%s", String.valueOf(outputFile), ex.getMessage()));
        } catch (IOException ex) {
            LOGGER.error(String.format("ERROR: cannot process read / writer operations on file=%s, message=%s", String.valueOf(outputFile), ex.getMessage()));
        }
    }
}
