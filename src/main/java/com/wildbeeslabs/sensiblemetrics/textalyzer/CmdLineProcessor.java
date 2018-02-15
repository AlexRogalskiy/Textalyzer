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

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.kohsuke.args4j.spi.ExplicitBooleanOptionHandler;

/**
 * Class to process input CLI arguments
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public final class CmdLineProcessor {

    /**
     * Default logger instance
     */
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Option(name = "-in", aliases = {"--input"}, required = true, usage = "sets input text file", metaVar = "INPUT FILE")
    private File inputSource;
    @Option(name = "-out", aliases = {"--output"}, required = true, usage = "sets output text file", metaVar = "OUTPUT FILE")
    private File outputSource;
    @Option(name = "-i", aliases = {"--ignore-case"}, required = false, usage = "enables/disables ignore case mode", metaVar = "IGNORE CASE MODE", handler = ExplicitBooleanOptionHandler.class)
    private boolean ignoreCase;
    // Error flag
    private boolean errorFlag = false;

    public CmdLineProcessor(final String... args) {
        final CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);

            if (null != getInputSource() && !getInputSource().isFile()) {
                throw new CmdLineException(parser, "Invalid argument: --input is not a valid input file.", null);
            }
//            if (null == getOutputSource() || !getOutputSource().isFile()) {
//                throw new CmdLineException(parser, "Invalid argument: --output is not a valid output file.", null);
//            }
            errorFlag = true;
        } catch (CmdLineException ex) {
            LOGGER.error(String.format("ERROR: cannot parse input / output arguments, message=(%s)", ex.getMessage()));
            LOGGER.error(String.format("Example: java %s %s", AppLoader.class.getName(), parser.printExample(OptionHandlerFilter.ALL)));
        }
    }

    /**
     * Returns whether the parameters could be parsed without an error.
     *
     * @return true if no error occurred, false - otherwise
     */
    public boolean isErrorFree() {
        return this.errorFlag;
    }

    /**
     * Returns the input source file.
     *
     * @return The input source file.
     */
    public File getInputSource() {
        return this.inputSource;
    }

    /**
     * Returns the output source file.
     *
     * @return The output source file.
     */
    public File getOutputSource() {
        return this.outputSource;
    }

    /**
     * Returns the flag of ignore case option
     *
     * @return boolean (true - if ignore case flag has been set, false -
     * otherwise)
     */
    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }
}
