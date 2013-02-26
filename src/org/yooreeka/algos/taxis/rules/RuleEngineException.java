package org.yooreeka.algos.taxis.rules;

public class RuleEngineException extends RuntimeException {

    private static final long serialVersionUID = 4267289121996977169L;

    public RuleEngineException(String message) {
        super(message);
    }

    public RuleEngineException(String message, Throwable cause) {
        super(message, cause);
    }

}