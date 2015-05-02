import java.sql.*;
import java.util.*;

/**
 * Created by tharald on 01/05/15.
 */
public class LinkObject {

    private NodeObject nodeObject;
    private KdTreeST<Bag<DirectedEdge>> links;
    private SeparateChainingHashST<DirectedEdge, Integer> edgeToLink;
    private SeparateChainingHashST<DirectedEdge,String> edgeToName;

    public LinkObject() throws SQLException, ClassNotFoundException {
        links = new KdTreeST<Bag<DirectedEdge>>();
        nodeObject = new NodeObject();
        edgeToLink = new SeparateChainingHashST<DirectedEdge,Integer>();
        edgeToName = new SeparateChainingHashST<DirectedEdge,String>();
        readLinks();
    }


    private void readLinks() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/Global";
        Connection m_Connection = DriverManager.getConnection(url, "tharald", "putin");

        PreparedStatement total = m_Connection.prepareStatement("SELECT * FROM Links");
        ResultSet resultSet = total.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            int nodeA = resultSet.getInt(2);
            int nodeB = resultSet.getInt(3);
            double distance = resultSet.getDouble(4);
            int roadType = resultSet.getInt(5);
            int nodeType = resultSet.getInt(6);
            String name = resultSet.getString(7);

            double weight;
            if (roadType == 1) {
                weight = (distance / 65) * 3600;
            } else if (roadType == 2) {
                weight = (distance / 65) * 3600;
            } else if (roadType == 3) {
                weight = (distance / 55) * 3600;
            } else if (roadType == 4) {
                weight = (distance / 40) * 3600;
            } else if (roadType == 5) {
                weight = (distance / 5) * 3600;
            } else if (roadType == 6) {
                weight = (distance / 30) * 3600;
            } else if (roadType == 7) {
                weight = (distance / 35) * 3600;
            } else {
                weight = (distance / 20) * 3600;
            }

            if (nodeType == 1) {
                DirectedEdge edge = new DirectedEdge(nodeA, nodeB, weight);
                addLink(edge, id, name);

            } else if (nodeType == 2) {
                DirectedEdge edge = new DirectedEdge(nodeB, nodeA, weight);
                addLink(edge, id, name);
            } else if (nodeType == 3) {
                DirectedEdge edgeA = new DirectedEdge(nodeA, nodeB, weight);
                DirectedEdge edgeB = new DirectedEdge(nodeB, nodeA, weight);
                addLink(edgeA, id, name);
                addLink(edgeB, id, name);
            }
        }
        resultSet.close();
        m_Connection.close();

    }

    public Iterable<DirectedEdge> getEdges(double xmin, double ymin, double xmax, double ymax){
        //StdOut.println("xmin: " + xmin + " ymin: " + ymin + " xmax:" + xmax + " ymax: " + ymax);
        RectHV rectHV = new RectHV(xmin, ymin, xmax, ymax);
        Queue<DirectedEdge> directedEdgeQueue = new Queue<DirectedEdge>();
         for (Point2D p: links.range(rectHV)){
             Bag<DirectedEdge> bag = links.get(p);
             for(DirectedEdge e: bag){
                 directedEdgeQueue.enqueue(e);
             }

         }
        return directedEdgeQueue;
    }

    private void addLink(DirectedEdge e, int linkID, String linkName){
        if (hasEdge(e) == false){
            int aNode = e.from();

            Point2D p = nodeObject.getPoint(aNode);


            /*
            StdOut.println("aNode: " + aNode);
            StdOut.println("Point: " + p.toString());
            StdOut.println("Edge: " + e.toString());
            */

            if(links.contains(p)){
                Bag<DirectedEdge> edgebag = links.get(p);
                edgebag.add(e);
                links.insert(p,edgebag);

            }
            else{
                Bag<DirectedEdge> edgebag = new Bag<DirectedEdge>();
                edgebag.add(e);
                links.insert(p,edgebag);
            }


            edgeToLink.put(e,linkID);
            edgeToName.put(e,linkName);
        }
    }

    private boolean hasEdge(DirectedEdge e){
        if (edgeToLink.contains(e)){
            return true;
        }
        else {
            return false;
        }

    }

    public Point2D getPoint(int node){
        return nodeObject.getPoint(node);
    }

    public Integer getNode(double pixel){
        return nodeObject.getNode(pixel);
    }

    public Integer getLinkId(DirectedEdge e){
        return edgeToLink.get(e);
    }

    public String getLinkName(DirectedEdge e){
        return edgeToName.get(e);
    }

    public static void main(String args[]) throws SQLException, ClassNotFoundException {
        LinkObject linkObject = new LinkObject();
    }
}
