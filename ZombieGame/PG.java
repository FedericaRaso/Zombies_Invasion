// Federica Raso
// classe del personaggio giocabile

import java.awt.geom.*;
import java.awt.Color;
import java.awt.Graphics2D;

public class PG{
	
	private Rectangle2D dimwin; // dimensioni del pannello (zombiepanel)
	private Ellipse2D.Double corpo, occhiodx, occhiosx;
	private Point2D centro;
	private Vettore directionshoot, directionwalk;
	private String nome;
	private int vite, pallottole, punti;
	private double vel, dimocchio, raggio;
	
	public PG(Rectangle2D bounds){
		dimwin = bounds;
		vite = 2;
		pallottole = 15;
		punti = 0;
		vel = 0;
		raggio = 15;
		dimocchio = 5;
		directionwalk = new Vettore(0, 0);
		directionshoot = new Vettore(0, 0);
		centro = new Point2D.Double(dimwin.getWidth()/2, dimwin.getHeight()/2); // posiziono il pg al centro del pannello
		corpo = new Ellipse2D.Double( (centro.getX()-raggio), (centro.getY()-raggio), raggio*2, raggio*2);
		occhiosx = new Ellipse2D.Double( (centro.getX() - dimocchio/2)+((raggio*3)/4)*Math.cos((Math.toRadians(directionshoot.getAngle()-30))), (centro.getY() - dimocchio/2)+((raggio*3)/4)*Math.sin((Math.toRadians(directionshoot.getAngle()-30))), dimocchio, dimocchio);
		occhiodx = new Ellipse2D.Double( (centro.getX() - dimocchio/2)+((raggio*3)/4)*Math.cos((Math.toRadians(directionshoot.getAngle()+30))), (centro.getY() - dimocchio/2)+((raggio*3)/4)*Math.sin((Math.toRadians(directionshoot.getAngle()+30))), dimocchio, dimocchio);
		
	}
	
	public void setNome(String nuovonome){
		nome = nuovonome;
	}
	
	public String getNome(){
		return nome;
	}
	
	public void setVite(int nuovevite){
		if(nuovevite < 0)
			nuovevite = 0;
		vite = nuovevite;
	}
	
	public int getVite(){
		return vite;
	}
	
	public String getVite2(){
		String vita = "";
		for(int i = 0; i<vite; i++)
			vita += "0";
		return vita;
	}
	
	public void setColpi(int nuovepallottole){
		pallottole = nuovepallottole;
	}
	
	public int getColpi(){
		return pallottole;
	}
	
	public void setPunti(int nuovipunti){
		punti = nuovipunti;
	}
	
	public int getPunti(){
		return punti;
	}
	
	public double getRaggio(){
		return raggio;
	}
	
	public void setCentre(double x, double y){
		centro = new Point2D.Double(x, y);
	}
	
	public Point2D getCentre(){
		return centro;
	}
	
	public void setVel(double v){
		vel = v;
	}
	
	public double getVel(){
		return vel;
	}
	
	public void setDirectionWalk(double x, double y){ // metodo per settare la direzione in cui il pg si muove
		directionwalk.setX(x);
		directionwalk.setY(y);
		if( x != 0 && y != 0)
		directionwalk.normalize();
	}
	
	public void setDirectionWalk(Vettore v){
		directionwalk = v;
	}
	
	public Vettore getDirectionWalk(){
		return directionwalk;
	}
	
	public void setDirectionShoot(double x, double y){ // metodo per settare la direzione in cui il pg guarda (punta verso il cursore del mouse)
		directionshoot.setX(x);
		directionshoot.setY(y);
		directionshoot.normalize();
		occhiosx.setFrame( (centro.getX() - dimocchio/2)+((raggio*3)/4)*Math.cos((Math.toRadians(directionshoot.getAngle()-30))), (centro.getY() - dimocchio/2)+((raggio*3)/4)*Math.sin((Math.toRadians(directionshoot.getAngle()-30))), dimocchio, dimocchio);
		occhiodx.setFrame( (centro.getX() - dimocchio/2)+((raggio*3)/4)*Math.cos((Math.toRadians(directionshoot.getAngle()+30))), (centro.getY() - dimocchio/2)+((raggio*3)/4)*Math.sin((Math.toRadians(directionshoot.getAngle()+30))), dimocchio, dimocchio);
	}
	
	public void setDirectionShoot(Vettore v){
		directionshoot = v;
	}
	
	public Vettore getDirectionShoot(){
		return directionshoot;
	}
	
	public Ellipse2D.Double getCorpo(){
		return corpo;
	}
	
	public void move(){ // metodo che gestisce il movimento
		// controllo che il pg non esca dal pannello
		if((centro.getX() + raggio >= dimwin.getWidth()) && directionwalk.getX() > 0)
			directionwalk.setX(0);
		else 
			if((centro.getX() - raggio <= 0) && directionwalk.getX() < 0)
				directionwalk.setX(0);

		if((centro.getY() + raggio >= (dimwin.getHeight() - 68)) && directionwalk.getY() > 0) // -30 evita di finire sotto il pannello coi dati
			directionwalk.setY(0);
		else
			if((centro.getY() - raggio <= 0) && directionwalk.getY() < 0)
				directionwalk.setY(0);
		
		// traslo il centro e tutto il resto di conseguenza
		centro = new Point2D.Double( centro.getX()+(directionwalk.getX()*vel) , centro.getY()+(directionwalk.getY()*vel)  );
		corpo.setFrame( (centro.getX()-raggio), (centro.getY()-raggio), raggio*2, raggio*2);
		occhiosx.setFrame( (centro.getX() - dimocchio/2)+((raggio*3)/4)*Math.cos((Math.toRadians(directionshoot.getAngle()-30))), (centro.getY() - dimocchio/2)+((raggio*3)/4)*Math.sin((Math.toRadians(directionshoot.getAngle()-30))), dimocchio, dimocchio);
		occhiodx.setFrame( (centro.getX() - dimocchio/2)+((raggio*3)/4)*Math.cos((Math.toRadians(directionshoot.getAngle()+30))), (centro.getY() - dimocchio/2)+((raggio*3)/4)*Math.sin((Math.toRadians(directionshoot.getAngle()+30))), dimocchio, dimocchio);
	}

	public void directTo( Point2D p ) // chiama il setdirection della direzione per sparare
	{
		setDirectionShoot( p.getX() - centro.getX(), p.getY() - centro.getY() );
	}
	
	public void disegna(Graphics2D g2){
		g2.setColor(Color.WHITE);
		g2.fill(corpo);
		g2.setColor(Color.BLACK);
		g2.fill(occhiodx);
		g2.fill(occhiosx);
	}
}
