package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.HotelAndRoomDAO;
import app.populators.HotelPopulator;
import app.security.daos.SecurityDAO;
import app.security.entities.Role;
import app.security.entities.User;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        HotelAndRoomDAO hotelDAO = new HotelAndRoomDAO(emf);
        SecurityDAO securityDAO = new SecurityDAO(emf);


        HotelPopulator.populateHotels(hotelDAO);

        Role r1 = new Role("Admin");
        Role r2 = new Role("User");

        securityDAO.createRole("Admin");
        securityDAO.createRole("User");

        // Test af hashing af passwords og persistering
        User user = new User("David", "kode123");
        User user1 = securityDAO.createUser("David", "kode123");
        securityDAO.addUserRole("David", "Admin");

        user1.addRole(r2);




        Javalin app = ApplicationConfig.startServer(7076);
    }
}