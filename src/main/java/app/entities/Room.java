package app.entities;


import app.dtos.RoomDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor // Til at Hibernate kan lave tables i DB
@AllArgsConstructor // Til Builder
@Builder
@ToString
@EqualsAndHashCode
@Getter

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @ManyToOne
    private Hotel hotel;

    private int number;

    private double price;




    public Room (RoomDTO roomDTO, Hotel hotel) {
        this.id = roomDTO.getId();
        this.hotel = hotel;
        this.number = roomDTO.getNumber();
        this.price = roomDTO.getPrice();
    }

    public Room (RoomDTO roomDTO) {
        this.id = roomDTO.getId();
        this.number = roomDTO.getNumber();
        this.price = roomDTO.getPrice();
    }
}
