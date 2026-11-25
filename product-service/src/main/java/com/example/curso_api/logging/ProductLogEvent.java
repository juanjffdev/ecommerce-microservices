package com.example.curso_api.logging;

import com.solenia.commons.logging.LogEvent;

public enum ProductLogEvent implements LogEvent {
    PRODUCTS_LISTED;

    @Override
    public String code() {
        return name();
    }
}
