package controller;

import com.sun.org.apache.xpath.internal.SourceTree;
import view.SimulationView;
import io.Factory;
import io.Statistics;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;
import model.Actor;

/**
 * The main class, controls the flow of the simulation
 * 
 * @author Jaeger, Schmidt modified by Team16
 * @version 2018-11-27
 */
public class Simulation {
	
	/** is the simulation running*/
	public static boolean isRunning = false;  
	
	/** a speed factor for the clock to vary the speed of the clock in a simple way*/
	public static int SPEEDFACTOR = 2;
	
	/**the beat or speed of the clock, e.g. 300 means one beat every 300 milli seconds*/
	public static final int CLOCKBEAT = 300 * SPEEDFACTOR;
	
	/**the global clock */
	//the clock must be thread safe -> AtomicLong. The primitive type long isn't, even if synchronized
	private static AtomicLong clock = new AtomicLong(0); 
	
	
	/**
	 * starts the simulation and asks which scenario should start
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		System.out.println("Welches Szenario soll gestartet werden? \n 0 für Szenario 0 \n 1 für Szenario 1 \n 2 für Szenario 2 \n 3 für Szenario 3 \n 4 für Szenario 4");
		Scanner scan = new Scanner(System.in);
		int i =scan.nextInt();
		Factory.setScenario(i);


		//a new simulation
		Simulation theSimulation = new Simulation();
		theSimulation.init();
		Factory.profitCounter();
		
	}
	
	/**
	 * initialize the simulation
	 * 
	 */
	private void init(){
		
		//create all stations and objects for the starting scenario out of XML
		Factory.createStartScenario();
				
		//the view of our simulation
		new SimulationView();
					
		// set up the the heartbeat (clock) of the simulation
		new HeartBeat().start();
		 		
		Statistics.show("---- Simulation gestartet ---\n");
				
		// start all the actor threads
		for (Actor actor : Actor.getAllActors()) {
			actor.start();		
						
		}
		
		/*
		 * Hinweis: wenn nicht �ber den Startbutton gestartet werden soll oder die Simulation ohne View laufen soll,
		 * den auskommentierten Code unten verwenden 
		 */
				
		/*
		//Zeitpuffer vor Start -> sonst l�uft der letzte manchmal nicht los
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		//wake up the start station -> lets the simulation run
		StartStation.getStartStation().wakeUp();
		
		*/
		
	}
			
	
	/**
	 * The heartbeat (the pulse) of the simulation, controls the clock.
	 * 
	 */
	private class HeartBeat extends Thread {
		
		@Override
		public void run() {
			
			while(true){
				
				try {
				
					Thread.sleep(CLOCKBEAT);
					
					//Increase the global clock
					clock.incrementAndGet();
					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	
	/** Get the global time
	 * 
	 * @return the global time
	 */
	public static long getGlobalTime() {
		return clock.get();
	}
	
}
