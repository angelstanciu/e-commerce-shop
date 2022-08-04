package com.endava.mentorship2022.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {
    PENDING,
    CANCELLED,
    REFUNDED,
    COMPLETED
}
