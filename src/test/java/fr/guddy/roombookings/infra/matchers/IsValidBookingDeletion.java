package fr.guddy.roombookings.infra.matchers;

import fr.guddy.roombookings.infra.ApiExternalExtension;
import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.requests.HttpBooking;
import org.cactoos.Func;
import org.cactoos.Scalar;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class IsValidBookingDeletion extends TypeSafeDiagnosingMatcher<Scalar<HttpBooking>> {

  private final ApiExternalExtension api;
  private final Func<Long, String> expectedBodyWithId;

  public IsValidBookingDeletion(
    final ApiExternalExtension api,
    final Func<Long, String> expectedBodyWithId
  ) {
    this.api = api;
    this.expectedBodyWithId = expectedBodyWithId;
  }

  @Override
  protected boolean matchesSafely(final Scalar<HttpBooking> result, final Description mismatch) {
    try {
      final HttpBooking booking = result.value();
      // 1. Check body
      final String expectedBody = this.expectedBodyWithId.apply(booking.id());
      final HasBody hasBody = new HasBody(expectedBody);
      final HttpTestCase<String> testCase = booking.testCase();
      if (!hasBody.matchesSafely(testCase, mismatch)) {
        mismatch.appendText("body mismatch: ");
        hasBody.describeMismatch(testCase.response().getBody(), mismatch);
        return false;
      }
      // 2. Check DB deletion
      if (this.api.bookings().byId(booking.id()).isPresent()) {
        mismatch.appendText("booking still present in database");
        return false;
      }
    } catch (final Exception e) {
      mismatch.appendText("exception while executing testcase: ").appendText(e.getMessage());
      return false;
    }
    return true;
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("valid booking deletion (correct body + database empty)");
  }
}
