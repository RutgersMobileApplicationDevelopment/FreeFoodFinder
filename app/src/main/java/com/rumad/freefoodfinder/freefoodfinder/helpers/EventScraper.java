package com.rumad.freefoodfinder.freefoodfinder.helpers;

import android.app.DownloadManager;
import android.util.Log;

import com.rumad.freefoodfinder.freefoodfinder.pojos.Event;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by shreyashirday on 9/23/15.
 *
 * CREDIT: Sam Agnew
 */
public class EventScraper {



    static JSONObject jsonObject;
    public ArrayList<Event> events;
    public final static int NUM_DAYS = 10;

    /** Creates a list of event objects from the results of the XML downloads.
     *
     * @return
     *
     * @throws IOException
     */
    public static ArrayList<Event> getEvents() throws IOException {


        ArrayList<Event> events = null;
        ArrayList<Event> studentLife = null;

        try {
            String xml = getXML(("http://ruevents.rutgers.edu/events/getEventsRss.xml?campus=NB&numberOfDays=" + NUM_DAYS));


            Log.d("JSON","trying json...");
            try{
                jsonObject = XML.toJSONObject(xml);

                JSONObject rss = jsonObject.getJSONObject("rss");
                JSONObject channel = rss.getJSONObject("channel");
                JSONArray items = channel.getJSONArray("item");

                events = getEventsFromRutgersEventsViaJSON(items);

                Log.d("JSON",items.toString());
            }catch (JSONException e){
                Log.e("JSON",e.getMessage());
            }


            //xml = getXML("http://getinvolved.rutgers.edu/feed.php");
            //studentLife = getEventsFromStudentLife(xml);
        } catch (ParseException e) {
            Log.e("EventScraper: PE", e.getMessage());

        } catch (ParserConfigurationException e) {
            Log.e("EventScraper: PCE", e.getMessage());

        } catch (SAXException e) {
            Log.e("EventScraper: SAXExcep", e.getMessage());

        } catch (IOException e) {
            Log.e("EventScraper: IOExcept", e.getMessage());
            throw e;
        }


        if(studentLife != null) {
            for (Event event : studentLife) {
                events.add(event);
            }
        }else{
            Log.e("NULLY","studentlife is null and jsonObject = " + jsonObject.toString());
        }



        Event[] eventArray = new Event[events.size()];
        for(int i = 0; i < eventArray.length; i++){
            eventArray[i] = events.get(i);
        }

        Arrays.sort(eventArray, new DateComparator());

        events = new ArrayList<>();

        for(Event event: eventArray){
            events.add(event);
        }
        return events;
    }






    /** Reads XML string that was returned from Rutgers Event API, parses it,
     * 	then creates a list of event objects.
     *
     * @param xml
     * @return list of event objects
     * @throws ParseException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static ArrayList<Event> getEventsFromRutgersEvents(String xml) throws ParseException, ParserConfigurationException, SAXException, IOException {

        Document doc = loadXMLFromString(xml);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("channel");

        ArrayList<Event> events = new ArrayList<Event>(50);

        for(int temp = 0; temp < nList.getLength(); temp++){
            Node nNode = nList.item(temp);
            if(nNode.getNodeType() == Node.ELEMENT_NODE){
                Element eElement = (Element) nNode;
                NodeList items = eElement.getElementsByTagName("item");

                for(int i = 0; i < items.getLength(); i++){
                    Element n = (Element) items.item(i);

                    String description = getTagValue("description", n);
                    if(hasFreeFood(description)){
                        GregorianCalendar startGC, endGC;
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEE");
                        Date startParsed = formatter.parse(getTagValue("event:beginDateTime", n));
                        Date endParsed = formatter.parse(getTagValue("event:beginDateTime", n));

                        startGC = endGC = (GregorianCalendar) GregorianCalendar.getInstance();
                        startGC.setTime(startParsed);
                        endGC.setTime(endParsed);

                        events.add(new Event(startGC, endGC, description, getTagValue("event:campus", n), getTagValue("title", n)));
                    }
                }
            }
        }
        return events;
    }

    public static ArrayList<Event> getEventsFromRutgersEventsViaJSON(JSONArray array) throws ParseException, ParserConfigurationException, SAXException, IOException {



        ArrayList<Event> events = new ArrayList<Event>(50);




                for(int i = 0; i < array.length(); i++){
                    try {
                        JSONObject event = array.getJSONObject(i);

                        String description = event.getString("description");
                        if (hasFreeFood(description)) {
                            GregorianCalendar startGC, endGC;
                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEE");
                            Date startParsed = formatter.parse(event.getString("event:beginDateTime"));
                            Date endParsed = formatter.parse(event.getString("event:beginDateTime"));

                            startGC = endGC = (GregorianCalendar) GregorianCalendar.getInstance();
                            startGC.setTime(startParsed);
                            endGC.setTime(endParsed);

                            events.add(new Event(startGC, endGC, description, event.getString("event:campus"),event.getString("title")));
                        }
                    }
                    catch (JSONException e){

                    }
                }


        return events;
    }

    /** Reads XML string that was returned from Student Life, parses it,
     * 	then creates a list of event objects.
     *
     * @param xml
     * @return list of event objects
     * @throws ParseException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static ArrayList<Event> getEventsFromStudentLife(String xml) throws ParseException, ParserConfigurationException, SAXException, IOException {

        Document doc = loadXMLFromString(xml);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("channel");

        ArrayList<Event> events = new ArrayList<Event>(50);

        for(int temp = 0; temp < nList.getLength(); temp++){
            Node nNode = nList.item(temp);
            if(nNode.getNodeType() == Node.ELEMENT_NODE){
                Element eElement = (Element) nNode;
                NodeList items = eElement.getElementsByTagName("item");
                for(int i = 0; i < items.getLength(); i++){
                    Element n = (Element) items.item(i);

                    String description = getTagValue("description", n);
                    if(hasFreeFood(description)){

                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEE");
                        Date startParsed = formatter.parse(getTagValue("event:beginDateTime", n));
                        Date endParsed = formatter.parse(getTagValue("event:beginDateTime", n));

                        GregorianCalendar startGC = (GregorianCalendar) GregorianCalendar.getInstance();
                        GregorianCalendar endGC = (GregorianCalendar) GregorianCalendar.getInstance();
                        startGC.setTime(startParsed);
                        endGC.setTime(endParsed);

                        if(description.length()<5){
                            description += "                 ";
                        }

                        events.add(new Event(startGC, endGC, description.substring(3,description.length()-4), "-unknown-", getTagValue("title", n)));
                    }
                }
            }
        }
        return events;
    }

    /** Retrieves XML from specified URL to be parsed.
     *
     * @param urlstr
     * @return
     */
    public static String getXML(String urlstr){

        StringBuffer buff = new StringBuffer();

        URL url = null;
        try {
            url = new URL(urlstr);
        } catch (MalformedURLException e) {
            Log.e("XML Error", "Error using specified URL.");
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(url.openConnection().getInputStream()));
        } catch (IOException e) {
            Log.e("XML Error,", "Error Reading I/O in XML Parse");
        }

