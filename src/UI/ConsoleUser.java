package UI;

import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import exception.UserNoteFoundException;
import model.Client;
import model.User;
import service.AuthService;
import util.InputUtils;

import javax.security.auth.login.CredentialException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUser {
    private AuthService authService;

    public ConsoleUser(AuthService authService) {
        this.authService = authService;
    }


    public void afficherMenuNonConneter(){
        System.out.println("1 . Register");
        System.out.println("2 . Login");
        System.out.println("0 . Exit");
    }

    public void afficherMenu(){
        System.out.println("========================");
        System.out.println("1 .Search available rooms");
        System.out.println("2 .View all rooms");
        System.out.println("3 .Create reservation");
        System.out.println("4 .My reservations");
        System.out.println("5 .Reservation details");
        System.out.println("6 .Update reservation");
        System.out.println("7 .Cancel reservation");
        System.out.println("8 .My Profil");
        System.out.println("9 .Update profile");
        System.out.println("10 .Change Password");
        System.out.println("11 .Logout");
        System.out.println("0 .Exit");
    }
    public void afficherMenuAdmin() {
        System.out.println("============ADMIN MENU=======");

        System.out.println("1. View all rooms");
        System.out.println("2. Create room");
        System.out.println("3. Update room");
        System.out.println("4. Delete room");
        System.out.println("5. View reservation");
        System.out.println("6. View all users");
        System.out.println("7. Logout");
        System.out.println("0. Exit");
        System.out.println("================================");
    }

    private final Scanner scanner = new Scanner(System.in);

    public void register() {
        String fullName = InputUtils.lireString(scanner,"Enter the FullName");
        String email = InputUtils.lireString(scanner,"Enter the email");
        String phone = InputUtils.lireString(scanner,"Enter the Phone");
        String password = InputUtils.lireString(scanner,"Enter the Password");
        try {
            authService.registerClient(fullName, email, phone, password);
        } catch (EmailAlreadyExistsException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void login() {
        try {
            String email = InputUtils.lireString(scanner,"Enter the email");
            String passWord = InputUtils.lireString(scanner,"Enter the Password");

            authService.login(email, passWord);
            System.out.println("Login with succes");
        } catch (InvalidCredentialsException | SQLException | NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }

    }

    public void logout() {
        authService.logout();
        System.out.println("Logout successful");
    }

    public void profileModif() {
        System.out.println("===== MODIFIER PROFILE =====");

        try {
            User user = authService.getCurrentUser();
            String newFullName = InputUtils.lireString(scanner,"Enter the new FullName");
            String newEmail = InputUtils.lireString(scanner,"Enter the new email");
            String newPhone = InputUtils.lireString(scanner,"Enter the new Phone");
            authService.profileModif(newFullName,newEmail,newPhone,user);
            System.out.println("Profile modifiee with succes");
        } catch (InvalidCredentialsException | EmailAlreadyExistsException | SQLException e) {
            System.out.println(e.getMessage());

        }
    }


    public void passModif() {
        System.out.println("===== MODIFY PASSWORD =====");
        User user = authService.getCurrentUser();
        String oldPassword = InputUtils.lireString(scanner,"Enter the old password");
        String newPassword = InputUtils.lireString(scanner,"Enter the new password");
        try {
            authService.passModif(user, newPassword, oldPassword);
            System.out.println("Password modified wit succes");
        } catch (InvalidCredentialsException e) {
            System.out.println(e.getMessage());

        } catch (UserNoteFoundException e) {
            System.out.println(e.getMessage());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (CredentialException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
    }

    public void profil(){
        try {
            System.out.println("===== MY PROFILE =====");
            User user = authService.getCurrentUserProfile();

            System.out.println("ID : " + user.getId());
            System.out.println("FullName : " + user.getFullName());
            System.out.println("Email : " + user.getEmail());
            System.out.println("Phone : " + user.getPhone());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (UserNoteFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    public void getALluser() {
        try {
            List<User> users = authService.getAllUsers();
            for (User u : users){
                System.out.println("ID : " + u.getId());
                System.out.println("FullName : " + u.getFullName());
                System.out.println("Email : " + u.getEmail());
                System.out.println("Phone : " + u.getPhone());            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
