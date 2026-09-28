package service;

import config.DatabaseConnection;
import exception.PaymentNotFound;
import exception.ReservationNotFoundException;
import exception.UserNoteFoundException;
import model.*;
import repository.PaymentsRepository;
import repository.ReservationRepository;
import repository.UserRepository;
import util.CalculUtil;
import util.Checkers;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PaymentService {
        private final Checkers userChecker;
        private final ReservationRepository reservationRepository;
        private final UserRepository userRepository;
        private final PaymentsRepository paymentsRepository;


        public PaymentService(Checkers userChecker,ReservationRepository reservationRepository,PaymentsRepository paymentsRepository,UserRepository userRepository){
            this.userChecker = userChecker;
            this.reservationRepository = reservationRepository;
            this.paymentsRepository = paymentsRepository;
            this.userRepository = userRepository;
        }


    public void makePayment(UUID userId, String reservationCode, PaymentMethode paymentMethode) throws SQLException, UserNoteFoundException, ReservationNotFoundException {
            userChecker.userCheckById(userId);
       Optional<Reservation> reservationOP = reservationRepository.findByCode(reservationCode);
        if (reservationOP.isEmpty()) {
            throw new ReservationNotFoundException();
        }
         Reservation reservation = reservationOP.get();
        PaymentStatus paymentStatus = PaymentStatus.COMPLETED;
        BigDecimal amountHt = reservation.getTotal_price();
        BigDecimal tva =  CalculUtil.TvaCalcul(amountHt);
        BigDecimal amountTTC = amountHt.add(tva);
        LocalDate payment_date = LocalDate.from(LocalDateTime.now());
        Payment payment = new Payment(UUID.randomUUID(),reservation,reservation.getTotal_price(),paymentStatus,tva,amountTTC,paymentMethode,payment_date);
        paymentsRepository.save(payment);
    }


    public BigDecimal calculateRefund(Reservation reservation, Payment payment) {
        LocalDate now = LocalDate.now();
        LocalDate checkIn = reservation.getCheckIn();

        long daysBefore = ChronoUnit.DAYS.between(now, checkIn);

        BigDecimal amountTTC = payment.getAmount_ttc();
        if (daysBefore > 14) {
            return amountTTC;
        }
        if (daysBefore >= 7) {
            return amountTTC.multiply(new BigDecimal("0.70"));
        }

        if (daysBefore >= 2) {
            return amountTTC.multiply(new BigDecimal("0.50"));
        }

        return BigDecimal.ZERO;
    }


    public void refund(UUID userId, String reservationCode)
            throws SQLException, UserNoteFoundException, ReservationNotFoundException {

        Connection cnx = DatabaseConnection.getInstance().getConnection();

        try {
            cnx.setAutoCommit(false);

            Optional<Client> clientOP = userRepository.findClientById(userId);

            if (clientOP.isEmpty()) {
                throw new UserNoteFoundException();
            }

            Client client = clientOP.get();
            Optional<Reservation> reservationOP = reservationRepository.findByCode(reservationCode);

            if (reservationOP.isEmpty()) {
                throw new ReservationNotFoundException();
            }

            Reservation reservation = reservationOP.get();
            List<Payment> payments =
                    paymentsRepository.findByReservation(reservationCode);

            if (payments.isEmpty()) {
                throw new PaymentNotFound();
            }

            Payment payment = payments.get(0);
            BigDecimal refundAmount = calculateRefund(reservation, payment);


            BigDecimal newSolde =client.getSolde().add(refundAmount);
            client.setSolde(newSolde);
            userRepository.updateSolde(client);
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentsRepository.update(payment);
            reservationRepository.updateStatus(reservationCode, ReservationStatus.CANCELLED);

            cnx.commit();

        } catch (SQLException e) {
            cnx.rollback();

            throw e;

        } finally {
            cnx.setAutoCommit(true);
        }
    }}
