package UI;

import exception.PaymentNotFound;
import exception.ReservationNotFoundException;
import exception.UserNoteFoundException;
import model.PaymentMethode;
import model.User;
import service.AuthService;
import service.PaymentService;
import util.InputUtils;

import java.sql.SQLException;
import java.util.Scanner;

public class ConsolePayment {

    private PaymentService paymentService;
    private AuthService authService;

    public ConsolePayment(PaymentService paymentService,
                          AuthService authService) {
        this.paymentService = paymentService;
        this.authService = authService;
    }

    Scanner scanner = new Scanner(System.in);


    public void makePayment() {

        User user = authService.getCurrentUser();

        String reservationCode = InputUtils.lireString(scanner, "Enter the Reservation Code: ");

        System.out.println("Choose Payment Method:");
        System.out.println("1. CASH");
        System.out.println("2. CARD");
        System.out.println("3. BANK TRANSFER");

        int choice = InputUtils.lireInt(scanner, "Enter your choice: ");

        PaymentMethode paymentMethode;

        switch (choice) {

            case 1:
                paymentMethode = PaymentMethode.CASH;
                break;

            case 2:
                paymentMethode = PaymentMethode.CARD;
                break;

            case 3:
                paymentMethode = PaymentMethode.BANK_TRANSFER;
                break;

            default:
                System.out.println("Invalid payment method.");
                return;
        }

        try {
            paymentService.makePayment(user.getId(), reservationCode, paymentMethode);
            System.out.println("Payment successful.");

        } catch (ReservationNotFoundException e) {
            System.out.println(e.getMessage());

        } catch (UserNoteFoundException e) {
            System.out.println(e.getMessage());

        } catch (SQLException e) {
            System.out.println("Payment error: " + e.getMessage());
        }
    }


    public void refund() {

        User user = authService.getCurrentUser();

        String reservationCode = InputUtils.lireString(scanner, "Enter the Reservation Code: ");

        try {
            paymentService.refund(user.getId(), reservationCode);
            System.out.println("Refund completed successfully.");

        } catch (ReservationNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (UserNoteFoundException e) {
            System.out.println(e.getMessage());
        } catch (PaymentNotFound e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Refund error: " + e.getMessage());
        }
    }
}