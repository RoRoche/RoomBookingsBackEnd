/*
 * MIT License
 *
 * Copyright (c) 2018-2025 Romain Rochegude
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.HttpTestCaseEnvelope;

/**
 * The reminders.
 *
 * @param testCase The {@link HttpTestCase}.
 * @param idMin15ToPlus15 The ID of the booking minus 15m to plus 15m.
 * @param idPlus15To45 The ID of the booking plus 15 to 45m.
 * @param tsStart The starting timestamp.
 * @param tsEndMin The ending timestamp with min.
 * @param tsEndPlus The ending timestamp with plus.
 */
public record Reminders(
    HttpTestCase<String> testCase,
    long idMin15ToPlus15,
    long idPlus15To45,
    long tsStart,
    long tsEndMin,
    long tsEndPlus
) implements HttpTestCaseEnvelope {
    @Override
    public HttpTestCase<String> value() throws Exception {
        return this.testCase;
    }
}
