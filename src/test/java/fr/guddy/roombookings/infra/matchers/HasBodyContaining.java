package fr.guddy.roombookings.infra.matchers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
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
    if (expected == null) return actual == null;

    if (expected instanceof Number e && actual instanceof Number a) {
      return a.longValue() == e.longValue();
    }

    if (expected instanceof Map<?, ?> expectedMap && actual instanceof Map<?, ?> actualMap) {
      return expectedMap
        .entrySet()
        .stream()
        .allMatch(
          (entry) ->
            actualMap.containsKey(entry.getKey()) &&
            deepMatches(actualMap.get(entry.getKey()), entry.getValue())
        );
    }

    if (expected instanceof List<?> expectedList && actual instanceof List<?> actualList) {
      return (
        expectedList.size() == actualList.size() &&
        IntStream.range(0, expectedList.size()).allMatch((i) ->
          deepMatches(actualList.get(i), actualList.get(i))
        )
      );
    }

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
