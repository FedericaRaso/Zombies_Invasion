// Federica Raso
// Progetto scelto: Zombies

import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.geom.*;
import java.applet.AudioClip;
import java.applet.Applet;
import sun.audio.*;
import java.io.*;
import java.lang.*;
import java.net.URL;

public class ZombieProg2{
	
	public static void main(String [] args){
		MyFrame zframe = new MyFrame(); // creo la finestra 
		zframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		zframe.setVisible(true);
	}
} // fine classe col main

class MyFrame extends JFrame{ // finestra (tutti i pannelli sono classi interne alla finestra)
	
	private ZombiePanel zombiepanel;
	private ZombieFile file;
	private File puntipg;
	private DatiPanel panel;
	private int schermoW, schermoH;
	private int ondatazombie = 0;
	private int numzombie = 0;
	private PG ammazzazombie; // personaggio con cui giochero'
	private ArrayList<Pallottola> sparo = new ArrayList<Pallottola>(); // array di proiettili
	private ArrayList<Zombie> zombie = new ArrayList<Zombie>(); // array degli zombie
	private Timer t; // timer che gestisce tutti i movimenti all'interno dello zombiepanel
	private AudioClip m1, m2;	
	
	public MyFrame(){
		super(" Zombie Splatter ");
		
		// setto la dimensione e la posizione della finestra
		Dimension schermo = Toolkit.getDefaultToolkit().getScreenSize();
		schermoH = schermo.height;
		schermoW = schermo.width;
		setSize( 700, 500);
		setLocation( schermoW/2 - 250, schermoH/2 - 250);
		setLayout(new BorderLayout());
		
		URL url1 = getClass().getResource("4-14 - Haunted House BGM.wav");
		URL url2 = getClass().getResource("4-26 - Game Over.wav");
			
		m1 = Applet.newAudioClip(url1);
		m2 = Applet.newAudioClip(url2);
		
		ammazzazombie = new PG(getBounds()); // creo il mio personaggio
		
		inizializzaPG();
		
		zombiepanel = new ZombiePanel(); // creo il pannello in cui si muoveranno il personaggio e gli zombie
		zombiepanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)); // setto il cursore a croce nel pannello
		panel = new DatiPanel(); // creo il pannello che contiene i dati relativi alle ondate, alle munizioni e alle vite del pg
		
		// aggiungo i pannelli alla finestra
		add(zombiepanel, BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);
	}
	
	public void inizializzaPG(){ // metodo che setta il nome del personaggio e controlla se esiste gia' un file che lo riguarda
		
		String name = JOptionPane.showInputDialog("Inserisci nome"); // do un nome al pg
		ammazzazombie.setNome(name);
		
		file = new ZombieFile(ammazzazombie); // crea uno zombiefile che si occupera' di leggere e scrivere file
		puntipg = new File(ammazzazombie.getNome()+".txt"); // creo un nuovo file col nome del pg
		if(puntipg.exists()) //se esiste mostro i punti attuali
			JOptionPane.showMessageDialog(null, ammazzazombie.getNome() + "  Punti: " + file.leggi1());
		else   // altrimenti gli do il benvenuto
			JOptionPane.showMessageDialog(null, "Benvenuto " + ammazzazombie.getNome() + "!");
		
	}
		
	class ZombiePanel extends JPanel{ 
		
		private double px=0, py=0; // variabili che indicano la direzione in cui si muove il pg
		private long ultimozombiecreato; // variabile che prende i millisecondi in cui ho creato l'ultimo zombie
		private long inizioRic; // variabile che prende in memoria il momento in cui inizio la ricarica
		private long millis = 0; // variabile che conta i millisecondi scanditi dal timer
		private int zombiecreati = 0; // variabile che conta quanti zombie ho creato finora (per ogni ondata)
		private boolean ric = false; // variabile che tiene in memoria se e' iniziata la fase di ricarica munizioni
		
		// metodo che setta il numero di zombie di ogni ondata, per poi crearli
		public void setOndata(){
			if( ondatazombie % 10 != 0)
				numzombie = 2*ondatazombie;
			else
				numzombie = ondatazombie/2; // nelle ondate multiple di 10 arrivano meno zombie, ma anche lo zombie gigante
			
			zombiecreati = 0;
			ultimozombiecreato = 0;
		}
		
		// metodo che crea uno zombie ogni secondo, finchè non raggiunge il numero massimo dell'ondata in corso
		public void creaZombie(){
			if(zombiecreati >= numzombie)
				return; // se ho creato tutti gli zombie dell'ondata esce
			else
				if(millis-ultimozombiecreato > 1000){
					if(zombiecreati<numzombie){
						zombie.add(new Zombie(ondatazombie, getBounds()));
						zombiecreati++;
						ultimozombiecreato = millis;
					} // se e' passato un secondo aggiungo uno zombie all'array, aggiorno le variabili e ridisegno
					
					if((ondatazombie % 10 == 0) && (zombiecreati==numzombie))
						zombie.add(new Zombie(getBounds()));
					repaint();
				}
		}
		
		// metodo che disegna gli zombie nel pannello
		public void disegnaZombie(Graphics2D g2){
			if(zombie.isEmpty() == false)
			for(int i=0; i<zombie.size(); i++)
				(zombie.get(i)).disegna(g2);
		}
		
		public ZombiePanel(){ // costruttore del pannello
			
			m1.loop();
			
			t = new Timer(50, new ActionListener(){ // creo il timer per gestire tutti i movimenti nel pannello	
					
				public void actionPerformed(ActionEvent event){
					millis += 50; // viene aggiornato millis
					ammazzazombie.move();
					creaZombie(); //inizio a creare gli zombie
					if(zombie.isEmpty() == false){ // se ci sono zombie, li muovo verso il personaggio
						for(int i=0; i<zombie.size(); i++){
								(zombie.get(i)).directTo(ammazzazombie.getCentre()); // gli zombie si dirigono sempre verso il pg
								(zombie.get(i)).move();
						}
						intersezionipgz(); // controllo se gli zombie toccano il personaggio
						if(ammazzazombie.getVite() == 0){
							m1.stop();
							m2.play();
							riportasuFile();
							JOptionPane.showMessageDialog(null, "GAME OVER!");
							int risp = JOptionPane.showConfirmDialog(null, "Vuoi Giocare ancora?", "Game Over", JOptionPane.YES_NO_OPTION);
							if(risp == JOptionPane.YES_OPTION){ //scelgo cosa voglio fare dopo il gameover
								inizializzaPG();            // se scelgo di continuare, inizializzo i dati del pg
								ammazzazombie.setVite(2);
								ammazzazombie.setVel(0);
								ammazzazombie.setColpi(15);
								ammazzazombie.setCentre(getWidth()/2, getHeight()/2);
								ondatazombie = 0; //riporto l'ondata a 0
								zombie.clear(); // pulisco gli array
								sparo.clear();
								m1.loop();
								setOndata();
								panel.aggiorna();
							}
							else	
								System.exit(0); // altrimenti esco dal gioco
						}
					}
					else // se non ci sono zombie
						if(zombiecreati == numzombie){ // controllo se ho creato tutti gli zombie dell'ondata
							ondatazombie ++; // se si (ovvero se li ho uccisi tutti) passo all'ondata successiva
							setOndata();
							if(ammazzazombie.getVite() < 5) // se il pg ha meno di 5 vite gli aumento di 1 le vite
								ammazzazombie.setVite(ammazzazombie.getVite() + 1);
						}
						
					if(sparo.isEmpty() == false){ // se ho sparato, muovo gli spari
						for(int i=0; i<sparo.size(); i++){
							(sparo.get(i)).move();
							if(sparo.get(i).getCentre().getX() < 0 || sparo.get(i).getCentre().getY() < 0 || sparo.get(i).getCentre().getX() > zombiepanel.getWidth() || sparo.get(i).getCentre().getY() > zombiepanel.getHeight() )
								sparo.remove(i); // se lo sparo esce dalla finestra viene eliminato dall'array
						}
						intersezionisparo(); // controllo se i proiettili colpiscono gli zombie
					}
					if(ric == true){ // ric = true se ho premuto il pulsante di ricarica
						if(millis - inizioRic >= 2000){ // se sono passati 2 secondi 
							ammazzazombie.setColpi(15); // da quando ho attivato la ricarica
							panel.aggiorna(); // riporto al max i proiettili e aggiorno il pannello coi dati
							ric = false; // riporto ric a falso
						}
					}
					repaint();
				}
			}); // fine actionlistener del timer
			t.start(); // attivo il timer
			
			addKeyListener(new KeyAdapter(){ // aggiungo un keylistener per gestire il personaggio
					
						public void keyPressed(KeyEvent e){ // utilizzo il metodo keypressed per gestire i movimenti
							
							switch(e.getKeyCode()){
							case KeyEvent.VK_D: // mi sposto a destra, se arrivo al bordo del pannello mi fermo
								px = 1;
								break;
							case KeyEvent.VK_A: // mi sposto a sinistra
								px = -1;
								break;
								
							case KeyEvent.VK_S: // mi sposto verso il basso
								py = 1;
								break;
								
							case KeyEvent.VK_W: // mi sposto verso l'alto
								py = -1;
								break;
								
							case KeyEvent.VK_R: // ricarico l'arma
								if(ric == false) // controllo di non aver già iniziato la ricarica
									ricarica();
								break;
							default:;
							}
							ammazzazombie.setDirectionWalk(px, py);
							if(ondatazombie <= 10)
								ammazzazombie.setVel(1);
							else 
								ammazzazombie.setVel(2);
						}
						
						public void keyReleased(KeyEvent e){ // utilizzo il metodo keypressed per gestire i movimenti
							switch(e.getKeyCode()){
							case KeyEvent.VK_D:
							case KeyEvent.VK_A:
								px = 0;
								ammazzazombie.setVel(0);
								break;
								
							case KeyEvent.VK_W:
							case KeyEvent.VK_S:
								py = 0;
								ammazzazombie.setVel(0);
								break;
							
							default: ;
							}
						}
						
				}); // fine keylistener
			
			setFocusable(true);
			
			addMouseListener( new MouseAdapter(){ // aggiungo il mouse listener per gestire gli spari
					
				public void mouseClicked(MouseEvent e){
					if(ammazzazombie.getColpi() > 0){ // se ho i proiettili genero un proiettile a ogni click
						ammazzazombie.setColpi(ammazzazombie.getColpi() - 1); // decremento il numero di proiettili
						sparo.add(new Pallottola(ammazzazombie, getBounds())); // aggiungo il proiettile all'array
						panel.aggiorna();
					}
					else{ // se non ho proiettili ricarico al click
						if(ric == false)
							ricarica();
					}
				}
			}); // fine mouselistener
				
			addMouseMotionListener(new MouseMotionAdapter(){ // aggiungo il mouse motion listener per gestire la direzione in cui guarda il pg
					
				public void mouseMoved(MouseEvent e){
					ammazzazombie.directTo(e.getPoint()); // al movimento del mouse, cambio la direzione dove guarda il pg
					repaint();
				}
			}); // fine mousemotionlistener
		} // fine costruttore dello zombiepanel
			
			public void ricarica(){
				ric = true;
				ammazzazombie.setColpi(0); //setto i colpi a 0 per evitare di sparare
				inizioRic = millis;		
			} //inizializzo inizioRic
		
			public void intersezionipgz(){ //metodo che controlla le intersezioni zombie-pg
				for(int i=0; i<zombie.size(); i++)
					/*if((zombie.get(i).getCorpo()).intersects(ammazzazombie.getCentre().getX() - ammazzazombie.getRaggio(),
					(controllo con metodo intersects)	ammazzazombie.getCentre().getY() - ammazzazombie.getRaggio(),
										ammazzazombie.getRaggio()*2, ammazzazombie.getRaggio()*2)){ */
					// controllo intersezione "a mano"
					if(((zombie.get(i).getCentre().getX() - ammazzazombie.getCentre().getX())*(zombie.get(i).getCentre().getX() - ammazzazombie.getCentre().getX()) + 
						(zombie.get(i).getCentre().getY() - ammazzazombie.getCentre().getY())*(zombie.get(i).getCentre().getY() - ammazzazombie.getCentre().getY()))
						<= (zombie.get(i).getRaggio() + ammazzazombie.getRaggio())*(zombie.get(i).getRaggio() + ammazzazombie.getRaggio())){
						
						zombie.get(i).setCentre(40, 40);
						ammazzazombie.setVite(ammazzazombie.getVite() - 1);
						panel.aggiorna();
						repaint();
					} //se lo zombie interseca il pg, questo perde una vita e lo zombie viene mandato indietro
			} // fine metodo intersezionepg
			
			public void intersezionisparo(){ // metodo che controlla le intersezioni zombie-proiettili
				for(int i=0; i<zombie.size(); i++)
					for(int j=0; j<sparo.size(); j++){ // scorro i 2 array
						// se non esco dai limiti dell'array controllo se i proiettili toccano gli zombie
						if((zombie.size() > i && zombie.get(i) != null) && (sparo.size() > j && sparo.get(j) != null)){
							
						/*if((zombie.get(i).getCorpo()).intersects(sparo.get(j).getCentre().getX() - sparo.get(j).getRaggio(),
				        (controllo con metodo intersects)	sparo.get(j).getCentre().getY() - sparo.get(j).getRaggio(),
										sparo.get(j).getRaggio()*2, sparo.get(j).getRaggio()*2)){ */
						// controllo intersezione "a mano"	
						if(((zombie.get(i).getCentre().getX() - sparo.get(j).getCentre().getX())*(zombie.get(i).getCentre().getX() - sparo.get(j).getCentre().getX()) + 
							(zombie.get(i).getCentre().getY() - sparo.get(j).getCentre().getY())*(zombie.get(i).getCentre().getY() - sparo.get(j).getCentre().getY())) <=
							(zombie.get(i).getRaggio() + sparo.get(j).getRaggio())*(zombie.get(i).getRaggio() + sparo.get(j).getRaggio())){
										
							zombie.get(i).setPuntiVita(zombie.get(i).getPuntiVita() - 1);
							sparo.remove(j); // se si intersecano, lo zombie perde una vita e il proiettile viene rimosso
							j--; // decremento j in modo che scalando l'array non venga saltato un controllo
							repaint();
						}
						if(zombie.get(i).getPuntiVita() == 0){
							zombie.remove(i); // se lo zombie perde tutti i punti vita viene rimosso
							ammazzazombie.setPunti(ammazzazombie.getPunti() + 3*ondatazombie);
							panel.aggiorna(); // e il personaggio guadagna punti
							repaint();
						}
						}
					}
			} //fine metodo intersezionesparo
			
			public void paintComponent(Graphics g){
				
				super.paintComponent( g );
				setBackground( Color.GRAY );
				Graphics2D g2 = (Graphics2D)g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				
				ammazzazombie.disegna(g2); // chiamo il metodo disegna del personaggio
				disegnaZombie(g2); // chiamo il metodo per disegnare gli zombie
				if(sparo != null) // controllo che che ci siano dei proiettili
					for(int i=0; i<sparo.size(); i++) //se ci sono chiamo il loro metodo disegna
						(sparo.get(i)).disegna(g2);
			} // fine del paintcomponent
			
			public void riportasuFile(){ // metodo per riportare i dati su un file
				if(puntipg.exists()){
					file.aggiungi(); // aggiorno il file
					JOptionPane.showMessageDialog(null, ammazzazombie.getNome() + " Punti: " + file.leggi1());
				}
				else
					file.scrivi1(); // scrivo su file
			}
				
	} // fine zombiepanel
	
	class DatiPanel extends JPanel{ 
		private JLabel punti, pistola, vite, zombie, numondata; // label
		private JTextField points, proiettili, life, zom, ondata; //textfield coi valori
		private JButton pause; // bottone per mettere il gioco in pausa
		
		public DatiPanel(){ // costruttore del pannello
			
			setLayout( new FlowLayout()); //setto il flowlayout per questo pannello
			
			// creo le label
			punti = new JLabel("Punti: ");
			pistola = new JLabel("Pallottole: ");
			vite = new JLabel("Vite: ");
			numondata = new JLabel("Ondata: ");
			
			// creo il bottone a cui aggiungo un listener
			pause = new JButton("Pausa");
			pause.addActionListener( new ActionListener(){
					
					public void actionPerformed(ActionEvent event){
						
						if(t.isRunning()){
							t.stop();
							pause.setText("Continua");
						}
						else{
							t.restart();
							pause.setText("Pausa");
						}
						
						if(!(zombiepanel.hasFocus())){ //controllo se il focus e' sul pannello di gioco
							zombiepanel.requestFocus(); // se no, gli restituisce il focus
						}
					}
			});
			
			// creo i textfields
			points = new JTextField("" + ammazzazombie.getPunti(), 6);
			points.setEditable(false);
			proiettili = new JTextField("" + ammazzazombie.getColpi() + "/15");
			proiettili.setEditable(false);
			life = new JTextField("" + ammazzazombie.getVite2(), 5);
			life.setEditable(false);
			ondata = new JTextField("" + ondatazombie, 3);
			ondata.setEditable(false);
			
			// aggiungo tutto al pannello
			add(pause);
			add(vite);
			add(life);
			add(punti);
			add(points);
			add(pistola);
			add(proiettili);
			add(numondata);
			add(ondata);
		} // fine costruttore del datipanel
		
		public void aggiorna(){ //metodo che aggiorna i textfields con i nuovi valori
			points.setText("" + ammazzazombie.getPunti());
			proiettili.setText("" + ammazzazombie.getColpi() + "/15");
			life.setText("" + ammazzazombie.getVite2());
			ondata.setText("" + ondatazombie);
		}
	} // fine datipanel
	
} // fine classe MyFrame
