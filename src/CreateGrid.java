import java.awt.*;
import java.awt.geom.*;

/**
 * Created by Tharald on 10/02/15.
 */
public class CreateGrid {
    private SeparateChainingHashST<Integer,Pixelization> pixels;

    public CreateGrid(){
        pixels = new SeparateChainingHashST<Integer, Pixelization>();
        int iStart = -1000;
        int iEnd = 1000;
        int jStart = -1000;
        int jEnd = 1000;



        for(int i = iStart; i < iEnd; i++){
            for(int j = jStart; j <  jEnd; j++){

                Pixelization pixel = new Pixelization(i,j);
                int key = pixel.getID();
                pixels.put(key,pixel);

                //StdOut.println("X: "+ x);

            }
            //StdOut.println("Y: "+ y);
        }

    }

    public Iterable<Integer> getKeys(){
        return pixels.keys();
    }



    public double Size(){
        return pixels.size();
    }

    public Point2D getPixelCentroid(int key){
        Pixelization pixel = pixels.get(key);
        Point2D returnPoint = pixel.getCentroid();
        return returnPoint;

    }

    public Point2D getPixelBr(int key){
        Pixelization pixel = pixels.get(key);
        Point2D returnPoint = pixel.getBr();
        return returnPoint;

    }

    public Point2D getPixelBl(int key){
        Pixelization pixel = pixels.get(key);
        Point2D returnPoint = pixel.getBl();
        return returnPoint;

    }

    public Point2D getPixelTr(int key){
        Pixelization pixel = pixels.get(key);
        Point2D returnPoint = pixel.getTr();
        return returnPoint;

    }

    public Point2D getPixelTl(int key){
        Pixelization pixel = pixels.get(key);
        Point2D returnPoint = pixel.getTl();
        return returnPoint;

    }

    private int cantor(int a, int b){
        return (int) 0.5*(a+b)*(a+b+1)+b;
    }

    public static void main(String[] args) {
        StdDraw.setPenColor(Color.RED);
        StdDraw.setXscale(-124.626080,-123.949894);
        StdDraw.setYscale(24.544091,25.987386);

        StdDraw.line(-124.626080,24.544091,-66.949894,48.987386);
        StdDraw.setPenColor(Color.BLACK);


        CreateGrid grid = new CreateGrid();

        StdOut.println("grid created, number of pxels: " + grid.Size());

        for(int key : grid.getKeys()){
            Point2D centroid = grid.getPixelCentroid(key);
            Point2D bl = grid.getPixelBl(key);
            Point2D br = grid.getPixelBr(key);
            Point2D tr = grid.getPixelTr(key);
            Point2D tl = grid.getPixelTl(key);
        }

        StdOut.println("Done");

    }


}
