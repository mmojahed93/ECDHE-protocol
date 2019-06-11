package ir.ac.modares;

import java.math.BigInteger;


/**
 * Created by Mohammad Mahdi Mojahed (m.mojahed@modares.ac.ir) on 6/10/19.
 */
public class DHE {

    private Point signedECEPublicKey;
    private BigInteger ecePrivateKey;
    private Point ecePublicKey;
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

    public void setParticipantSignedECPublicKey(Point participantSignedECPublicKey) {
        this.participantSignedECEPublicKey = participantSignedECPublicKey;
    }

    public void setParticipantPublicKey(RSA.RSAPublicKey participantRSAPublicKey) {
        this.participantRSAPublicKey = participantRSAPublicKey;
    }

    public Point getSignedECEPublicKey() {
        EllipticCurveEncryption ellipticCurveEncryption = new EllipticCurveEncryption();
        ecePrivateKey = ellipticCurveEncryption.getPrivateKey();
        ecePublicKey = ellipticCurveEncryption.getPublicKey();

        RSA rsa = new RSA();
        rsa.generateKeys();
        BigInteger encryptedX = rsa.sign(ecePublicKey.getX());
        BigInteger encryptedY = rsa.sign(ecePublicKey.getY());
        signedECEPublicKey = new Point(encryptedX, encryptedY);

        return signedECEPublicKey;
    }

    public Point getSharedKey() throws Exception {
        BigInteger psx = participantSignedECEPublicKey.getX();
        BigInteger psy = participantSignedECEPublicKey.getY();

        BigInteger px = participantECEPublicKey.getX();
        BigInteger py = participantECEPublicKey.getY();

        RSA rsa = new RSA();
        rsa.setPublicKey(participantRSAPublicKey);
        boolean isSignatureValid = rsa.checkSignature(px, psx) && rsa.checkSignature(py, psy);
        if (!isSignatureValid) {
            throw new Exception("Participant ECE public key not valid! Signature verification failed :(");
        }

        BigInteger sharedKeyX = ecePrivateKey.multiply(px);
        BigInteger sharedKeyY = ecePrivateKey.multiply(py);
        this.sharedKey = new Point(sharedKeyX, sharedKeyY);

        return sharedKey;
    }

}
