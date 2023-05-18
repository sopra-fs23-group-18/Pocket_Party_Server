package ch.uzh.ifi.hase.soprafs23.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "PLAYER")
public class Player implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String nickname;

    // @JsonIgnore
    // @ManyToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "teamId")
    // private Team team;

    @Column(nullable = false)
    private int roundsPlayed = 0;

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }    

    public Player() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }

        else if (obj == this){
            return true;
        }

        else if (obj.getClass() != this.getClass()) {
            return false;
        }

        else{
            return this.id == ((Player)obj).id && this.nickname == ((Player)obj).nickname;
        }
    }

    @Override
    public int hashCode(){
        return Long.hashCode(id);
    }

    
}
