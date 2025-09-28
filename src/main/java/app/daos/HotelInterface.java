package app.daos;

import app.dtos.HotelDTO;
import app.dtos.RoomDTO;
import app.entities.Hotel;
import app.entities.Room;

import java.util.List;

public interface HotelInterface {
    List<HotelDTO> getAllHotels();
    HotelDTO getHotelById(int id);
    HotelDTO createHotel(Hotel hotel);
    HotelDTO updateHotel(int id, Hotel hotel);
    boolean deleteHotel(int id);

    void addRoom(int hotelId, Room room);
    void removeRoom(int hotelId, int roomId);
    List<RoomDTO> getRoomsForHotel(int hotelId);
}
