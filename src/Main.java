import UI.ConsolePayment;
import UI.ConsoleReservation;
import UI.ConsoleRoom;
import UI.ConsoleUser;

import exception.UserNoteFoundException;
import policy.PricingStrategy;
import policy.impl.StandardPricingStrategy;
import repository.PaymentsRepository;
import repository.impl.JdbcPaymentsRepository;
import repository.impl.JdbcReservationRepository;
import repository.impl.JdbcRoomRepository;
import repository.impl.JdbcUserRepository;
import service.AuthService;
import service.PaymentService;
import service.ReservationService;
import service.RoomService;
import util.Checkers;
import util.InputUtils;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException, UserNoteFoundException {

        JdbcUserRepository userRepository = new JdbcUserRepository();
        JdbcReservationRepository reservationRepository = new JdbcReservationRepository();
        JdbcRoomRepository roomRepository = new JdbcRoomRepository();
        JdbcPaymentsRepository paymentsRepository = new JdbcPaymentsRepository();

        Checkers checker = new Checkers(userRepository, roomRepository);
        StandardPricingStrategy pricingStrategy = new StandardPricingStrategy();
        AuthService authService = new AuthService(userRepository);
        RoomService roomService = new RoomService(roomRepository);
        ReservationService reservationService = new ReservationService(authService, reservationRepository, checker, pricingStrategy);
        PaymentService paymentService = new PaymentService(checker, reservationRepository, paymentsRepository, userRepository);
        ConsoleUser consoleUI = new ConsoleUser(authService);
        ConsoleRoom consoleRoom = new ConsoleRoom(roomService);
        ConsoleReservation consoleReservation = new ConsoleReservation(reservationService, authService);
        ConsolePayment consolePayment = new ConsolePayment(paymentService, authService);
        Scanner scanner = new Scanner(System.in);

        boolean continuer = true;

        while (continuer) {

            if (authService.getCurrentUser() == null) {
                consoleUI.afficherMenuNonConneter();
                int choix = InputUtils.lireInt(scanner, "Entrer Votre choix");
                switch (choix) {
                    case 1:
                        consoleUI.register();
                        break;

                    case 2:
                        consoleUI.login();
                        break;
                    case 3:
                        consoleRoom.createRoom();
                    case 0:
                        continuer = false;
                        break;

                    default:
                        System.out.println("Choix invalide.");
                }
            } else {
                String role = authService.getCurrentUser().getRole().getName();
                if (role.equals("ADMIN")) {
                    consoleUI.afficherMenuAdmin();

                    int choix = InputUtils.lireInt(scanner, "ENtrer Votre choix");
                    switch (choix) {
                        case 1:
                            consoleRoom.viewAllRoom();
                            break;

                        case 2:
                            consoleRoom.createRoom();
                            break;

                        case 3:
                            consoleRoom.updateRoom();
                            break;

                        case 4:
                            consoleRoom.deleteRoom();
                            break;

                        case 5:
                            consoleReservation.showReservation();
                            break;
                        case 6:
                            consoleUI.getALluser();
                            break;
                        case 7:
                            consoleUI.logout();
                            break;

                        case 0:
                            continuer = false;
                            break;

                        default:
                            System.out.println("Choix invalid.");
                    }
                } else {
                    consoleUI.afficherMenu();
                    int choix = InputUtils.lireInt(scanner, "Entrer Votre choix");
                    switch (choix) {
                        case 1:
                            consoleRoom.roomDisponible();
                            break;
                        case 2:
                            consoleRoom.viewAllRoom();
                            break;
                        case 3:
                            consoleReservation.makeReservation();
                            break;
                        case 4:
                            consoleReservation.myReservation();
                            break;
                        case 5:
                            consoleReservation.showReservation();
                            break;
                        case 6:
                            consoleReservation.modifierReservation();
                            break;
                        case 7:
                            consoleReservation.cancelReservation();
                            break;
                        case 8:
                            consoleUI.profil();
                            break;
                        case 9:
                            consoleUI.profileModif();
                            break;
                        case 10:
                            consoleUI.passModif();
                            break;

                        case 11:
                            consolePayment.makePayment();
                            break;

                        case 12:
                            consolePayment.refund();
                            break;
                        case 13:
                            consoleUI.logout();
                            break;
                        case 14:
                            consoleRoom.deleteRoom();
                            break;

                        case 15:
                            consoleRoom.updateRoom();
                            break;
                        case 0:
                            continuer = false;
                            break;
                        default:
                            System.out.println("choix invalid.");
                            break;
                    }

                }

            }

        }

        scanner.close();


    }
}

