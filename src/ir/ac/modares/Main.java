package ir.ac.modares;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Mohammad Mahdi Mojahed (m.mojahed@modares.ac.ir) on 6/6/19.
 */

public class Main {
    public static void main(String[] args) {

        // todo add jUnit test
        testDHE();

    }

    private static void testDHE() {
        DHE saberDHE = new DHE();
        saberDHE.generateKeys();

        DHE mohammadDHE = new DHE();
        mohammadDHE.generateKeys();

        saberDHE.setParticipantRSAPublicKey(mohammadDHE.getRsaPublicKey());
        saberDHE.setParticipantSignedECPublicKey(mohammadDHE.getSignedECEPublicKey());
        saberDHE.setParticipantECEPublicKey(mohammadDHE.getEcePublicKey());
        Point saberSharedKey = null;
        try {
            saberSharedKey = saberDHE.getSharedKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mohammadDHE.setParticipantRSAPublicKey(saberDHE.getRsaPublicKey());
        mohammadDHE.setParticipantSignedECPublicKey(saberDHE.getSignedECEPublicKey());
        mohammadDHE.setParticipantECEPublicKey(saberDHE.getEcePublicKey());
        Point mohammadSharedKey = null;
        try {
            mohammadSharedKey = mohammadDHE.getSharedKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (saberSharedKey != null && mohammadSharedKey != null) {
            System.out.println("saberSharedKeyX: " + saberSharedKey.getX() + " saberSharedKeyY: " + saberSharedKey.getY());
            System.out.println("mohammadSharedKeyX: " + mohammadSharedKey.getX() + " mohammadSharedKeyY: " + mohammadSharedKey.getY());
            System.out.println("equals: " + saberSharedKey.equals(mohammadSharedKey));
        } else {
            System.out.println("Generate shared key failed :(");
        }
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
//            BigInteger decryptedMessage = rsa.decrypt(encryptedMessage);

            boolean check = rsa.checkEncryption(msg, encryptedMessage);
            if (!check) {
//                System.out.println("encryptedMessage: " + encryptedMessage);
//                System.out.println("decryptedMessage: " + decryptedMessage);
//                System.out.println("Checked: " + check);
                count++;
                System.out.println("I: " + i);
            }
        }
        System.out.println("count: " + count);
    }
}
