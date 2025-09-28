package app.entities;


import app.dtos.HotelDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor // Til Hibernate til at lave tables i DB
@AllArgsConstructor // Til Builder
@Builder
@ToString
@EqualsAndHashCode

@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    private String name;

    @Setter
    private String address;

    // Cascade sørger for at alle CRUD-operationer når room, orphanRemoval fjerner et child (Room) der ikke længere har en parent (Hotel)
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // Sørger for at Lombok initialiserer HashSettet
    @EqualsAndHashCode.Exclude // Sikrer mod stackOwerflow-error
    @ToString.Exclude // Sikrer mod stackOwerflow-error
    private Set<Room> rooms = new HashSet<>();


    public Hotel (HotelDTO hotelDTO) {
        this.id = hotelDTO.getId();
        this.name = hotelDTO.getName();
        this.address = hotelDTO.getAddress();
        this.rooms = hotelDTO.getRooms()
                .stream()
                .map(roomDTO -> new Room(roomDTO))
                .collect(Collectors.toSet());
    }

    // Bi-directional hjælpemetoder
    public void addRoom (Room room) {
        this.rooms.add(room);
        if (room != null) {
            room.setHotel(this);
        }
    }

    public void removeRoom (Room room) {
        this.rooms.remove(room);
        if (room != null) {
            room.setHotel(null);
        }
    }

}
