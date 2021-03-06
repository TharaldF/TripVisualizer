import jdk.internal.org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.*;



import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;


/**
 * Created by Tharald on 17/02/15.
 */
public class XMLReader {
    private KdTreeST<Polygon> treeST;
    private SeparateChainingHashST<String,Polygon> states;


    public XMLReader(){
        treeST = new KdTreeST<Polygon>();
        states = new SeparateChainingHashST<String, Polygon>();

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        try{
            DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
            Document document = dBuilder.parse(XMLReader.class.getResourceAsStream("/states.xml"));
            Document centroidsDocument = dBuilder.parse(XMLReader.class.getResourceAsStream("/centroids.xml"));
            document.normalize();
            centroidsDocument.normalize();

            NodeList centroids = centroidsDocument.getElementsByTagName("states");
            Node centroidRoot = centroids.item(0);
            Element centroidElementRoot = (Element) centroidRoot;

            NodeList rootNodes = document.getElementsByTagName("states");
            org.w3c.dom.Node rootNode = rootNodes.item(0);
            org.w3c.dom.Element rootElement = (org.w3c.dom.Element) rootNode;

            NodeList statesList = rootElement.getElementsByTagName("state");
            NodeList centroidList = centroidElementRoot.getElementsByTagName("state");

            for(int i = 0; i < statesList.getLength();i++){
                org.w3c.dom.Node theState = statesList.item(i);
                org.w3c.dom.Element stateElement = (org.w3c.dom.Element) theState;
                //StdOut.println("This state is " + stateElement.getAttribute("name"));
                Polygon newState = new Polygon(stateElement.getAttribute("name"), stateElement.getAttribute("colour"));

                NodeList pointList = stateElement.getElementsByTagName("point");
                for(int j = 0; j < pointList.getLength();j++){
                    org.w3c.dom.Element pointElement = (org.w3c.dom.Element) pointList.item(j);
                    Point2D newPoint = new Point2D(Double.parseDouble(pointElement.getAttribute("lng")),Double.parseDouble(pointElement.getAttribute("lat")));
                    newState.add(newPoint);
                    //StdOut.println("Lat: " + pointElement.getAttribute("lat") + "Long: " + pointElement.getAttribute("lng"));
                }
                Node statecentroid = centroidList.item(i);
                Element centroidElement = (Element) statecentroid;
                Point2D centroid = new Point2D(Double.parseDouble(centroidElement.getAttribute("lon")),Double.parseDouble(centroidElement.getAttribute("lat")));
                treeST.insert(centroid,newState);
                states.put(stateElement.getAttribute("name"),newState);

            }
            Polygon nostate = new Polygon("NoState","#ff0000");
            states.put("NoState", nostate);


        }
        catch (ParserConfigurationException e){
            e.printStackTrace();
        }
        catch (org.xml.sax.SAXException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public Polygon getStatePolygon(String state){
        return states.get(state);
    }

    public Iterable<Polygon> getAllPolygons(){
        return treeST.getAllValues();
    }

    public String getClosestState(Point2D queryPoint){
        Point2D nearestPoint = treeST.nearest(queryPoint);
        Polygon nearestState = treeST.get(nearestPoint);
        if(nearestState.contains(queryPoint)){
            return nearestState.getName();
        }
        else{
            for(Polygon pol:treeST.getAllValues()){
                if(pol.contains(queryPoint)){
                    return pol.getName();
                }
            }
        }
        return "NoState";
    }

    public int size(){
        return treeST.size();
    }

    public Iterable<String> getAllStates() {
        return states.keys();

    }

    public Iterable<Point2D> getAllPoints(){
        return treeST.getAllPoints();
    }
    public void drawTree(){
        treeST.draw();
    }



    public static void main(String[] args){
       XMLReader xmlReader = new XMLReader();
        StdOut.println(xmlReader.size());
       // StdDraw.setXscale(-136,-66);
        // StdDraw.setYscale(20.54,53);
        for(String s: xmlReader.getAllStates()){
            StdOut.println(s);
        }

        /*
        StdDraw.setPenColor(Color.GREEN);
        int points= 0;
        for(Point2D p:xmlReader.getAllPoints()){
            p.draw();
            points++;
        }

        for(Polygon p:xmlReader.getAllPolygons()){
            StdDraw.setPenColor(p.color());
            p.draw();
        }
        //StdOut.println(points);
        //xmlReader.drawTree();
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius(.006);

        for(Point2D p:xmlReader.getAllPoints()){
            p.draw();
        }
        */

    }
}
