package org.acme.model;

public class CorpsCeleste {
    public double x, y;
    public double vx, vy;
    public double masse;

    public CorpsCeleste() {}

    public CorpsCeleste(double x, double y, double vx, double vy, double masse) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.masse = masse;
    }
}
