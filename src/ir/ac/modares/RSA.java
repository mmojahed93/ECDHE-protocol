package ir.ac.modares;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Mohammad Mahdi Mojahed (m.mojahed@modares.ac.ir) on 6/7/19.
 */

public class RSA {

    private BigInteger p;
    private BigInteger q;

    public RSA() {
        long mills1 = System.currentTimeMillis();
        this.p = this.q = findRandomNumber(100);
//        this.q = findRandomNumber(256);

        long mills2 = System.currentTimeMillis();
        long timeDuration = (mills2 - mills1);
        System.out.println("Time duration in: " + timeDuration + "'ms " + (timeDuration / 1000) + "'s");

    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }

    private BigInteger findRandomNumber(int bitLength) {
        Random randomSource = new Random();
        BigInteger randomNumber;
        do {
//            randomNumber = new BigInteger(bitLength, 3, randomSource); // BigInteger(int bitLength, int certainty, Random rnd), Constructs a randomly generated positive BigInteger that is probably prime, with the specified bitLength.
            randomNumber = new BigInteger(bitLength, randomSource);
            if (randomNumber.bitLength() < bitLength) {
                System.out.println("[findRandomNumber] Bit len is small, len: " + randomNumber.bitLength());
                continue;
            }

        } while (!isPrime(randomNumber)); // continue until randomNumber is prime

        return randomNumber;
    }

//    public boolean isPrime(BigInteger number) {
//        // Check via BigInteger.isProbablePrime(certainty)
//        if (!number.isProbablePrime(5)) {
//            return false;
//        }
//
//        System.out.println("[isPrime] 2 number: " + number);
//
//        // Check if even
//        BigInteger two = new BigInteger("2");
//        if (!two.equals(number) && BigInteger.ZERO.equals(number.mod(two))) {
//            return false;
//        }
//
//        System.out.println("[isPrime] 3 number: " + number);
//
//        // Find divisor if any from 3 to 'number'
//        for (BigInteger i = new BigInteger("3"); i.multiply(i).compareTo(number) < 1; i = i.add(two)) { // Start from 3, 5, etc. the odd number, and look for a divisor if any
//            if (BigInteger.ZERO.equals(number.mod(i))) { // Check if 'i' is divisor of 'number'
//                return false;
//            }
//        }
//
//        return true;
//    }


    private boolean isPrime(BigInteger number) {

        if (!number.isProbablePrime(10)) {
            return false;
        }

        BigInteger two = new BigInteger("2");
        if (number.compareTo(BigInteger.ONE) == 0 || number.compareTo(two) == 0) {
            return true;
        }

        BigInteger root = appxRoot(number);
        System.out.println("Using approximate root: " + root);

        BigInteger three = new BigInteger("3");
        int cnt = 0;
        for (BigInteger i = three; i.compareTo(root) <= 0; i = i.nextProbablePrime()) {
            cnt++;
            if (cnt % 1000 == 0) {
                System.out.println(cnt + " Using next prime " + i);
            }
            if (number.mod(i).equals(BigInteger.ZERO)) {
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
}
