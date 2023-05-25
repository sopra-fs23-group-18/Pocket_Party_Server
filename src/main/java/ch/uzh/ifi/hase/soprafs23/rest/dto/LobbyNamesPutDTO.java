package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class LobbyNamesPutDTO {
    private List<TeamNamePutDTO> teams;

    public List<TeamNamePutDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamNamePutDTO> teams) {
        this.teams = teams;
    }
}
