package repository;

import model.Room;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    public void save(Room room) throws SQLException;
    public Optional<Room> findByRoomNumber(String roomNumber) throws SQLException;
    public List<Room> findAll() throws SQLException;
    public void update(Room room) throws SQLException;
    public void delete(String roomNumber) throws SQLException;
}
