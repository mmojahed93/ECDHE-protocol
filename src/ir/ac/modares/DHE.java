package ir.ac.modares;

import java.math.BigInteger;


/**
 * Created by Mohammad Mahdi Mojahed (m.mojahed@modares.ac.ir) on 6/10/19.
 */
public class DHE {

    private Point signedECEPublicKey;
    private BigInteger ecePrivateKey;
    private Point ecePublicKey;

    private EllipticCurveEncryption ellipticCurveEncryption;
    private BigInteger rsaPrivateKey;
    private RSA.RSAPublicKey rsaPublicKey;

    private Point sharedKey;

    private Point participantSignedECEPublicKey;
    private Point participantECEPublicKey;
    private RSA.RSAPublicKey participantRSAPublicKey;

    public DHE() {
    }

    public DHE(Point participantSignedECPublicKey, Point participantECEPublicKey, RSA.RSAPublicKey participantRSAPublicKey) {
        this.participantSignedECEPublicKey = participantSignedECPublicKey;
        this.participantECEPublicKey = participantECEPublicKey;
        this.participantRSAPublicKey = participantRSAPublicKey;
    }

    public void setParticipantECEPublicKey(Point participantECEPublicKey) {
        this.participantECEPublicKey = participantECEPublicKey;
    }

    public void setParticipantSignedECPublicKey(Point participantSignedECPublicKey) {
        this.participantSignedECEPublicKey = participantSignedECPublicKey;
    }

    public void setParticipantRSAPublicKey(RSA.RSAPublicKey participantRSAPublicKey) {
        this.participantRSAPublicKey = participantRSAPublicKey;
    }

    public Point getSignedECEPublicKey() {
        return this.signedECEPublicKey;
    }

    public Point getEcePublicKey() {
        return ecePublicKey;
    }

    public RSA.RSAPublicKey getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void generateKeys() {
        ellipticCurveEncryption = new EllipticCurveEncryption();
        ellipticCurveEncryption.generateKeys();
        ecePrivateKey = ellipticCurveEncryption.getPrivateKey();
        ecePublicKey = ellipticCurveEncryption.getPublicKey();

        RSA rsa = new RSA();
        rsa.generateKeys();
        this.rsaPrivateKey = rsa.getPrivateKey();
        this.rsaPublicKey = rsa.getPublicKey();

        BigInteger encryptedX = rsa.sign(ecePublicKey.getX());
        BigInteger encryptedY = rsa.sign(ecePublicKey.getY());
        signedECEPublicKey = new Point(encryptedX, encryptedY);
    }

    public Point getSharedKey() throws Exception {
        BigInteger psx = participantSignedECEPublicKey.getX();
        BigInteger psy = participantSignedECEPublicKey.getY();

        BigInteger px = participantECEPublicKey.getX();
        BigInteger py = participantECEPublicKey.getY();

        // todo implement checkSignature with point
        RSA rsa = new RSA();
        rsa.setPublicKey(participantRSAPublicKey);
        boolean isSignatureValid = rsa.checkSignature(px, psx) && rsa.checkSignature(py, psy);
        if (!isSignatureValid) {
            throw new Exception("Participant ECE public key not valid! Signature verification failed :(");
        }

        this.sharedKey = ellipticCurveEncryption.doubleAndAdd(ecePrivateKey, participantECEPublicKey);

        return sharedKey;
    }

}
