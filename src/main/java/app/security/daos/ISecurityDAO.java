package app.security.daos;

import app.exceptions.ValidationException;
import app.security.entities.Role;
import app.security.entities.User;

public interface ISecurityDAO {
    User getVerifiedUser(String username, String password) throws ValidationException; // used for login
    User createUser(String username, String password); // used for register
    Role createRole(String rolename);
    User addUserRole(String username, String rolename);
}
