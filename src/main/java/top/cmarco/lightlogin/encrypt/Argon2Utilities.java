package top.cmarco.lightlogin.encrypt;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class used to encrypt important data
 * using the Argon2 hashing algorithm.
 */
public final class Argon2Utilities {

    private Argon2Utilities() {
        throw new RuntimeException("You may not instantiate this utility class.");
    }

    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a secure salt bytes with defined length.
     *
     * @param bytes The amount of bytes.
     * @return The array.
     */
    public static byte[] generateSaltByte(final int bytes) {
        final byte[] salt = new byte[bytes];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private static final int ITERATIONS = 4;
    private static final int MEMORY_LIMIT = 65336;
    private static final int HASH_LENGTH = 32;
    private static final int PARALLELISM = 1;

    /**
     * A function to encrypt a string in Argon2 and encode it using Base64.
     * This functions adds a safety feature and needs to be used carefully.
     * 16 bytes used, hash length of 32.
     *
     * @param password The plaintext password input.
     * @param salt a salt to use.
     * @return Hashed and encoded password.
     */
    public static String encryptArgon2(@NotNull String password, final byte[] salt) {
        // final byte[] salt = generateSaltByte(0x10);

        final Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(ITERATIONS)
                .withMemoryAsKB(MEMORY_LIMIT)
                .withParallelism(PARALLELISM)
                .withSalt(salt);
        final Argon2BytesGenerator generator = new Argon2BytesGenerator();

        generator.init(builder.build());
        final byte[] result = new byte[HASH_LENGTH];
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0x00, result.length);
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * A function to encrypt a string in Argon2 and encode it using Base64.
     * This functions adds a safety feature and needs to be used carefully.
     * 16 bytes used, hash length of 32.
     * Uses random salt.
     *
     * @param password The plaintext password input.
     * @return Hashed and encoded password.
     */
    public static String encryptArgon2(@NotNull String password) {
        return encryptArgon2(password, generateSaltByte(0x10));
    }

}
