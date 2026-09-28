package repository;

import model.Reservation;
import model.ReservationStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {

    public void save(Reservation reservation) throws SQLException;
    public Optional<Reservation> findByCode(String code) throws SQLException;
    public List<Reservation> findByUserId(UUID userId) throws SQLException;
    public List<Reservation> findByRoomNumber(String roomNumber) throws SQLException;
    public List<Reservation> findAll() throws SQLException;
    void updateStatus(String reservationCode, ReservationStatus status) throws SQLException;
}
