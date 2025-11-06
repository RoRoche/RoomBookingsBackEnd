package fr.guddy.roombookings.architecture.rules;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;

public abstract class ArchRuleEnvelope implements ArchRule {

    private final ArchRule delegate;

    protected ArchRuleEnvelope(final ArchRule delegate) {
        this.delegate = delegate;
    }

    @Override
    public void check(final JavaClasses classes) {
        this.delegate.check(classes);
    }

    @Override
    public ArchRule because(final String reason) {
        return this.delegate.because(reason);
    }

    @Override
    public ArchRule allowEmptyShould(final boolean allowEmptyShould) {
        return this.delegate.allowEmptyShould(allowEmptyShould);
    }

    @Override
    public ArchRule as(final String newDescription) {
        return this.delegate.as(newDescription);
    }

    @Override
    public EvaluationResult evaluate(final JavaClasses classes) {
        return this.delegate.evaluate(classes);
    }

    @Override
    public String getDescription() {
        return this.delegate.getDescription();
    }
}
