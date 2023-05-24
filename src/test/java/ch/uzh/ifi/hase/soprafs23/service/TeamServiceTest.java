package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.MinigamePlayers;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Team;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.TeamRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;

@WebAppConfiguration
@SpringBootTest
public class TeamServiceTest {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private LobbyRepository lobbyRepository;

    @BeforeEach
    public void setup() {
        teamRepository.deleteAll();
        lobbyRepository.deleteAll();
    }

    @Test
    public void testCreateTeam() {
        // Create a lobby
        Lobby lobby = new Lobby();

        // Create a team
        String teamName = "Team 1";
        Team team = teamService.createTeam(lobby, teamName);

        // Verify the team is created with the correct properties
        assertNotNull(team);
        assertEquals(lobby, team.getLobby());
        assertEquals(teamName, team.getName());
    }

    @Test
    public void testAddPlayer() {
        // Create a lobby
        Lobby lobby = new Lobby();

        // Create two teams
        String teamName1 = "Team 1";
        Team team1 = teamService.createTeam(lobby, teamName1);

        String teamName2 = "Team 2";
        Team team2 = teamService.createTeam(lobby, teamName2);

        // form list of teams
        List<Team> teams = List.of(team1, team2);

        lobby.setTeams(teams);

        // save lobby
        lobbyRepository.saveAndFlush(lobby);

        // Create a player
        Player player = new Player();
        player.setNickname("John Doe");
        Player newPlayer = playerService.createPlayer(player);

        // Call the addPlayer method
        teamService.addPlayer(lobby, teamName1, newPlayer);

        Team team = teamService.getByNameAndLobby(lobby, teamName1);

        // Verify that the player is added to the team and saved
        assertNotNull(team.getPlayers());
    }

    @Test
    public void testAddPlayerFail() {
        assertThrows(ResponseStatusException.class, () -> {
            teamService.addPlayer(null, null, null);
        });
    }

    @Test
    public void testRemovePlayer() {
        /// Create a lobby
        Lobby lobby = new Lobby();

        // Create two teams
        String teamName1 = "Team 1";
        Team team1 = teamService.createTeam(lobby, teamName1);

        String teamName2 = "Team 2";
        Team team2 = teamService.createTeam(lobby, teamName2);

        // form list of teams
        List<Team> teams = List.of(team1, team2);

        lobby.setTeams(teams);

        // save lobby
        lobbyRepository.saveAndFlush(lobby);

        // Create a player
        Player player = new Player();
        player.setNickname("John Doe");
        Player newPlayer = playerService.createPlayer(player);

        // Call the addPlayer method
        teamService.addPlayer(lobby, teamName1, newPlayer);

        Team team = lobby.getTeams().get(0);

        // Call the removePlayer method
        teamService.removePlayer(lobby, team.getName(), player);

        // Verify that the player is removed from the team and saved
        assertEquals(0, team.getPlayers().size());
        assertFalse(team.getPlayers().contains(player));
    }

    @Test
    public void testRemovePlayerFail() {
        assertThrows(ResponseStatusException.class, () -> {
            teamService.removePlayer(null, null, null);
        });
    }

    @Test
    public void testGetTeam() {
        /// Create a team
        Team team = new Team();
        team.setName("Team 1");

        teamRepository.saveAndFlush(team);
        assertEquals(team.getId(), teamService.getTeam(team.getId()).getId());
    }

    @Test
    public void testGetTeamFail() {
        assertThrows(ResponseStatusException.class, () -> {
            teamService.getTeam(100L);
        });
    }

    @Test
    public void testUpdateScore() {
        /// Create a lobby
        Lobby lobby = new Lobby();

        // Create two teams
        String teamName1 = "Team 1";
        Team team1 = teamService.createTeam(lobby, teamName1);

        String teamName2 = "Team 2";
        Team team2 = teamService.createTeam(lobby, teamName2);

        // form list of teams
        List<Team> teams = List.of(team1, team2);

        lobby.setTeams(teams);

        // save lobby
        lobbyRepository.saveAndFlush(lobby);

        teamService.updateScore(lobby, teamName2, 10);

        Team team = teamService.getByNameAndLobby(lobby, teamName2);

        assertEquals(10, team.getScore());

    }

