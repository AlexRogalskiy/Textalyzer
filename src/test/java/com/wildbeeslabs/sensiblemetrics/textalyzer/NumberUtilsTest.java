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

import com.wildbeeslabs.sensiblemetrics.textalyzer.utils.NumberUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * Unit test case for Textalyzer NumberUtils class
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-12-12
 */
public class NumberUtilsTest {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(NumberUtilsTest.class);

    @Before
    public void setUp() {
    }

    @Test
    @SuppressWarnings("AssertEqualsBetweenInconvertibleTypes")
    public void testformat() {
        final double value = 3.54;
        Assert.assertEquals("3.54", NumberUtils.format(value));
    }

    @Test
    @SuppressWarnings("AssertEqualsBetweenInconvertibleTypes")
    public void testFormatByPattern() {
        final double value = 3.54;
        Assert.assertNotEquals("3.54", NumberUtils.formatByPattern(value, "#.#"));
        Assert.assertEquals("3.5", NumberUtils.formatByPattern(value, "#.#"));
    }

    @After
    public void tearDown() {
    }
}
