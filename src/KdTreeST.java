import java.awt.geom.*;
import java.util.*;

public class KdTreeST<Value>
{
    // flags for red or blue lines
    private static final boolean RED = true;
    private static final boolean BLUE = false;

    private int size; // number of nodes in tree
    private boolean isEmpty; // is the tree empty?
    private Node root; // root node

    // Node data type in a 2d-tree
    private class Node
    {
        private Point2D p; // the point
        private Value value; // the symbol table maps the point to this value
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree
        private boolean color; // true = RED or false = BLUE
    }

    // construct an empty symbol table of points
    public KdTreeST()
    {
        root = new Node();
        // root.p = new Point2D(null, null);
        root.value = null;
        // root.rect = new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 
        //                      Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        root.rect = new RectHV(-1000, -1000, 1000, 1000);
        root.lb = null;
        root.rt = null;
        size = 0;
        isEmpty = true;
    }

    // is the symbol table empty?
    public boolean isEmpty()
    {
        return isEmpty;
    }

    // number of points in the ST
    public int size()
    {
        return size;
    }

    // add the point p to the ST or if it already exists, update
    public void insert(Point2D p, Value v)
    {
        // is this the first node to be inserted in the tree?
        if (isEmpty())
        {
            isEmpty = false;
            root.p = p;
            root.value = v;
            root.color = RED;
            size++;
            return;
        }
        // call private helper insert method
        insertR(root, p, v);
    }

    // private helper method to insert new node into ST
    private void insertR(Node prev, Point2D point, Value value)
    {
        // checks if same point
        if (point.compareTo(prev.p) == 0)
        {
            prev.value = value; // update value
            return;
        }

        if (prev.color == RED) // color of parent node is red
        {
            if (xGreaterThan(point, prev.p)) // node will go right
            {
                if (prev.rt == null) // at an empty place in the tree
                {
                    // create the new node
                    prev.rt = makeChild(prev, point, value, true);
                    return;
                }
                insertR(prev.rt, point, value); // recursively insert the node
            }
            else
            {
                // at an empty place in the tree
                if (prev.lb == null)
                {
                    // create the new node
                    prev.lb = makeChild(prev, point, value, false);
                    return;
                }
                insertR(prev.lb, point, value); // recursively insert the node
            }
        }

        else // color of parent node is blue
        {
            if (yGreaterThan(point, prev.p)) // node will go right
            {
                if (prev.rt == null) // at an empty place in the tree
                {
                    // create the new node
                    prev.rt = makeChild(prev, point, value, true);
                    return;
                }
                insertR(prev.rt, point, value); // recursively insert the node
            }
            else
            {
                // at an empty place in the tree
                if (prev.lb == null)
                {
                    // create the new node
                    prev.lb = makeChild(prev, point, value, false);
                    return;
                }
                insertR(prev.lb, point, value); // recursively insert the node
            }
        }
    }

    // helper function to create a new Node to be inserted
    private Node makeChild(Node prev, Point2D p, Value v, boolean lr)
    {
        Node child = new Node();
        child.p = p;
        child.value = v;
        child.rect = prev.rect;

        if (prev.color == RED) // y-determined coordinates -> x-determined rectangle
        {
            if (lr) // greater than; going right
                child.rect = new RectHV(prev.p.x(), prev.rect.ymin(),
                        prev.rect.xmax(), prev.rect.ymax());
            else // less than; going left
                child.rect = new RectHV(prev.rect.xmin(), prev.rect.ymin(),
                        prev.p.x(), prev.rect.ymax());
            child.color = BLUE;
        }
        else // x-determined coordinates -> y-determined rectangle
        {
            if (lr) // greater than; going right
                child.rect = new RectHV(prev.rect.xmin(), prev.p.y(),
                        prev.rect.xmax(), prev.rect.ymax());
            else // less than; going left
                child.rect = new RectHV(prev.rect.xmin(), prev.rect.ymin(),
                        prev.rect.xmax(), prev.p.y());
            child.color = RED;
        }
        child.lb = null;
        child.rt = null;
        size++;
        return child;
    }

