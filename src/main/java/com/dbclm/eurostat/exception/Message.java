package com.dbclm.eurostat.exception;

public class Message {
    private String message;
    private String resolutionMessage;

    public Message(final String message, final String resolutionMessage) {
        this.message = message;
        this.resolutionMessage = resolutionMessage;
    }

    public String getMessage() {
        return message;
    }

    public Message setMessage(final String message) {
        this.message = message;
        return this;
    }

    public String getResolutionMessage() {
        return resolutionMessage;
    }

    public Message setResolutionMessage(final String resolutionMessage) {
        this.resolutionMessage = resolutionMessage;
        return this;
    }
}
