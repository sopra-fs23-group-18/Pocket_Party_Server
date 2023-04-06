package ch.uzh.ifi.hase.soprafs23.websocket.dto;

import ch.uzh.ifi.hase.soprafs23.constant.SignalType;

public class SignalDTO {
    private String senderId, recipentId;
    private Object data;
    private SignalType type;

    public SignalType getType() {
        return type;
    }

    public void setType(SignalType type) {
        this.type = type;
    }

    public String getRecipentId() {
        return recipentId;
    }

    public void setRecipentId(String recipentId) {
        this.recipentId = recipentId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
