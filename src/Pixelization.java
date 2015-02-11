/**
 * Created by Tharald on 10/02/15.
 */
import java.util.*;
import java.io.*;



public class Pixelization {
		double radian_long = 0;
		double radian_lat = 1.57079633;
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
		this.long_bottomright = longbottomleft;
		this.lat_bottomright = compute_lat_bottomright(latbottomleft, longbottomleft);
		this.long_topleft = compute_long_topleft(latbottomleft, longbottomleft);
		this.lat_topleft = latbottomleft;
		this.long_topright = compute_long_topleft(latbottomleft, longbottomleft);
		this.lat_bottomright = compute_lat_bottomright(latbottomleft, longbottomleft);
		this.lat_centroid = compute_lat_centroid(lat_bottomleft, lat_bottomright);
		this.long_centroid = compute_long_centroid(lat_bottomleft, long_topleft);
	}

	private double compute_long_topleft(double latbottomleft, double longbottomleft) {
		double x1 = latbottomleft;
		double y1 = longbottomleft;
		double x2 = x1;
		double y2; 

		y2 = y1 - Math.acos((Math.cos(radian_long) - Math.sin(x1)*Math.sin(x2))/ (Math.cos(x1)*Math.cos(x2)));
		return y2; 
	}

	private double compute_lat_centroid(double latbottomleft, lat_bottomright) {
		return (latbottomleft + lat_bottomright)/2;
	}

	private double compute_long_centroid(double longbottomleft, double longtopleft) {
		return (longbottomleft + long_topleft)/2;
	}

	private double compute_lat_bottomright(double latbottomleft, double longbottomleft) {
		double x1 = latbottomleft;
		double y1 = longbottomleft;
		double y2 = y1; 
		double recipx2;
		double x2; 
		recipx2 = Math.atan((Math.cos(radian_lat)/(Math.cos(x1)*Math.cos(y1- y2))) - (Math.tan(x1)/Math.cos(y1 - y2)))

		x2 = 1/recipx2;

		return x2; 

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

	public double get_Pixel() {
		return pixel;
	}


}
