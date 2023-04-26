package ch.uzh.ifi.hase.soprafs23.websocket.dto;

import ch.uzh.ifi.hase.soprafs23.constant.InputType;

public class InputDTO {
    private InputType inputType;
 
    private Object rawData;

    public Object getRawData() {
        return rawData;
    }
    public void setRawData(Object rawData) {
        this.rawData = rawData;
    }
    public InputType getInputType() {
        return inputType;
    }
    public void setInputType(InputType type) {
        this.inputType = type;
    }
}
