package ch.uzh.ifi.hase.soprafs23.websocket.dto;

public class PlayerRejoinDTO {
    private long id;
    private String avatar;
    
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

  
}
