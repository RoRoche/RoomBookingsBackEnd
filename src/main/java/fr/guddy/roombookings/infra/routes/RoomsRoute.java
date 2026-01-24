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
package fr.guddy.roombookings.infra.routes;

import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.domain.rooms.Rooms;
import fr.guddy.roombookings.infra.params.IntegerParameter;
import fr.guddy.roombookings.infra.params.OptionalParameter;
import fr.guddy.roombookings.infra.params.Parameter;
import fr.guddy.roombookings.infra.params.QueryParameter;
import fr.guddy.roombookings.infra.params.StringParameter;
import fr.guddy.roombookings.infra.requests.GetAvailableRoomsRequest;
import fr.guddy.roombookings.infra.requests.GetBookingsForRoomInSlotRequest;
import fr.guddy.roombookings.infra.requests.GetCapableRoomsRequest;
import fr.guddy.roombookings.infra.requests.GetNamedRoomRequest;
import fr.guddy.roombookings.infra.requests.GetRoomsRequest;
import fr.guddy.roombookings.infra.requests.PostBookingRequest;
import fr.guddy.roombookings.infra.requests.PostRoomRequest;
import fr.guddy.roombookings.infra.requests.Request;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Routes to expose {@link Rooms}.
 *
 * @since 1.0.0
 */
public final class RoomsRoute implements EndpointGroup {

    /**
     * Collection of {@link Rooms}.
     */
    private final Rooms rooms;

    /**
     * Collection of {@link fr.guddy.roombookings.domain.booking.Booking}.
     */
    private final Bookings bookings;

    public RoomsRoute(final Rooms rooms, final Bookings bookings) {
        this.rooms = rooms;
        this.bookings = bookings;
    }

    @Override
    public void addEndpoints() {
        ApiBuilder.get(
            (final Context ctx) -> {
                final OptionalParameter<String> capacity = new OptionalParameter<>(
                    new QueryParameter("capacity", ctx)
                );
                final Request request;
                if (
                    Stream.of(
                        capacity,
                        new OptionalParameter<>(new QueryParameter("timestamp_start", ctx))
                    ).allMatch(
                        (final Parameter<Optional<String>> parameter) ->
                            parameter.value().isPresent()
                    )
                ) {
                    request = new GetAvailableRoomsRequest(this.rooms, this.bookings, ctx);
                } else {
                    request = capacity
                        .value()
                        .<Request>map(
                            (final String capaAsString) ->
                                new GetCapableRoomsRequest(
                                    this.rooms,
                                    new IntegerParameter(
                                        new StringParameter("capacity", capaAsString)
                                    )
                                )
                        )
                        .orElseGet(() -> new GetRoomsRequest(this.rooms));
                }
                request.perform(ctx);
            }
        );
        ApiBuilder.get(
            "/{name}",
            (final Context ctx) -> new GetNamedRoomRequest(this.rooms, ctx).perform(ctx)
        );
        ApiBuilder.get(
            "/{name}/bookings",
            (final Context ctx) -> new GetBookingsForRoomInSlotRequest(
                this.rooms,
                this.bookings,
                ctx
            ).perform(ctx)
        );
        ApiBuilder.post(
            "/{name}/bookings",
            (final Context ctx) -> new PostBookingRequest(
                this.rooms,
                this.bookings,
                ctx
            ).perform(ctx)
        );
        ApiBuilder.post(
            (final Context ctx) -> new PostRoomRequest(this.rooms, ctx).perform(ctx)
        );
    }
}
