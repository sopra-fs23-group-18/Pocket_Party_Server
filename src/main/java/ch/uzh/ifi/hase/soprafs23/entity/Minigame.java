package ch.uzh.ifi.hase.soprafs23.entity;

public abstract class Minigame {
    private boolean isFinished = false;
    private Team winner;
    private int scoreToGain;
    private String team1Player;
    private String team2Player;
    protected String name;
    protected String description;

    public Minigame(int scoreToGain) {
        this.scoreToGain = scoreToGain;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getTeam2Player() {
        return team2Player;
    }

    public void setTeam2Player(String team2Player) {
        this.team2Player = team2Player;
    }

    public String getTeam1Player() {
        return team1Player;
    }

    public void setTeam1Player(String team1Player) {
        this.team1Player = team1Player;
    }

    public int getScoreToGain() {
        return scoreToGain;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
        isFinished = true;
    }

    public Team getWinner() {
        return winner;
    }

    public boolean isFinished() {
        return isFinished;
    }


    

}
