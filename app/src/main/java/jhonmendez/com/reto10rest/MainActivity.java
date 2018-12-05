package jhonmendez.com.reto10rest;

import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView sal;
    private Spinner spinner;

    String deptoSelected = "";
    String anioSelected = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buscarBtn = (Button) findViewById(R.id.buscarId);
        getDeptos();
        getYear();
        buscarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("salida", deptoSelected);
//                Log.d("salida", deptoSelected);
                getData(deptoSelected,anioSelected);
            }
        });

    }


    public String getDeptos(){
        spinner = findViewById(R.id.deptoSpinner);
        List<String> deptosList = new ArrayList<>();
        deptosList.add(0,"Seleccione un Departamento");
        deptosList.add("CUNDINAMARCA");
        deptosList.add("AMAZONAS");
        deptosList.add("ARAUCA");
        deptosList.add("ATLANTICO");
        deptosList.add("BOLIVAR");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, deptosList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Seleccione un Departamento")){
                }else{
                    deptoSelected = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    return deptoSelected;
    }

    public String getYear(){
        spinner = findViewById(R.id.yearSpinner);
        List<String> deptosList = new ArrayList<>();
        deptosList.add(0,"Seleccione un Año");
        deptosList.add("2018");
        deptosList.add("2017");
        deptosList.add("2016");
        deptosList.add("2015");
        deptosList.add("2014");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, deptosList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Seleccione un Departamento")){


                }else{

                    anioSelected = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(),"seleccionó: " + anioSelected, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return anioSelected;
    }

    public void getData(String dep, String ani){
        sal = (TextView) findViewById(R.id.salida);
        //https://www.datos.gov.co/resource/rn8c-hqmd.json?$where=departamento='CUNDINAMARCA'AND anhodesmovilizacion=2015
        String sql ="https://www.datos.gov.co/resource/rn8c-hqmd.json?$where=departamento='"+dep+"'AND anhodesmovilizacion="+ani;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(sql);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json= "";

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            json = response.toString();

            JSONArray jsonArr = null;

            jsonArr = new JSONArray(json);
            String mensaje ="";
            String deptos;
            String anios;
            String desm;
            for (int i =0; i< jsonArr.length(); i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                deptos = jsonObject.optString("departamento");
                anios = jsonObject.optString("anhodesmovilizacion");
                desm = jsonObject.optString("numerodesmovilizados");
                mensaje += "El departamento de "+ deptos+ " tiene un total de " + desm + " desmovilizados para el año "+ anios;
            }
            sal.setText(mensaje);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
