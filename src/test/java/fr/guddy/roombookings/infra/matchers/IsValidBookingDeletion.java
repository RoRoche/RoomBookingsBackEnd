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
package fr.guddy.roombookings.infra.matchers;

import com.mashape.unirest.http.exceptions.UnirestException;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.requests.HttpBooking;
import org.cactoos.Func;
import org.cactoos.Scalar;
import org.cactoos.Text;
import org.cactoos.func.UncheckedFunc;
import org.cactoos.scalar.Unchecked;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Check if a {@link fr.guddy.roombookings.domain.booking.Booking} deletion
 * was successful.
 *
 * @since 1.0.0
 */
public class IsValidBookingDeletion extends TypeSafeDiagnosingMatcher<Scalar<HttpBooking>> {

    /**
     * The API.
     */
    private final ApiExternalExtension api;

    /**
     * {@link Func} to get body with generated ID.
     */
    private final Func<Long, Text> body;

    public IsValidBookingDeletion(
        final ApiExternalExtension api,
        final Func<Long, Text> body
    ) {
        this.api = api;
        this.body = body;
    }

    @Override
    public final void describeTo(final Description description) {
        description.appendText("valid booking deletion (correct body + database empty)");
    }

    @Override
    protected final boolean matchesSafely(
        final Scalar<HttpBooking> result,
        final Description mismatch
    ) {
        boolean matches = true;
        final HttpBooking booking = new Unchecked<>(result).value();
        final HasBody actual = new HasBody(
            new UncheckedFunc<>(this.body).apply(booking.identifier()).toString()
        );
        final HttpTestCase<String> testcase = booking.testCase();
        if (!actual.matchesSafely(testcase, mismatch)) {
            mismatch.appendText("body mismatch: ");
            try {
                actual.describeMismatch(testcase.response().getBody(), mismatch);
            } catch (final UnirestException exception) {
                throw new MatcherRuntimeException(exception);
            }
            matches = false;
        }
        if (this.api.bookings().byId(booking.identifier()).isPresent()) {
            mismatch.appendText("booking still present in database");
            matches = false;
        }
        return matches;
    }
}
