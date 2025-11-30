package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.infra.HttpTestCase;
import fr.guddy.roombookings.infra.HttpTestCaseEnvelop;

public record Reminders(
  HttpTestCase<String> testCase,
  long idMinus15ToPlus15m,
  long idPlus15To45m,
  long tsStart,
  long tsEndMinus,
  long tsEndPlus
) implements HttpTestCaseEnvelop {
  @Override
  public HttpTestCase<String> value() throws Exception {
    return this.testCase;
  }
}
