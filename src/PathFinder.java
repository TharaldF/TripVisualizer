
import java.sql.*;

/**
 * Created by tharald on 30/04/15.
 */
public class PathFinder {

    private LinkObject linkObject;


    public PathFinder() throws SQLException, ClassNotFoundException {
        linkObject = new LinkObject();
        readNodeTable();
    }


    private void readNodeTable() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/Global";
        String urlTrip = "jdbc:mysql://127.0.0.1:3306/oTrip";
        String pathFinder = "jdbc:mysql://127.0.0.1:3306/Path";
        Connection m_Connection = DriverManager.getConnection(url, "tharald", "putin");
        Connection trip_Connection = DriverManager.getConnection(urlTrip, "tharald", "putin");
        Connection path_connetction = DriverManager.getConnection(pathFinder, "tharald", "putin");

        PreparedStatement total = m_Connection.prepareStatement("SELECT Pixel, EntryNode FROM EntryNodes order by EntryNode, Pixel");
        ResultSet resultSet = total.executeQuery();

        // Find Paths
        // Create empty linkDij
        LinkDij linkDij = new LinkDij();
        int prevEntryNode = -1;
        //StdOut.println("ID \t PersonID \t Segment \t aNode \t bNode \t LinkID \t LinkName \t oPixel \t dPixel \t dTime \t aTime \t oType \t dType \t a_lat \t a_Long \t b_Lat \t b_Long \t aNode_Time \t bNode_Time");
        while (resultSet.next()){
            double pixel = resultSet.getDouble(1);
            int entryNode = resultSet.getInt(2);
            if(entryNode != prevEntryNode){
                linkDij = getDijkstra(entryNode);
            }
            prevEntryNode = entryNode;

            // Find State
            PreparedStatement getState = m_Connection.prepareStatement("SELECT State FROM Info WHERE (ID="+pixel+")");
            ResultSet resultState = getState.executeQuery();
            if(!resultState.next()){
                resultState.close();
                getState.close();
                StdOut.printf(" NO STATE FOUND FOR PIXEL: %.2f", pixel);
                StdOut.println();
                continue;
            }
            String state = resultState.getString(1);
            resultState.close();
            getState.close();

            //Find trips
            PreparedStatement getTrips = trip_Connection.prepareStatement("SELECT ID, PersonID,oPixel,dPixel, dTime, aTime,oType, dType FROM "+ state + " Where (oPixel = "+ pixel +")");
            ResultSet resultTrips = getTrips.executeQuery();
            if(!resultTrips.next()){
                resultTrips.close();
                getTrips.close();
                StdOut.printf(" NO TRIP FOUND FOR STATE: " + state + "PIXEL: %.2f", pixel);
                StdOut.println();
                continue;
            }
            while (resultTrips.next()){
                double tripID = resultTrips.getDouble(1);
                double personID = resultTrips.getDouble(2);
                double oPixel = resultTrips.getDouble(3);
                double dPixel = resultTrips.getDouble(4);
                double dTime = resultTrips.getDouble(5);
                double aTime = resultTrips.getDouble(6);
                String oType = resultTrips.getString(7);
                String dType = resultTrips.getString(8);


                if(linkObject.getNode(dPixel) == null){
                    StdOut.println("NO Node FOUND FOR PIXEL: " + dPixel);
                    continue;
                }
                int destNode = linkObject.getNode(dPixel);

                Iterable<DirectedEdge> path = linkDij.pathTo(destNode);

                if (path == null){
                    StdOut.println("NO PATH FOUND FOR:");
                    StdOut.printf("Trip: %.1f oPix: %.1f dPixel %.1f", tripID, oPixel, dPixel);
                    StdOut.println("from: " + entryNode+ " to: " + destNode);
                    continue;
                }

                int segment = 1;
                double aNode_Time = aTime;

                for(DirectedEdge e: path){
                    int linkId = linkObject.getLinkId(e);
                    double a_lat = linkObject.getPoint(e.from()).y();
                    double a_long = linkObject.getPoint(e.from()).x();
                    double b_lat = linkObject.getPoint(e.to()).y();
                    double b_long = linkObject.getPoint(e.to()).x();
                    double bNode_Time = aNode_Time + e.weight();
                    String linkName = linkObject.getLinkName(e);
                    PreparedStatement pathInsert = path_connetction.prepareStatement("REPLACE INTO " + state + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    pathInsert.setDouble(1,tripID);
                    pathInsert.setDouble(2,personID);
                    pathInsert.setInt(3, segment);
                    pathInsert.setInt(4, e.from());
                    pathInsert.setInt(5, e.to());
                    pathInsert.setInt(6, linkId);
                    pathInsert.setString(7, linkName);
                    pathInsert.setDouble(8, oPixel);
                    pathInsert.setDouble(9, dPixel);
                    pathInsert.setDouble(10, dTime);
                    pathInsert.setDouble(11, aTime);
                    pathInsert.setString(12, oType);
                    pathInsert.setString(13, dType);
                    pathInsert.setDouble(14, a_lat);
                    pathInsert.setDouble(15, a_long);
                    pathInsert.setDouble(16, b_lat);
                    pathInsert.setDouble(17, b_long);
                    pathInsert.setDouble(18, aNode_Time);
                    pathInsert.setDouble(19, bNode_Time);
                    pathInsert.executeUpdate();
                    aNode_Time = bNode_Time;
                    segment++;
                }


            }
            resultTrips.close();
            getTrips.close();

        }

        m_Connection.close();
        trip_Connection.close();
        path_connetction.close();
    }



    private LinkDij getDijkstra(int source){
        Point2D focal = linkObject.getPoint(source);
        Point2D NE = getBearing(focal, 45.0);
        Point2D SW = getBearing(focal, 230.0);
        //StdOut.println("Square: Center: " + focal.toString()+ " NE: "+NE.toString()+ " SW: "+ SW.toString());
        double xMin = SW.x();
        double yMin = SW.y();
        double xMax = NE.x();
        double yMax = NE.y();
        Iterable<DirectedEdge> edges = linkObject.getEdges(xMin, yMin, xMax, yMax);
        /*
        int count = 0;
        for(DirectedEdge e: edges){
            StdOut.println(e.toString());
            count++;
        }
        StdOut.println("SIZE OF DIJKSTRA: "+ count);
        */

        LinkDij linkDij = new LinkDij(edges,source);
        return linkDij;
    }


    private Point2D getBearing(Point2D focal, double bearingDeg){
        double focalLat = focal.y();
        double focalLong = focal.x();
        focalLat = Math.toRadians(focalLat);
        focalLong = Math.toRadians(focalLong);
        double bearing = Math.toRadians(bearingDeg);
        double ratio = 250.0/3963.1676;

        double newLat = Math.asin(Math.sin(focalLat) * Math.cos(ratio) + Math.cos(focalLat) * Math.sin(ratio) * Math.cos(bearing));
        double newLong = focalLong + Math.atan2(Math.sin(bearing)*Math.sin(ratio)*Math.cos(focalLat), Math.cos(ratio)-Math.sin(focalLat)*Math.sin(newLat));

        newLat = Math.toDegrees(newLat);
        newLong = Math.toDegrees(newLong);

        Point2D p = new Point2D(newLong,newLat);
        return p;

    }

    public static void main(String args[]) throws SQLException, ClassNotFoundException {
        PathFinder pathFinder = new PathFinder();
    }


}
