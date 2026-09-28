package model;

import java.math.BigDecimal;
import java.util.UUID;

public class Client extends User{
    public Client(UUID id, String fullName, String email, String password, Role role, String phone, String hashedPassword, String salt, BigDecimal solde) {
        super(id, fullName, email, password, role, phone, hashedPassword, salt,solde);
    }

}
