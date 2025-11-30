package fr.guddy.roombookings.infra;

import org.cactoos.Scalar;

@FunctionalInterface
public interface HttpTestCaseEnvelop extends Scalar<HttpTestCase<String>> {}
