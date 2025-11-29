package fr.guddy.roombookings.infra;

import com.mashape.unirest.http.HttpResponse;
import java.util.List;

@FunctionalInterface
public interface HttpTestCase<T> {
  HttpResponse<T> response() throws Exception;

  final class WithFixtures<T> implements HttpTestCase<T> {

    private final List<Runnable> fixtures;
    private final HttpTestCase<T> delegate;

    public WithFixtures(final List<Runnable> fixtures, final HttpTestCase<T> delegate) {
      this.fixtures = fixtures;
      this.delegate = delegate;
    }

    @Override
    public HttpResponse<T> response() throws Exception {
      this.fixtures.forEach(Runnable::run);
      return this.delegate.response();
    }
  }
}
