package repository.impl;

import config.DatabaseConnection;
import model.Reservation;
import model.ReservationStatus;
import model.Room;
import repository.ReservationRepository;
import util.Convert;

import java.sql.*;
import java.util.*;

public class JdbcReservationRepository implements ReservationRepository {
        static Connection cnx = DatabaseConnection.getInstance().getConnection();
    @Override
    public void save(Reservation reservation) throws SQLException {
        String query = "Insert INTO reservations (reservation_code, user_id, room_number, checkin, checkout, number_of_guests, number_of_night, reservation_status,created_at,total_price) VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1, reservation.getReservationCode());
        statement.setObject(2,reservation.getUserId());
        statement.setString(3, reservation.getRoomNumber());
        statement.setObject(4,reservation.getCheckIn());
        statement.setObject(5,reservation.getCheckOut());
        statement.setInt(6,reservation.getNumberOfGuests());
        statement.setInt(7,reservation.getNumberOfNights());
        statement.setObject(8,reservation.getStatus().name(),Types.OTHER);
        statement.setObject(9,reservation.getCreatedAt(), Types.TIMESTAMP);
        statement.setBigDecimal(10,reservation.getTotal_price());
        statement.executeUpdate();
    }

    @Override
    public Optional<Reservation> findByCode(String reservationCode) throws SQLException {
        String query = "SELECT * from reservations where reservation_code = ? ";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1,reservationCode);
        ResultSet result = statement.executeQuery();
        if (result.next()){
            Reservation reservation = Convert.mapToReservation(result);
            Optional<Reservation> OptionnalReservation = Optional.of(reservation);
            return OptionnalReservation;
        }

        return Optional.empty();
    }

    @Override
    public List<Reservation> findByUserId(UUID userId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations where user_id = ? ";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setObject(1,userId);
        ResultSet result = statement.executeQuery();
        while (result.next()){
            Reservation reservation = Convert.mapToReservation(result);
            reservations.add(reservation);
        }

        return reservations;
    }

    @Override
    public List<Reservation> findByRoomNumber(String roomNumber) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT reservations.* , users.id AS user_id , rooms.roomnumber AS room_number  FROM users join reservations on users.id = reservations.user_id join rooms on rooms.roomnumber = reservations.room_number where reservations.room_number = ? ";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1,roomNumber);
        ResultSet result = statement.executeQuery();
        while (result.next()){
            Reservation reservation = Convert.mapToReservation(result);
            reservations.add(reservation);
        }

        return reservations;
    }

    @Override
    public List<Reservation> findAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * from reservations";
        PreparedStatement statement = cnx.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        while (result.next()){
            Reservation reservation = Convert.mapToReservation(result);
            reservations.add(reservation);
        }

        return reservations;
    }

    @Override
    public void updateStatus(String reservationCode, ReservationStatus status) throws SQLException {

        String sql = " UPDATE reservations SET reservation_status = ? WHERE reservation_code = ? ";
        PreparedStatement statement = cnx.prepareStatement(sql);
            statement.setString(1, status.name());
            statement.setString(2, reservationCode);

            statement.executeUpdate();

    }
}



