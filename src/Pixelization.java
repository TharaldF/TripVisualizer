import java.util.*;
import java.io.*;
import java.awt.*;
import java.lang.Math;

public class Pixelization {
		int id; 
		int x;
		int y;
		double CENTERLON = -97.5;
		double CENTERLAT = 37; 
		double YHEIGHT = 0.00722814;
		double XWIDTH =  0.00944344;
		Point2D bottomleft;
		Point2D bottomright; 
		Point2D topleft;
		Point2D topright; 
		Point2D centroid; 


	public Pixelization(int x, int y) {
		this.x = x;
		this.y = y;
		this.id = createid(x, y);
		this.bottomleft = createbottomleft(x, y);
		this.bottomright = createbottomright(x,y);
		this.topleft = createtopleft(x,y);
		this.topright = createtopright(x,y);
		this.centroid = createcentroid();




	}
	private int createid(int a, int b) {
		return (int)0.5*(a+b)*(a+b+1)+b;
	}

	private Point2D createbottomleft(int i, int j) {
		double lon, lat; 
		double cosangle = Math.toDegrees(Math.cos(Math.toRadians(CENTERLAT + j)));
		lon = (CENTERLON + (YHEIGHT*i)/cosangle); 
		lat = (CENTERLAT + YHEIGHT*j);
		Point2D p = new Point2D(lon, lat);
		return p; 

	}
	private Point2D createbottomright(int i, int j) {
		i++;
		double lon, lat; 
		double cosangle = Math.toDegrees(Math.cos(Math.toRadians(CENTERLAT + j)));
		lon = (CENTERLON + (YHEIGHT*i)/cosangle); 
		lat = (CENTERLAT + YHEIGHT*j);
		Point2D p = new Point2D(lon, lat);
		return p; 

	}

	private Point2D createtopleft(int i, int j) {
		j++;
		double lon, lat; 
		double cosangle = Math.toDegrees(Math.cos(Math.toRadians(CENTERLAT + j)));
		lon = (CENTERLON + (YHEIGHT*i)/cosangle); 
		lat = (CENTERLAT + YHEIGHT*j);
		Point2D p = new Point2D(lon, lat);
		return p; 

	}

	private Point2D createtopright(int i, int j) {
		j++;
		i++;
		double lon, lat; 
		double cosangle = Math.toDegrees(Math.cos(Math.toRadians(CENTERLAT + j)));
		lon = (CENTERLON + (YHEIGHT*i)/cosangle); 
		lat = (CENTERLAT + YHEIGHT*j);
		Point2D p = new Point2D(lon, lat);
		return p; 

	}

	private Point2D createcentroid() {
		double lon, lat; 
		lon = (bottomleft.x() + bottomright.x())/2;
		lat = (bottomleft.y() + topleft.y())/2;



		Point2D p = new Point2D(lon, lat);
		return p; 

	}



	public Point2D get_br() {
		return bottomright;

	}

	public int get_x() {
		return x;

	}

	public int get_y() {
		return y; 
	}

	public int get_id() {
		return id;
	}

	public Point2D get_bl() {
		return bottomleft;

	}

	public Point2D get_tl() {
		return topleft;

	}

	public Point2D get_tr() {
		return topright;

	}

    public Point2D get_centroid(){
        return centroid;
    }
/*
    public static void main(String[] args) {
    	double lat = Double.parseDouble(args[0]);
    	double lon = Double.parseDouble(args[1]);

    	System.out.println("Starting lat: " + lat + " , Starting lon: " + lon);
    	Point2D p = new Point2D(0.9,1.9);
    	Pixelization pixel = new Pixelization(lat, lon, p);
    	System.out.println("Lat : " + pixel.get_brLat() + "Long + " + pixel.get_brLong());





    }
    */

}