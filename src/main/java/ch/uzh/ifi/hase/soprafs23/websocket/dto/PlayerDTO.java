package ch.uzh.ifi.hase.soprafs23.websocket.dto;

public class PlayerDTO {
    private String name;
    // invite code or lobby id may be needed for finding the right lobby
    private int invitecode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(int invitecode) {
        this.invitecode = invitecode;
    }

}
