import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Point2D;

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
                x++;
            }
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

    public Point2D getPixelCentroid(Point2D key){
        Pixelization pixel = pixels.get(key);
        Point2D returnPoint = new Point2D(pixel.get_centroidLong,pixel.get_centroidLat);
        return returnPoint;


    }

    public static void main(String[] args) {
        StdDraw.setPenColor(Color.RED);

        StdDraw.line(-124.626080,18.005611,-62.361014,48.987386);
        StdDraw.setPenColor(Color.BLACK);

        CreateGrid grid = new CreateGrid();

        for(Point2D p : grid.getKeys()){
            Point2D centroid = grid.getPixelCentroid(p);
            StdDraw.point(centroid.getX(),centroid.getY());
        }






    }


}
