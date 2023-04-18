package ch.uzh.ifi.hase.soprafs23.websocket.dto;

public class PlayerDTO {
    private String nickname;
    private long id;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
