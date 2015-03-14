/**
 * Created by jacobperricone on 3/14/15.
 */
import java.util.*;
import java.io.*;
import java.io.FileNotFoundException;

public class TripGenerator {

    public TripGenerator(File filename) {
        generateTrips(filename);

    }

    private void generateTrips(File filename){
        BufferedReader reader = null;
        String line = "";
        String csvSplitBy = ",";
        try{
            reader = new BufferedReader(new FileReader(filename));
            reader.readLine();
            while((line = reader.readLine()) != null) {
                String[] info = line.split(csvSplitBy);
                double person_id = Long.parseLong(info[5]);
                for (int i = 1; i <= 7;i++) {
                    int index = (i-1)*9 + 12;
                    String oType = info[index - 5];
                    String dType = info[index - 3];
                    if(oType.equals("NA") || dType.equals("N")){
                        break;
                    }
                    double unique_id = createId(person_id, i);
                    double otime = Double.parseDouble(info[index + 2]);
                    double dtime = Double.parseDouble(info[index + 3]);
                    double atime = Double.parseDouble(info[index + 2]);
                    int ox_pixel = get_x(Double.parseDouble(info[index]), Double.parseDouble(info[index + 1]));
                    int oy_pixel = get_y(Double.parseDouble(info[index]), Double.parseDouble(info[index +1]));
                    int ax_pixel;
                    int ay_pixel;
                    if(i==7){
                        ax_pixel = get_x(Double.parseDouble(info[12]), Double.parseDouble(info[13]));
                        ay_pixel = get_y(Double.parseDouble(info[12]), Double.parseDouble(info[13]));
                    }else {
                        ax_pixel = get_x(Double.parseDouble(info[index + 9]), Double.parseDouble(info[index + 10]));
                        ay_pixel = get_y(Double.parseDouble(info[index + 9]), Double.parseDouble(info[index + 10]));
                    }

                    double oId = fetchPixelId(ox_pixel, oy_pixel);
                    double aId = fetchPixelId(ax_pixel, ay_pixel);


                    System.out.printf("Trip Number: %d, Person_id: %f, Unique Id: %f, oPixelid: %f, aPixelid: %f, oTime: %f, dTime: %f, aTime: %f, otype: %s, dType: %s \n",
                            i, person_id, unique_id, oId, aId, otime, dtime, atime, oType, dType);



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
    private double createId(double pid, double trip) {
        double id = pid + (trip/10);
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
        //String filePath = "/Users/Tharald/IdeaProjects/TripVisualizer/src/Colorado_08001_Module6NN1stRun.csv";
       // File inputFile = new File(filePath);
       // TripGenerator gen = new TripGenerator(inputFile.getAbsoluteFile());
    }
}

