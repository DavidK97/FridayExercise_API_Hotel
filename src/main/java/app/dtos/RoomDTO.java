package app.dtos;


import app.entities.Hotel;
import app.entities.Room;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor // Til Builder
@Data
public class RoomDTO {
    private Integer id;
    private Integer hotelId;
    private int number;
    private double price;

    public RoomDTO (Room room) {
        this.id = room.getId();
        this.hotelId = room.getHotel().getId();
        this.number = room.getNumber();
        this.price  = room.getPrice();
    }

    public static List<RoomDTO> toDTOList (List<Room> rooms) {
        List<RoomDTO> roomDTOS = rooms
                .stream()
                .map(room -> new RoomDTO(room))
                .toList();

        return roomDTOS;
    }
}
