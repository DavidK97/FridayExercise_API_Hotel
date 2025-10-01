package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.HotelAndRoomDAO;
import app.populators.HotelPopulator;
import app.security.daos.SecurityDAO;
import app.security.entities.User;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        HotelAndRoomDAO hotelDAO = new HotelAndRoomDAO(emf);
        SecurityDAO securityDAO = new SecurityDAO(emf);

        HotelPopulator.populateHotels(hotelDAO);


        // Test af hashing af passwords
        User user = new User("user1", "pass123");
        System.out.println(user.getUsername() +": "+user.getPassword());

        User user1 = securityDAO.createUser("David", "kode123");



        Javalin app = ApplicationConfig.startServer(7076);
    }
}