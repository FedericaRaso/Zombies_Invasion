// Federica Raso
// classe che gestisce i vettori direzione

public class Vettore{
	
	private double x, y;
	
	public Vettore( double nx, double ny){
		x = nx;
		y = ny;
	}
	
	public Vettore(Vettore v){
		x = v.getX();
		y = v.getY();
		normalize();
	}
	
	public void setX(double nx){
		x = nx;
	}
	
	public double getX(){
		return x;
	}
	
	public void setY(double ny){
		y = ny;
	}
	
	public double getY(){
		return y;
	}
	
	public double length(){
		return Math.sqrt((x*x) + (y*y));
	}
	
	public void normalize(){
		double l = length();
			x = x/l;
			y = y/l;
	}
	
	public double getAngle(){
		if(y<0)
			return 360-Math.toDegrees(Math.acos(x));
		else
			return Math.toDegrees(Math.acos(x));
	}

}