    // if point a's x-coord < point b's x-coord, move left; else move right
    private boolean xGreaterThan(Point2D a, Point2D b)
    {
        if (a.x() < b.x()) return false; // go left
        return true; // go right
    }

    // if point a's y-coord < point b's y-coord, move left; else move right
    private boolean yGreaterThan(Point2D a, Point2D b)
    {
        if (a.y() < b.y()) return false; // go left
        return true; // go right
    }

    // returns value mapped to by p
    public Value get(Point2D p)
    {
        // if coordinates are equal, return value
        if ((root.p.x() == p.x()) && (root.p.y() == p.y())) return root.value;

        Node n = root;
        while (n != null)
        {
            // if coordinates are equal, return value
            if ((n.p.x() == p.x()) && (n.p.y() == p.y())) return n.value;
            if (n.color == RED)
            {
                if (xGreaterThan(p, n.p)) n = n.rt; // go right
                else n = n.lb; // go left
            }
            else
            {
                if (yGreaterThan(p, n.p)) n = n.rt; // go right
                else n = n.lb; // go left
            }
        }
        return null;
    }

    // does the ST contain the point p?
    public boolean contains(Point2D p)
    {
        return (get(p) != null);
    }

    // draw points in the unit square to standard draw
    public void draw()
    {
        // StdDraw
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        Node prev = new Node();
        prev = root;

        // iterate through nodes
        for (Node n : iterate())
        {
            n.p.draw();
            StdDraw.setPenRadius();
            if (n.color == RED)
            {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
                prev = n;
            }
            else
            {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
                prev = n;
            }
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
        }
    }

    // all points in the ST that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect)
    {
        // stack to store valid points 
        Stack<Point2D> tempstack = new Stack<Point2D>();

        // call recursive helper function
        recursive(root, tempstack, rect);
        return tempstack;
    }

    // helper function to iterate recursively through all points
    private void recursive(Node x, Stack<Point2D> tempstack, RectHV r)
    {
        if (x == null) return; // base case

        if (r.contains(x.p)) tempstack.push(x.p); // point exists in rectangle

        // recurse; travel down/left in the tree
        if ((x.lb != null) && x.lb.rect.intersects(r))
            recursive(x.lb, tempstack, r);

        // recurse; travel down/right in the tree 
        if ((x.rt != null) && x.rt.rect.intersects(r))
            recursive(x.rt, tempstack, r);

        return;
    }

    // iterable to iterate through all points in the ST
    private Iterable<Node> iterate()
    {
        // structure to hold valid points
        Stack<Node> tempstack = new Stack<Node>();

        // call recursive helper function
        recurNode(root, tempstack);
        return tempstack;
    }

    public Iterable<Point2D> getAllPoints(){
        Stack<Point2D> returnStack = new Stack<Point2D>();
        for(Node n:iterate()){
            returnStack.push(n.p);
        }
        return returnStack;
    }

    public Iterable<Value> getAllValues(){
        Stack<Value> returnStack = new Stack<Value>();
        for(Node n:iterate()){
            returnStack.push(n.value);
        }
        return returnStack;
    }

    private void recurNode(Node x, Stack<Node> tempstack)
    {
        if (x == null) return; // base case

        // no children, dead end
        if ((x.lb == null) && (x.rt == null))
        {
            tempstack.push(x);
            return;
        }

        // check left child, if exists
        if (x.lb != null)
        {
            tempstack.push(x);
            recurNode(x.lb, tempstack);
        }

        // check right child, if exists
        if (x.rt != null)
        {
            tempstack.push(x);
            recurNode(x.rt, tempstack);
        }
        return;
    }

    // a nearest neighbor in the ST to p; null if set is empty
    public Point2D nearest(Point2D p)
    {
        if (isEmpty) return null; // base case

        Point2D nearest = new Point2D(100, 100); // base faraway test point
        Node n = root;

        // call recursive helper function
        nearest = recurNear(n, nearest, p);
        return nearest;
    }

