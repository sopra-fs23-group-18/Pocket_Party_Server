package ch.uzh.ifi.hase.soprafs23.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs23.entity.Player;

@Repository("playerRepository")
public interface PlayerRepository extends JpaRepository<Player, Long>{
    Player findById(long id);   
}
