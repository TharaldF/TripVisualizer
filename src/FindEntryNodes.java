import java.sql.*;

/**
 * Created by tharald on 30/04/15.
 */
public class FindEntryNodes {
    private KdTreeST<Integer> nodes;
    private XMLReader xmlReader;

    public FindEntryNodes() throws ClassNotFoundException, SQLException {
        nodes = new KdTreeST<Integer>();
        xmlReader = new XMLReader();

        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/Global";
        Connection m_Connection = DriverManager.getConnection(url, "tharald", "putin");

        PreparedStatement total = m_Connection.prepareStatement("SELECT * FROM Nodes");
        ResultSet resultSet = total.executeQuery();

        while(resultSet.next()){
            int id = resultSet.getInt(1);
            double lat = resultSet.getDouble(2);
            double lng = resultSet.getDouble(3);
            Point2D point = new Point2D(lng,lat);
            nodes.insert(point,id);
        }
        resultSet.close();
        total.close();
        System.out.println("Created KdTree");

    }
    public Iterable<String> getAllStates(){
        return xmlReader.getAllStates();
    }

    public Integer iFindClosest(Point2D q){
        return nodes.get(q);
    }
    public Point2D pFindClosest(Point2D p){
        return nodes.nearest(p);
    }

    public static void main(String args[]) throws ClassNotFoundException, SQLException{
        FindEntryNodes entryNodes = new FindEntryNodes();

        for(String state : entryNodes.getAllStates()){
            if(state.equals("NoState")){
                System.out.println("State finished:" + state);
                continue;
            }
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://127.0.0.1:3306";
            String urlGlobal = "jdbc:mysql://127.0.0.1:3306/Global";
            Connection m_Connection = DriverManager.getConnection(url, "tharald", "putin");
            Connection c_Connection = DriverManager.getConnection(urlGlobal, "tharald", "putin");
            PreparedStatement total = m_Connection.prepareStatement("SELECT oTrip." + state + ".oPixel, Grid." + state + ".C_Long, Grid." + state + ".C_Lat from oTrip." + state + " LEFT JOIN Grid." + state + " ON oTrip." + state + ".oPixel = Grid." + state + ".ID GROUP BY oPixel");
                    ResultSet resultSet = total.executeQuery();

            while(resultSet.next()){
                double id = resultSet.getDouble(1);
                double c_lng = resultSet.getDouble(2);
                double c_lat = resultSet.getDouble(3);
                Point2D qPoint = new Point2D(c_lng,c_lat);
                Point2D mPoint = entryNodes.pFindClosest(qPoint);
                int entryN = entryNodes.iFindClosest(mPoint);
                double m_lng = mPoint.x();
                double m_lat = mPoint.y();
                PreparedStatement insert = c_Connection.prepareStatement("REPLACE INTO EntryNodes VALUES(?,?,?,?,?,?)");
                insert.setDouble(1, id);
                insert.setInt(2, entryN);
                insert.setDouble(3, c_lat);
                insert.setDouble(4, c_lng);
                insert.setDouble(5, m_lat);
                insert.setDouble(6, m_lng);
                insert.execute();
                insert.close();
            }
            m_Connection.close();
            c_Connection.close();
            System.out.println("State finished:" + state);
        }

    }
}
