package ir.ac.modares.cryptography;

import ir.ac.modares.model.Point;

import java.math.BigInteger;


/**
 * Created by
 * Hafmgh (hafmgh@riseup.net)
 * Mohammad Mahdi Mojahed (m.mojahed@modares.ac.ir)
 * on 6/7/19.
 */

public class DHE {

    private EllipticCurveEncryption ellipticCurveEncryption;
    private EllipticCurveEncryption.PublicParams publicParams;
    private BigInteger signedPublicParams;

    private BigInteger ecePrivateKey;

    private RSA.RSAPublicKey rsaPublicKey;

    private Point sharedKey;

    private BigInteger participantSignedPublicParams;
    private EllipticCurveEncryption.PublicParams participantPublicParams;
    private RSA.RSAPublicKey participantRSAPublicKey;

    public DHE() {
    }

    public DHE(BigInteger participantSignedPublicParams, EllipticCurveEncryption.PublicParams participantPublicParams, RSA.RSAPublicKey participantRSAPublicKey) {
        this.participantSignedPublicParams = participantSignedPublicParams;
        this.participantPublicParams = participantPublicParams;
        this.participantRSAPublicKey = participantRSAPublicKey;
    }

    public void setParticipantPublicParams(EllipticCurveEncryption.PublicParams participantPublicParams) {
        this.participantPublicParams = participantPublicParams;
    }

    public void setParticipantSignedPublicParams(BigInteger participantSignedPublicParams) {
        this.participantSignedPublicParams = participantSignedPublicParams;
    }

    public void setParticipantRSAPublicKey(RSA.RSAPublicKey participantRSAPublicKey) {
        this.participantRSAPublicKey = participantRSAPublicKey;
    }

    public EllipticCurveEncryption.PublicParams getPublicParams() {
        return publicParams;
    }

    public BigInteger getSignedPublicParams() {
        return this.signedPublicParams;
    }

    public Point getSharedKey() {
        return sharedKey;
    }

    public RSA.RSAPublicKey getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void generateKeys() {
        this.ellipticCurveEncryption = new EllipticCurveEncryption();
        ellipticCurveEncryption.generateKeys();
        this.publicParams = ellipticCurveEncryption.getPublicParams();

        ecePrivateKey = ellipticCurveEncryption.getPrivateKey();

        RSA rsa = new RSA();
        rsa.generateKeys();
        this.rsaPublicKey = rsa.getPublicKey();

        signedPublicParams = rsa.sign(EllipticCurveEncryption.concatPublicParams(publicParams));
    }

    public Point createSharedKey() throws Exception {

        RSA rsa = new RSA();
        rsa.setPublicKey(participantRSAPublicKey);

        BigInteger participantConcatPublicParams = EllipticCurveEncryption.concatPublicParams(participantPublicParams);
        boolean isSignatureValid = rsa.checkSignature(participantConcatPublicParams, participantSignedPublicParams);

        if (!isSignatureValid) {
            throw new Exception("Participant ECE public key not valid! Signature verification failed :(");
        }

        this.sharedKey = ellipticCurveEncryption.doubleAndAdd(ecePrivateKey, participantPublicParams.getPublicKey());

        return sharedKey;
    }

}
