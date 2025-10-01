package app.daos;

import app.config.HibernateConfig;
import app.dtos.HotelDTO;
import app.exceptions.ApiException;
import app.populators.HotelPopulator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HotelAndRoomDAOTest {
    private EntityManagerFactory emf;
    private HotelAndRoomDAO hotelAndRoomDAO;


    HotelDTO h1;
    HotelDTO h2;
    HotelDTO h3;

    private List<HotelDTO> hotelDTOList;

    @BeforeAll
    void initOnce () {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        hotelAndRoomDAO = new HotelAndRoomDAO(emf);
    }

    @BeforeEach
    void setUp() {
        // Restart all tables
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE hotel, room RESTART IDENTITY CASCADE")
                    .executeUpdate();
            em.getTransaction().commit();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to truncate tables", e);
        }

        // Populate tables
        hotelDTOList = HotelPopulator.populateHotels();
        if (hotelDTOList.size() == 3) {
            h1 = hotelDTOList.get(0);
            h2 = hotelDTOList.get(1);
            h3 = hotelDTOList.get(2);
        } else {
            throw new ApiException(500, "Populator doesnt work");
        }
    }

    @AfterAll
    void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void getInstance () {
        assertNotNull(emf);
    }



    @Test
    void getAllHotels() {
        // Arrange
        int expected = 3; // Kendes fra populator

        // Act
        List<HotelDTO> allHotels = hotelAndRoomDAO.getAllHotels();

        // Assert
        assertEquals(allHotels.size(), expected);
    }

    @Test
    void getHotelById() {

        //Arrange
        int expectedId = 1;


        //Act
        Hotel result =



        //Assert




    }

    @Test
    void createHotel() {

    }

    @Test
    void updateHotel() {

    }

    @Test
    void removeHotel() {

    }

    @Test
    void addRoom() {

    }

    @Test
    void removeRoom() {

    }

    @Test
    void getRoomsForHotel() {

    }

    @Test
    void deleteHotel() {

    }
}