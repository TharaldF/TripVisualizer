/**
 * Created by Tharald on 10/02/15.
 */
public class CreateGrid {
    private SeparateChainingHashST<Point2D,Pixelization> pixels;
    private Pixelization root;
    public CreateGrid(){
        pixels = new SeparateChainingHashST<Point2D, Pixelization>();
        Pixelization rowStart;

        double rootLat = 0.0;
        double rootLong = 0.0;
        double brLat = rootLat;
        double brLong rootLong;
        double eastEdge;
        double northEdge;
        root = new Pixelization(rootLat, rootLong);
        rowStart = root;

        double x = 0;
        double y = 0;


        while(brLong <= northEdge) {
            while(brLat <= eastEdge) {
                Pixelization pixel = new Pixelization(brLat, brLong);
                brLat = pixel.get_brLat();
                brLong = pixel.get_brLong();
                Point2D key = new Point2D(x,y);
                pixels.put(key,pixel);
            }
            brLat = rowStart.get_tlLat();
            brLong = rowStart.get_tlLong();
            rowStart
        }

    }

    public static void main(String[] args) {






    }


}
