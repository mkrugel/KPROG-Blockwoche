package io;

import com.sun.org.apache.bcel.internal.generic.JsrInstruction;
import model.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import view.QueueViewJPanel;
import view.QueueViewText;


import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * @author Melanie Krugel
 * @version 28.11.2018
 */
public class FactoryJson implements Convertable {

    /**
     * the objects XML data file
     */
    private static String theObjectDataFile = "Json/Szenario 1/object.json";

    /**
     * the stations XML data file
     */
    private static String theStationDataFile = "Json/Szenario 1/station.json";

    /**
     * the start station XML data file
     */
    private static String theStartStationDataFile = "Json/Szenario 1/startstation.json";

    /**
     * the end station XML data file
     */
    private static String theEndStationDataFile = "Json/Szenario 1/endstation.json";


    /**
     * the x position of the starting station, also position for all starting objects
     */
    private static int XPOS_STARTSTATION;

    /**
     * the y position of the starting station, also position for all starting objects
     */
    private static int YPOS_STARTSTATION;


    /**
     * set the scenario which was chosen
     *
     * @param i the number to choose a scenario
     */

    public static void setScenario(int i) {
        if (i == 0) {
            theObjectDataFile = "Json/Szenario 0/object.json";
            theStationDataFile = "Json/Szenario 0/station.json";
            theStartStationDataFile = "Json/Szenario 0/startstation.json";
            theEndStationDataFile = "Json/Szenario 0/endstation.json";
        } else if (i == 1) {
            theObjectDataFile = "Json/Szenario 1/object.json";
            theStationDataFile = "Json/Szenario 1/station.json";
            theStartStationDataFile = "Json/Szenario 1/startstation.json";
            theEndStationDataFile = "Json/Szenario 1/endstation.json";
        } else if (i == 2) {
            theObjectDataFile = "Json/Szenario 2/object.json";
            theStationDataFile = "Json/Szenario 2/station.json";
            theStartStationDataFile = "Json/Szenario 2/startstation.json";
            theEndStationDataFile = "Json/Szenario 2/endstation.json";
        } else if (i == 3) {
            theObjectDataFile = "Json/Szenario 3/object.json";
            theStationDataFile = "Json/Szenario 3/station.json";
            theStartStationDataFile = "Json/Szenario 3/startstation.json";
            theEndStationDataFile = "Json/Szenario 3/endstation.json";
        } else if (i == 4) {
            theObjectDataFile = "Json/Szenario 4/object.json";
            theStationDataFile = "Json/Szenario 4/station.json";
            theStartStationDataFile = "Json/Szenario 4/startstation.json";
            theEndStationDataFile = "Json/Szenario 4/endstation.json";
        }
    }

