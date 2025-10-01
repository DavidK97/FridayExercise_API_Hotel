package app.daos;

import app.config.HibernateConfig;
import app.dtos.HotelDTO;
import app.dtos.RoomDTO;
import app.entities.Hotel;
import app.entities.Room;
import app.exceptions.ApiException;
import app.populators.HotelPopulator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
        hotelDTOList = HotelPopulator.populateHotels(hotelAndRoomDAO);
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
        assertThat(allHotels, containsInAnyOrder(h1, h2, h3));
    }

    @Test
    void getHotelById() {

        //Arrange
        Hotel h4 = Hotel.builder()
                .name("Hotel 4")
                .address("Hotelgade 4")
                .build();
        hotelAndRoomDAO.createHotel(h4);

        //Act
        HotelDTO actualHotel = hotelAndRoomDAO.getHotelById(4);

        //Assert
        assertThat(actualHotel.getId(), is(4));
        assertThat(actualHotel.getName(), is("Hotel 4"));
        assertThat(actualHotel.getAddress(), is("Hotelgade 4"));
    }

    @Test
    void createHotel() {
        // Arrange
        Hotel h4 = Hotel.builder()
                .name("Hotel 4")
                .address("Hotelgade 4")
                .build();
        hotelAndRoomDAO.createHotel(h4);

        // Act
        List<HotelDTO> allHotels = hotelAndRoomDAO.getAllHotels();
        HotelDTO foundHotel = hotelAndRoomDAO.getHotelById(4);

        // Assert
        assertThat(foundHotel.getId(), is(4));
        assertThat(foundHotel.getName(), is("Hotel 4"));
        assertThat(foundHotel.getAddress(), is("Hotelgade 4"));

        assertEquals(4, allHotels.size());
    }

    @Test
    void updateHotel() {
        // Arrange
        Hotel hotelInfoToUpdate = Hotel.builder()
                .name("Nyt navn")
                .address("Ny adresse")
                .build();

        // Act
        HotelDTO updatedHotel = hotelAndRoomDAO.updateHotel(1, hotelInfoToUpdate);

        // Assert
        assertThat(updatedHotel.getId(), is(1));
        assertThat(updatedHotel.getName(), is("Nyt navn"));
        assertThat(updatedHotel.getAddress(), is("Ny adresse"));
    }

    @Test
    void removeHotel() {
        // Arrange

        // Act
        HotelDTO deletedHotelDTO = hotelAndRoomDAO.removeHotel(h1.getId());
        List<HotelDTO> allHotels = hotelAndRoomDAO.getAllHotels();

        // Assert
        assertEquals(h1, deletedHotelDTO);
        assertEquals(allHotels.size(), 2);
        assertThat(allHotels, containsInAnyOrder(h2, h3));
    }

    @Test
    void addRoom() {
        // Arrange
        Room r1 = Room.builder()
                .number(1)
                .price(100)
                .build();

        // Act
        hotelAndRoomDAO.addRoom(h1.getId(), r1); //Tilføjer et rum

        HotelDTO updatedHotel = hotelAndRoomDAO.getHotelById(h1.getId()); //Henter h1 fra DB

        // Assert
        assertEquals(3, updatedHotel.getRooms().size());
    }

    @Test
    void removeRoom() {
        // Arrange

        // Act
        hotelAndRoomDAO.removeRoom(h1.getId(), 1);
        HotelDTO updatedHotel = hotelAndRoomDAO.getHotelById(h1.getId()); //Henter h1 fra DB

        // Assert
        assertEquals(1, updatedHotel.getRooms().size());
    }

    @Test
    void getRoomsForHotel() {
        // Arrange
        // Værdier kendes fra populator
        int r1Price = 100;
        int r1Id = 1;

        // Act
        List<RoomDTO> allRoomsForHotel = hotelAndRoomDAO.getRoomsForHotel(1);

        // Assert
        assertEquals(2, allRoomsForHotel.size());
        assertEquals(r1Id, allRoomsForHotel.get(0).getHotelId());
        assertEquals(r1Price, allRoomsForHotel.get(0).getPrice());
    }
}