package co.unicauca.usermanagement.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordPolicyTest {

    @Test
    void shouldValidateCorrectPassword() {
        assertTrue(PasswordPolicy.isValid("Abc123!"));
    }

    @Test
    void shouldRejectShortPassword() {
        assertFalse(PasswordPolicy.isValid("A1!a"));
    }

    @Test
    void shouldRejectWithoutUppercase() {
        assertFalse(PasswordPolicy.isValid("abc123!"));
    }

    @Test
    void shouldRejectWithoutDigit() {
        assertFalse(PasswordPolicy.isValid("Abcdef!"));
    }

    @Test
    void shouldRejectWithoutSpecialChar() {
        assertFalse(PasswordPolicy.isValid("Abc1234"));
    }
}
