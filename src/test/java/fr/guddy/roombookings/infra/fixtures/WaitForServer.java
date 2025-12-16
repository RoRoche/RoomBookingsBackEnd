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
package fr.guddy.roombookings.infra.fixtures;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.guddy.roombookings.infra.Api;
import java.util.stream.IntStream;

public final class WaitForServer implements Runnable {

  private final Api api;

  public WaitForServer(final Api api) {
    this.api = api;
  }

  @Override
  public void run() {
    final int maxRetries = 10;
    final int delay = 200; // ms
    final boolean isReady = IntStream.range(0, maxRetries).anyMatch((final int attempt) -> {
      try {
        final HttpResponse<String> response = Unirest.get(
          String.format("http://localhost:%d/ready", this.api.port().value())
        ).asString();

        return response.getStatus() == 200 && "READY".equals(response.getBody());
      } catch (final UnirestException e) {
        try {
          Thread.sleep(delay);
        } catch (final InterruptedException ie) {
          Thread.currentThread().interrupt();
        }
        return false;
      }
    });

    if (!isReady) {
      throw new RuntimeException("Server didn't start in time");
    }
  }
}
