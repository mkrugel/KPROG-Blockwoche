package controller;

import com.sun.org.apache.xpath.internal.SourceTree;

import controller.model_plotter.CustomPoint;
import controller.view_plotter.PlotterPane;

import io.FactoryJson;
import model.TheObject;
import view.SimulationView;
import io.Factory;
import io.Statistics;

import java.io.*;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

import model.Actor;
import view.StartButton;

import javax.swing.*;

/**
 * The main class, controls the flow of the simulation
 *
 * @author Jaeger, Schmidt modified by Team16
 * @version 2018-11-27
 */
public class Simulation {

    /**
     * is the simulation running
     */
    public static boolean isRunning = false;

    /**
     * a speed factor for the clock to vary the speed of the clock in a simple way
     */
    public static int SPEEDFACTOR = 2;

    /**
     * the beat or speed of the clock, e.g. 300 means one beat every 300 milli seconds
     */
    public static final int CLOCKBEAT = 300 * SPEEDFACTOR;

    /**
     * the global clock
     */
    //the clock must be thread safe -> AtomicLong. The primitive type long isn't, even if synchronized
    private static AtomicLong clock = new AtomicLong(0);
    public static String[] parts;
    public static String[] partsO;
    static double[] values;
    static int[] objects;
    static String part2;
    static String partO1;
    static String partO2;


    /**
     * starts the simulation and asks which scenario should start
     *
     * @param args
     */
    public static void main(String[] args) {

        xmlJsonoderAuswertung();

    }

    /**
     * initialize the simulation
     */
    private void init() {

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
    }

        private void JSONinit() {

            //create all stations and objects for the starting scenario out of XML
            FactoryJson.createStartScenario();

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
     */
    private class HeartBeat extends Thread {

        @Override
        public void run() {

            while (true) {

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

    /**
     * Shows a GUI where you can choose if you want to open the "Auswertung" or the normal Simulation with xml/json
     */
    public static void xmlJsonoderAuswertung() {

        String[] option = {"XML", "JSON", "Auswertung"};
        try {
            int i = JOptionPane.showOptionDialog(null, "Wollen sie die Auswertung oder die Simulation?", "Auswertung oder Simulation", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);

            if (option[i].equals("XML")) {
                //method "SzenarioXML" searches in the xml folder after "Szenarien" and returns them
                SzenarioXML();

            } else if (option[i].equals("JSON")) {
                //method "SzenarioJSON" searches in the json folder after "Szenarien" and returns them
                SzenarioJSON();



            } else if (option[i].equals("Auswertung")) {
                //method "auswertug" starts the graph plotter which reads the statistics file
                auswertung();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /* Displays all XML szenarios so the user can choose what szenario should play
     *
     * @throws IOException
     */
    private static void SzenarioXML() throws IOException {
        //reads subdirectories
        Path path = Paths.get("xml");
        Statistics.show(path.toString());
        path = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
        Statistics.show(path.toString());

        File file = new File(String.valueOf(path));
        String[] directories = file.list(new FilenameFilter() {
            /*
             * @param current
             * @param name
             * @return
             */
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        //zeigt sie im fenster an

        try {
            int i = JOptionPane.showOptionDialog(null, "Welches Szenario?", "Szenarioauswahl", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, directories, directories[0]);

            String xmlDateiEndung = ".xml";
            //Statistics.show("xml/" + directories[i] + "/object");
            Factory.setTheObjectDataFile("xml/" + directories[i] + "/object" + xmlDateiEndung);
            Factory.setTheStartStationDataFile("xml/" + directories[i] + "/startstation" + xmlDateiEndung);
            Factory.setTheStationDataFile("xml/" + directories[i] + "/station" + xmlDateiEndung);
            Factory.setTheEndStationDataFile("xml/" + directories[i] + "/endstation" + xmlDateiEndung);
        } catch (ArrayIndexOutOfBoundsException e) {
            // e.printStackTrace();
            System.exit(0);
        }
        //a new simulation
        Simulation theSimulation = new Simulation();
        theSimulation.init();
        Factory.profitCounter();

    }

    /* Displays all JSON szenarios so the user can choose what szenario should play
     *
     * @throws IOException
     */
    private static void SzenarioJSON() throws IOException {
        //reads subdirectories
        Path path = Paths.get("Json");
        Statistics.show(path.toString());
        path = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
        Statistics.show(path.toString());

        File file = new File(String.valueOf(path));
        String[] directories = file.list(new FilenameFilter() {
            /*
             * @param current
             * @param name
             * @return
             */
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        //zeigt sie im fenster an

        try {
            int i = JOptionPane.showOptionDialog(null, "Welches Szenario?", "Szenarioauswahl", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, directories, directories[0]);

            String jsonDateiEndung = ".json";
            //Statistics.show("xml/" + directories[i] + "/object");
            FactoryJson.setTheObjectDataFile("Json/" + directories[i] + "/object" + jsonDateiEndung);
            FactoryJson.setTheStartStationDataFile("Json/" + directories[i] + "/startstation" + jsonDateiEndung);
            FactoryJson.setTheStationDataFile("Json/" + directories[i] + "/station" + jsonDateiEndung);
            FactoryJson.setTheEndStationDataFile("Json/" + directories[i] + "/endstation" + jsonDateiEndung);
        } catch (ArrayIndexOutOfBoundsException e) {
            // e.printStackTrace();
            System.exit(0);
        }
        //a new simulation
        Simulation theSimulation = new Simulation();
        theSimulation.JSONinit();
        FactoryJson.profitCounter();

    }

    /**
     * reads the statistics file and makes a graph
     *
     * @throws IOException
     */
    private static void auswertung() throws IOException {
        FileReader fr = new FileReader("statistics.txt");
        BufferedReader bw = new BufferedReader(fr);

        String zeile = "";

        ArrayList<String> parts1AsString = new ArrayList<>();
        ArrayList<String> parts2AsString = new ArrayList<>();

        while ((zeile = bw.readLine()) != null) {

            parts = zeile.trim().split("_");
            part2 = parts[1];
            partsO = parts[1].trim().split("\\s+");
            partO1 = partsO[0];
            parts1AsString.add(partO1);
            partO2 = partsO[1];
            parts2AsString.add(partO2);
        }

        objects = new int[parts1AsString.size()];
        for (int i = 0; i < parts1AsString.size(); i++) {
            // Note that this is assuming valid input
            // If you want to check then add a try/catch
            // and another index for the numbers if to continue adding the others (see below)
            objects[i] = Integer.parseInt(parts1AsString.get(i));
        }
        values = new double[parts2AsString.size()];
        for (int i = 0; i < parts2AsString.size(); i++) {
            // Note that this is assuming valid input
            // If you want to check then add a try/catch
            // and another index for the numbers if to continue adding the others (see below)
            values[i] = Double.parseDouble(parts2AsString.get(i));

        }

        bw.close();
        ArrayList<CustomPoint> timerList = new ArrayList<>();
        PlotterPane visual;
        visual = new PlotterPane(timerList, 800, 600, true, "Zeiteinheiten", "Objekte", "Objekte/Zeiteinheiten");
        for (int i = 0; i < values.length; i++) {

            visual.addPoint(new CustomPoint(objects[i], values[i]));
        }
    }


    /**
     * Get the global time
     *
     * @return the global time
     */
    public static long getGlobalTime() {
        return clock.get();
    }
}