    // recursive helper function to find shortest distance to a point
    private Point2D recurNear(Node current, Point2D nearest, Point2D p)
    {
        // initial point comparison
        if (p.distanceSquaredTo(current.p) < p.distanceSquaredTo(nearest))
            nearest = current.p;

        if (current.color == RED)
        {
            if (p.x() < current.p.x()) // compare left then right
            {
                // set new shortest distance if found
                if ((current.lb != null)
                        && (current.lb.rect.distanceSquaredTo(p)
                        < p.distanceSquaredTo(nearest)))
                    nearest = recurNear(current.lb, nearest, p);

                if ((current.rt != null)
                        && (current.rt.rect.distanceSquaredTo(p)
                        < p.distanceSquaredTo(nearest)))
                    nearest = recurNear(current.rt, nearest, p);
            }
            else // compare right then left
            {
                // set new shortest distance if found
                if ((current.rt != null)
                        && (current.rt.rect.distanceSquaredTo(p)
                        < p.distanceSquaredTo(nearest)))
                    nearest = recurNear(current.rt, nearest, p);

                if ((current.lb != null)
                        && (current.lb.rect.distanceSquaredTo(p)
                        < p.distanceSquaredTo(nearest)))
                    nearest = recurNear(current.lb, nearest, p);
            }
        }

        else // BLUE   
        {
            if (p.y() < current.p.y()) // compare left then right
            {
                // set new shortest distance if found
                if ((current.lb != null)
                        && (current.lb.rect.distanceSquaredTo(p)
                        < p.distanceSquaredTo(nearest)))
                    nearest = recurNear(current.lb, nearest, p);

                if ((current.rt != null)
                        && (current.rt.rect.distanceSquaredTo(p)
                        < p.distanceSquaredTo(nearest)))
                    nearest = recurNear(current.rt, nearest, p);
            }
            else // compare right then left
            {
                // set new shortest distance if found
                if ((current.rt != null)
                        && (current.rt.rect.distanceSquaredTo(p)
                        < p.distanceSquaredTo(nearest)))
                    nearest = recurNear(current.rt, nearest, p);

                if ((current.lb != null)
                        && (current.lb.rect.distanceSquaredTo(p)
                        < p.distanceSquaredTo(nearest)))
                    nearest = recurNear(current.lb, nearest, p);
            }
        }
        return nearest;
    }


    // unit testing of the methods
    public static void main(String[] args)
    {
        /*Point2D a = new Point2D(.5, .5);
        Point2D a = new Point2D(0.206107, 0.095492);
        Point2D b = new Point2D(0.975528, 0.654508);
        Point2D c = new Point2D(0.024472, 0.345492);
        Point2D d = new Point2D(0.793893, 0.095492);
        Point2D e = new Point2D(0.793893, 0.904508);
        Point2D f = new Point2D(0.975528, 0.345492);
        Point2D g = new Point2D(0.206107, 0.904508);
        Point2D h = new Point2D(0.500000, 0.000000);
        Point2D i = new Point2D(0.024472, 0.654508);
        Point2D j = new Point2D(0.500000, 1.000000);
        KdTreeST tree = new KdTreeST();
        tree.insert(a, "dummya");
        tree.insert(b, "dummyb");
        tree.insert(c, "dummyc");
        tree.insert(d, "dummyd");
        tree.insert(e, "dummye");
        tree.insert(f, "dummyf");
        tree.insert(g, "dummyg");
        tree.insert(h, "dummyh");
        tree.insert(i, "dummyi");
        tree.insert(j, "dummyj");
        StdOut.println("size is " + tree.size());
        StdOut.println("isEmpty? " + tree.isEmpty());
        Point2D sample = new Point2D(0.81, 0.3);
        Point2D sample2 = new Point2D(0.206107, 0.904508);
        StdOut.println("should be false " + tree.contains(sample));
        StdOut.println("should be true " + tree.contains(sample2));
        
        
        StdOut.println(tree.contains(a));
        StdOut.println(tree.contains(b));
        StdOut.println(tree.get(c));
        StdOut.println(tree.get(d));
        StdOut.println(tree.get(e));
        tree.draw();*/
    }
}