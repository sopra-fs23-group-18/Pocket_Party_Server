package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void findById_success() {
        // given
        Team team = new Team();
        team.setScore(0);
        team.setName("test");

        teamRepository.save(team);
        teamRepository.flush();

        // when
        Team found = teamRepository.findById(team.getId()).orElse(null);

        // then
        assertNotNull(found);
        assertEquals(team.getId(), found.getId());
        assertEquals(team.getScore(), found.getScore());
        assertEquals(team.getName(), found.getName());
    }

}
