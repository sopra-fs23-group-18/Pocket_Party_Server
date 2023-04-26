package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Minigame;
import ch.uzh.ifi.hase.soprafs23.constant.MinigameType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class MinigameRepositoryTest {

    @Autowired
    private MinigameRepository minigameRepository;

    @Test
    public void findById_success() {
        // given
        Minigame minigame = new Minigame();
        minigame.setType(MinigameType.TIMING_GAME);
        minigame.setDescription("test");

        minigameRepository.save(minigame);
        minigameRepository.flush();

        // when
        Minigame found = minigameRepository.findById(minigame.getId()).orElse(null);

        // then
        assertNotNull(found);
        assertEquals(minigame.getId(), found.getId());
        assertEquals(minigame.getType(), found.getType());
        assertEquals(minigame.getDescription(), found.getDescription());
    }

}
