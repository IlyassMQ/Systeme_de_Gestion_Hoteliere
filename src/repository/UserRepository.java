package repository;

import model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    public void save(User user) throws SQLException;
    public void update(User user) throws SQLException;
    public void updatePassword(User user) throws SQLException;
    public Optional<User> findByEmail(String email) throws SQLException;
    public Optional<User> findById(UUID userId) throws SQLException;
    public boolean existsByEmail(String email) throws SQLException;
    public List<User> findAll() throws SQLException;

}
