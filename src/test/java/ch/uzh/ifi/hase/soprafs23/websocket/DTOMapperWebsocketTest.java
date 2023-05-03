package ch.uzh.ifi.hase.soprafs23.websocket;

import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerDTO;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerJoinDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.mapper.DTOMapperWebsocket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOMapperWebsocketTest {
    @Test
    public void testCretaePlayer_validInputs_success() {
        PlayerJoinDTO playerJoinDTO = new PlayerJoinDTO();
        playerJoinDTO.setNickname("nickname");

        // MAP -> Create Player
        Player player = DTOMapperWebsocket.INSTANCE.convertPlayerJoinDTOtoEntity(playerJoinDTO);

        // check content
        assertEquals(playerJoinDTO.getNickname(), player.getNickname());
    }

    @Test
    public void testGetPlayer_fromPlayer_toPlayerDTO_success() {
        // create Player
        Player player = new Player();
        player.setNickname("nickname");
        player.setId(1L);

        // MAP -> Create PlayerJoinDTO
        PlayerDTO playerDTO = DTOMapperWebsocket.INSTANCE.convertEntityToPlayerDTO(player);

        // check content
        assertEquals(player.getId(), playerDTO.getId());
        assertEquals(player.getNickname(), playerDTO.getNickname());
    }

}
