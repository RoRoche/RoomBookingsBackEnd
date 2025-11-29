package fr.guddy.roombookings.infra.requests;

import fr.guddy.roombookings.infra.HttpTestCase;

public record Reminders(
  HttpTestCase<String> testCase,
  long idMinus15ToPlus15m,
  long idPlus15To45m,
  long tsStart,
  long tsEndMinus,
  long tsEndPlus
) {}
