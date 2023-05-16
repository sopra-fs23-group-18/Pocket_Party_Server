package ch.uzh.ifi.hase.soprafs23.websocket.dto;

import ch.uzh.ifi.hase.soprafs23.constant.TeamType;

public class PlayerAssignTeamDTO {
    private long playerId;
    //private TeamType team;
    private String name;
   
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    // public TeamType getTeam() {
    //     return team;
    // }
    // public void setTeam(TeamType team) {
    //     this.team = team;
    // }
    public long getPlayerId() {
        return playerId;
    }
    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

}
