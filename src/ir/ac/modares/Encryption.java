package ir.ac.modares;

import java.math.BigInteger;

/**
 * Created by noname on 6/10/19.
 */
public interface Encryption {

    void generateKeys();

    BigInteger encryption(BigInteger msg);

    BigInteger sign(BigInteger msg);

    BigInteger validateSignature(BigInteger msg);

    BigInteger decryption(BigInteger msg);

}
