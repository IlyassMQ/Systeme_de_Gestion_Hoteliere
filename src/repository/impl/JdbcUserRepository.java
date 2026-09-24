package repository.impl;

import config.DatabaseConnection;
import model.User;
import repository.UserRepository;
import util.Convert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcUserRepository implements UserRepository {

   Connection cnx = DatabaseConnection.getInstance().getConnection();


    @Override
    public void save(User user) throws SQLException {

            String query = "insert into users (id,full_name,email,password,password_hash,role_id,phone,salt) VALUES (?,?,?,?,?,?,?,?)";

            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setObject(1,user.getId());
            statement.setString(2,user.getFullName());
            statement.setString(3,user.getEmail());
            statement.setString(4,user.getPassword());
            statement.setString(5,user.getHashedPassword());
            statement.setInt(6,user.getRole().getId());
            statement.setString(7,user.getPhone());
            statement.setString(8, user.getSalt());

            statement.executeUpdate();
        }

    public void update(User user) throws SQLException {
        String query = "UPDATE users set full_name = ? , email = ? ,phone = ? where id = ?";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1, user.getFullName());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPhone());
        statement.setObject(4,user.getId());

        statement.executeUpdate();

    }

    public void updatePassword(User user) throws SQLException {
        String query = "UPDATE users SET password = ? ,password_hash = ? where id = ?";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1,user.getPassword());
        statement.setString(2, user.getHashedPassword());
        statement.setObject(3,user.getId());
        statement.executeUpdate();
    }

    @Override
    public Optional<User> findByEmail(String email) throws SQLException {
        String query = "select users.*,role.id AS role_id ,role.name AS role_name  from users join role on users.role_id = role.id where email = ?";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1,email);

        ResultSet result = statement.executeQuery();

        if (result.next()){
            User user = Convert.mapToUser(result);
            Optional<User> OptionnalUser = Optional.of(user);
            return OptionnalUser;
        }
        return Optional.empty();
    }


    @Override
    public boolean existsByEmail(String email) throws SQLException {
        String query = "select * from users where email = ?";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1,email);
        ResultSet result = statement.executeQuery();
        if (result.next()){
            return true;
        }
        return false;
    }
    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "select users.* , role.id AS role_id ,role.name AS role_name from users join role on users.role_id = role.id";
        PreparedStatement statement = cnx.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        while (result.next()){
            User user = Convert.mapToUser(result);
            users.add(user);
        }
        return users;

    }

    @Override
    public Optional<User> findById(UUID userId) throws SQLException {
        String query = "select users.*, role.id AS role_id ,role.name AS role_name from users join role on users.role_id = role.id where users.id = ?";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setObject(1,userId);

        ResultSet result = statement.executeQuery();

        if (result.next()){
            User user = Convert.mapToUser(result);
            Optional<User> OptionnalUser = Optional.of(user);
            return OptionnalUser;
        }
        return Optional.empty();
    }
}
