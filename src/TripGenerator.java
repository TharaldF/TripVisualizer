/**
 * Created by jacobperricone on 3/14/15.
 */
import java.util.*;
import java.io.*;
import java.io.FileNotFoundException;

public class TripGenerator {

    public TripGenerator(String filename) {
        generateTrips(filename);

    }

    private void generateTrips(String filename){
        BufferedReader reader = null;
        String line = "";
        String csvSplitBy = ",";
        try{
            reader = new BufferedReader(new FileReader(filename));
            while((line = reader.readLine()) != null) {
                String[] info = line.split(csvSplitBy);
                int person_id = Integer.parseInt(info[5]);
                for (int i = 1; i <= 8;i++) {
                    int index = (i-1)*9 + 12;
                    String oType = info[index - 5];
                    String dType = info[index - 3];
                    if(oType == "NA" || dType == "N"){
                        break;
                    }
                    double unique_id = createId(person_id, i);
                    double otime = Double.parseDouble(info[index + 2]);
                    double dtime = Double.parseDouble(info[index + 3]);
                    double atime = Double.parseDouble(info[index + 2]);
                    int ox_pixel = get_x(Double.parseDouble(info[index]), Double.parseDouble(info[index + 1]));
                    int oy_pixel = get_y(Double.parseDouble(info[index]), Double.parseDouble(info[index +1]));
                    int ax_pixel = get_x(Double.parseDouble(info[index + 9]), Double.parseDouble(info[index + 10]));
                    int ay_pixel = get_y(Double.parseDouble(info[index + 9]), Double.parseDouble(info[index + 10 ]));
                    double oId = fetchPixelId(ox_pixel, oy_pixel);
                    double aId = fetchPixelId(ax_pixel, ay_pixel);

                    System.out.println("Id: " + unique_id + " Personid: " + person_id +
                            " Trip Number: " + i + " oxPixelId: " + oId + " aPixelID: " + aId
                     + " oTime: " + otime + " dTime: " + dtime + " aTime " + atime + " oType: " + oType
                    + " dType : " + dType);

                }
            }


        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    private double createId(int a, int b) {
        double id = 0.5*(a+b)*(a+b+1)+b;
        return id;
    }

    private double fetchPixelId(int a, int b) {
        double pixelId = 0.5*(a+195593+b+1724)*(a+195593+b+1724+1)+b+1724;
        // StdOut.println(returnn);
        return pixelId;
    }
    private int get_x(double lat, double lon){
        int x = (int) Math.floor(138.348 * (lon + 97.5) * Math.cos(Math.toRadians(lat)));
        return x;
    }
    private int get_y(double lat, double lon){
        int y = (int) Math.floor(138.348 * (lat - 37.0));
        return y;
    }

    public static void main(String[] args)  {
        TripGenerator gen = new TripGenerator(args[0]);
    }
}


