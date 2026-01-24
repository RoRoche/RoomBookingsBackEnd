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

import com.mashape.unirest.http.Unirest;
import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.fixtures.CreateSimpleRoom;
import fr.guddy.roombookings.infra.matchers.HasBody;
import fr.guddy.roombookings.infra.matchers.HasStatus;
import org.cactoos.list.ListOf;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

/**
 * Tests on the {@link GetRoomsRequest}.
 *
 * @since 1.0.0
 */
final class GetRoomsRequestTest {

    /**
     * The API.
     */
    @RegisterExtension
    @SuppressWarnings("JTCOP.RuleProhibitStaticFields")
    static final ApiExternalExtension API = new ApiExternalExtension();

    @Test
    void hasNoContent() {
        MatcherAssert.assertThat(
            "No rooms",
            new HttpTestCase.WithFixtures<>(
                new ListOf<>(GetRoomsRequestTest.API.rooms()::clearAll),
                Unirest.get(
                    "http://localhost:%d/rooms".formatted(GetRoomsRequestTest.API.port().value())
                )::asString
            ),
            new HasStatus(HttpStatus.NO_CONTENT_204)
        );
    }

    @Test
    void isOK() {
        MatcherAssert.assertThat(
            "Has rooms",
            new HttpTestCase.WithFixtures<>(
                new ListOf<>(
                    GetRoomsRequestTest.API.rooms()::clearAll,
                    new CreateSimpleRoom(GetRoomsRequestTest.API)
                ),
                Unirest.get(
                    "http://localhost:%d/rooms".formatted(GetRoomsRequestTest.API.port().value())
                )::asString
            ),
            new AllOf<>(
                new HasStatus(HttpStatus.OK_200),
                new HasBody("[{\"name\":\"test_name\",\"capacity\":12}]")
            )
        );
    }
}
