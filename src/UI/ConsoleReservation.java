package UI;

import exception.*;
import model.Reservation;
import model.User;
import service.AuthService;
import service.ReservationService;
import util.Checkers;
import util.InputUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleReservation {


    private ReservationService reservationService;
    private AuthService authService;

    public ConsoleReservation(ReservationService reservationService,AuthService authService) {
        this.reservationService = reservationService;
        this.authService = authService;

    }
    Scanner scanner = new Scanner(System.in);

    public void makeReservation() throws SQLException, UserNoteFoundException {
        User user = authService.getCurrentUser();
        String roomNumber = InputUtils.lireString(scanner,"Enter the Room Number");
        LocalDate checkIn = InputUtils.lireDate(scanner,"Entrer date CheckIN (DD/MM/YYYY)");
        LocalDate checkOut = InputUtils.lireDate(scanner,"Entrer date CheckOut (DD/MM/YYYY)");
        int numberOfGuests = InputUtils.lireInt(scanner,"Entrer Number of guests");
        try {
            reservationService.createReservation(roomNumber,user.getId(),checkIn,checkOut,numberOfGuests);
        } catch (RoomNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (UserNoteFoundException e) {
            System.out.println(e.getMessage());
        } catch (RoomUnavailableException e) {
            System.out.println(e.getMessage());
        } catch (InvalidReservationDateException e) {
            System.out.println(e.getMessage());
        } catch (RoomCapacityException e){
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void myReservation(){
       try {
           List<Reservation> reservations= reservationService.getUserReservations();
           printReservation(reservations);
       } catch (ReservationNotFoundException e) {
           System.out.println("No reservation for now");
           System.out.println(e.getMessage());
       } catch (SQLException e) {
           throw new RuntimeException(e);
       } catch (UserNoteFoundException e) {
           throw new RuntimeException(e);
       }
    }

    private void printReservation(List<Reservation> reservations) {
        for (Reservation r : reservations){
            System.out.println("Reservation Code : " + r.getReservationCode());
            System.out.println("Room Number : "+ r.getRoomNumber());
            System.out.println("Check In : " + r.getCheckIn());
            System.out.println("Check Out : " + r.getCheckOut());
            System.out.println("Number of Guests : " + r.getNumberOfGuests());
            System.out.println("Number of Nights : " + r.getNumberOfNights());
            System.out.println("Status : " + r.getStatus());
            System.out.println("--------------------------------------");
        }
    }

    public void showReservation(){ //Admin
        String reservationCode = InputUtils.lireString(scanner,"Entrer reservation Code : ");
        try {
            Optional<Reservation> reservation = reservationService.findReservationByCode(reservationCode);
            if (reservation.isPresent()){
                Reservation res = reservation.get();
                System.out.println("Reservation Code : " + res.getReservationCode());
                System.out.println("Room Number : "+ res.getRoomNumber());
                System.out.println("Check In : " + res.getCheckIn());
                System.out.println("Check Out : " + res.getCheckOut());
                System.out.println("Number of Guests : " + res.getNumberOfGuests());
                System.out.println("Number of Nights : " + res.getNumberOfNights());
                System.out.println("Status : " + res.getStatus());
                System.out.println("--------------------------------------");
            }
        } catch (ReservationNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void modifierReservation() {
        try {
            printReservation(reservationService.getUserReservations());
            String reservationCode = InputUtils.lireString(scanner,"Enter the Reservation Code To Update");
            String roomNumber = InputUtils.lireString(scanner,"Enter the new Room Number");
            LocalDate checkIn = InputUtils.lireDate(scanner,"Enter the new Check IN");
            LocalDate checkOut = InputUtils.lireDate(scanner,"Enter the new Check Out");
            int numberOfGuests = InputUtils.lireInt(scanner,"Enter the new number of guests");
            reservationService.UpdateReservation(reservationCode,roomNumber,checkOut,checkIn,numberOfGuests);
        } catch (ReservationNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        } catch (RoomNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (RoomUnavailableException e) {
            System.out.println(e.getMessage());
        } catch (InvalidReservationDateException e) {
            System.out.println(e.getMessage());
        } catch (UserNoteFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void cancelReservation() {
        String reservationCode = InputUtils.lireString(scanner,"Enter Reservation code to cancel");
        try {
            reservationService.cancelReservation(reservationCode);
            System.out.println("The reservation with the Code :" + reservationCode + " is Canceled");
        } catch (ReservationNotFoundException | SQLException | UserNoteFoundException e) {
            System.out.println(e.getMessage());

        }
    }
}
