package app.security.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter

@Entity
@Table(name = "users") // "User" er protected i Postgres
public class User implements ISecurityUser {
    @Id
    @Column(nullable = false)
    private String username;
    private String password;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles", // Navn på Joining table
            joinColumns = @JoinColumn(name = "username"), // Denne side af relationen: User
            inverseJoinColumns = @JoinColumn(name = "rolename") // Anden side af relationen: Role
    )
    private Set<Role> roles = new HashSet<>();


    // Konstruktør til at hashe passwords
    public User(String username, String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        this.username = username;
        this.password = hashed;
        this.roles = new HashSet<>();
    }


    @Override
    public boolean verifyPassword(String candidate) {
        if (BCrypt.checkpw(candidate, password))
            return true;
        else
            return false;
    }

    @Override
    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    @Override
    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }
}
