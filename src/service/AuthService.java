package service;

import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import exception.UserNoteFoundException;
import model.Admin;
import model.Client;
import model.Role;
import model.User;
import repository.UserRepository;
import util.PasswordHash;
import util.ValidationUtils;

import javax.security.auth.login.CredentialException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthService {
    private final UserRepository userRepository;

    private User currentUser;

    public AuthService(UserRepository clientRepository) {
        this.userRepository = clientRepository;
    }

    public void registerClient(String fullName ,String email,String phone,String password) throws EmailAlreadyExistsException, SQLException, NoSuchAlgorithmException {
        if (userRepository.existsByEmail(email)){
           throw new EmailAlreadyExistsException();
       } else if (ValidationUtils.emailVerfication(email) && ValidationUtils.passwordVerfication(password) && ValidationUtils.phoneVerfication(phone)) {

            byte[] saltByte = PasswordHash.salt();
            String salt = Base64.getEncoder().encodeToString(saltByte); //convert Byte to string
            String passwordHached = PasswordHash.hashPassword(password,saltByte);
            if (!userRepository.existsUsers()){
                Role role = new Role(2, "ADMIN");
                Admin admin = new Admin(UUID.randomUUID(), fullName, email, password, role, phone, passwordHached, salt,null);
                userRepository.save(admin);
            }else {
                Role role = new Role(1, "CLIENT");
                Client client = new Client(UUID.randomUUID(), fullName, email, password, role, phone, passwordHached, salt, new BigDecimal("5000.00"));
                userRepository.save(client);

            }
       }

    }

    public void login (String email,String password) throws InvalidCredentialsException, SQLException, NoSuchAlgorithmException {
        Optional<User> userFound = userRepository.findByEmail(email);

            if (userFound.isEmpty()) {
                throw new InvalidCredentialsException();
            }

            User user = userFound.get();
            byte[] saltByte = Base64.getDecoder().decode(user.getSalt());//convert from string bas64 to byte
            String hashedPassword = PasswordHash.hashPassword(password,saltByte);
            if (!user.getHashedPassword().equals(hashedPassword)) {
                throw new InvalidCredentialsException();
            }
            currentUser = user;

    }

    public void logout(){
        currentUser = null;
    }

    public void profileModif(String newFullname,String newEmail,String newPhone,User user) throws InvalidCredentialsException, EmailAlreadyExistsException, SQLException {
        Optional<User> currentUser = userRepository.findByEmail(user.getEmail());
        if (currentUser.isEmpty()){
            throw new InvalidCredentialsException();
        }
        User userNow = currentUser.get();

        if (ValidationUtils.emailVerfication(newEmail)) {
            boolean emailEX = userRepository.existsByEmail(newEmail);
            if (emailEX && !userNow.getEmail().equals(newEmail)) {
                throw new EmailAlreadyExistsException();
            }
            userNow.setEmail(newEmail);
        }
        userNow.setFullName(newFullname);
        userNow.setPhone(newPhone);
        userRepository.update(userNow);
    }

public void passModif(User user,String newPassword ,String oldPassword) throws InvalidCredentialsException, UserNoteFoundException, SQLException, NoSuchAlgorithmException, CredentialException {
    Optional<User> currentUser = userRepository.findByEmail(user.getEmail());
    if (currentUser.isEmpty()) {
        throw new UserNoteFoundException();
    }
    User userNow = currentUser.get();
    byte[] saltByte = Base64.getDecoder().decode(user.getSalt());//convert from string bas64 to byte
    String hashedOldPassword = PasswordHash.hashPassword(oldPassword,saltByte);
    if (!user.getHashedPassword().equals(hashedOldPassword)){
        throw new CredentialException();
    }


    if (!oldPassword.equals(userNow.getPassword())) {
        throw new InvalidCredentialsException();
    }
    if (ValidationUtils.passwordVerfication(newPassword)) {
        String hashedNewPassword = PasswordHash.hashPassword(newPassword,saltByte);
        userNow.setPassword(newPassword);
        userNow.setHashedPassword(hashedNewPassword);

    }

    userRepository.updatePassword(userNow);

}

    public User getCurrentUserProfile() throws SQLException, UserNoteFoundException {
        Optional<User> User = userRepository.findById(currentUser.getId());
        if (User.isEmpty()) {
            throw new UserNoteFoundException();
        }
            return User.get();


    }

    public User getCurrentUser() {
        return currentUser;
    }


    public List<User> getAllUsers() throws SQLException {
        return userRepository.findAll();
    }
}