    @Test
    public void testUpdateScoreFail() {
        assertThrows(ResponseStatusException.class, () -> {
            teamService.updateScore(null, null, 0);
        });
    }

    @Test
    public void testGetByNameAndLobby() {
        /// Create a lobby
        Lobby lobby = new Lobby();

        // Create two teams
        String teamName1 = "Team 1";
        Team team1 = teamService.createTeam(lobby, teamName1);

        String teamName2 = "Team 2";
        Team team2 = teamService.createTeam(lobby, teamName2);

        // form list of teams
        List<Team> teams = List.of(team1, team2);

        lobby.setTeams(teams);

        // save lobby
        lobbyRepository.saveAndFlush(lobby);

        Team team = teamService.getByNameAndLobby(lobby, teamName2);

        assertEquals(teamName2, team.getName());
    }

    @Test
    public void testGetByNameAndLobbyFail() {
        /// Create a lobby
        Lobby lobby = new Lobby();

        // Create two teams
        String teamName1 = "Team 1";
        Team team1 = teamService.createTeam(lobby, teamName1);

        String teamName2 = "Team 2";
        Team team2 = teamService.createTeam(lobby, teamName2);

        // form list of teams
        List<Team> teams = List.of(team1, team2);

        lobby.setTeams(teams);

        // save lobby
        lobbyRepository.saveAndFlush(lobby);

        assertEquals(null, teamService.getByNameAndLobby(lobby, "Team 3"));
    }

    @Test
    public void testGetTeams() {
        /// Create a lobby
        Lobby lobby = new Lobby();

        // Create two teams
        String teamName1 = "Team 1";
        Team team1 = teamService.createTeam(lobby, teamName1);

        String teamName2 = "Team 2";
        Team team2 = teamService.createTeam(lobby, teamName2);

        // form list of teams
        List<Team> teams = List.of(team1, team2);

        lobby.setTeams(teams);

        // save lobby
        lobbyRepository.saveAndFlush(lobby);

        List<Team> teamList = teamService.getTeams(lobby);

        assertEquals(2, teamList.size());

    }

    @Test
    public void testRandomPlayerChoice() {
        /// Create a lobby
        Lobby lobby = new Lobby();

        // Create two teams
        String teamName1 = "Team 1";
        Team team1 = teamService.createTeam(lobby, teamName1);

        String teamName2 = "Team 2";
        Team team2 = teamService.createTeam(lobby, teamName2);

        // form list of teams
        List<Team> teams = List.of(team1, team2);

        lobby.setTeams(teams);

        // save lobby
        lobbyRepository.saveAndFlush(lobby);

        // Create a player
        Player player = new Player();
        player.setNickname("John Doe");
        Player newPlayer = playerService.createPlayer(player);

        // Call the addPlayer method
        teamService.addPlayer(lobby, teamName1, newPlayer);

        Team team = teamService.getByNameAndLobby(lobby, teamName1);

        // Verify that the player is added to the team and saved
        assertNotNull(team.getPlayers());

        // Call the randomPlayerChoice method
        List<Player> randomPlayer = teamService.randomPlayerChoice(teamName1, lobby, MinigamePlayers.ALL);

        // Verify that the player is not null
        assertNotNull(randomPlayer);
    }

    @Test
    public void testUpdateNames() {
        /// Create a lobby
        Lobby lobby = new Lobby();

        // Create two teams
        String teamName1 = "Team 1";
        Team team1 = teamService.createTeam(lobby, teamName1);

        String teamName2 = "Team 2";
        Team team2 = teamService.createTeam(lobby, teamName2);

        // form list of teams
        List<Team> teams = List.of(team1, team2);

        lobby.setTeams(teams);

        // save lobby
        lobbyRepository.saveAndFlush(lobby);

        // Call the updateNames method
        teamService.updateNames(lobby, teams);

        // Verify that the team names are updated
        assertEquals(teamName1, team1.getName());
        assertEquals(teamName2, team2.getName());
    }

}
