package fr.guddy.roombookings.infra.matchers;

import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.HttpTestCaseEnvelop;
import org.cactoos.Func;
import org.cactoos.Scalar;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class HasScalarMatching<T extends HttpTestCaseEnvelop>
  extends TypeSafeDiagnosingMatcher<Scalar<T>> {

  private final Func<T, Matcher<HttpTestCase<String>>> matcherFunc;

  public HasScalarMatching(final Func<T, Matcher<HttpTestCase<String>>> matcherFunc) {
    this.matcherFunc = matcherFunc;
  }

  @Override
  protected boolean matchesSafely(final Scalar<T> result, final Description mismatch) {
    try {
      final T envelop = result.value();
      final HttpTestCase<String> testCase = envelop.value();
      final Matcher<HttpTestCase<String>> matcher = this.matcherFunc.apply(envelop);
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
    description.appendText("test case must match");
  }
}
