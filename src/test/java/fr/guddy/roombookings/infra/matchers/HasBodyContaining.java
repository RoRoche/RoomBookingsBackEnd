package fr.guddy.roombookings.infra.matchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.cactoos.map.MapOf;
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
      return deepMatches(actual, expectedEntries);
    } catch (Exception e) {
      return false;
    }
  }

  private boolean deepMatches(final Object actual, final Object expected) {
    // null
    if (expected == null) {
      return actual == null;
    }

    // Numbers comparison : int vs long vs double
    if (actual instanceof Number a && expected instanceof Number e) {
      return a.longValue() == e.longValue();
    }

    // Map : recursive comparison
    if (expected instanceof Map<?, ?> expectedMap) {
      if (!(actual instanceof Map<?, ?> actualMap)) {
        return false;
      }

      for (final Map.Entry<?, ?> entry : expectedMap.entrySet()) {
        final Object key = entry.getKey();
        final Object expectedValue = entry.getValue();

        if (!actualMap.containsKey(key)) {
          return false;
        }

        final Object actualValue = actualMap.get(key);

        if (!deepMatches(actualValue, expectedValue)) {
          return false;
        }
      }

      return true;
    }

    // List
    if (expected instanceof List<?> expectedList) {
      if (!(actual instanceof List<?> actualList)) {
        return false;
      }

      if (expectedList.size() != actualList.size()) {
        return false;
      }

      for (int i = 0; i < expectedList.size(); i++) {
        if (!deepMatches(actualList.get(i), expectedList.get(i))) {
          return false;
        }
      }

      return true;
    }

    // Simple comparison
    return Objects.equals(actual, expected);
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
