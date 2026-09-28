package repository.impl;

import config.DatabaseConnection;
import model.Payment;
import repository.PaymentsRepository;
import util.Convert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcPaymentsRepository implements PaymentsRepository {
        Connection cnx = DatabaseConnection.getInstance().getConnection();
    @Override
    public void save(Payment payment) throws SQLException {
        String query = "INSERT INTO payments (id,reservation_code,amount_ht,status,tva,amount_ttc,payment_methode,payment_date) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setObject(1, payment.getId());
        statement.setString(2, payment.getReservation_code().getReservationCode());
        statement.setBigDecimal(3, payment.getAmount_ht());
        statement.setObject(4, payment.getStatus().name(), Types.OTHER);
        statement.setBigDecimal(5, payment.getTva());
        statement.setBigDecimal(6, payment.getAmount_ttc());
        statement.setObject(7, payment.getPaymentMethode().name(), Types.OTHER);
        statement.setObject(8, payment.getPayment_date(), Types.DATE);
        statement.executeUpdate();
    }

    @Override
    public List<Payment> findAllPayments() throws SQLException {

        String query = "SELECT * FROM payments";
        List<Payment> payments = new ArrayList<>();
        PreparedStatement statement = cnx.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Payment payment = Convert.mapToPayment(resultSet);

                payments.add(payment);
            }


        return payments;
    }



    @Override
    public List<Payment>findById(UUID id)throws SQLException {

        String query = "SELECT * FROM payments WHERE id = ? ";

        List<Payment> payments = new ArrayList<>();

        PreparedStatement statement = cnx.prepareStatement(query);

            statement.setObject(1, id);

            ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Payment payment = Convert.mapToPayment(resultSet);
                    payments.add(payment);
                }

        return payments;
    }

    @Override
    public List<Payment> findByReservation(String reservation_code) throws SQLException {

        String query = " SELECT * FROM payments WHERE reservation_code = ?";

        List<Payment> payments = new ArrayList<>();

        PreparedStatement statement = cnx.prepareStatement(query);

            statement.setString(1, reservation_code);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Payment payment = Convert.mapToPayment(resultSet);
                    payments.add(payment);
                }
            }

        return payments;
    }
    @Override
    public void update(Payment payment) throws SQLException {
        String query = "UPDATE payments SET status = ? WHERE id = ?";

            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setObject(1, payment.getStatus().name(), Types.OTHER);
            statement.setObject(2, payment.getId());

            statement.executeUpdate();

    }
}
