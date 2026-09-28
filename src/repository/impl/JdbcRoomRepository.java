package repository.impl;

import config.DatabaseConnection;
import model.Room;
import model.RoomStatus;
import model.User;
import repository.RoomRepository;
import util.Convert;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

public class JdbcRoomRepository implements RoomRepository {
    static Connection cnx = DatabaseConnection.getInstance().getConnection();

    @Override
    public void save(Room room) throws SQLException {
        String query = "insert into rooms (roomnumber,room_status,price,capacity) VALUES (?,?,?,?)";

        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1,room.getRoomNumber());
        statement.setObject(2,room.getStatus().name(),Types.OTHER);
        statement.setBigDecimal(3,room.getPricePerNight());
        statement.setInt(4,room.getCapacity());
        statement.executeUpdate();
    }

    @Override
    public Optional<Room> findByRoomNumber(String roomNumber) throws SQLException {
        String query = "select * from rooms where roomnumber = ? ";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1,roomNumber);
        ResultSet result = statement.executeQuery();
        while (result.next()){
            Room room = Convert.mapToRoom(result);
            Optional<Room> OptionnalRoom = Optional.ofNullable(room);
            return OptionnalRoom;
        }

        return Optional.empty();
    }

    @Override
    public List<Room> findAll() throws SQLException {
        List<Room> rooms = new LinkedList<>();
        String query = "select * from rooms";

        PreparedStatement statement = cnx.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        while (result.next()){
            Room room = Convert.mapToRoom(result);
            rooms.add(room);
        }
        return rooms;
    }
    @Override
    public void update(Room room) throws SQLException {
        String query = "UPDATE rooms set  capacity = ? , price = ?  , room_status = ? where roomnumber = ?";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setInt(1, room.getCapacity());
        statement.setBigDecimal(2, room.getPricePerNight());
        statement.setObject(3, room.getStatus().name(),Types.OTHER);
        statement.setString(4, room.getRoomNumber());
        statement.executeUpdate();

    }


    @Override
    public void delete(String roomNumber) throws SQLException {
        String query = "DELETE from rooms where roomnumber = ?";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1,roomNumber);

        statement.executeUpdate();
    }
}
