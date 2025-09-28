package app.controllers;

import app.config.HibernateConfig;
import app.daos.HotelDAO;
import app.dtos.HotelDTO;
import app.dtos.RoomDTO;
import app.entities.Hotel;
import app.entities.Room;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

public class RoomController {
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    public HotelDAO hotelDAO = new HotelDAO(emf);


    public void createRoom(Context ctx) {
        // 1. Hent info fra client-request
        int hotelId = Integer.parseInt(ctx.pathParam("id"));
        Room room = ctx.bodyAsClass(Room.class);

        // 2. Tilføj nyt room til hotel
        hotelDAO.addRoom(hotelId, room);

        // 3. Hent hotel og returner til klient med det nye rum
        HotelDTO hotelDTO = hotelDAO.getHotelById(hotelId);

        ctx.status(HttpStatus.CREATED);
        ctx.json(hotelDTO);
    }

    public void removeRoom(Context ctx) {
        // 1. Hent info fra client-request
        int hotelId = Integer.parseInt(ctx.pathParam("id"));
        int roomId = Integer.parseInt(ctx.pathParam("roomId"));

        // 2. Tilføj nyt room til hotel
        hotelDAO.removeRoom(hotelId, roomId);

        // 3. Hent hotel og returner til klient med det nye rum
        HotelDTO hotelDTO = hotelDAO.getHotelById(hotelId);

        ctx.status(HttpStatus.CREATED);
        ctx.json(hotelDTO);
    }
}
