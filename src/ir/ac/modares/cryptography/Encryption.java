package ir.ac.modares.cryptography;

import java.math.BigInteger;

/**
 * Created by noname on 6/10/19.
 */
public interface Encryption {

    void generateKeys();

    BigInteger encrypt(BigInteger msg);

    BigInteger decrypt(BigInteger encryptedMsg);

    BigInteger sign(BigInteger msg);

    BigInteger validateSignature(BigInteger encryptedMsg);

    boolean checkEncryption(BigInteger msg, BigInteger encryptedMsg);

    boolean checkSignature(BigInteger msg, BigInteger encryptedMsg);

    BigInteger hash(BigInteger msg);

}
