package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void findById_success() {
        playerRepository.deleteAll();
        // given
        Player player = new Player();
        player.setNickname("test");

        playerRepository.save(player);
        playerRepository.flush();

        // when
        Player found = playerRepository.findById(player.getId()).orElse(null);

        // then
        assertNotNull(found);
        assertEquals(player.getId(), found.getId());
        assertEquals(player.getNickname(), found.getNickname());
    }

}
