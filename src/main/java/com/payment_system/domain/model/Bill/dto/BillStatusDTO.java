package com.payment_system.domain.model.Bill.dto;

import java.util.UUID;

public class BillStatusDTO {
    private UUID id;
    private String status;
    private String message;

    public BillStatusDTO(UUID id, String status, String message) {
        this.id = id;
        this.status = status;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
