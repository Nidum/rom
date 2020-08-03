package com.smarthost.rom.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse implements Serializable {
    private final List<String> messages;
    private final int status;

    public ErrorResponse(String message, int status) {
        this.messages = List.of(message);
        this.status = status;
    }

    public ErrorResponse(List<String> messages, int status) {
        this.messages = new ArrayList<>(messages);
        this.status = status;
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public int getStatus() {
        return status;
    }
}
