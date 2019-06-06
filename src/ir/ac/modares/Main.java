package ir.ac.modares;

import java.math.BigInteger;

/**
 * Created by Mohammad Mahdi Mojahed (m.mojahed@modares.ac.ir) on 6/6/19.
 */

public class Main {
    public static void main(String[] args) {

        DHE dhe = new DHE();
        BigInteger privateKey = dhe.getPrivateKey();
        Point publicKey = dhe.getPublicKey();
        System.out.println("privateKey: " + privateKey);
        System.out.println("publicKeyX: " + publicKey.getX());
        System.out.println("publicKeyY: " + publicKey.getY());
    }

}
