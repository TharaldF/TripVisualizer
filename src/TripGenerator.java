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
                double atime;
                String[] info = line.split(csvSplitBy);
                double person_id = Long.parseLong(info[5]);
                if(Double.parseDouble(info[12]) == 0.0 || Double.parseDouble(info[13])==0){
                    continue;
                }
                int k = 0;
                for (int i = 1; i <= 7;i++) {
                    StdOut.println(i);
                    k++;
                    boolean skip = false;
                    int j = i;
                    int index = (i-1)*9 + 12;
                    String oType = info[index - 5];
                    String dType = info[index - 3];
                    if(oType.equals("NA") || dType.equals("N")){
                        break;
                    }
                    if(info[index - 5].equals("NA") || info[index - 3].equals("N")){
                        break;
                    }
                    double unique_id = createId(person_id, k);
                    double otime = Double.parseDouble(info[index + 2]);
                    double dtime = Double.parseDouble(info[index + 3]);
                    double olat = Double.parseDouble(info[index]);
                    double olon = Double.parseDouble(info[index + 1]);
                    int ox_pixel = get_x(olat, olon);
                    int oy_pixel = get_y(olat, olon);
                    int dx_pixel;
                    int dy_pixel;
                    double dlat;
                    double dlon;
                    if(i==7){
                        atime = Double.parseDouble(info[index + 3]);
                        dlat  = Double.parseDouble(info[12]);
                        dlon = Double.parseDouble(info[13]);
                        dx_pixel = get_x(dlat,dlon);
                        dy_pixel = get_y(dlat,dlon);
                    }else {
                        atime = Double.parseDouble(info[index + 11]);
                         dlat = Double.parseDouble(info[index + 9]);
                        dlon = Double.parseDouble(info[index + 10]);
                        if(dlat == 0.0 || dlon == 0.0) {
                            for(j = i + 1; (dlat == 0.0 || dlon == 0.0)  && j < 7; j++) {
                                    int tempindex = (j - 1) * 9 + 12;
                                    if(info[tempindex - 5].equals("NA") || info[tempindex - 3].equals("N")){
                                        skip = true;
                                        break;
                                    }
                                    atime = Double.parseDouble(info[tempindex + 11]);
                                    dlat = Double.parseDouble(info[tempindex + 9]);
                                    dlon = Double.parseDouble(info[tempindex + 10]);
                                    dType = info[tempindex - 3];
                                    StdOut.println(dlat + " " + dlon + " " + dType + "first" + j + "i: " + i);
                            }
                            if(skip){
                                break;
                            }
                            if(dlat == 0.0 || dlon == 0.0) {
                                dType = "H";
                                atime = 0;
                                dlat = Double.parseDouble(info[12]);
                                dlon = Double.parseDouble(info[13]);
                            }
                            i = j-1;
                        }
                        StdOut.println(dlat + " " + dlon + " " + dType + j);
                        dx_pixel = get_x(dlat, dlon);
                        dy_pixel = get_y(dlat, dlon);
                    }

                    double oId = fetchPixelId(ox_pixel, oy_pixel);
                    double aId = fetchPixelId(dx_pixel, dy_pixel);

                    String s = String.format("SELECT State from Global.Info WHERE ID = %f", oId);
                    StdOut.println(s);
                    StdOut.println(person_id + " " + Double.parseDouble(info[index]) +" " + Double.parseDouble(info[index + 1]));

                    PreparedStatement total = m_Connection.prepareStatement(s);
                    ResultSet result = total.executeQuery();
                    if(!result.next()){
                        continue;
                    }

                    String state = result.getString(1);
                    PreparedStatement insert = m_Connection.prepareStatement("REPLACE INTO oTrip."+ state +" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    insert.setDouble(1, unique_id); insert.setDouble(2, person_id);
                    insert.setInt(3, k); insert.setDouble(4, oId); insert.setInt(5,ox_pixel);
                    insert.setInt(6,oy_pixel); insert.setDouble(7, olat); insert.setDouble(8,olon);
                    insert.setDouble(9, aId); insert.setInt(10, dx_pixel); insert.setInt(11,dy_pixel);
                    insert.setDouble(12,dlat); insert.setDouble(13,dlon);
                    insert.setDouble(14, otime);  insert.setDouble(15, dtime);
                    insert.setDouble(16, atime);
                    insert.setString(17, oType); insert.setString(18, dType);
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
        String filePath = "/Users/jacobperricone/Desktop/triptest.csv";
        File inputFile = new File(filePath);
        TripGenerator gen = new TripGenerator(inputFile.getAbsoluteFile());
    }
}