        int c;

        try {
            while((c=br.read())!=-1)
            {
                buff.append((char)c);
            }
            br.close();
        } catch(IOException e){
            e.printStackTrace();
        }

        String xml = buff.toString();
        return xml;
    }


    /** This function takes the event description string and looks for
     *  the specified parameters.
     * @param description : string to be vetted
     * @return boolean with result of event vetting process
     */
    public static boolean hasFreeFood(String description){

        description = description.toLowerCase();

        String[] keywords = {"food", "appetizer", "snack", "pizza", "lunch", "dinner", "breakfast", "meal",
                "candy", "drink", "punch", "pie", "cake", "soda", "chicken", "wing", "burger",
                "burrito", "shirt", "stuff", "bagel", "coffee", " ice ", "cream", "water", "donut", "beer",
                "sub", "hoagie", "sandwich", "turkey", "supper", "brunch", "takeout", "refresh",
                "beverage", "cookie", "brownie", "corn", "chips", "soup", "grill", "bbq", "barbecue"};

        if(description.contains("free")){
            for(String word: keywords){
                if(description.contains(word)){
                    return true;
                }
            }
        }
        return false;
    }


    /** Helper function to parse XML
     *
     * @param xml
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Document loadXMLFromString(String xml) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return (Document) builder.parse(is);
    }

    /** Helper function in XML parsing.
     *
     * @param sTag
     * @param eElement
     * @return
     */
    private static String getTagValue(String sTag, Element eElement) {
        NodeList nlList = ((Element) eElement).getElementsByTagName(sTag).item(0).getChildNodes();

        Node nValue = (Node) nlList.item(0);

        return nValue.getNodeValue();
    }

    private static class DateComparator implements Comparator<Event> {

        @Override
        public int compare(Event e1, Event e2) {
            if(e1 == null)
                return 1;
            if(e2 == null)
                return -1;

            if (e1.startDate.getTimeInMillis() > e2.startDate.getTimeInMillis())
                return 1;
            else
                return -1;
        }

    }

    /** Print string representation of event results.
     *
     */
    public String printResults(){

        String resultString = "";

        for(int i = 0; i < events.size(); i++){
            Event tempEvent = events.get(i);
            String dateString = tempEvent.startDate.get(Calendar.MONTH) + "/" +
                    tempEvent.startDate.get(Calendar.DATE)
                    + "/" + tempEvent.startDate.get(Calendar.YEAR);

            resultString += "Name: " + tempEvent.name + "\n";
            resultString +="Date: " + dateString + "\n";
            resultString +="Description: " + tempEvent.description + "\n";
            resultString +="Campus: " + tempEvent.campus + "\n";
            resultString +="\n\n\n";
        }
        return resultString;
    }


}
