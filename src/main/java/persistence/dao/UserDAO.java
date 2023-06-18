package persistence.dao;

import persistence.entity.*;

public class UserDAO extends DAO<User, Long> {
    private static UserDAO userDAO;

    public static UserDAO getInstance() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    private UserDAO() {
        super(User.class);
    }
}
