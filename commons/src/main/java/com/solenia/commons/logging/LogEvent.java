package com.solenia.commons.logging;

/** Cada microservicio implementa este contrato con sus propios eventos. */
public interface LogEvent {
    String code();
}
