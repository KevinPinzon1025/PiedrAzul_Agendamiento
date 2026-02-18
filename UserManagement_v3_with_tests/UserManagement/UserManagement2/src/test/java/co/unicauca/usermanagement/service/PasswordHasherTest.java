package co.unicauca.usermanagement.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordHasherTest {

    @Test
    void shouldGenerateDifferentHashesForDifferentSalts() {
        String salt1 = PasswordHasher.newSaltBase64();
        String salt2 = PasswordHasher.newSaltBase64();

        String hash1 = PasswordHasher.hashBase64("Abc123!".toCharArray(), salt1);
        String hash2 = PasswordHasher.hashBase64("Abc123!".toCharArray(), salt2);

        assertNotEquals(hash1, hash2);
    }

    @Test
    void shouldVerifyCorrectPassword() {
        String salt = PasswordHasher.newSaltBase64();
        String hash = PasswordHasher.hashBase64("Abc123!".toCharArray(), salt);

        assertTrue(PasswordHasher.verify("Abc123!", salt, hash));
        assertFalse(PasswordHasher.verify("WrongPass1!", salt, hash));
    }
}
