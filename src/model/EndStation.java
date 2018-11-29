package model;

import java.io.IOException;
import java.util.Collection;

import controller.Simulation;
import io.Statistics;
import view.StartButton;

/**
 * Class for the end station. This is the last station where all objects are collected
 *
 * @author Jaeger, Schmidt modified by Team16
 * @version 2018-11-28
 */
public class EndStation extends SimpleStation {

	/** instance of the start station */
	@SuppressWarnings("unused")
	private static EndStation theEndStation;
	private static long allTime;

	/** (private!) Constructor, creates a new end station
	 *
	 * @param label of the station 
	 * @param inQueue the incoming queue
	 * @param outQueue the outgoing queue
	 * @param xPos x position of the station 
	 * @param yPos y position of the station 
	 * @param image image of the station  
	 */
	private EndStation(String label, SynchronizedQueue inQueue, SynchronizedQueue outQueue, int xPos, int yPos, String image){
		super(label, inQueue, outQueue, xPos, yPos, image);


	}

	/** creates a new end station
	 *
	 * @param label of the station 
	 * @param inQueue the incoming queue
	 * @param outQueue the outgoing queue
	 * @param xPos x position of the station 
	 * @param yPos y position of the station 
	 * @param image image of the station  
	 */
	public static void create(String label, SynchronizedQueue inQueue, SynchronizedQueue outQueue, int xPos, int yPos, String image){

		theEndStation = new EndStation(label, inQueue, outQueue, xPos, yPos, image);

	}

	@Override
	protected boolean work() {

		//let the thread wait only if there are no objects in the incoming queue
		if (numberOfInQueueObjects() == 0 ) return false;

		//If there is an inqueue object found, handle it
		if (numberOfInQueueObjects() > 0) this.handleObject(this.getNextInQueueObject());

		//maybe there is more work to do
		return true;

	}

	@Override
	protected void handleObject(TheObject theObject){


		// the object chooses the outgoing queue and enter it
		theObject.enterOutQueue(this);

		//  this is a just for fun action, the object gets a new location, but CAUTION !!!! magic numbers :-( !!!!!!
		theObject.theView.setLocation((this.getXPos() -100) + 18 * numberOfOutQueueObjects(), this.getYPos() + 120);

		//End the simulation if the condition is met

		try {
			endSimulation();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}


	/** End the simulation if the condition is met
	 *and prints the Time which was needed for the process
	 *
	 */
	private void endSimulation() throws IOException {

		// Are all objects in the stations outgoing queue, then we are finish
		if(TheObject.getAllObjects().size() == numberOfOutQueueObjects()){
			allTime=Simulation.getGlobalTime()- StartButton.getStartTime2();
			System.out.println("Benötigte Zeiteinheit: "+allTime);
			TheObject.createDiagramWithSizeAndTime();
			Statistics.show("\n--- Simulation beendet ----");

			//show some station statistics
			for (ProcessStation station : ProcessStation.getAllProcessStations()) {
				station.printStatistics();
			}

			//show some objects statistics
			for (Object object : this.outGoingQueue){
				((TheObject) object).printStatistics();
			}
			TheObject.printStatsPerObject();

			// end simulation
			// System.exit(0);

		}
	}

	@Override
	protected void handleObjects(Collection<TheObject> theObjects) {

	}

	@Override
	protected Collection<TheObject> getNextInQueueObjects() {
		return null;
	}

	@Override
	protected Collection<TheObject> getNextOutQueueObjects() {
		return null;
	}




}
