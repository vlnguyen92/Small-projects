package com.example.coms410.erg;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


public class HandleXML {

    private LinkedList<Float> solar_radiation = new LinkedList<>();
    private LinkedList<Float> temperature = new LinkedList<>();
    private LinkedList<Float> humidity = new LinkedList<>();
    private LinkedList<Float> pressure = new LinkedList<>();
    //    private String humidity = "humidity";
//    private String pressure = "pressure";
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;
    public HandleXML(String url){
        this.urlString = url;
    }
    public Float[] getTemp(){
        return Arrays.copyOf(temperature.toArray(),temperature.size(), Float[].class);
    }
    public Float[] getRadiation(){
        return Arrays.copyOf(solar_radiation.toArray(),solar_radiation.size(), Float[].class);
    }
    public Float[] getHumidity(){
        return Arrays.copyOf(humidity.toArray(),humidity.size(), Float[].class);
    }
    public Float[] getPressure(){
        return Arrays.copyOf(pressure.toArray(),pressure.size(), Float[].class);
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals("temp_f")){
                            temperature.addLast(Float.parseFloat(text));
                        }
                        else if(name.equals("pressure_in")){
                            pressure.addLast(Float.parseFloat(text));
                        }
                        else if(name.equals("relative_humidity")){
                            float f = Float.parseFloat(text);
                            humidity.addLast(Float.parseFloat(text));
                        }
                        else if(name.equals("solar_radiation")){
                            solar_radiation.addLast(Float.parseFloat(text));
                        }
                        else{
                        }
                        break;
                }
                event = myParser.next();

            }
            parsingComplete = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection)
                            url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES
                            , false);
                    myparser.setInput(stream, null);
                    parseXMLAndStoreIt(myparser);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


    }

}