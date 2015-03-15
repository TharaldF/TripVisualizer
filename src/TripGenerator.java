/**
 * Created by jacobperricone on 3/14/15.
 */

import java.util.*;
import java.io.*;
import java.io.FileNotFoundException;
import java.sql.*;

public class TripGenerator  {

    public TripGenerator(File filename) throws ClassNotFoundException, SQLException {
        generateTrips(filename);

    }

    private void generateTrips(File filename) throws ClassNotFoundException, SQLException {
        BufferedReader reader = null;
        String line = "";
        String csvSplitBy = ",";
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/Global";
        Connection m_Connection = DriverManager.getConnection(url, "tharald", "putin");
        try{
            reader = new BufferedReader(new FileReader(filename));
            reader.readLine();
            while((line = reader.readLine()) != null) {
                String[] info = line.split(csvSplitBy);
                double person_id = Long.parseLong(info[5]);
                if(Double.parseDouble(info[12]) == 0.0 || Double.parseDouble(info[13])==0){
                    continue;
                }
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

                    String s = String.format("SELECT State from Global.Info WHERE ID = %f", oId);
                    StdOut.println(s);
                    StdOut.println(person_id + " " + Double.parseDouble(info[index]) +" " + Double.parseDouble(info[index + 1]));

                    PreparedStatement total = m_Connection.prepareStatement(s);
                    ResultSet result = total.executeQuery();
                    if(!result.next()){
                        continue;
                    }

                    String state = result.getString(1);
                    PreparedStatement insert = m_Connection.prepareStatement("REPLACE INTO oTrip."+ state +" VALUES(?,?,?,?,?,?,?,?,?,?)");
                    insert.setDouble(1, unique_id); insert.setDouble(2, person_id);
                    insert.setInt(3, i); insert.setDouble(4, oId); insert.setDouble(5, aId);
                    insert.setDouble(6, otime);  insert.setDouble(7, dtime);
                    insert.setDouble(8, atime);
                    insert.setString(9, oType); insert.setString(10, dType);
                    insert.executeUpdate();
                    /*
                    System.out.printf("Trip Number: %d, Person_id: %f, Unique Id: %f, oPixelid: %f, aPixelid: %f, oTime: %f, dTime: %f, aTime: %f, otype: %s, dType: %s \n",
                            i, person_id, unique_id, oId, aId, otime, dtime, atime, oType, dType);
                    */



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

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String filePath = "/users/tharald/documents/NNFiles/Alabama/Alabama_01001_Module6NN1stRun.csv";
        File inputFile = new File(filePath);
        TripGenerator gen = new TripGenerator(inputFile.getAbsoluteFile());
    }
}

