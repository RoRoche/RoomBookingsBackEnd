package fr.guddy.roombookings.infra.matchers;

import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.requests.Reminders;
import org.cactoos.Func;
import org.cactoos.Scalar;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class HasReminders extends TypeSafeDiagnosingMatcher<Scalar<Reminders>> {

  private final Func<Reminders, Matcher<HttpTestCase<String>>> matcherFunc;

  public HasReminders(final Func<Reminders, Matcher<HttpTestCase<String>>> matcherFunc) {
    this.matcherFunc = matcherFunc;
  }

  @Override
  protected boolean matchesSafely(final Scalar<Reminders> result, final Description mismatch) {
    try {
      final Reminders reminders = result.value();
      final HttpTestCase<String> testCase = reminders.testCase();
      final Matcher<HttpTestCase<String>> matcher = this.matcherFunc.apply(reminders);
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
    description.appendText("Valid reminders");
  }
}
