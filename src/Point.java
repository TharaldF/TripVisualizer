/*************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *
 *  Implementation of 2D point using rectangular coordinates.
 *
 *************************************************************************/

public final class Point {
    private final double x;    // x-coordinate
    private final double y;    // y-coordinate

    // random point
    public Point() {
        x = Math.random();
        y = Math.random();
    }

    // point initialized from parameters
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // accessor methods
    public double x() { return x; }
    public double y() { return y; }
    public double r() { return Math.sqrt(x*x + y*y); }
    public double theta() { return Math.atan2(y, x); }

    // Euclidean distance between this point and that point
    public double distanceTo(Point that) {
        double dx = this.x - that.x;
        double dy = this.y - that.y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    // return a string representation of this point
    public String toString() {
        return "(" + x + ", " + y + ")";
    }



    // test client
    public static void main(String[] args) {
        Point p = new Point();
        System.out.println("p  = " + p);
        System.out.println("   x     = " + p.x());
        System.out.println("   y     = " + p.y());
        System.out.println("   r     = " + p.r());
        System.out.println("   theta = " + p.theta());
        System.out.println();

        Point q = new Point(0.5, 0.5);
        System.out.println("q  = " + q);
        System.out.println("dist(p, q) = " + p.distanceTo(q));
    }
}