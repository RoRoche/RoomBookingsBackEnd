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
import fr.guddy.roombookings.infra.matchers.HasHeaderWithValue;
import fr.guddy.roombookings.infra.matchers.HasStatus;
import org.cactoos.list.ListOf;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

/**
 * Tests on the {@link PostRoomRequest}.
 *
 * @since 1.0.0
 */
final class PostRoomRequestTest {

    /**
     * The API.
     */
    @RegisterExtension
    @SuppressWarnings("JTCOP.RuleProhibitStaticFields")
    static final ApiExternalExtension API = new ApiExternalExtension();

    @Test
    void isOK() {
        MatcherAssert.assertThat(
            "Create room is successful",
            new HttpTestCase.WithFixtures<>(
                new ListOf<>(PostRoomRequestTest.API.rooms()::clearAll),
                Unirest.post(
                    "http://localhost:%d/rooms".formatted(PostRoomRequestTest.API.port().value())
                ).body("{\"name\":\"test_name\",\"capacity\":12}")::asString
            ),
            new AllOf<>(
                new HasStatus(HttpStatus.CREATED_201),
                new HasHeaderWithValue("Location", Matchers.startsWith("/rooms/"))
            )
        );
    }

    @Test
    void isConflict() {
        MatcherAssert.assertThat(
            "Is conflicting on name",
            new HttpTestCase.WithFixtures<>(
                new ListOf<>(
                    PostRoomRequestTest.API.rooms()::clearAll,
                    new CreateSimpleRoom(PostRoomRequestTest.API)
                ),
                Unirest.post(
                    "http://localhost:%d/rooms".formatted(PostRoomRequestTest.API.port().value())
                ).body("{\"name\":\"test_name\",\"capacity\":12}")::asString
            ),
            new AllOf<>(
                new HasStatus(HttpStatus.CONFLICT_409),
                new HasBody("A room named 'test_name' already exists")
            )
        );
    }
}
