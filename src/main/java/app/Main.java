package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.HotelAndRoomDAO;
import app.populators.HotelPopulator;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        HotelAndRoomDAO hotelDAO = new HotelAndRoomDAO(emf);
        HotelPopulator hotelPopulator = new HotelPopulator();
        HotelPopulator.populateHotels(hotelDAO);


        Javalin app = ApplicationConfig.startServer(7076);
    }
}