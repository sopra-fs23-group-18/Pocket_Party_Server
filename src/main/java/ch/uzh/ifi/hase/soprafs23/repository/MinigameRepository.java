package ch.uzh.ifi.hase.soprafs23.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs23.entity.minigame.Minigame;

@Repository("minigameRepository")
public interface MinigameRepository extends JpaRepository<Minigame, Long>{
    Minigame findById(long id);
}
