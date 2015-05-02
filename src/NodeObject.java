import java.sql.*;

/**
 * Created by tharald on 01/05/15.
 */
public class NodeObject {

    private KdTreeST<Integer> nodes;
    private SeparateChainingHashST<Integer,Point2D> nodesPoints;
    private SeparateChainingHashST<Double, Integer> pixelPoints;

    public NodeObject() throws SQLException, ClassNotFoundException {
        nodes = new KdTreeST<Integer>();
        nodesPoints = new SeparateChainingHashST<Integer,Point2D>();
        pixelPoints = new SeparateChainingHashST<Double,Integer>();
        readNodes();
    }


    private void readNodes() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/Global";
        Connection m_Connection = DriverManager.getConnection(url, "tharald", "putin");

        // Get Nodes
        PreparedStatement total = m_Connection.prepareStatement("SELECT * FROM Nodes");
        ResultSet resultSet = total.executeQuery();

        while(resultSet.next()){
            int id = resultSet.getInt(1);
            double lat = resultSet.getDouble(2);
            double lng = resultSet.getDouble(3);
            Point2D point = new Point2D(lng,lat);
            nodes.insert(point,id);
            nodesPoints.put(id,point);
        }
        resultSet.close();
        total.close();

        // Get entrynodes
        PreparedStatement eNodes = m_Connection.prepareStatement("SELECT Pixel, EntryNode FROM EntryNodes");
        ResultSet resultNodes = eNodes.executeQuery();

        while(resultNodes.next()){
            double pixel = resultNodes.getDouble(1);
            int node = resultNodes.getInt(2);
            pixelPoints.put(pixel,node);
        }

        resultNodes.close();
        eNodes.close();
        m_Connection.close();

    }

    public Point2D getPoint(Integer node){
        return nodesPoints.get(node);
    }

    public Integer getNode(Point2D point){
        return nodes.get(point);
    }

    public Integer getNode(double pixel){
        return pixelPoints.get(pixel);
    }
}
