package fr.guddy.roombookings.infra.matchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import java.util.Map;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public final class HasBodyContaining extends TypeSafeMatcher<HttpResponse<String>> {

  private final Map<String, Object> expectedEntries;

  public HasBodyContaining(final Map<String, Object> expectedEntries) {
    this.expectedEntries = expectedEntries;
  }

  @Override
  protected boolean matchesSafely(final HttpResponse<String> response) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final Map<String, Object> actual = mapper.readValue(
        response.getBody(),
        new TypeReference<>() {}
      );
      return new DeepMatches().apply(actual, expectedEntries);
    } catch (final Exception e) {
      return false;
    }
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("HttpResponse body containing body ").appendValue(this.expectedEntries);
  }

  @Override
  protected void describeMismatchSafely(
    final HttpResponse<String> response,
    final Description mismatchDescription
  ) {
    mismatchDescription.appendText("body was ").appendValue(response.getBody());
  }
}
