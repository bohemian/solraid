package helper;

import jakarta.validation.constraints.Email;

public class EmailPayload {
    @Email
    final String email;

    public EmailPayload(String email) {
        this.email = email;
    }
}
