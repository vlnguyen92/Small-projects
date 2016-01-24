package com.example.coms410.erg;

import java.io.IOException;
import java.util.Hashtable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.linh.erg.R;

public class MainActivity extends Activity {

    DatePicker datePicker;
    private class myDate
    {
        String day;
        String month;
        String year;
    }
    private String headurl = "http://www.wunderground.com/weatherstation/WXDailyHistory.asp?ID=KVAHAMPD2&day=";
    private String month = "&month=";
    private String year = "&year=";
    private String tailurl = "&graphspan=day&format=XML";

    private Spinner spinner;
    private HandleXML obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_date_picker);
        datePicker = (DatePicker) findViewById(R.id.dp);


//        obj = new HandleXML("http://api.openweathermap.org/data/2.5/weather?q=london&mode=xml");
        spinner = (Spinner) findViewById(R.id.data);



        //System.out.println(finalUrl);
//        country.setText(finalUrl);



        Button plot = (Button) findViewById(R.id.choose);
        plot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                final Hashtable<String,float[]> table = getUrl();
                String item = String.valueOf(spinner.getSelectedItem());
                Intent myIntent = new Intent(view.getContext(), plotActivity.class);
                Bundle b = new Bundle();
                b.putFloatArray(item,table.get(item));
                b.putString("key",item);
                b.putInt("size",Math.round(table.get("size")[0]));
                myIntent.putExtras(b);
                startActivity(myIntent);
            }
        });
    }

    public myDate getDate() {
        myDate d = new myDate();
        d.day = Integer.toString(datePicker.getDayOfMonth());
        d.month = Integer.toString(datePicker.getMonth()+1);
        d.year = Integer.toString(datePicker.getYear());
        return d;
    }

    public Hashtable<String,float[]> getUrl() {
        myDate d = getDate();
        String finalUrl = headurl + d.day + month + d.month + year + d.year + tailurl;
        obj = new HandleXML(finalUrl);
        obj.fetchXML();
        while(obj.parsingComplete);
        Float[] radiation = obj.getRadiation();
        Float[] pressure = obj.getPressure();
        Float[] humidity = obj.getHumidity();
        Float[] temperature = obj.getTemp();
        final int size = radiation.length;
        final float[] tempArray = new float[size];
        final float[] pressArray = new float[size];
        final float[] radArray = new float[size];
        final float[] humidArray = new float[size];
        float[] sizeArr = new float[1];
        sizeArr[0]=(float)size;
        for(int i = 0; i < size; i++)
        {
            tempArray[i]=temperature[i];
            humidArray[i]=humidity[i];
            radArray[i]=radiation[i];
            pressArray[i]=pressure[i];
        }
        final Hashtable<String,float[]> table = new Hashtable<>();

        table.put("temperature",tempArray);
        table.put("pressure",pressArray);
        table.put("radiation",radArray);
        table.put("humidity",humidArray);
        table.put("size",sizeArr);
        return table;
    }

}