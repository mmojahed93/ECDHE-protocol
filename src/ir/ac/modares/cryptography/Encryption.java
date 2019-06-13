package ir.ac.modares.cryptography;

import java.math.BigInteger;

/**
 * Created by
 * Hafmgh (hafmgh@riseup.net)
 * Mohammad Mahdi Mojahed (m.mojahed@modares.ac.ir)
 * on 6/7/19.
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
