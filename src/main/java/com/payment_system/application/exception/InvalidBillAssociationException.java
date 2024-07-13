package com.payment_system.application.exception;

import java.util.List;
import java.util.UUID;

public class InvalidBillAssociationException extends RuntimeException {
    private final List<UUID> notFoundBillIds;
    private final List<UUID> alreadyAssociatedBillIds;

    public InvalidBillAssociationException(List<UUID> notFoundBillIds, List<UUID> alreadyAssociatedBillIds) {
        super("Invalid bill association");
        this.notFoundBillIds = notFoundBillIds;
        this.alreadyAssociatedBillIds = alreadyAssociatedBillIds;
    }

    public List<UUID> getNotFoundBillIds() {
        return notFoundBillIds;
    }

    public List<UUID> getAlreadyAssociatedBillIds() {
        return alreadyAssociatedBillIds;
    }
}
