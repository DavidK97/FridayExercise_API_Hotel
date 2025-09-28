package app.dtos;


import app.entities.Hotel;
import app.entities.Room;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor // Til Builder
@Data
public class HotelDTO {
    private Integer id;
    private String name;
    private String address;
    private Set<RoomDTO> rooms;


    public HotelDTO (Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.rooms = hotel.getRooms()
                .stream()
                .map(room -> new RoomDTO(room))
                .collect(Collectors.toSet());
    }

    public static List<HotelDTO> toDTOList(List<Hotel> hotels) {
        return hotels
                .stream()
                .map(hotel -> new HotelDTO(hotel))
                .toList();
    }
}
