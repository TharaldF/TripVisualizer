import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Tharald on 10/02/15.
 */
public class CreateGrid {
    private SeparateChainingHashST<String,SeparateChainingHashST<Double,Pixelization>> pixels;
    private XMLReader xmlReader;
    private SeparateChainingHashST<Double,String> keyState;


    public CreateGrid() throws ClassNotFoundException, SQLException {
        xmlReader = new XMLReader();
        pixels = new SeparateChainingHashST<String, SeparateChainingHashST<Double, Pixelization>>();
        keyState = new SeparateChainingHashST<Double, String>();

        for(String state: xmlReader.getAllStates()){
            SeparateChainingHashST<Double,Pixelization> stateTable = new SeparateChainingHashST<Double, Pixelization>();
            pixels.put(state,stateTable);
        }
        int iStart;
        int iEnd;
        int jStart;
        int jEnd;

        double initiallat = 24.544091;
        double initallon = -124.626080;
        double finallon = -66.949894;
        double finallat = 48.987386;

         iStart = (int) Math.floor(
            138.348*(initallon + 97.5)*Math.toDegrees(Math.cos(Math.toRadians(initiallat)))
            );
        jStart = (int) Math.floor(138.348*(initiallat - 37.0));

        iEnd = (int) Math.floor(
            138.348*(finallon + 97.5)*Math.toDegrees(Math.cos(Math.toRadians(finallat)))
            );

        jEnd = (int) Math.floor(138.348*(finallat - 37.0));

        StdOut.println("iStart:" + iStart);
        StdOut.println("iEnd:" + iEnd);
        StdOut.println("jStart:" + jStart);
        StdOut.println("jEnd:" + jEnd);
/*
       Pixelization firstP = new Pixelization(iStart,jStart);
       Point2D bl = firstP.get_bl();
        StdOut.println(bl.toString());

        Pixelization centerP = new Pixelization(0,0);
        Point2D centerBl = centerP.get_bl();
        Point2D centerBr = centerP.get_br();
        StdOut.println(centerBr.toString());
        */
        int a = 0;


        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/Grid";
        Connection m_Connection = DriverManager.getConnection(url, "tharald", "putin");
        StdOut.println("Connection Successful");


        for(int i = iStart; i <= iEnd; i++){
            for(int j = jStart; j <= jEnd; j++){

                Pixelization pixel = new Pixelization(i,j);
                double key = pixel.get_id();
                Point2D cent = pixel.get_centroid();
                String state = xmlReader.getClosestState(cent);

                Point2D bl = pixel.get_bl();
                Point2D br = pixel.get_br();
                Point2D tr = pixel.get_tr();
                Point2D tl = pixel.get_tl();
                int x = i;
                int y = j;

                PreparedStatement total = m_Connection.prepareStatement("REPLACE INTO "+ state + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");

                //ID
                total.setDouble(1, key);

                //X/Y
                total.setInt(2, x);
                total.setInt(3, y);

                //Centroid Long/Lat
                total.setDouble(4, cent.x());
                total.setDouble(5, cent.y());

                //Bottom Left Long/Lat
                total.setDouble(6, bl.x());
                total.setDouble(7, bl.y());

                //Bottom Right Long/Lat
                total.setDouble(8, br.x());
                total.setDouble(9, br.y());

                //Top Left Long/Lat
                total.setDouble(10, tl.x());
                total.setDouble(11, tl.y());

                //Top Right Long/Lat
                total.setDouble(12, tr.x());
                total.setDouble(13, tr.y());

                total.executeUpdate();
                /*
                pixels.get(state).put(key,pixel);
                keyState.put(key,state);
                */


                //StdOut.println("X: "+ x);
                //a++;
                //StdOut.println(a);
                //Point2D bottomleft = pixel.get_bl();
                //StdOut.println(bottomleft.toString());

            }
            //StdOut.println("Y: "+ y);
        }


    }

    public Iterable<String> getStates(){
        return pixels.keys();
    }



    public double Size(){
        return pixels.size();
    }

    public Iterable<Double> getKeys(String state){
        return pixels.get(state).keys();
    }



    public int getX(double key){
        String state = keyState.get(key);
        Pixelization pixel = pixels.get(state).get(key);
        return pixel.get_x();
    }

    public int getY(double key){
        String state = keyState.get(key);
        Pixelization pixel = pixels.get(state).get(key);
        return pixel.get_y();
    }

    public Point2D getPixelCentroid(double key){
        String state = keyState.get(key);
        Pixelization pixel = pixels.get(state).get(key);
        Point2D returnPoint = pixel.get_centroid();
        return returnPoint;

    }

    public Point2D getPixelBr(double key){
        String state = keyState.get(key);
        Pixelization pixel = pixels.get(state).get(key);
        Point2D returnPoint = pixel.get_br();
        return returnPoint;

    }

    public Point2D getPixelBl(double key){
        String state = keyState.get(key);
        Pixelization pixel = pixels.get(state).get(key);
        Point2D returnPoint = pixel.get_bl();
        return returnPoint;

    }

    public Point2D getPixelTr(double key){
        String state = keyState.get(key);
        Pixelization pixel = pixels.get(state).get(key);
        Point2D returnPoint = pixel.get_tr();
        return returnPoint;

    }

    public Point2D getPixelTl(double key){
        String state = keyState.get(key);
        Pixelization pixel = pixels.get(state).get(key);
        Point2D returnPoint = pixel.get_tl();
        return returnPoint;

    }


    public static void main(String[] args) {
        StdDraw.setPenColor(Color.RED);
        StdDraw.setXscale(-136,-66);
        StdDraw.setYscale(20.54,53);

        StdDraw.line(-124.626080,24.544091,-66.949894,48.987386);
        StdDraw.setPenColor(Color.BLACK);


        CreateGrid grid = null;
        try {
            grid = new CreateGrid();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StdOut.println("grid created, number of pxels: " + grid.Size());
        for(String state: grid.getStates()){
            for(double key : grid.getKeys(state)){
                Point2D bl = grid.getPixelBl(key);
                Point2D br = grid.getPixelBr(key);
                Point2D tr = grid.getPixelTr(key);
                Point2D tl = grid.getPixelTl(key);
                bl.drawTo(br);
                bl.drawTo(tl);
                br.drawTo(tr);
            }
        }

        StdOut.println("Done");

    }


}
