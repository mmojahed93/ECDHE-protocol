package ir.ac.modares;

import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Mohammad Mahdi Mojahed (m.mojahed@modares.ac.ir) on 6/7/19.
 */

public class RSA implements Encryption {

    private int pqBitLength = 1024;
    private float publicKeyRatio = (float) 0.5;

    private BigInteger privateKey;
    private BigInteger publicKey;
    private BigInteger mod;

    public RSA() {
    }

    public RSA(int pqBitLength) {
        this.pqBitLength = pqBitLength;
    }

    public RSA(int pqBitLength, float publicKeyRatio) {
        this.pqBitLength = pqBitLength;
        this.publicKeyRatio = publicKeyRatio;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public BigInteger getMod() {
        return mod;
    }

    @Override
    public void generateKeys() {
        long mills1 = System.currentTimeMillis();
        BigInteger p = findRandomPrime(pqBitLength);
        BigInteger q = findRandomPrime(pqBitLength);

        BigInteger n = p.multiply(q);
        BigInteger phN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = findGcdOne(phN); // Find relatively prime of phN
        BigInteger d = e.modPow(BigInteger.valueOf(-1), phN);

        this.privateKey = d;
        this.publicKey = e;
        this.mod = n;

        long mills2 = System.currentTimeMillis();
        long timeDuration = (mills2 - mills1);
        System.out.println("[RSA] Keys generated in " + timeDuration + "'ms " + (timeDuration / 1000) + "'s");
    }

    private BigInteger findGcdOne(BigInteger number) {
        int upperBitLength = (int) (number.bitLength() * this.publicKeyRatio);
        Random randomSource = new Random();
        BigInteger randomNumber;

        do {
            randomNumber = new BigInteger(upperBitLength, randomSource);

        } while (randomNumber.compareTo(number) != -1
                || randomNumber.compareTo(BigInteger.ONE) != 1
                || randomNumber.gcd(number).compareTo(BigInteger.ONE) != 0); // continue until gcd = 1 (1 < randomNumber < number)

        return randomNumber;
    }

    private BigInteger findRandomPrime(int bitLength) {
        Random randomSource = new Random();
        BigInteger randomNumber;

        do {
//            randomNumber = new BigInteger(bitLength, 3, randomSource); // BigInteger(int bitLength, int certainty, Random rnd), Constructs a randomly generated positive BigInteger that is probably prime, with the specified bitLength.
            randomNumber = new BigInteger(bitLength, randomSource);

        } while (randomNumber.bitLength() != bitLength
                || !isPrimeV1(randomNumber)); // continue until randomNumber is prime with proper bit len

        return randomNumber;
    }

    private boolean isPrimeV1(BigInteger number) {
        return isPrimeV1(number, 32);
    }

    private boolean isPrimeV1(BigInteger number, int certainty) {
        return isPrimeV1(number, certainty, false);
    }

    private boolean isPrimeV1(BigInteger number, int certainty, boolean completeCheck) {
        BigInteger two = new BigInteger("2");

        // Check 0 and 1
        if (number.compareTo(BigInteger.ONE) == 0 || number.compareTo(two) == 0) {
            return true;
        }

        // Check if even
        if (!two.equals(number) && BigInteger.ZERO.equals(number.mod(two))) {
            return false;
        }

        // Check via BigInteger.isProbablePrime(certainty), probability of false positive is: (1 - 2^certainty)
        if (!number.isProbablePrime(certainty)) {
            return false;
        }

        if (completeCheck) {
            BigInteger three = new BigInteger("3");
            // Find divisor if any from 3 to 'number'
            for (BigInteger i = three; i.multiply(i).compareTo(number) < 1; i = i.add(two)) { // Start from 3, 5, etc. the odd number, and look for a divisor if any
                if (BigInteger.ZERO.equals(number.mod(i))) { // Check if 'i' is divisor of 'number'
                    return false;
                }
            }
        }

        return true;
    }


    private boolean isPrimeV2(BigInteger number) {
        // Check via BigInteger.isProbablePrime(certainty)
        if (!number.isProbablePrime(10)) {
            return false;
        }

        BigInteger two = new BigInteger("2");

        // Check 0 and 1
        if (number.compareTo(BigInteger.ONE) == 0 || number.compareTo(two) == 0) {
            return true;
        }

        // Check if even
        if (!two.equals(number) && BigInteger.ZERO.equals(number.mod(two))) {
            return false;
        }

        BigInteger three = new BigInteger("3");

        BigInteger root = appxRoot(number);
        for (BigInteger i = three; i.compareTo(root) <= 0; i = i.nextProbablePrime()) {
            if (BigInteger.ZERO.equals(number.mod(i))) {
                return false;
            }
        }

        return true;
    }

    private BigInteger appxRoot(final BigInteger n) {
        BigInteger half = n.shiftRight(1);
        while (half.multiply(half).compareTo(n) > 0) {
            half = half.shiftRight(1);
        }
        return half.shiftLeft(1);
    }

    @Override
    public BigInteger encrypt(BigInteger msg) {
        BigInteger hashedMessage = hash(msg);
        return hashedMessage.modPow(getPublicKey(), getMod());
    }

    @Override
    public BigInteger decrypt(BigInteger encryptedMsg) {
        return encryptedMsg.modPow(getPrivateKey(), getMod());
    }

    @Override
    public BigInteger sign(BigInteger msg) {
        BigInteger hashedMessage = hash(msg);
        return hashedMessage.modPow(getPrivateKey(), getMod());
    }

    @Override
    public BigInteger validateSignature(BigInteger encryptedMsg) {
        return encryptedMsg.modPow(getPublicKey(), getMod());
    }

    @Override
    public boolean checkEncryption(BigInteger msg, BigInteger encryptedMsg) {
        BigInteger hashedMessage = hash(msg);
        BigInteger decryptedMessage = decrypt(encryptedMsg);
        return hashedMessage.equals(decryptedMessage);
    }

    @Override
    public boolean checkSignature(BigInteger msg, BigInteger encryptedMsg) {
        BigInteger hashedMessage = hash(msg);
        BigInteger decryptedMessage = validateSignature(encryptedMsg);
        return hashedMessage.equals(decryptedMessage);
    }

    @Override
    public BigInteger hash(BigInteger msg) {
        byte[] hashMessageByteArr = DigestUtils.sha256(msg.toByteArray());
        return new BigInteger(hashMessageByteArr);
    }
}
