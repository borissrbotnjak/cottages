package com.isa.cottages.Exception;

import lombok.*;

@Getter
@Setter
public class ResourceConflictException extends RuntimeException {

    private Long resourceId;

    public ResourceConflictException(Long resourceId, String message) {
        super(message);
        this.setResourceId(resourceId);
    }
}
