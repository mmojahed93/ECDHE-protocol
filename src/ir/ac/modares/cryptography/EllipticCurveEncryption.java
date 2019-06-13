package ir.ac.modares.cryptography;

import ir.ac.modares.model.Point;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Mohammad Mahdi Mojahed (m.mojahed@modares.ac.ir) on 6/6/19.
 */

public class EllipticCurveEncryption {

    public static class PublicParams {
        private Point g;
        private BigInteger mod;
        private Point publicKey;
        private BigInteger a;

        public PublicParams(Point g, BigInteger mod, Point publicKey, BigInteger a) {
            this.g = g;
            this.mod = mod;
            this.publicKey = publicKey;
            this.a = a;
        }

        public Point getG() {
            return g;
        }

        public BigInteger getMod() {
            return mod;
        }

        public Point getPublicKey() {
            return publicKey;
        }

        public BigInteger getA() {
            return a;
        }
    }

//    int b = 0x64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1;
//    int gx = 0x188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012;
//    int gy = 0x07192b95ffc8da78631011ed6b24cdd573f977a11e794811;
//    int p = 6277101735386680763835789423207666416083908700390324961279;
//    int r = 6277101735386680763835789423176059013767194773182842284081;

//     y^2 = x^3 - 3*x + b (mod p)

    private final BigInteger a = BigInteger.valueOf(-3);
    private final BigInteger b = new BigInteger("2455155546008943817740293915197451784769108058161191238065");
    private final BigInteger mod = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
    private final BigInteger r = new BigInteger("6277101735386680763835789423176059013767194773182842284081");
    private final BigInteger gx = new BigInteger("602046282375688656758213480587526111916698976636884684818");
    private final BigInteger gy = new BigInteger("174050332293622031404857552280219410364023488927386650641");
    private final Point g = new Point(gx, gy);

    private BigInteger privateKey;
    private Point publicKey;
    private PublicParams publicParams;

    public EllipticCurveEncryption() {
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public Point getPublicKey() {
        return publicKey;
    }

    public PublicParams getPublicParams() {
        return publicParams;
    }

    public void generateKeys() {
        generatePrivateKey();
        generatePublicKey();
        setPublicParams();
    }

    public static BigInteger concatPublicParams(PublicParams publicParams) {
        if (publicParams.getPublicKey() == null || publicParams.getPublicKey().getX() == null || publicParams.getPublicKey().getY() == null) {
            System.out.println("Public key not valid!");
            return null;
        }

        String concatParams = String.valueOf(publicParams.getG().getX())
                + publicParams.getG().getY()
                + publicParams.getMod()
                + publicParams.getPublicKey().getX()
                + publicParams.getPublicKey().getY();

        return new BigInteger(concatParams);
    }

    private void generatePrivateKey() {
        BigInteger upperLimit = r.subtract(BigInteger.ONE); // a, b should be in {2, 3, ..., #Ep-1} (#Ep=r)
        this.privateKey = generateRandomNumber(BigInteger.ONE, upperLimit);
    }

    private void generatePublicKey() {
        BigInteger d = this.privateKey;
        Point p = this.g;
        this.publicKey = doubleAndAdd(d, p);
    }

    private void setPublicParams() {
        this.publicParams = new PublicParams(this.g, this.mod, this.publicKey, this.a);
    }

    private BigInteger generateRandomNumber(BigInteger upperLimit) {
        return generateRandomNumber(BigInteger.ZERO, upperLimit);
    }

    private BigInteger generateRandomNumber(BigInteger lowerLimit, BigInteger upperLimit) {
        Random randomSource = new Random();
        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(upperLimit.bitLength(), randomSource);
        } while (randomNumber.compareTo(lowerLimit) <= 0
                || randomNumber.compareTo(upperLimit) >= 0); // continue until randomNumber is bigger than lowerLimit and smaller than upperLimit

        return randomNumber;
    }

    public Point doubleAndAdd(BigInteger d, Point p) {

        String binaryD = d.toString(2);
        Point q = new Point(null, null);

        for (int i = 0; i < binaryD.length(); i++) {
            int b = Integer.parseInt(String.valueOf(binaryD.charAt(i)));
            q = addPoints(q, q);
            if (b == 1) {
                q = addPoints(p, q);
            }
        }

        return q;
    }

    private Point addPoints(Point point1, Point point2) {
        // Handle Neutral points
        if (point1.isNeutral()) {
            return point2;

        } else if (point2.isNeutral()) {
            return point1;
        }

        BigInteger s;

        if (point1.equals(point2)) {
            BigInteger b1 = point1.getX().pow(2).multiply(BigInteger.valueOf(3)).add(this.a).mod(this.mod);
            BigInteger b2 = point1.getY().multiply(BigInteger.valueOf(2)).mod(this.mod);
            BigInteger b3 = b1.multiply(b2.modPow(BigInteger.valueOf(-1), this.mod));
            s = b3.mod(this.mod);

        } else {
            BigInteger b1 = point2.getY().subtract(point1.getY()).mod(this.mod);
            BigInteger b2 = point2.getX().subtract(point1.getX()).mod(this.mod);
            BigInteger b3 = b1.multiply(b2.modPow(BigInteger.valueOf(-1), this.mod));
            s = b3.mod(this.mod);

        }

        BigInteger x3 = s.pow(2).subtract(point1.getX()).subtract(point2.getX()).mod(this.mod);
        BigInteger y3 = point1.getX().subtract(x3).multiply(s).subtract(point1.getY()).mod(this.mod);

        return new Point(x3, y3);
    }

}
