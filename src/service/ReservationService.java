package service;

import exception.*;
import model.*;
import policy.PricingStrategy;
import repository.ReservationRepository;
import util.CalculUtil;
import util.Checkers;
import util.CodeGenerater;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AuthService authService;
    private final PricingStrategy pricingStrategy;
    private final Checkers checkers;




    public ReservationService(AuthService authService, ReservationRepository reservationRepository,Checkers checkers,PricingStrategy pricingStrategy) {
        this.reservationRepository = reservationRepository;
        this.authService = authService;
        this.checkers = checkers;
        this.pricingStrategy = pricingStrategy;
    }

    public void createReservation(String roomNumber, UUID userId, LocalDate checkIn, LocalDate checkOut, int numberOfGuests) throws RoomNotFoundException, UserNoteFoundException, RoomUnavailableException, InvalidReservationDateException, SQLException {

        Room room = checkers.roomCheck(roomNumber);
        Client client = checkers.clientCheckById(userId);

        checkers.roomDispo(room.getRoomNumber());
        checkers.timeCheck(checkIn,checkOut);
        List<Reservation> roomReservation = reservationRepository.findByRoomNumber(roomNumber);
        for (Reservation r : roomReservation) {
            if (r.getStatus().equals(ReservationStatus.CANCELLED)) {
                continue;
            }
            if (checkIn.isBefore(r.getCheckOut()) && checkOut.isAfter(r.getCheckIn())) {
                throw new InvalidReservationDateException();
            }
        }
        int numberOfNights = Math.toIntExact(ChronoUnit.DAYS.between(checkIn, checkOut));
        String reservationCode =CodeGenerater.resevationCode();
        LocalDateTime createdAt = LocalDateTime.now();
        BigDecimal roomPrice = CalculUtil.PriceCalcul(checkIn,checkOut,room.getPricePerNight());
        BigDecimal totalPrice = pricingStrategy.calculatePrice(roomPrice,checkIn,checkOut,createdAt);
        checkers.guestsCheck(room,numberOfGuests);
        Reservation reservation = new Reservation(reservationCode, client.getId(), room.getRoomNumber(),
                checkIn, checkOut, numberOfGuests, numberOfNights, ReservationStatus.CONFIRMED, createdAt,totalPrice);
        reservationRepository.save(reservation);

    }

    public List<Reservation> getUserReservations() throws ReservationNotFoundException, SQLException, UserNoteFoundException {
        User user = authService.getCurrentUser();
        List<Reservation> userReservations = reservationRepository.findByUserId(user.getId());
        if (userReservations.isEmpty()){
            throw new ReservationNotFoundException();
        }

        return userReservations;
    }

    public void UpdateReservation(String reservationCode, String roomNumber, LocalDate checkOut, LocalDate checkIn, int numberOfGuests) throws ReservationNotFoundException, RoomNotFoundException, RoomUnavailableException, InvalidReservationDateException, SQLException, UserNoteFoundException {
        List<Reservation> userReservations = getUserReservations();
        Reservation reservationToModify = null;
        for (Reservation r : userReservations) {
            if (r.getReservationCode().equals(reservationCode)) {
                reservationToModify = r;
            }
        }
        if (reservationToModify == null || reservationToModify.getStatus().equals(ReservationStatus.CANCELLED)) {
            throw new ReservationNotFoundException();
        }
        Room room = checkers.roomCheck(roomNumber);

        checkers.roomDispo(room.getRoomNumber());
        checkers.timeCheck(checkIn, checkOut);

        List<Reservation> roomReservations = reservationRepository.findByRoomNumber(roomNumber);

        for (Reservation r : roomReservations) {
            if (r.getReservationCode().equals(reservationToModify.getReservationCode())) {
                continue;
            }

            if (checkIn.isBefore(r.getCheckOut()) && checkOut.isAfter(r.getCheckIn()) || checkIn.equals(checkOut)) {
                throw new InvalidReservationDateException();
            }
        }

        int numberOfNights = Math.toIntExact(ChronoUnit.DAYS.between(checkIn, checkOut));
        reservationToModify.setRoomNumber(room.getRoomNumber());
        reservationToModify.setCheckIn(checkIn);
        reservationToModify.setCheckOut(checkOut);
        reservationToModify.setNumberOfGuests(numberOfGuests);
        reservationToModify.setNumberOfNights(numberOfNights);

        reservationRepository.save(reservationToModify);
    }


    public void cancelReservation(String reservationCode) throws ReservationNotFoundException, SQLException, UserNoteFoundException {
        List<Reservation> userReservations = getUserReservations();
        Reservation reservationToCancel = null;
        for (Reservation r : userReservations) {
            if (r.getReservationCode().equals(reservationCode)) {
                reservationToCancel = r;
            }
        }
        if (reservationToCancel == null) {
            throw new ReservationNotFoundException();
        }
        reservationToCancel.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservationToCancel);
    }


    public Optional<Reservation> findReservationByCode(String Code) throws ReservationNotFoundException, SQLException {
        Optional<Reservation> reservation = reservationRepository.findByCode(Code);
        if (reservation.isEmpty()){
            throw new ReservationNotFoundException();
        }
        return reservation;
    }
    
}




