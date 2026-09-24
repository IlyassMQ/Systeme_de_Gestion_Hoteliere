package util;

import model.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class Convert {


    public static Room mapToRoom(ResultSet result) throws SQLException {
        String roomNumber = result.getString("roomnumber");

        RoomStatus status = RoomStatus.valueOf(
                result.getString("room_status")
        );
        int capacity = result.getInt("capacity");
        BigDecimal price = result.getBigDecimal("price");
        return new Room(roomNumber,capacity,price,status);
    }

    public static User mapToUser(ResultSet result) throws SQLException {
        UUID userId = result.getObject("id", UUID.class);
        String userFullname = result.getString("full_name");
        String email = result.getString("email");
        String password = result.getString("password");
        String passwordHash = result.getString("password_hash");
        String salt = result.getString("salt");

        int role_id = result.getInt("role_id");
        String role_name = result.getString("role_name");
        Role role = new Role(role_id,role_name);
        String phone = result.getString("phone");

        return new User(userId,userFullname,email,password,role,phone,passwordHash,salt);



    }

    public static Reservation mapToReservation(ResultSet result) throws SQLException {
       String reservationCode  = result.getString("reservation_code");
       UUID userId = result.getObject("user_id", UUID.class);
       String roomNumber = result.getString("room_number");
       LocalDate checkIn = result.getDate("checkin").toLocalDate();
       LocalDate checkOut = result.getDate("checkout").toLocalDate();
       int guestNumber = result.getInt("number_of_guests");
       int nightNumber = result.getInt("number_of_night");
       LocalDateTime createdAt = result.getObject("created_at",LocalDateTime.class);
       ReservationStatus status = ReservationStatus.valueOf(
                result.getString("reservation_status")
        );

       return new Reservation(reservationCode,userId,roomNumber,checkIn,checkOut,guestNumber,nightNumber,status,createdAt);
    }
}
