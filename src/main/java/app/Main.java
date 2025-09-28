package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.HotelDAO;
import app.dtos.HotelDTO;
import app.populators.HotelPopulator;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        HotelDAO hotelDAO = new HotelDAO(emf);
        HotelPopulator hotelPopulator = new HotelPopulator();
        hotelPopulator.populateHotels();


        Javalin app = ApplicationConfig.startServer(7076);
    }
}