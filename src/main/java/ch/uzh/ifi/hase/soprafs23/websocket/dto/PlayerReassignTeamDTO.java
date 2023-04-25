package ch.uzh.ifi.hase.soprafs23.websocket.dto;

import ch.uzh.ifi.hase.soprafs23.constant.TeamType;

public class PlayerReassignTeamDTO {
    private long playerId;
    private TeamType from;
    private TeamType to;
    
    public TeamType getFrom() {
        return from;
    }
    public void setFrom(TeamType from) {
        this.from = from;
    }
  

  
    public TeamType getTo() {
        return to;
    }
    public void setTo(TeamType to) {
        this.to = to;
    }
    public long getPlayerId() {
        return playerId;
    }
    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

}
