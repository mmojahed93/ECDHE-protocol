package ir.ac.modares;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Mohammad Mahdi Mojahed (m.mojahed@modares.ac.ir) on 6/6/19.
 */

public class Main {
    public static void main(String[] args) {

        // todo add jUnit test

        ////////////////////////
        // TEST RSA
        ////////////////////////
//        testRSA();

        testDHE();

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
                System.out.println("DHE shared key generated successfully :)");
                System.out.println("SharedKeyX: " + mohammadSharedKey.getX() + " SharedKeyY: " + mohammadSharedKey.getY());

            } else {
                System.out.println("Oops, DHE shared key generation FAILED :(");

            }

        } else {
            System.out.println("Something went wrong, DHE shared key generation FAILED :(");
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
}
