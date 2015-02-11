import java.awt.*;

/**
 * Created by Tharald on 10/02/15.
 */
public class CreateGrid {
    private SeparateChainingHashST<Point2D,Pixelization> pixels;
    private Pixelization root;

    public CreateGrid(){
        pixels = new SeparateChainingHashST<Point2D, Pixelization>();
        Pixelization rowStart;

        double rootLat = 18.005611;
        double rootLong = -124.626080;
        double brLat = rootLat;
        double brLong = rootLong;
        double eastEdge = -62.361014;
        double northEdge = 48.987386;



        double x = 0;
        double y = 0;
        Point2D rootPoint = new Point2D(x,y);
        root = new Pixelization(rootLat, rootLong, rootPoint);
        rowStart = root;


        while(brLat <= northEdge) {
            while(brLong <= eastEdge) {
                Point2D key = new Point2D(x,y);
                Pixelization pixel = new Pixelization(brLat, brLong, key);
                pixels.put(key,pixel);

                brLat = pixel.get_brLat();
                brLong = pixel.get_brLong();
                StdOut.println("X: "+ x);
                x++;

            }
            StdOut.println("Y: "+ y);
            y++;
            brLat = rowStart.get_tlLat();
            brLong = rowStart.get_tlLong();
            Point2D nmbr = new Point2D(x,y);
            rowStart = new Pixelization(brLat, brLong, nmbr);
        }

    }

    public Iterable<Point2D> getKeys(){
        return pixels.keys();
    }

    public double Size(){
        return pixels.size();
    }

    public Point2D getPixelCentroid(Point2D key){
        Pixelization pixel = pixels.get(key);
        double x = pixel.get_centroidLong();
        double y = pixel.get_centroidLat();
        Point2D returnPoint = new Point2D(x,y);
        return returnPoint;

    }

    public Point2D getPixelBr(Point2D key){
        Pixelization pixel = pixels.get(key);
        double x = pixel.get_brLong();
        double y = pixel.get_brLat();
        Point2D returnPoint = new Point2D(x,y);
        return returnPoint;

    }

    public Point2D getPixelBl(Point2D key){
        Pixelization pixel = pixels.get(key);
        double x = pixel.get_blLong();
        double y = pixel.get_blLat();
        Point2D returnPoint = new Point2D(x,y);
        return returnPoint;

    }

    public static void main(String[] args) {
        StdDraw.setPenColor(Color.RED);
        StdDraw.setXscale(-124.626080,-62.361014);
        StdDraw.setYscale(18.005611,48.987386);

        StdDraw.line(-124.626080,18.005611,-62.361014,48.987386);
        StdDraw.setPenColor(Color.BLACK);


        CreateGrid grid = new CreateGrid();

        StdOut.println("grid created, number of pxels: " + grid.Size());

        for(Point2D p : grid.getKeys()){
            Point2D bl = grid.getPixelBl(p);
            StdOut.println(bl.toString());
            StdDraw.point(bl.x(),bl.y());
        }

        StdOut.println("Done");

    }


}
