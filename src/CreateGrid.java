import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.io.*;


/**
 * Created by Tharald AND JACOB on 10/02/15.
 */
public class CreateGrid {
    private SeparateChainingHashST<Double,Pixelization> pixels;

    public CreateGrid(){
        pixels = new SeparateChainingHashST<Double, Pixelization>();
        int iStart;
        int iEnd;
        int jStart;
        int jEnd;

        double initiallat = 24.544091;
        double initallon = -124.626080;
        double finallon = -66.949894;
        double finallat = 48.987386;

         iStart = (int) Math.floor(
            138.348*(initallon + 97.5)*Math.cos(Math.toRadians(initiallat))
            );
        jStart = (int) Math.floor(138.348*(initiallat - 37.0));

        iEnd = (int) Math.floor(
            138.348*(finallon + 97.5)*Math.cos(Math.toRadians(finallat))
            );

        jEnd = (int) Math.floor(138.348*(finallat - 37.0));

        StdOut.println("iStart:" + iStart);
        StdOut.println("iEnd:" + iEnd);
        StdOut.println("jStart:" + jStart);
        StdOut.println("jEnd:" + jEnd);

       Pixelization firstP = new Pixelization(iStart,jStart);
       Point2D bl = firstP.get_bl();
        StdOut.println(bl.toString());

        Pixelization centerP = new Pixelization(0,0);
        Point2D centerBl = centerP.get_bl();
        Point2D centerBr = centerP.get_br();
        StdOut.println(centerBr.toString());
        int a = 0;


        for(int i = iStart; i <= iEnd; i++){
            for(int j = jStart; j <= jEnd; j++){
                Pixelization pixel = new Pixelization(i,j);
                double key = pixel.get_id();
                Point2D p = pixel.get_bl();
                StdOut.println(p.toString());
                pixels.put(key,pixel);

                //StdOut.println("X: "+ x);
                a++;
                //StdOut.println(a);

            }
            if(a>10000) break;
            //StdOut.println("Y: "+ y);
        }


    }

    public Iterable<Double> getKeys(){
        return pixels.keys();
    }



    public double Size(){
        return pixels.size();
    }

    public int getX(double key){
        Pixelization pixel = pixels.get(key);
        return pixel.get_x();
    }

    public int getY(double key){
        Pixelization pixel = pixels.get(key);
        return pixel.get_y();
    }

    public Point2D getPixelCentroid(double key){
        Pixelization pixel = pixels.get(key);
        Point2D returnPoint = pixel.get_centroid();
        return returnPoint;

    }

    public Point2D getPixelBr(double key){
        Pixelization pixel = pixels.get(key);
        Point2D returnPoint = pixel.get_br();
        return returnPoint;

    }

    public Point2D getPixelBl(double key){
        Pixelization pixel = pixels.get(key);
        Point2D returnPoint = pixel.get_bl();
        return returnPoint;

    }

    public Point2D getPixelTr(double key){
        Pixelization pixel = pixels.get(key);
        Point2D returnPoint = pixel.get_tr();
        return returnPoint;

    }

    public Point2D getPixelTl(double key){
        Pixelization pixel = pixels.get(key);
        Point2D returnPoint = pixel.get_tl();
        return returnPoint;

    }


    public static void main(String[] args) {
        
        StdDraw.setPenColor(Color.RED);
        StdDraw.setXscale(-130.626080,-62.949894);
        StdDraw.setYscale(20.544091,52.987386);

        StdDraw.line(-124.626080,24.544091,-66.949894,48.987386);
        StdDraw.setPenColor(Color.BLACK);


        CreateGrid grid = new CreateGrid();

        StdOut.println("grid created, number of pxels: " + grid.Size());

        for(double key : grid.getKeys()){
            //Point2D centroid = grid.getPixelCentroid(key);
            Point2D bl = grid.getPixelBl(key);
            Point2D br = grid.getPixelBr(key);
            Point2D tr = grid.getPixelTr(key);
            Point2D tl = grid.getPixelTl(key);
            bl.drawTo(br);
            bl.drawTo(tl);
            br.drawTo(tr);
        }
        StdOut.println("Done");

    }


}
