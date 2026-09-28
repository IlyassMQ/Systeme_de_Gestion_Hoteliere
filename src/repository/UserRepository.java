package repository;

import model.Client;
import model.User;
import org.postgresql.core.SqlCommand;

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
    public Optional<Client> findClientById(UUID userId) throws SQLException;
    public boolean existsByEmail(String email) throws SQLException;
    public List<User> findAll() throws SQLException;
    public void updateSolde(Client client) throws SQLException;
    public boolean existsUsers() throws SQLException;
}
