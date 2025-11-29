package fr.guddy.roombookings.infra.matchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import fr.guddy.roombookings.infra.HttpTestCase;
import java.util.Map;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class HasBodyContaining extends TypeSafeDiagnosingMatcher<HttpTestCase<String>> {

  private final Map<String, Object> expectedEntries;

  public HasBodyContaining(final Map<String, Object> expectedEntries) {
    this.expectedEntries = expectedEntries;
  }

  @Override
  protected boolean matchesSafely(final HttpTestCase<String> testCase, final Description mismatch) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final HttpResponse<String> response = testCase.response();
      final Map<String, Object> actual = mapper.readValue(
        response.getBody(),
        new TypeReference<>() {}
      );
      final Boolean matches = new DeepMatches().apply(actual, expectedEntries);
      if (!matches) {
        mismatch.appendText("body was ").appendValue(response.getBody());
      }
      return matches;
    } catch (final Exception e) {
      mismatch.appendText("exception while executing testcase: ").appendText(e.getMessage());
      return false;
    }
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("HttpResponse body containing body ").appendValue(this.expectedEntries);
  }
}
