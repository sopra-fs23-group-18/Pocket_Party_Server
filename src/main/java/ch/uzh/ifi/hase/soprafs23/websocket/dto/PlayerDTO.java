package ch.uzh.ifi.hase.soprafs23.websocket.dto;

public class PlayerDTO {
    private String nickname;
    private long id;
    private String avatar;
    private long lobbyId;
    
    public long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(long lobbyId) {
        this.lobbyId = lobbyId;
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
