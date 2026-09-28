package service;

import exception.RoomNotFoundException;
import model.Room;
import model.RoomStatus;
import model.RoomType;
import repository.RoomRepository;
import util.CodeGenerater;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class RoomService {
    private final RoomRepository RoomRepository;

    public RoomService(RoomRepository RoomRepository) {
        this.RoomRepository = RoomRepository;
    }

    public void createRoom( int capacity , BigDecimal pricePerNight) throws SQLException {
        String roomNNumber = CodeGenerater.randomCode();
        Room room = new Room(roomNNumber,capacity,pricePerNight,RoomStatus.AVAILABLE);
        RoomRepository.save(room);

    }
    public List<Room> showAllRoom() throws SQLException {
       return RoomRepository.findAll();
    }

    public List<Room> findRoomDispo() throws SQLException {
        List<Room> allRoom = RoomRepository.findAll();
        List<Room> roomsDispo= new ArrayList<>();
        for (Room r : allRoom){
            if (r.getStatus() == RoomStatus.AVAILABLE){
                roomsDispo.add(r);
            }
        }
        return roomsDispo;
    }

    public Optional<Room> searchRoomByNumber(String roomNumber) throws SQLException {
        return RoomRepository.findByRoomNumber(roomNumber);
    }

    public void updateRoom(BigDecimal newPrice,int newCapacity,String newStatus,String roomnumber) throws SQLException, RoomNotFoundException {
            Optional<Room> room = searchRoomByNumber(roomnumber);

            if (room.isEmpty()){
                throw new RoomNotFoundException();
            }
        Room currentRoom = room.get();
        Room rooms = new Room(currentRoom.getRoomNumber(),newCapacity,newPrice,RoomStatus.valueOf(newStatus));
        RoomRepository.update(rooms);

    }
    public void deleteRoom(String roomNumber) throws SQLException, RoomNotFoundException {
        Optional<Room> room = searchRoomByNumber(roomNumber);
        if (room.isEmpty()){
            throw new RoomNotFoundException();
        }
        RoomRepository.delete(roomNumber);
    }

}
