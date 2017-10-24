package com.android.acadgild.weatherapp191;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.android.acadgild.weatherapp191.network.CallAddr;
import com.android.acadgild.weatherapp191.network.NetworkStatus;
import com.android.acadgild.weatherapp191.network.OnWebServiceResult;
import com.android.acadgild.weatherapp191.utils.CommonUtilities;
import com.squareup.okhttp.FormEncodingBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnWebServiceResult {

    //Webservice URL to be called
    String url= "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b1b15e88fa797225412429c1c50c122a1";

    String finDetails="";
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //call hitRequest
        hitRequest();
    }

    private void hitRequest(){
        //Create Object of FormEncodingBuilder
        FormEncodingBuilder parameters= new FormEncodingBuilder();
        parameters.add("page" , "1");
        //Check if Network status is connected with internet or not If connected then
        if(NetworkStatus.getInstance(this).isConnectedToInternet()) {
            //Pass URL, Parameters to CallAddr class
            CallAddr call = new CallAddr(this, url,parameters, CommonUtilities.SERVICE_TYPE.GET_DATA, this);
            //CAll execute method
            call.execute();
        }else {
            Toast.makeText(this, "No Network!!", Toast.LENGTH_SHORT).show();
        }
    }

    //getWebResponse method
    @Override
    public void getWebResponse(String result, CommonUtilities.SERVICE_TYPE type) {
        Log.e("res", "type= "+ type+ " res= "+ result);
        try {
            //object JSONObject class
            JSONObject obj= new JSONObject(result);
            //Get Value of Name
            String name = obj.getString("name");
            finDetails = finDetails + "Name : "+name+"\n";
            //Get coord object to print Longitude & Latitude
            JSONObject jobj1= obj.getJSONObject("coord");
            String lon = jobj1.getString("lon");
            String lat = jobj1.getString("lat");
            finDetails = finDetails + "Longitude : "+lon+" Latitude : "+lat+"\n";

            //Get weather array to print details
            JSONArray arr= obj.getJSONArray("weather");
            //loop through the length of JSONArray
            for(int i=0; i<arr.length(); i++) {
                //Object of JSONObject class to get single item from array
                JSONObject jobj= arr.getJSONObject(i);

                finDetails = finDetails + "Main :"+jobj.getString("main") + "\n";
                finDetails = finDetails + "Description: "+jobj.getString("description") + "\n";

            }

            //Get Main Object to print Temperature,Pressure,Humidity,Temp Min,Temp Max
            JSONObject jobj2= obj.getJSONObject("main");
            finDetails = finDetails + "Temperature :"+jobj2.getString("temp") + "\n";
            finDetails = finDetails + "Pressure :"+jobj2.getString("pressure") + "\n";
            finDetails = finDetails + "Humidity :"+jobj2.getString("humidity") + "\n";
            finDetails = finDetails + "Temp Min :"+jobj2.getString("temp_min") + "\n";
            finDetails = finDetails + "Temp Max :"+jobj2.getString("temp_max") + "\n";
            //Get Value of Visibility
            finDetails = finDetails + "Visibility :"+obj.getString("visibility") + "\n";
            //Get Wind object to print Wind Speed & Wind Degree
            JSONObject jobj3= obj.getJSONObject("wind");
            finDetails = finDetails + "Wind Speed :"+jobj3.getString("speed") + "\n";
            finDetails = finDetails + "Wind Degree :"+jobj3.getString("deg") + "\n";
            //Get sys object to print Country, Sunrise & Sunset
            JSONObject jobj4= obj.getJSONObject("sys");
            finDetails = finDetails + "Country :"+jobj4.getString("country") + "\n";
            finDetails = finDetails + "Sunrise :"+jobj4.getString("sunrise") + "\n";
            finDetails = finDetails + "Sunset :"+jobj4.getString("sunset") + "\n";
            //Get clouds obejct to print Clouds %
            JSONObject jobj5= obj.getJSONObject("clouds");
            finDetails = finDetails + "Clouds :"+jobj5.getString("all") +"%";

            //Assign final value to Textview
            tv = (TextView) findViewById(R.id.showWeather);
            tv.setText(finDetails);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
