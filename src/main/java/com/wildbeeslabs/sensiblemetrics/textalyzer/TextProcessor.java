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

import com.wildbeeslabs.sensiblemetrics.textalyzer.analyzer.interfaces.ILexicalTokenAnalyzer;
import com.wildbeeslabs.sensiblemetrics.textalyzer.analyzer.VowelLexicalTokenAnalyzer;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.IVowelLexicalToken;
import com.wildbeeslabs.sensiblemetrics.textalyzer.entities.interfaces.IVowelLexicalTokenTerm;
import com.wildbeeslabs.sensiblemetrics.textalyzer.utils.FileUtils;

import java.util.List;
import java.util.Objects;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Text processor class to operate on input / output text stream
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public class TextProcessor {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(TextProcessor.class);

    public void init(final String[] args) {
        LOGGER.info("Initializing command line processor...");
        final CmdLineProcessor cmdProcessor = new CmdLineProcessor(args);
        LOGGER.info("Initializing vowel lexical token analyzer...");
        final ILexicalTokenAnalyzer<String, IVowelLexicalToken<String>, IVowelLexicalTokenTerm<String, IVowelLexicalToken<String>>> analyzer = new VowelLexicalTokenAnalyzer<>();

        List<IVowelLexicalTokenTerm<String, IVowelLexicalToken<String>>> tokenTermList = null;
        if (Objects.nonNull(cmdProcessor.getInputSource())) {
            tokenTermList = FileUtils.readFile(cmdProcessor.getInputSource(), analyzer);
        }
        if (Objects.nonNull(cmdProcessor.getOutputSource())) {
            FileUtils.writeFile(cmdProcessor.getOutputSource(), tokenTermList);
        }
    }
}
