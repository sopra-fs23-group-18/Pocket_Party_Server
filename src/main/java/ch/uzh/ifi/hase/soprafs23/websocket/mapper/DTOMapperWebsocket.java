package ch.uzh.ifi.hase.soprafs23.websocket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerDTO;
import ch.uzh.ifi.hase.soprafs23.websocket.dto.PlayerJoinDTO;

@Mapper
public interface DTOMapperWebsocket {
    DTOMapperWebsocket INSTANCE = Mappers.getMapper(DTOMapperWebsocket.class);

    @Mapping(source = "nickname", target = "nickname")
    Player convertPlayerJoinDTOtoEntity(PlayerJoinDTO playerJoinDTO);
    
    @Mapping(source = "nickname", target = "nickname")
    @Mapping(source = "id", target = "id")
    PlayerDTO convertEntityToPlayerDTO(Player player);

}
