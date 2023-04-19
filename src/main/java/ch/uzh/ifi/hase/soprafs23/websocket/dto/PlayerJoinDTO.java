package ch.uzh.ifi.hase.soprafs23.websocket.dto;

public class PlayerJoinDTO {
    private String nickname;
    private String avatar;


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
