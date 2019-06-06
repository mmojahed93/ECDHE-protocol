package ir.ac.modares;

import java.math.BigInteger;

/**
 * Created by Mohammad Mahdi Mojahed (m.mojahed@modares.ac.ir) on 6/6/19.
 */

public class Point {

    private BigInteger x;
    private BigInteger y;

    public Point() {
    }

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public void setX(BigInteger x) {
        this.x = x;
    }

    public BigInteger getY() {
        return y;
    }

    public void setY(BigInteger y) {
        this.y = y;
    }

    public boolean equals(Point point) {
        return this.x.equals(point.getX()) && this.y.equals(point.getY());
    }

    public boolean isNeutral(){
        return this.getX().equals(BigInteger.ZERO) && this.getY().equals(BigInteger.ZERO);
    }
}
