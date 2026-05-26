// Federica Raso
// classe per gestire i file

import java.io.*;
import java.lang.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class ZombieFile{
	
	private PG p;
	private ZombieFileS files;
	private ZombieFileR filer;
	
	public ZombieFile(PG ammazzazombie){
		
		   p = ammazzazombie;
		   
		   files = new ZombieFileS(ammazzazombie);
		   filer = new ZombieFileR(ammazzazombie);
	}
	
	public void aggiungi(){
		
		int num = 0;
		
		if(filer.openFile()){
		num = filer.leggiNum();
		filer.closeFile();
		}
		
		int puntitot = num + p.getPunti();
		
		if(files.openFile()){
		files.scrivi("" + puntitot);
		files.closeFile();
		}
		
	}
	
	public void scrivi1(){
	    
	    if(files.openFile()){	
	    	    String testo = files.scriviDati();
	    	    files.scrivi(testo);
	    	    files.closeFile();
	    }
	}
	
	public String leggi1(){
	    
	    String testo = "";	
	    if(filer.openFile()){
		testo = filer.leggi();
		filer.closeFile();
	    }
	    return testo;
	}
	
	
}

class ZombieFileS{

   private PG p;	
   private Formatter output; 
   
   public ZombieFileS(PG ammazzazombie){
   	   p = ammazzazombie;
   }
      
   public boolean openFile(){
   	   
      try{
      	      
         output = new Formatter( p.getNome()+".txt" );
      } 
      
      catch ( SecurityException securityException ){
      	      
         System.err.println("Non hai i permessi per scrivere questo file" );
         System.exit( 1 );
      }
      
      catch ( FileNotFoundException filesNotFoundException ){
      	      
         System.err.println( "Errore nel creare il file" );
         System.exit( 1 );
      }
      
       if(output != null)
      	      return true;
      else
      	      return false;
      
   } 
	
   public String scriviDati(){
   	   
   	   return String.format( "%d \n", p.getPunti());
   }
   
   public void scrivi(String testo){
   	   
   	    try{
   	   	  output.format( testo );
            } 
         
           catch ( FormatterClosedException formatterClosedException ){
           	   
           	   System.err.println( "Errore di scrittura" );
           	   return;
           } 
   }

   public void closeFile()
   {
      if ( output != null )
         output.close();
   }
   
} 


class ZombieFileR{
   
   private PG p;	
   private Scanner input;
   
   public ZombieFileR(PG ammazzazombie){
   	   p = ammazzazombie;
   }
   
   public boolean openFile(){
   	   
      try{
      	      
         input = new Scanner( new File(p.getNome()+".txt") );
      } 
      
      catch ( FileNotFoundException filesNotFoundException ){
      	      
         System.err.println( "Errore durante l'apertura del file" );
         System.exit( 1 );
      }
      
      if(input != null)
      	      return true;
      else
      	      return false;
      
   }
   
   public String leggi(){
   	   
   	   String contenuto = "";
   	  
   	   try{
   	   	   while(input.hasNext())
   	   	   	   contenuto += input.next() + " ";
   	   
   	   	   return contenuto;
   	    }
   	    
   	    catch ( NoSuchElementException elementException ){
   	    	    
   	    	    System.err.println( "File con struttura errata." );
   		    input.close();
   	    }
   	    
   	    catch ( IllegalStateException stateException ){
   	    	    
   		    System.err.println( "Error durante la lettura del file." );
   	     }
   		
   	     return "";
   }
   
   public int leggiNum(){
   	   int num = 0;
   	   
   	   try{
   	   	   while(input.hasNext())
   	   	   	   num = input.nextInt();
   	    }
   	    
   	    catch ( NoSuchElementException elementException ){
   	    	    
   	    	    System.err.println( "File con struttura errata." );
   		    input.close();
   	    }
   	    
   	    catch ( IllegalStateException stateException ){
   	    	    
   		    System.err.println( "Error durante la lettura del file." );
   	     }
   		
   	     return num;
   }
   	
   public void closeFile(){
   	   
   	if ( input != null )
   		input.close();
   }
   
   
   
}