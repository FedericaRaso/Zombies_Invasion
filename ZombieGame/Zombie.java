// Federica Raso
// classe degli zombie

import java.awt.geom.*;
import java.awt.Color;
import java.awt.Graphics2D;

public class Zombie{
	
	private Ellipse2D.Double corpo, occhiodx, occhiosx;
	private Point2D centro;
	private Vettore direction;
	private int puntivita;
	private double vel, dimocchio, raggio;
	
	public Zombie(int ondata, Rectangle2D bounds){
		
		dimocchio = 5;
		raggio = 15;
		direction = new Vettore(0, 0);
		posiRandom(bounds);
		corpo = new Ellipse2D.Double( (centro.getX()-raggio), (centro.getY()-raggio), raggio*2, raggio*2);
		occhiosx = new Ellipse2D.Double( (centro.getX() - dimocchio/2)+((raggio*3)/4)*Math.cos((Math.toRadians(direction.getAngle()-30))), (centro.getY() - dimocchio/2)+((raggio*3)/4)*Math.sin((Math.toRadians(direction.getAngle()-30))), dimocchio, dimocchio);
		occhiodx = new Ellipse2D.Double( (centro.getX() - dimocchio/2)+((raggio*3)/4)*Math.cos((Math.toRadians(direction.getAngle()+30))), (centro.getY() - dimocchio/2)+((raggio*3)/4)*Math.sin((Math.toRadians(direction.getAngle()+30))), dimocchio, dimocchio);
		
		// a seconda dell'ondata aumenta la vita o la velocita' dello zombie
		if(ondata % 2 == 0){
			puntivita = 2 + (ondata-1)/4;
			vel = 0.3;
		}
		else{
			puntivita = 2;
			if(ondata < 10)
				vel = 0.3 + (ondata-1)/10;
			else
				vel = 0.3 + (ondata-1)/20;
		}
	}
	
	public Zombie(Rectangle2D bounds){ // costruttore zombie gigante
		dimocchio = 15;
		raggio = 45;
		vel = 0.3;
		puntivita = 30;
		direction = new Vettore(0, 0);
		posiRandom(bounds);
		corpo = new Ellipse2D.Double( (centro.getX()-raggio), (centro.getY()-raggio), raggio*2, raggio*2);
		occhiosx = new Ellipse2D.Double( (centro.getX() - dimocchio/2)+((raggio*3)/4)*Math.cos((Math.toRadians(direction.getAngle()-30))), (centro.getY() - dimocchio/2)+((raggio*3)/4)*Math.sin((Math.toRadians(direction.getAngle()-30))), dimocchio, dimocchio);
		occhiodx = new Ellipse2D.Double( (centro.getX() - dimocchio/2)+((raggio*3)/4)*Math.cos((Math.toRadians(direction.getAngle()+30))), (centro.getY() - dimocchio/2)+((raggio*3)/4)*Math.sin((Math.toRadians(direction.getAngle()+30))), dimocchio, dimocchio);
	}
	
	public void posiRandom(Rectangle2D bounds){ // metodo che posiziona gli zombie casualmente ai margini del pannello
		int num = (int)(Math.round(Math.random()*3 + 1));
		double x=0, y=0;
		switch(num){
		case 1:
			x = 0;
			y = Math.random()*bounds.getHeight();
			centro = new Point2D.Double(x + raggio, y + raggio);
			break;
		case 2:
			x = bounds.getWidth();
			y = Math.random()*bounds.getHeight();
			centro = new Point2D.Double(x - raggio, y + raggio);
			break;
		case 3:
			x = Math.random()*bounds.getWidth();
			y = 0;
			centro = new Point2D.Double(x + raggio, y + raggio);
			break;
		case 4:
			x = Math.random()*bounds.getWidth();
			y = bounds.getHeight();
			centro = new Point2D.Double(x + raggio, y - raggio);
			break;
		default:;
		}
	}
	
	public void setPuntiVita(int nuovipv){
		if(nuovipv < 0)
			nuovipv = 0;
		puntivita = nuovipv;
	}
	
	public int getPuntiVita(){
		return puntivita;
	}
	
	public double getVel(){
		return vel;
	}
	
	public double getRaggio(){
		return raggio;
	}
	
	public void setCentre(int x, int y){
		centro = new Point2D.Double(centro.getX()-x*(direction.getX()) , centro.getY()-y*(direction.getY()) );
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
		direction.normalize();
	}
	
	public Vettore getDirection(){
		return direction;
	}
	
	public void directTo( Point2D p )
	{
		setDirection( p.getX() - centro.getX(), p.getY() - centro.getY() );
	}
	
	public Ellipse2D.Double getCorpo(){
		return corpo;
	}
	
	public void move(){
		centro = new Point2D.Double( centro.getX()+(direction.getX()*vel) , centro.getY()+(direction.getY()*vel)  );
		corpo.setFrame( (centro.getX()-raggio), (centro.getY()-raggio), raggio*2, raggio*2);
		occhiosx.setFrame( (centro.getX() - dimocchio/2)+((raggio*3)/4)*Math.cos((Math.toRadians(direction.getAngle()-30))), (centro.getY() - dimocchio/2)+((raggio*3)/4)*Math.sin((Math.toRadians(direction.getAngle()-30))), dimocchio, dimocchio);
		occhiodx.setFrame( (centro.getX() - dimocchio/2)+((raggio*3)/4)*Math.cos((Math.toRadians(direction.getAngle()+30))), (centro.getY() - dimocchio/2)+((raggio*3)/4)*Math.sin((Math.toRadians(direction.getAngle()+30))), dimocchio, dimocchio);
	}
	
	public void disegna(Graphics2D g2){
		g2.setColor(new Color(0, 180, 100));
		g2.fill(corpo);
		g2.setColor(new Color(200, 20, 0));
		g2.fill(occhiodx);
		g2.fill(occhiosx);
	}
}
