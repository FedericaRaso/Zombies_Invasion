// Federica Raso
// classe per i proiettili

import java.awt.geom.*;
import java.awt.Color;
import java.awt.Graphics2D;

public class Pallottola{
	
	private Ellipse2D.Double corpo;
	private Point2D centro, centrop;
	private Vettore direction;
	private double vel, raggio;
	
	public Pallottola(PG p, Rectangle2D bounds){
		
		raggio = 3;
		vel = 4;
		direction = new Vettore(p.getDirectionShoot());
		centrop = p.getCentre();
		centro = new Point2D.Double(centrop.getX() + ((p.getRaggio()*5)/4)*Math.cos((Math.toRadians(direction.getAngle()))), centrop.getY() + ((p.getRaggio()*5)/4)*Math.sin((Math.toRadians(direction.getAngle()))));
		corpo = new Ellipse2D.Double( (centro.getX()-raggio), (centro.getY()-raggio), raggio*2, raggio*2);
	}
	
	public double getVel(){
		return vel;
	}
	
	public Point2D getCentre(){
		return centro;
	}
	
	public void setDirection(double x, double y){
		direction.setX(x);
		direction.setY(y);
		direction.normalize();
	}
	
	public void setDirection(Vettore v){
		direction = v;
	}
	
	public Vettore getDirection(){
		return direction;
	}
	
	public void directTo( Point2D p )
	{
		setDirection( p.getX() - centro.getX(), p.getY() - centro.getY() );
	}
	
	public double getRaggio(){
		return raggio;
	}
	
	public Ellipse2D.Double getCorpo(){
		return corpo;
	}
	
	public void move(){
		centro = new Point2D.Double( centro.getX()+(direction.getX()*vel) , centro.getY()+(direction.getY()*vel)  );
		corpo.setFrame( (centro.getX()-raggio), (centro.getY()-raggio), raggio*2, raggio*2);
	}
	
	public void disegna(Graphics2D g2){
		g2.setColor(new Color(100, 180, 100));
		g2.fill(corpo);
	}
}
