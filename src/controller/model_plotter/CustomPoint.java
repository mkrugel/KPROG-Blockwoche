package controller.model_plotter;

/** A custom point model class */
public class CustomPoint {

    public int x;
    public double y;

    public CustomPoint(int xValue, double yValue) {
        this.x = xValue;
        this.y = yValue;
    }

    public int getX() {
        return this.x;
    }
    public int getIntY() {
        return (int) this.y;
    }
    public double getY() {
        return this.y;
    }


    @Override
    public String toString() {
        return "x: " + this.x + "; y: " + this.y;
    }
}
