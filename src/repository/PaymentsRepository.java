package repository;

import model.Payment;


import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface PaymentsRepository {


    public void save(Payment payments) throws SQLException;
    public List<Payment>findAllPayments() throws SQLException;
    public List<Payment>findById(UUID id) throws SQLException;

    public List<Payment>findByReservation(String reservation_code) throws SQLException;

    public void update(Payment payment) throws SQLException;

}
