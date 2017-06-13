package com.example.richa.kuromasu3;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menu extends AppCompatActivity {

    private Button[] btnLevels;
    private Button btnGameContinue, btnGameNew, btnGameInfo;
    private TextView txtSelectedLevel;
    private int[][] initialMap;
    private char[][] savedMap, succesMap;
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
        //ListSavesGames();

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

    private void PrintMapChar(char[][] arr){
        Log.d("SIZE", mapCols+"x"+mapRows);
        for (int i =0; i<mapRows; i++){
            String row = "";
            for (int j=0; j < mapCols; j++){
                row+= arr[i][j]+" ";
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
        succesMap = new char[mapRows][mapCols];
        // eje y
        int j =0, counterZ=0;
        for (String line : lines) {
            //eje x
            int counterX =0,counterY =0;
            //Log.d("LINE", line);
            //char[] chars = line.toCharArray();
            if(j != mapRows){
                String[] r = line.split("\\s");
                for (int i =0; i< r.length; i++){
                    //Log.d("TOKEN",r[i]);
                    if(j <= mapRows){
                        //break;
                        if(r[i].equals(".")){
                            //Log.d("TOKEN1",r[i]);
                            initialMap[j][counterX++] = 0;
                        }
                        else{
                            //Log.d("TOKEN2",r[i]);
                            initialMap[j][counterX++] = Integer.parseInt(r[i]);
                        }
                    }else{
                        char[] chars = r[i].toCharArray();
                        //Log.d("CHAR1",counterZ+"");
                        succesMap[counterZ][counterY++] = chars[0];
                        //Log.d("CHAR2",succesMap[counterZ][counterY++]+"");
                    }
                }

                if(j > mapRows && counterZ < mapRows - 1) {counterZ++;}
            }
            if(j<=mapRows){j++;}

        }
        PrintMapChar(succesMap);
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
        btnGameContinue.setEnabled(false);
        btnGameNew = (Button) findViewById(R.id.btnGameNew);
        btnGameNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGame();
            }
        });
        btnGameInfo = (Button) findViewById(R.id.btnGameInfo);
        btnGameInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Rules.class));
            }
        });
        txtSelectedLevel = (TextView) findViewById(R.id.txtLevelSelected);
    }

    // debe pasar el mapa inicial cargado desde los archivos a la clase Game
    private void NewGame(){
        Intent intent = new Intent(this, Game.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("InitialMap", initialMap);
        bundle.putSerializable("SuccesMap", succesMap);
        intent.putExtras(bundle);
        //intent.putExtra("MapaInicial", initialMap);
        intent.putExtra("MODE","NEWGAME");
        intent.putExtra("MapaColumnas", mapCols);
        intent.putExtra("MapaFilas", mapRows);
        intent.putExtra("LevelId", currentLevel);
        startActivity(intent);
    }

    // debe cargar el save game y pasarle a Game una matriz con un estado del juego guardado por el usuario
    private void ContinueGame(){
        Intent intent = new Intent(this, Game.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("InitialMap", initialMap);
        bundle.putSerializable("SuccesMap", succesMap);
        //bundle.putSerializable("SaveMap", savedMap);
        intent.putExtras(bundle);
        intent.putExtra("MODE","CONTINUE");
        intent.putExtra("MapaColumnas", mapCols);
        intent.putExtra("MapaFilas", mapRows);
        intent.putExtra("LevelId", currentLevel);
        startActivity(intent);
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
                LoadSavedGame("save0"+currentLevel);
                Log.d("SETTINGS", mapCols+"x"+mapRows);
                //PrintMap(initialMap, mapCols, mapRows);
            }else{
                Log.d("LOADMAP", "Archivo no disponible");
            }

        }
    }


    public void ListSavesGames(){
        String path = "/sdcard/";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }

        Log.d("READ",read("save013"));
    }

    public void LoadSavedGame(String text){
        String savedLevel = read(text);

        if(savedLevel != null){
            Log.e("READ", "Saved Game cargado" );
            btnGameContinue.setEnabled(true);
            ParseSavedGame(savedLevel);
        }else{
            Log.e("READ", "Saved Game no existente" );
            btnGameContinue.setEnabled(false);
        }

    }

    public String read(String fname){

        BufferedReader br = null;
        String response = null;

        try {

            StringBuffer output = new StringBuffer();
            String fpath = "/sdcard/"+fname+".txt";

            br = new BufferedReader(new FileReader(fpath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line +System.getProperty("line.separator"));
            }
            response = output.toString();

            //Log.d("READ", response);
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
        return response;
    }


    public void ParseSavedGame(String savedLevel){
        String[] lines = savedLevel.split("\n|\n ");
        savedMap = new char[mapRows][mapCols];
        // eje y
        int j =0;
        for (String line : lines) {
            //eje x
            int counterX =0;
            String[] r = line.split("\\s");
            for (int i =0; i< r.length; i++){
                char[] chars = r[i].toCharArray();
                savedMap[j][counterX++] = chars[0];
            }
            j++;
            if(j > mapRows){break;}
        }
        //PrintMapChar(savedMap);
    }
}
