package app.controllers;

import app.config.HibernateConfig;
import app.daos.HotelAndRoomDAO;
import app.dtos.HotelDTO;
import app.dtos.RoomDTO;
import app.entities.Hotel;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HotelController {
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    public HotelAndRoomDAO hotelDAO = new HotelAndRoomDAO(emf);

    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);
    private static final Logger debugLogger = LoggerFactory.getLogger("app");

//TODO håndtering af fejl? Globalt eller lokalt?
    public void getAllHotels(Context ctx) {
        try {
            // 1. Hent fra DB
            List<HotelDTO> hotelDTOList = hotelDAO.getAllHotels();

            // 2. Send til klient
            if (!hotelDTOList.isEmpty()) {
                ctx.status(HttpStatus.OK); // 200
                ctx.json(hotelDTOList);
                logger.info("Fetched all Hotels, count: " + hotelDTOList.size());
            } else {
                ctx.status(HttpStatus.NOT_FOUND); // 404
                ctx.result("Error retrieving hotels");
                logger.warn("Hotels could not be retrieved");
            }
        } catch (Exception e) {
            logger.error("Error retrieving hotels", e);
            throw e;
        }
    }

    public void getHotelById(Context ctx) {
        // 1. Hent klient input (id)
        int id = Integer.parseInt(ctx.pathParam("id"));

        // 2. Hente fra DB
        HotelDTO hotelDTO = hotelDAO.getHotelById(id);

        // 3. Response til klient
        if (hotelDTO != null) {
            ctx.status(HttpStatus.OK);
            ctx.json(hotelDTO);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Hotel with id: " + id + "could not be found");
        }
    }

    public void getRoomsForHotel(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));

        List<RoomDTO> roomsForHotel = hotelDAO.getRoomsForHotel(id);

        if (!roomsForHotel.isEmpty()) {
            ctx.status(HttpStatus.OK);
            ctx.json(roomsForHotel);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Error retrieving hotel and rooms");
        }
    }

    public void createHotel(Context ctx) {
        Hotel hotel = ctx.bodyAsClass(Hotel.class);
        HotelDTO hotelDTO = hotelDAO.createHotel(hotel);

        if (hotelDTO != null) {
            ctx.status(HttpStatus.CREATED);
            ctx.json(hotelDTO);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Hotel could not be created");
        }
    }

    public void updateHotel(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Hotel hotel = ctx.bodyAsClass(Hotel.class);

        HotelDTO updatedHotelDTO = hotelDAO.updateHotel(id, hotel);

        if (updatedHotelDTO != null) {
            ctx.status(HttpStatus.OK);
            ctx.json(updatedHotelDTO);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Hotel with id: " + id + "could not be updated");
        }
    }

    public void deleteHotel(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        HotelDTO deletedHotel = hotelDAO.removeHotel(id);

        if (deletedHotel != null) {
            ctx.status(HttpStatus.OK);
            ctx.json(deletedHotel);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.result("Hotel could not be found or deleted");
        }
    }
}
