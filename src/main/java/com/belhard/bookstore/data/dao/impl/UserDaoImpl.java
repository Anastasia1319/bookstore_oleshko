package com.belhard.bookstore.data.dao.impl;

import com.belhard.bookstore.data.connection.DataSource;
import com.belhard.bookstore.data.dao.UserDao;
import com.belhard.bookstore.data.entity.Role;
import com.belhard.bookstore.data.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final String SELECT_ALL = "SELECT user_id, first_name, last_name, email, \"password\", name_role FROM users, \"role\" WHERE users.role_id = role.role_id";
    private static final String FIND_BY_EMAIL = "SELECT user_id, first_name, last_name, email, \"password\", name_role FROM users, \"role\" WHERE users.role_id = role.role_id AND email = ?";
    private static final String FIND_BY_ID = "SELECT user_id, first_name, last_name, email, \"password\", name_role FROM users, \"role\" WHERE users.role_id = role.role_id AND user_id = ?";
    private static final String CREATE = "INSERT INTO users (first_name, last_name, email, \"password\", role_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE users SET first_name = ?, last_name = ?, email = ?, \"password\" = ?, role_id = ?)";
    private static final String DELETE_BY_ID = "DELETE FROM users WHERE user_id = ?";
    private static final String COUNT_ALL = "SELECT COUNT(*) FROM users";
    private final DataSource dataSource;

    public UserDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public User create(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            mapUserToStatementData(user, statement);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                Long id = resultSet.getLong("user_id");
                return findById(id);
            }
            throw new RuntimeException("Couldn't create user: " + user);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't create user: " + user, e);
        }
    }

    public User findById(Long id) {
        User user = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = mapResultSetToUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't find user with id: " + id, e);
        }
        return user;
    }

    @Override
    public User update(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            mapUserToStatementData(user, statement);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't update user: " + user, e);
        }
        return findById(user.getId());
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't delete user with id: " + id, e);
        }
    }

    @Override
    public User findByEmail(String email) {
        User user = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = mapResultSetToUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't find user with email: " + email, e);
        }
        return user;
    }

    @Override
    public int countAll() {
        int count;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_ALL)) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            count = resultSet.getInt("count");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("user_id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(toRole(resultSet.getString("name_role")));
        return user;
    }

    private Role toRole (String nameRole){
        Role role;
        switch (nameRole) {
            case "Admin":
                role = Role.ADMIN;
                break;
            case "Manager":
                role = Role.MANAGER;
                break;
            case "Customer":
                role = Role.CUSTOMER;
                break;
            default:
                throw new RuntimeException("Not found this role");
        }
        return role;
    }

    private void mapUserToStatementData (User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getFirstName());
        statement.setString(2, user.getLastName());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getPassword());
        statement.setInt(5, toIdRole(user.getRole()));
    }

    private int toIdRole (Role role) {
        int nameRole;
        switch (role) {
            case ADMIN:
                nameRole = 1;
                break;
            case MANAGER:
                nameRole = 2;
                break;
            case CUSTOMER:
                nameRole = 3;
                break;
            default:
                throw new RuntimeException("Not found this role");
        }
        return nameRole;
    }
}