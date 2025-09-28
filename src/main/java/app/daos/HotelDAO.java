package app.daos;

import app.dtos.HotelDTO;
import app.dtos.RoomDTO;
import app.entities.Hotel;
import app.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class HotelDAO implements HotelInterface {
    private EntityManagerFactory emf;

    public HotelDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<HotelDTO> getAllHotels() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Hotel> hotels = em.createQuery("SELECT h FROM Hotel h", Hotel.class).getResultList();

            List<HotelDTO> hotelDTOs = HotelDTO.toDTOList(hotels);
            return hotelDTOs;
        }
    }

    @Override
    public HotelDTO getHotelById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Hotel hotel = em.find(Hotel.class, id);
            return new HotelDTO(hotel);
        }
    }

    @Override
    public HotelDTO createHotel(Hotel hotel) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(hotel);
            return new HotelDTO(hotel);
        }
    }

    @Override
    public HotelDTO updateHotel(int id, Hotel hotel) {
        try (EntityManager em = emf.createEntityManager()) {
            Hotel foundHotel = em.find(Hotel.class, id);
            foundHotel.setName(hotel.getName());
            foundHotel.setAddress(hotel.getAddress());

            em.getTransaction().begin();
            Hotel mergedHotel = em.merge(foundHotel);
            em.getTransaction().commit();

            return new HotelDTO(mergedHotel);
        }
    }

    // Fungerer med Cascade.Remove
    public HotelDTO removeHotel(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel hotel = em.find(Hotel.class, id);

            if (hotel != null) {
                HotelDTO deletedHotelDTO = new HotelDTO(hotel);
                em.remove(hotel);
                em.getTransaction().commit();
                return deletedHotelDTO;
            } else {
                em.getTransaction().rollback();
                return null;
            }
        }
    }

    @Override
    public void addRoom(int hotelId, Room room) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel hotel = em.find(Hotel.class, hotelId);
            hotel.addRoom(room);

            em.merge(hotel);
            em.getTransaction().commit();
        }
    }

    @Override
    public void removeRoom(int hotelId, int roomId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel hotel = em.find(Hotel.class, hotelId);
            Room room = em.find(Room.class, roomId);

            hotel.removeRoom(room);
            em.merge(hotel);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<RoomDTO> getRoomsForHotel(int hotelId) {
        try (EntityManager em = emf.createEntityManager()) {
            List<Room> rooms = em.createQuery("SELECT r from Room r WHERE r.hotel.id = :id", Room.class)
                    .setParameter("id", hotelId)
                    .getResultList();

            List<RoomDTO> roomDTOS = RoomDTO.toDTOList(rooms);

            return roomDTOS;
        }
    }

    // Fungerer ikke med Cascade.remove, derfor giver den fejl: "unique-constraint" da room peger på et hotelId der ikke længere eksisterer
    @Override
    public boolean deleteHotel(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            int result = em.createQuery("DELETE FROM Hotel p WHERE p.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            em.getTransaction().commit();
            return result > 0;
        }
    }

}

