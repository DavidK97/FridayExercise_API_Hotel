package app.populators;

import app.config.HibernateConfig;
import app.daos.HotelDAO;
import app.dtos.HotelDTO;
import app.dtos.RoomDTO;
import app.entities.Hotel;
import app.entities.Room;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class HotelPopulator {
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    HotelDAO hotelDAO = new HotelDAO(emf);

    public List<HotelDTO> populateHotels () {
        List<HotelDTO> hotelDTOS = new ArrayList<>();
        List<Hotel> hotels = new ArrayList<>();


        Hotel h1 = Hotel.builder().name("Hotel 1").address("Hotelgade 1").build();
        Hotel h2 = Hotel.builder().name("Hotel 2").address("Hotelgade 2").build();
        Hotel h3 = Hotel.builder().name("Hotel 3").address("Hotelgade 3").build();

        Room r1 = Room.builder().hotel(h1).number(1).price(100).build();
        Room r2 = Room.builder().hotel(h1).number(2).price(120).build();
        h1.addRoom(r1);
        h1.addRoom(r2);

        Room r3 = Room.builder().hotel(h2).number(1).price(150).build();
        h2.addRoom(r3);

        Room r4 = Room.builder().hotel(h3).number(1).price(100).build();
        h3.addRoom(r4);

        hotelDAO.createHotel(h1);
        hotelDAO.createHotel(h2);
        hotelDAO.createHotel(h3);


        hotels.add(h1);
        hotels.add(h2);
        hotels.add(h3);

        hotelDTOS = HotelDTO.toDTOList(hotels);


        return hotelDTOS;
    }
}
