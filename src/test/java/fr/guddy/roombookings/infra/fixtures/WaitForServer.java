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

import fr.guddy.roombookings.infra.Api;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.cactoos.Scalar;

/**
 * Wait for server to be ready.
 *
 * @since 1.0.0
 */
public final class WaitForServer implements Runnable {

    /**
     * Is server ready.
     */
    private final Scalar<Boolean> ready;

    public WaitForServer(final Scalar<Boolean> ready) {
        this.ready = ready;
    }

    public WaitForServer(final Api api) {
        this(new IsServerReady(api));
    }

    @Override
    public void run() {
        Awaitility.await()
            .atMost(Duration.ofSeconds(2))
            .pollInterval(Duration.ofMillis(200))
            .untilAsserted(
                () -> {
                    if (!this.ready.value()) {
                        throw new AssertionError("Server didn't start in time");
                    }
                }
            );
    }
}
