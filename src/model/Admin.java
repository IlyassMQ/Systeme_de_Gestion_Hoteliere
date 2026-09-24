package model;

import java.util.UUID;

public class Admin extends User{

    public Admin(UUID id, String fullName, String email, String password, Role role, String phone, String hashedPassword, String salt) {
        super(id, fullName, email, password, role, phone, hashedPassword, salt);
    }
}
