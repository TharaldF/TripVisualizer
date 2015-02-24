import java.awt.*;

/*************************************************************************
 *  Compilation:  javac Polygon.java
 *  Execution:    java Polygon
 *  Dependencies: Point2D.java
 *
 *  Implementation of 2D polygon, possibly intersecting.
 *
 *
 *  Original Obtained from Princeton University algs4 library
 *  This version has been altered from the original
 *************************************************************************/

public class Polygon {
    private int N;        // number of points in the polygon
    private Point2D[] a;    // the points, setting points[0] = points[N]
    private String name;
    private Color color;

    // default buffer = 4
    public Polygon(String state, String colour) {
        N = 0;
        a = new Point2D[4];
        name = state;
        color = Color.decode(colour);

    }


    // double size of array
    private void resize() {
        Point2D[] temp = new Point2D[2*N+1];
        for (int i = 0; i <= N; i++) temp[i] = a[i];
        a = temp;
    }

    // return size
    public int size() { return N; }

    // draw polygon
    public void draw() {
        for (int i = 0; i < N; i++)
            a[i].drawTo(a[i+1]);
    }


    // add point p to end of polygon
    public void add(Point2D p) {
        if (N >= a.length - 1) resize();   // resize array if needed
        a[N++] = p;                        // add point
        a[N] = a[0];                       // close polygon
    }

    // return the perimeter
    public double perimeter() {
        double sum = 0.0;
        for (int i = 0; i < N; i++)
            sum = sum + a[i].distanceTo(a[i+1]);
        return sum;
    }

    public Iterable<Point2D> getAll(){
        Stack<Point2D> stack = new Stack<Point2D>();
        for (int i = 0; i < N; i++){
            stack.push(a[i]);
        }
        return stack;

    }

    // return signed area of polygon
    public double area() {
        double sum = 0.0;
        for (int i = 0; i < N; i++) {
            sum = sum + (a[i].x() * a[i+1].y()) - (a[i].y() * a[i+1].x());
        }
        return 0.5 * sum;
    }

    // does this Polygon contain the point p?
    // if p is on boundary then 0 or 1 is returned, and p is in exactly one point of every partition of plane
    // Reference: http://exaflop.org/docs/cgafaq/cga2.html
    public boolean contains2(Point2D p) {
        int crossings = 0;
        for (int i = 0; i < N; i++) {
            int j = i + 1;
            boolean cond1 = (a[i].y() <= p.y()) && (p.y() < a[j].y());
            boolean cond2 = (a[j].y() <= p.y()) && (p.y() < a[i].y());
            if (cond1 || cond2) {
                // need to cast to double
                if (p.x() < (a[j].x() - a[i].x()) * (p.y() - a[i].y()) / (a[j].y() - a[i].y()) + a[i].x())
                    crossings++;
            }
        }
        if (crossings % 2 == 1) return true;
        else                    return false;
    }

    // does this Polygon contain the point p?
    // Reference: http://softsurfer.com/Archive/algorithm_0103/algorithm_0103.htm
    public boolean contains(Point2D p) {
        int winding = 0;
        for (int i = 0; i < N; i++) {
            int ccw = Point2D.ccw(a[i], a[i+1], p);
            if (a[i+1].y() >  p.y() && p.y() >= a[i].y())  // upward crossing
                if (ccw == +1) winding++;
            if (a[i+1].y() <= p.y() && p.y() <  a[i].y())  // downward crossing
                if (ccw == -1) winding--;
        }
        return winding != 0;
    }


    // return string representation of this point
    public String toString() {
        if (N == 0) return "[ ]";
        String s = "";
        s = s + "[ ";
        for (int i = 0; i <= N; i++)
            s = s + a[i] + " ";
        s = s + "]";
        return s;
    }

    public String getName(){
        return name;
    }

    public Point2D centroid(){
        double cX = 0;
        double cY = 0;
        for (int i = 0; i <= N; i++){
            cX = cX + a[i].x();
            cY = cY + a[i].y();
        }
        cX = cX/N;
        cY = cY/N;
        Point2D rPoint =new Point2D(cX,cY);
        return rPoint;
    }

    public Color color(){
        return color;
    }


    // test client
    public static void main(String[] args) {
        int N = 10;

        // a square
        Polygon poly = new Polygon("STATE", "#ff0000");
        poly.add(new Point2D(5, 5));
        poly.add(new Point2D(9, 5));
        poly.add(new Point2D(9, 9));
        poly.add(new Point2D(5, 9));

        System.out.println("polygon    = " + poly);
        System.out.println("perimeter  = " + poly.perimeter());
        System.out.println("area       = " + poly.area());

        System.out.println("contains(5, 5) = " + poly.contains(new Point2D(5, 5)));
        System.out.println("contains(9, 5) = " + poly.contains(new Point2D(9, 5)));
        System.out.println("contains(9, 9) = " + poly.contains(new Point2D(9, 9)));
        System.out.println("contains(5, 9) = " + poly.contains(new Point2D(5, 9)));
        System.out.println("contains(7, 5) = " + poly.contains(new Point2D(7, 5)));
        System.out.println("contains(5, 7) = " + poly.contains(new Point2D(5, 7)));
        System.out.println("contains(7, 9) = " + poly.contains(new Point2D(7, 9)));
        System.out.println("contains(9, 7) = " + poly.contains(new Point2D(9, 7)));

        // generate N random points in the unit square and check what fraction are in the polygon
        int yes = 0;
        for (int i = 0; i < N; i++) {
            int x = (int) (10 * Math.random());
            int y = (int) (10 * Math.random());
            Point2D p = new Point2D(x, y);
            if (poly.contains(p)) yes++;
            if (poly.contains(p) != poly.contains2(p)) System.out.println("different " + p);
        }

        // true answer is = 0.16 (depends on how boundary points are handled)
        System.out.println("Fraction in polygon = " + 1.0 * yes / N);


    }
}