    public static void profitCounter() {

        double gewinn = 0.0;
        double umsatz = 0.0;
        double kosten = 0.0;
        double preis = 0.0;


        JSONObject setting = getJSONObject().getJSONObject("settings");
        JSONArray allObjects = setting.getJSONArray("object");


        try {

            for (Iterator i = allObjects.iterator(); i.hasNext(); ) {


                JSONObject object1 = (JSONObject) i.next();
                preis = object1.getDouble("preis");

                kosten = object1.getDouble("kosten");

                umsatz += (preis * 5000);
            }

            gewinn = umsatz - kosten;
            System.out.println("Profit: " + gewinn);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * create the actors for the starting scenario
     */
    public static void createStartScenario() {

        /*NOTE: The start station must be created first,
         * because the objects constructor puts the objects into the start stations outgoing queue
         */
        createStartStation();
        createObjects();
        createProcessStations();
        createEndStation();
    }


    /**
     * create the start station
     */
    private static void createStartStation() {


        JSONObject setting = getJSONStartStation().getJSONObject("settings");
        JSONObject startStation = setting.getJSONObject("start_station");

        try {

            //get the label
            String label = startStation.getString("label");

            //get the position
            XPOS_STARTSTATION = startStation.getInt("x_position");
            YPOS_STARTSTATION = startStation.getInt("y_position");

            //the <view> ... </view> node
            JSONObject view = startStation.getJSONObject("view");

            // the image
            String image = view.getString("image");

            //CREATE THE INQUEUE
            //the <inqueue> ... </inqueue> node
            JSONObject inqueueGroup = startStation.getJSONObject("inqueue");

            // the positions
            int xPosInQueue = inqueueGroup.getInt("x_position");
            int yPosInQueue = inqueueGroup.getInt("y_position");

            //create the inqueue
            SynchronizedQueue theInQueue = SynchronizedQueue.createQueue(QueueViewText.class, xPosInQueue, yPosInQueue);

            //CREATE THE OUTQUEUE
            //the <outqueue> ... </outqueue> node
            JSONObject outqueueGroup = startStation.getJSONObject("outqueue");

            // the positions
            int xPosOutQueue = outqueueGroup.getInt("x_position");
            int yPosOutQueue = outqueueGroup.getInt("y_position");

            //create the outqueue
            SynchronizedQueue theOutQueue = SynchronizedQueue.createQueue(QueueViewText.class, xPosOutQueue, yPosOutQueue);

            //creating a new StartStation object
            StartStation.create(label, theInQueue, theOutQueue, XPOS_STARTSTATION, YPOS_STARTSTATION, image);

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private static void createSimpleStation() {

    }

    /**
     * create some objects out of the XML file
     */
    private static void createObjects() {


        JSONObject setting1 = getJSONObject().getJSONObject("settings");
        JSONArray allObjects = setting1.getJSONArray("object");

        try {

            //separate every JDOM "object" Element from the list and create Java TheObject objects
            for (Iterator i = allObjects.iterator(); i.hasNext(); ) {


                JSONObject theObject = (JSONObject) i.next();

                // data variables:
                String label = null;
                int processtime = 0;
                int speed = 0;
                int capacity = 0;
                String image = null;


                // read data
                label = theObject.getString("label");
                processtime = theObject.getInt("processtime");
                speed = theObject.getInt("speed");
                capacity = theObject.getInt("capacity");

                //the <view> ... </view> node
                JSONObject viewGroup = theObject.getJSONObject("view");

                // read data

                image = viewGroup.getString("image");

                //get all the stations, where the object wants to go to
                //the <sequence> ... </sequence> node
                JSONObject sequenceGroup = theObject.getJSONObject("sequence");

                JSONArray allStations = sequenceGroup.getJSONArray("station");

                //get the elements into a list
                ArrayList<String> stationsToGo = new ArrayList<>();


                for (Iterator it = allStations.iterator(); it.hasNext(); ) {



                    String theStation = (String) it.next();
                    stationsToGo.add(theStation);

                }
                JSONObject sequenceGroupFill = theObject.getJSONObject("sequence");
                JSONArray allFillStations = sequenceGroupFill.getJSONArray("einfuellStation");
                ArrayList<String> fillStationsToGo = new ArrayList<String>();
                for (Iterator iterator = allFillStations.iterator(); iterator.hasNext();) {

                    String fillStation = (String) iterator.next();
                    fillStationsToGo.add(fillStation);

                }
                //creating a new TheObject object
                TheObject.create(label, stationsToGo, fillStationsToGo, processtime, speed,capacity, XPOS_STARTSTATION, YPOS_STARTSTATION, image);



            }

        } catch (JSONException e) {
            e.printStackTrace();


        }
    }

    /**
     * create some process stations out of the XML file
     */
    private static void createProcessStations() {
        System.out.println("create JSON objects");

        JSONObject setting2 = getJSONStation().getJSONObject("settings");
        JSONArray allObjects = setting2.getJSONArray("station");

        try {
            //separate every JDOM "station" Element from the list and create Java Station objects

            for (Iterator i = allObjects.iterator(); i.hasNext(); ) {


                JSONObject station = (JSONObject) i.next();
                // data variables:
                String label = null;
                double troughPut = 0;
                int xPos = 0;
                int yPos = 0;
                String image = null;

                // read data
                label = station.getString("label");
                troughPut = station.getDouble("troughput");
                xPos = station.getInt("x_position");
                yPos = station.getInt("y_position");

                //the <view> ... </view> node
                JSONObject viewGroup = station.getJSONObject("view");
                // read data
                image = viewGroup.getString("image");

                //CREATE THE INQUEUES

                //get all the inqueues into a List object

                JSONObject inqueue = station.getJSONObject("inqueue");
                //create a list of the stations inqueues
                ArrayList<SynchronizedQueue> theInqueues = new ArrayList<SynchronizedQueue>(); //ArrayList for the created inqueues



                    int xPosInQueue = inqueue.getInt("x_position");
                    int yPosInQueue = inqueue.getInt("y_position");

                    //create the actual inqueue an add it to the list
                    theInqueues.add(SynchronizedQueue.createQueue(QueueViewJPanel.class, xPosInQueue, yPosInQueue));


                //CREATE THE OUTQUEUES

                //get all the outqueues into a List object
                JSONObject outqueue = station.getJSONObject("outqueue");

                //create a list of the stations outqueues
                ArrayList<SynchronizedQueue> theOutqueues = new ArrayList<SynchronizedQueue>(); //ArrayList for the created outqueues



                    int xPosOutQueue = outqueue.getInt("x_position");
                    int yPosOutQueue = outqueue.getInt("y_position");

                    //create the actual outqueue an add it to the list
                    theOutqueues.add(SynchronizedQueue.createQueue(QueueViewText.class, xPosOutQueue, yPosOutQueue));

                //creating a new Station object
                ProcessStation.create(label, theInqueues, theOutqueues, troughPut, xPos, yPos, image);

            }


        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    /**
     * create the end station
     */
    private static void createEndStation() {


        JSONObject setting3 = getJSONEndStation().getJSONObject("settings");
        JSONObject endStation = setting3.getJSONObject("end_station");

        try {


            //get label
            String label = endStation.getString("label");

            //position
            int xPos = endStation.getInt("x_position");
            int yPos = endStation.getInt("y_position");

            //the <view> ... </view> node
            JSONObject viewGroup = endStation.getJSONObject("view");
            // the image
            String image = viewGroup.getString("image");

            //CREATE THE INQUEUE
            //the <inqueue> ... </inqueue> node
            JSONObject inqueueGroup = endStation.getJSONObject("inqueue");

            // the positions
            int xPosInQueue = inqueueGroup.getInt("x_position");
            int yPosInQueue = inqueueGroup.getInt("y_position");

            //create the inqueue
            SynchronizedQueue theInQueue = SynchronizedQueue.createQueue(QueueViewText.class, xPosInQueue, yPosInQueue);

            //CREATE THE OUTQUEUE
            //the <outqueue> ... </outqueue> node
            JSONObject outqueueGroup = endStation.getJSONObject("outqueue");

            // the positions
            int xPosOutQueue = outqueueGroup.getInt("x_position");
            int yPosOutQueue = outqueueGroup.getInt("y_position");

            //create the outqueue
            SynchronizedQueue theOutQueue = SynchronizedQueue.createQueue(QueueViewText.class, xPosOutQueue, yPosOutQueue);

            //creating a new EndStation object
            EndStation.create(label, theInQueue, theOutQueue, xPos, yPos, image);


        } catch (JSONException e) {
            e.printStackTrace();

        }


    }

    public static JSONObject getJSONStation() {
        JSONObject jsonObject = null;
        if (jsonObject == null) {
            try {
                // load the JSON-File into a String
                FileReader fr = new FileReader(theStationDataFile);
                BufferedReader br = new BufferedReader(fr);
                String json = "";
                for (String line = ""; line != null; line = br.readLine())
                    json += line;
                br.close();

                // create a new JSON Object with the
                jsonObject = new JSONObject(json);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return jsonObject;
    }

    public static JSONObject getJSONStartStation() {
        JSONObject jsonObject = null;
        if (jsonObject == null) {
            try {
                // load the JSON-File into a String
                FileReader fr = new FileReader(theStartStationDataFile);
                BufferedReader br = new BufferedReader(fr);
                String json = "";
                for (String line = ""; line != null; line = br.readLine())
                    json += line;
                br.close();

                // create a new JSON Object with the
                jsonObject = new JSONObject(json);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return jsonObject;
    }

    public static JSONObject getJSONEndStation() {
        JSONObject jsonObject = null;
        if (jsonObject == null) {
            try {
                // load the JSON-File into a String
                FileReader fr = new FileReader(theEndStationDataFile);
                BufferedReader br = new BufferedReader(fr);
                String json = "";
                for (String line = ""; line != null; line = br.readLine())
                    json += line;
                br.close();

                // create a new JSON Object with the
                jsonObject = new JSONObject(json);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return jsonObject;
    }

    public static JSONObject getJSONObject() {
        JSONObject jsonObject = null;
        if (jsonObject == null) {
            try {
                // load the JSON-File into a String
                FileReader fr = new FileReader(theObjectDataFile);
                BufferedReader br = new BufferedReader(fr);
                String json = "";
                for (String line = ""; line != null; line = br.readLine())
                    json += line;
                br.close();

                // create a new JSON Object with the
                jsonObject = new JSONObject(json);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return jsonObject;
    }
    public static void setTheObjectDataFile(String theObjectDataFile) {
        FactoryJson.theObjectDataFile = theObjectDataFile;
    }

    public static void setTheStationDataFile(String theStationDataFile) {
        FactoryJson.theStationDataFile = theStationDataFile;
    }

    public static void setTheStartStationDataFile(String theStartStationDataFile) {
        FactoryJson.theStartStationDataFile = theStartStationDataFile;
    }

    public static void setTheEndStationDataFile(String theEndStationDataFile) {
        FactoryJson.theEndStationDataFile = theEndStationDataFile;
    }

}


