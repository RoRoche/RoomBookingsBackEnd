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

import fr.guddy.roombookings.domain.booking.Booking;
import fr.guddy.roombookings.domain.booking.JsonBooking;
import fr.guddy.roombookings.domain.bookings.Bookings;
import fr.guddy.roombookings.infra.params.Parameter;
import fr.guddy.roombookings.infra.params.QueryParameter;
import fr.guddy.roombookings.infra.params.RequiredParameter;
import io.javalin.http.Context;
import java.util.List;
import org.eclipse.jetty.http.HttpStatus;

public final class GetRemindersRequest implements Request {

  private final Bookings bookings;
  private final String userId;

  public GetRemindersRequest(final Bookings bookings, final String userId) {
    this.bookings = bookings;
    this.userId = userId;
  }

  public GetRemindersRequest(final Bookings bookings, final Parameter<String> userId) {
    this(bookings, userId.value());
  }

  public GetRemindersRequest(final Bookings bookings, final Context context) {
    this(bookings, new RequiredParameter<>(new QueryParameter("user_id", context)));
  }

  @Override
  public void perform(final Context context) {
    final List<Booking> reminders = bookings.forUserFromStartDate(userId, 1764352800);
    if (reminders.isEmpty()) {
      context.status(HttpStatus.NO_CONTENT_204);
    } else {
      context
        .json(reminders.stream().map(JsonBooking::new).map(JsonBooking::map).toList())
        .status(HttpStatus.OK_200);
    }
  }
}
