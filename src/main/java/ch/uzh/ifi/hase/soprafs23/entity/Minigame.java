package ch.uzh.ifi.hase.soprafs23.entity;


public abstract class Minigame {
    private boolean isFinished = false;
    private Team winner;
    private int scoreToGain;
    private String team1Player;
    private String team2Player;

    public Minigame(int scoreToGain){
        this.scoreToGain = scoreToGain;
    }

    public void setWinner(Team winner){
        this.winner = winner;
        isFinished = true;
    }

    public abstract String toString();


}
