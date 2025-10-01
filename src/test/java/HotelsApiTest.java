import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.HotelAndRoomDAO;
import app.dtos.HotelDTO;
import app.entities.Hotel;
import app.exceptions.ApiException;
import app.populators.HotelPopulator;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class HotelsApiTest {
    private static Javalin app;
    private static EntityManagerFactory emf;
    private static HotelAndRoomDAO hotelAndRoomDAO;


    HotelDTO h1;
    HotelDTO h2;
    HotelDTO h3;

    @BeforeAll
    public static void setup() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        hotelAndRoomDAO = new HotelAndRoomDAO(emf);
        app = ApplicationConfig.startServer(7076);
    }

    @AfterAll
    public static void afterAll() {
        ApplicationConfig.stopServer(app);

        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @BeforeEach
    public void beforeEach() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE hotel, room RESTART IDENTITY CASCADE")
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to truncate table", e);
        }


        List<HotelDTO> hotelDTOList = HotelPopulator.populateHotels(hotelAndRoomDAO);
        if (hotelDTOList.size() == 3) {
            h1 = hotelDTOList.get(0);
            h2 = hotelDTOList.get(1);
            h3 = hotelDTOList.get(2);
        } else {
            throw new ApiException(500, "Populator doesnt work");
        }
    }

    @Test
    public void testGetAllHotels() {
        RestAssured.baseURI = "http://localhost:7076/api/v1";


        given()
                .when()
                .get("/hotels")
                .then()
                .statusCode(200)
                // .body(containsInAnyOrder(h1, h2, h3));
                .body("name", containsInAnyOrder("Hotel 1", "Hotel 2", "Hotel 3"))
                .body("address", containsInAnyOrder("Hotelgade 1", "Hotelgade 2", "Hotelgade 3"));
    }

    @Test
    public void testGetHotel() {
        RestAssured.baseURI = "http://localhost:7076/api/v1";

        given()
                .when()
                .get("/hotels/1")
                .then()
                .statusCode(200)
                .body("name", is("Hotel 1"))
                .body("address", is("Hotelgade 1"));
    }

    @Test
    public void getAllRoomsForHotel () {
        RestAssured.baseURI = "http://localhost:7076/api/v1";

        given()
                .when()
                .get("/hotels/1/rooms")
                .then()
                .statusCode(200)
                .body("size()", is(2))  // Størrelse på rooms.array
                .body("[0].number", is(1))
                .body("[0].price", is(100.0F))
                .body("[1].number", is(2))
                .body("[1].price", is(120.0F));
    }

    @Test
    public void testCreateHotel() {
        RestAssured.baseURI = "http://localhost:7076/api/v1";

        String newHotelJson = """
                {
                    "name": "Hotel 4",
                    "address": "Hotelgade 4"
                }
                """;

        given()
                .body(newHotelJson)

                .when()
                .post("/hotels")

                .then()
                .statusCode(201)
                .body("id", is(4))
                .body("name", is("Hotel 4"))
                .body("address", is("Hotelgade 4"));
    }

    @Test
    public void testUpdateHotel() {
        RestAssured.baseURI = "http://localhost:7076/api/v1";

        String updatedHotelJson = """
        {
            "name": "Opdateret Hotel 1",
            "address": "Opdateret hotelgade 1"
        }
    """;

        given()
                .body(updatedHotelJson)
                .when()
                .put("/hotels/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Opdateret Hotel 1"))
                .body("address", is("Opdateret hotelgade 1"));
    }


    @Test
    public void testDeleteHotel() {
        RestAssured.baseURI = "http://localhost:7076/api/v1";

        given()
                .when()
                .delete("/hotels/3")
                .then()
                .statusCode(200)
                .body("id", is(3))
                .body("name", is("Hotel 3"))
                .body("address", is("Hotelgade 3"));
    }
}
