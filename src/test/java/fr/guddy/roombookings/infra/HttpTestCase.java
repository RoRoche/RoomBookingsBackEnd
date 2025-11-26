package fr.guddy.roombookings.infra;

import com.mashape.unirest.http.HttpResponse;
import java.util.List;
import org.cactoos.list.ListOf;

public interface HttpTestCase<T> {
  HttpResponse<T> response() throws Exception;

  final class WithFixtures<T> implements HttpTestCase<T> {

    private final HttpTestCase<T> delegate;
    private final List<Runnable> fixtures;

    public WithFixtures(final HttpTestCase<T> delegate, final List<Runnable> fixtures) {
      this.delegate = delegate;
      this.fixtures = fixtures;
    }

    public WithFixtures(final HttpTestCase<T> delegate, final Runnable... fixtures) {
      this(delegate, new ListOf<>(fixtures));
    }

    @Override
    public HttpResponse<T> response() throws Exception {
      this.fixtures.forEach(Runnable::run);
      return this.delegate.response();
    }
  }
}
