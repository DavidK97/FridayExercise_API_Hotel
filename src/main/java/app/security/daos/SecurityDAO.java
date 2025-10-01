package app.security.daos;

import app.exceptions.ValidationException;
import app.security.entities.Role;
import app.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class SecurityDAO implements ISecurityDAO {
    EntityManagerFactory emf;

    public SecurityDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public User getVerifiedUser(String username, String password) throws ValidationException {
        try (EntityManager em = emf.createEntityManager()) {
            User foundUser = em.find(User.class, username);

            // Tjek af om password passer
            if (foundUser != null && foundUser.verifyPassword(password)) {
                return foundUser;
            } else {
                throw new ValidationException("Username or Password was incorrect");
            }
        }
    }

    @Override
    public User createUser(String username, String password) {
        try (EntityManager em = emf.createEntityManager()) {
            User newUser = new User(username, password);
            em.getTransaction().begin();
            em.persist(newUser);
            em.getTransaction().commit();
            return newUser;
        }
    }

    @Override
    public Role createRole(String rolename) {
        return null;
    }

    @Override
    public User addUserRole(String username, String role) {
        return null;
    }
}
