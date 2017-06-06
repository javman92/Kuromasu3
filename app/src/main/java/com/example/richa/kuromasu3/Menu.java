package com.example.richa.kuromasu3;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menu extends AppCompatActivity {

    private Button[] btnLevels;
    private Button btnGameContinue, btnGameNew;
    private TextView txtSelectedLevel;
    private int[][] initialMap;
    private int mapCols, mapRows;
    private int totalLevels, currentLevel;
    private Map listLevels = new HashMap();

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        LoadLevelList();
        IniButtons();

    }

    /*
    Inicializa el arreglo initialMap y setea sus configuraciones, tales como : cantidad columnas y filas.
    columns: eje x
    rows: eje Y
    */
    private int[][] MapInitialize(String[] text){
        int rows = text.length/2;
        double col = text[0].length();
        int columns = (int) Math.ceil(col/2);
        //Log.d("SIZE ", rows + "x" + columns);
        mapCols = columns;
        mapRows = rows;
        int[][] mapRow = new int[rows][columns];
        //PrintMap(mapRow,columns,rows);
        return mapRow;
    }

    //Se encarga de imprimir alguna matriz
    private void PrintMap(int[][] arr, int cols, int rows){
        Log.d("SIZE", cols+"x"+rows);
        for (int i =0; i<rows; i++){
            String row = "";
            for (int j=0; j < cols; j++){
                row+= arr[i][j]+",";
            }
            Log.d("Row", row);
        }
    }

    //Carga el contenido del archivo que se le indica y luego llama a GetMap()
    private void LoadLevel(String level){
        String text="No se cargo el mapa";
        try{
            InputStream is = getApplicationContext().getAssets().open("mapas/"+level);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text=new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GetMap(text);
    }

    //Se encarga de parsear el contenido traido desde LoadLeve y ponerlo en la matriz initialMap
    private void GetMap(String level){
        String[] lines = level.split("\n|\n ");
        initialMap = MapInitialize(lines);
        // eje y
        int j =0;
        for (String line : lines) {
            //eje x
            int counterX =0;
            //Log.d("LINE", line);
            char[] chars = line.toCharArray();
            String[] r = line.split("\\s");
            for (int i =0; i< r.length; i++){
                //Log.d("TOKEN",r[i]);
                if(r[i].equals(".")){
                    //Log.d("TOKEN1",r[i]);
                    initialMap[j][counterX++] = 0;
                }
                else{
                    //Log.d("TOKEN2",r[i]);
                    initialMap[j][counterX++] = Integer.parseInt(r[i]);
                }
            }
            j++;
            if(j > mapRows){break;}
        }
    }

    // Carga en un diccionario una lista de todos los niveles disponibles en la carpeta mapas en assets
    private void LoadLevelList(){
        String[] mapas = new String[0];
        try{
            mapas = getAssets().list("mapas");
            totalLevels = mapas.length;
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i< totalLevels; i++){
            listLevels.put(i,mapas[i]);
            //Log.d("id:"+i,mapas[i]);
        }
    }

    // Inicializa todos los botones: hace referencias y setea su funcionalidad
    private void IniButtons(){
        btnLevels = new Button[totalLevels];
        for (int i = 0 ; i < totalLevels ; i++){
            String ButtonId = "btnLevel0"+(i+1);
            //Log.d("id: ",ButtonId);
            int resID = getResources().getIdentifier(ButtonId, "id", getPackageName());
            btnLevels[i] = (Button) findViewById(resID);
            btnLevels[i].setOnClickListener(new ButtonLevelListener());
        }

        btnGameContinue = (Button) findViewById(R.id.btnGameContinue);
        btnGameContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContinueGame();
            }
        });
        btnGameNew = (Button) findViewById(R.id.btnGameNew);
        btnGameNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGame();
            }
        });
        txtSelectedLevel = (TextView) findViewById(R.id.txtLevelSelected);
    }

    // debe pasar el mapa inicial cargado desde los archivos a la clase Game
    private void NewGame(){
        Intent intent = new Intent(this, Game.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("InitialMap", initialMap);
        intent.putExtras(bundle);
        //intent.putExtra("MapaInicial", initialMap);
        intent.putExtra("MapaColumnas", mapCols);
        intent.putExtra("MapaFilas", mapRows);
        startActivity(intent);
    }

    // debe cargar el save game y pasarle a Game una matriz con un estado del juego guardado por el usuario
    private void ContinueGame(){
        startActivity(new Intent(getApplicationContext(), Game.class));
    }

    // Clase que implementa la funcionalidad de los botones de selecion de nivel
    private class ButtonLevelListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d("Button", v.getTag().toString());
            currentLevel = Integer.parseInt(v.getTag().toString());
            txtSelectedLevel.setText(v.getTag().toString());

            if(listLevels.get(currentLevel - 1 ) != null){
                String mapa = listLevels.get(currentLevel - 1 ).toString();
                LoadLevel(mapa);
                Log.d("SETTINGS", mapCols+"x"+mapRows);
                PrintMap(initialMap, mapCols, mapRows);
            }else{
                Log.d("LOADMAP", "Archivo no disponible");
            }

        }
    }
}
