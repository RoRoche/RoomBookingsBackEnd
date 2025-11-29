package fr.guddy.roombookings.infra.matchers;

import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.requests.HttpBooking;
import org.cactoos.Func;
import org.cactoos.Scalar;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class HasHttpBookingMatching extends TypeSafeDiagnosingMatcher<Scalar<HttpBooking>> {

  private final Func<Long, Matcher<HttpTestCase<String>>> matcherFunc;

  public HasHttpBookingMatching(final Func<Long, Matcher<HttpTestCase<String>>> matcherFunc) {
    this.matcherFunc = matcherFunc;
  }

  @Override
  protected boolean matchesSafely(Scalar<HttpBooking> result, Description mismatch) {
    try {
      final HttpBooking booking = result.value();
      final HttpTestCase<String> testCase = booking.testCase();
      final Matcher<HttpTestCase<String>> matcher = this.matcherFunc.apply(booking.id());
      final boolean matches = matcher.matches(testCase);
      if (!matches) {
        matcher.describeMismatch(testCase, mismatch);
      }
      return matches;
    } catch (final Exception e) {
      mismatch.appendText("exception while executing testcase: ").appendText(e.getMessage());
      return false;
    }
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("HttpBooking must match");
  }
}
