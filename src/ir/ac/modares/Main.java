package ir.ac.modares;

import ir.ac.modares.cryptography.DHE;
import ir.ac.modares.cryptography.RSA;
import ir.ac.modares.model.Point;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by
 * Hafmgh (hafmgh@riseup.net)
 * Mohammad Mahdi Mojahed (m.mojahed@modares.ac.ir)
 * on 6/7/19.
 */

public class Main {
    public static void main(String[] args) {

        // todo add jUnit test

        ////////////////////////
        // TEST RSA
        ////////////////////////
//        testRSA();

        testDHE();

//        BigInteger b1 = BigInteger.valueOf(20);
//        BigInteger b2 = BigInteger.valueOf(3);
////        System.out.println(b1.gcd(b2));
//        System.out.println(b1.modPow(BigInteger.valueOf(-1), b2));
//        System.out.println(gcdExtended(b1, b2));

    }

    private static void testRSA() {
        int count = 0;
        for (int i = 0; i < 100; i++) {
            RSA rsa = new RSA(1024);
            rsa.generateKeys();
            BigInteger msg = BigInteger.valueOf(new Random().nextInt());
            BigInteger encryptedMessage = rsa.encrypt(msg);
            BigInteger decryptedMessage = rsa.decrypt(encryptedMessage);

            boolean check = rsa.checkEncryption(msg, encryptedMessage);
            if (!check) {
                System.out.println("[testRSA] RSA encryption/decryption FAILED!");
                System.out.println("[testRSA] encryptedMessage: " + encryptedMessage);
                System.out.println("[testRSA] decryptedMessage: " + decryptedMessage);
                count++;
                System.out.println("I: " + i);
            }
        }
        System.out.println("[testRSA] Count of failure: " + count);
    }

//    // extended Euclidean Algorithm
//    public static int gcdExtended(int a, int b, int x, int y) {
//        // Base Case
//        if (a == 0) {
//            x = 0;
//            y = 1;
//            return b;
//        }
//
//        int x1 = 1, y1 = 1; // To store results of recursive call
//        int gcd = gcdExtended(b % a, a, x1, y1);
//
//        // Update x and y using results of recursive
//        // call
//        x = y1 - (b / a) * x1;
//        y = x1;
//
//        return gcd;
//    }

    public static BigInteger gcdExtended(BigInteger a, BigInteger b) {
        BigInteger nextR;
        BigInteger prevR = b;
        BigInteger prevPrevR = a;

        BigInteger nextS;
        BigInteger prevS = BigInteger.ZERO;
        BigInteger prevPrevS = BigInteger.ONE;


        BigInteger nextT;
        BigInteger prevT = BigInteger.ONE;
        BigInteger prevPrevT = BigInteger.ZERO;


        do {
            BigInteger quotient = prevPrevR.divide(prevR);
//            nextR = prevPrevR.mod(prevR);
            nextR = prevPrevR.subtract(quotient.multiply(prevR));
            prevPrevR = prevR;
            prevR = nextR;

            nextS = prevPrevS.subtract(quotient.multiply(prevS));
            prevPrevS = prevS;
            prevS = nextS;

            nextT = prevPrevT.subtract(quotient.multiply(prevT));
            prevPrevT = prevT;
            prevT = nextT;
        }
        while (nextR.compareTo(BigInteger.ZERO) != 0);

//        return prevPrevR;
        return prevPrevS;
    }

    private static void testDHE() {
        long startTime = System.currentTimeMillis();
        System.out.println("[testDHE] Start at: " + startTime);

        DHE saberDHE = new DHE();
        saberDHE.generateKeys();

        DHE mohammadDHE = new DHE();
        mohammadDHE.generateKeys();

        Point saberSharedKey = createUserSharedKey(saberDHE, mohammadDHE);
        Point mohammadSharedKey = createUserSharedKey(mohammadDHE, saberDHE);

        if (saberSharedKey != null && mohammadSharedKey != null) {
            if (saberSharedKey.equals(mohammadSharedKey)) {
                System.out.println("[testDHE] ********** DHE shared key generated successfully :) **********");
                System.out.println("[testDHE] SharedKeyX: " + mohammadSharedKey.getX() + " SharedKeyY: " + mohammadSharedKey.getY());

            } else {
                System.out.println("[testDHE] Oops, DHE shared key generation FAILED :(");

            }

        } else {
            System.out.println("[testDHE] Something went wrong, DHE shared key generation FAILED :(");
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("[testDHE] End at: " + endTime);
        System.out.println("[testDHE] Duration: " + duration + "'ms " + duration / 1000 + "'s");
    }

    private static Point createUserSharedKey(DHE userDHE, DHE participantDHE) {
        userDHE.setParticipantRSAPublicKey(participantDHE.getRsaPublicKey());
        userDHE.setParticipantSignedPublicParams(participantDHE.getSignedPublicParams());
        userDHE.setParticipantPublicParams(participantDHE.getPublicParams());
        Point sharedKey = null;
        try {
            sharedKey = userDHE.createSharedKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sharedKey;
    }

//    private static void checkSlideCase() {
//        EllipticCurveEncryption ellipticCurveEncryption1 = new EllipticCurveEncryption();
//        ellipticCurveEncryption1.setPrivateKey(BigInteger.valueOf(5));
//        ellipticCurveEncryption1.generateKeys();
//
//        EllipticCurveEncryption ellipticCurveEncryption2 = new EllipticCurveEncryption();
//        ellipticCurveEncryption2.setPrivateKey(BigInteger.valueOf(12));
//        ellipticCurveEncryption2.generateKeys();
//
//        System.out.println("1 => X: " + ellipticCurveEncryption1.getPublicKey().getX());
//        System.out.println("1 => Y: " + ellipticCurveEncryption1.getPublicKey().getY());
//
//        System.out.println("2 => X: " + ellipticCurveEncryption2.getPublicKey().getX());
//        System.out.println("2 => Y: " + ellipticCurveEncryption2.getPublicKey().getY());
//
//    }

}
