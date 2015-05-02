/**
 * Created by tharald on 01/05/15.
 */
public class LinkDij {
    private EdgeWeightedDigraph digraph;
    private SET<Integer> nodes;
    private DijkstraSP dijkstraSP;

    public LinkDij(){

    }

    public LinkDij(Iterable<DirectedEdge> iterable, Integer source){
        nodes = new SET<Integer>();


        digraph = new EdgeWeightedDigraph(497713);

        for (DirectedEdge e: iterable){
            digraph.addEdge(e);
        }

        dijkstraSP = new DijkstraSP(digraph, source);

    }

    public Iterable<DirectedEdge> pathTo(int dest){
        return dijkstraSP.pathTo(dest);
    }

    public  double distTo(int dest){
        return dijkstraSP.distTo(dest);
    }
}
