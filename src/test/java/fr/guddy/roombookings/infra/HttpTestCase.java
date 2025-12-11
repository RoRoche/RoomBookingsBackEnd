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
package fr.guddy.roombookings.infra;

import com.mashape.unirest.http.HttpResponse;
import java.util.List;

@FunctionalInterface
public interface HttpTestCase<T> {
  HttpResponse<T> response() throws Exception;

  final class WithFixtures<T> implements HttpTestCase<T> {

    private final List<Runnable> fixtures;
    private final HttpTestCase<T> delegate;

    public WithFixtures(final List<Runnable> fixtures, final HttpTestCase<T> delegate) {
      this.fixtures = fixtures;
      this.delegate = delegate;
    }

    @Override
    public HttpResponse<T> response() throws Exception {
      this.fixtures.forEach(Runnable::run);
      return this.delegate.response();
    }
  }
}
