import java.util.*;
import java.io.*;
import java.awt.*;
import java.lang.Math;

public class Pixelization {
		double radian_long = 1.57079;
		double radian_lat = 0;
		double lat_topleft;
		double lat_bottomleft;
		double lat_topright;
		double lat_bottomright; 
		double long_topleft;
		double long_bottomleft;
		double long_topright;
		double long_bottomright;
		double lat_centroid;
		double long_centroid; 
		Point2D pixel;


	public Pixelization(double latbottomleft, double longbottomleft, Point2D pixel) {
		this.pixel = pixel;
		this.lat_bottomleft = latbottomleft;
		this.long_bottomleft = longbottomleft;

		this.long_bottomright = compute_long_bottomright(latbottomleft, longbottomleft);
		this.lat_bottomright = latbottomleft;

		this.long_topleft = longbottomleft;
		this.lat_topleft = compute_lat_topleft(latbottomleft, longbottomleft);

		this.long_topright = compute_long_bottomright(latbottomleft, longbottomleft);
		this.lat_bottomright = latbottomleft;
		this.lat_centroid = compute_lat_centroid(lat_bottomleft, lat_topleft);
		this.long_centroid = compute_long_centroid(long_bottomleft, long_bottomright);
	}

	private double compute_lat_topleft(double latbottomleft, double longbottomleft) {
		double x1 = Math.toRadians(latbottomleft);
		double y1 = Math.toRadians(longbottomleft);
		double y2 = y1; 
		double recipx2;
		double x2; 
		double distance = (Math.PI/(180*60))*.25;

		x2 = Math.toDegrees(Math.asin(Math.sin(x1)*Math.cos(distance) + Math.cos(x1)*Math.sin(distance)*
			Math.cos(radian_lat)));

		//System.out.println(x2);
		return x2; 

	}

	private double compute_long_centroid(double latbottomleft, double lat_bottomright) {
		return (latbottomleft + lat_bottomright)/2;
	}

	private double compute_lat_centroid(double longbottomleft, double longtopleft) {
		return (longbottomleft + long_topleft)/2;
	}

	private double compute_long_bottomright(double latbottomleft, double longbottomleft) {
		double x1 = Math.toRadians(latbottomleft);
		double y1 = Math.toRadians(longbottomleft);
		double lon;
		double distance = (Math.PI/(180*60))*.25;

<<<<<<< HEAD
		lon= (y1 - Math.asin((Math.sin(radian_long)*Math.sin(distance))/Math.cos(x1)) + Math.PI)%(2*Math.PI) - Math.PI;
=======
		lon= Math.toDegrees((y1 - Math.asin((Math.sin(-radian_long)*Math.sin(distance))/Math.cos(x1)) + Math.PI)%(2*Math.PI) - Math.PI);
>>>>>>> baf6804bc772315bf194bc37a47cadf07e08e06e

		//System.out.println(lon);
		return lon; 


	}

	public double get_brLat() {
		return lat_bottomright;

	}

	public double get_brLong() {
		return long_bottomright;
	}

	public double get_blLat() {
		return lat_bottomleft;

	}

	public double get_blLong() {
		return long_bottomleft;
	}

	public double get_tlLat() {
		return lat_topleft;

	}

	public double get_tlLong() {
		return long_topleft;
	}

	public double get_trLat() {
		return lat_topright;

	}

	public double get_trLong() {
		return long_topright;
	}

	public Point2D get_Pixel() {
		return pixel;
	}

    public double get_centroidLat(){
        return lat_centroid;
    }

    public double get_centroidLong(){
        return long_centroid;
    }

    public static void main(String[] args) {
    	double lat = Double.parseDouble(args[0]);
    	double lon = Double.parseDouble(args[1]);

    	System.out.println("Starting lat: " + lat + " , Starting lon: " + lon);
    	Point2D p = new Point2D(0.9,1.9);
    	Pixelization pixel = new Pixelization(lat, lon, p);
    	System.out.println("Lat : " + pixel.get_brLat() + "Long + " + pixel.get_brLong());





    }

}
