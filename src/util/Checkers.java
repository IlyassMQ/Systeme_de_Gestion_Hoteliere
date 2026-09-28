package util;

import exception.*;
import model.*;
import repository.RoomRepository;
import repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class Checkers {
    private final UserRepository clientRepository;
    private final RoomRepository roomRepository;


    public Checkers(UserRepository clientRepository, RoomRepository roomRepository) {
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
    }

    public User userCheckbyEmail(String email) throws UserNoteFoundException, SQLException {
        Optional<User> user = clientRepository.findByEmail(email);
        if (user.isEmpty()){
            throw new UserNoteFoundException();
        }
        return user.get();
    }

    public User userCheckById(UUID userId) throws SQLException, UserNoteFoundException {
        Optional<User> user = clientRepository.findById(userId);
        if (user.isEmpty()){
            throw new UserNoteFoundException();
        }
        return user.get();
    }

    public Client clientCheckById(UUID userId) throws SQLException, UserNoteFoundException {
        Optional<Client> client = clientRepository.findClientById(userId);
        if (client.isEmpty()){
            throw new UserNoteFoundException();
        }
        return client.get();
    }




    public Room roomCheck(String roomNumber) throws RoomNotFoundException, SQLException {
        Optional<Room> room = roomRepository.findByRoomNumber(roomNumber);
        if (room.isEmpty()){
            throw new RoomNotFoundException();
        }
        return room.get();
    }

    public boolean roomDispo(String roomNumber) throws RoomNotFoundException, RoomUnavailableException, SQLException {
        if (roomCheck(roomNumber).getStatus() != RoomStatus.AVAILABLE){
            throw new RoomUnavailableException();
        }
        return true;
    }
    public boolean timeCheck(LocalDate checkIn,LocalDate checkOut) throws InvalidReservationDateException {
        if (checkIn.isAfter(checkOut) || checkIn.isBefore(LocalDate.now()) ||checkIn.equals(checkOut)) {
            throw new InvalidReservationDateException();
        }
        return true;
    }
    public boolean guestsCheck(Room room, int numberOfGuests){
                if (room.getCapacity() < numberOfGuests){
                    throw new RoomCapacityException();
                }
                return true;
    }




}
