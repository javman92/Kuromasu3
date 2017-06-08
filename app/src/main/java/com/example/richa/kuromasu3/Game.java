package com.example.richa.kuromasu3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Game extends AppCompatActivity {

//    int[][] matrizInicial = {{1,2,0}, {1, 1, 1}};
    int[][] matrizInicial;
    int[][] matrizEstado;
    char[][] matrizEstadoGuardar;
    int filas=2,columnas=3, levelId;
    LinearLayout layoutPrincipal;
    private GridLayout gridMap;
    private Button btnRestartGame, btnRules, btnSaveGame;
    int screenWidth, screeHeight;


    private int linearLayoutWidth;

    //gris=0, negro=1, blanco=2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screeHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        btnRestartGame = (Button) findViewById(R.id.btnRestartGame);
        btnSaveGame = (Button) findViewById(R.id.btnSaveGame);
        btnSaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveGame();
            }
        });

        matrizInicial = GetInitialMap();
        matrizEstado = new int[filas][columnas];
        matrizEstadoGuardar = new char[filas][columnas];
        //gridMap = (GridLayout) findViewById(R.id.gridMap);

        layoutPrincipal=(LinearLayout) this.findViewById(R.id.lLTablero);

        CrearTablero();

    }

    //Parsea arreglo de chars
    private void SaveGame() {

        String content="";
        String line="";
        for (int i =0; i< filas; i++){
            line="";
            for (int j =0; j < columnas; j++){
                line += matrizEstadoGuardar[i][j] + " ";
            }
            line+= System.getProperty("line.separator");
            content += line;
        }
        Log.d("CONTENT",content);
        write("save0"+levelId,content);
    }

    public Boolean write(String fname, String fcontent){
        try {
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/kuromasu/saves/";
//            String fpath = fname+".txt";
//
//            new File(path).mkdir();
//            File file = new File(path + fpath);
//            if (!file.exists()) {
//                file.createNewFile();
//            }
            String fpath = "/sdcard/"+fname+".txt";
            File file = new File(fpath);
            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();

            Log.d("Suceess","Sucess");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    private void PrintMatrizInt(int[][] m){
        String line;
        for (int i =0; i< filas; i++){
            line ="";
            for (int j =0; j < columnas; j++){
                line += m[i][j] + ",";
            }
        }
    }

    private void PrintMatrizChar(char[][] m){
        String line;
        for (int i =0; i< filas; i++){
            line ="";
            for (int j =0; j < columnas; j++){
                line += m[i][j] + ",";
            }
            Log.d("ROW", line);
        }
    }

    private int[][] GetInitialMap(){
        Intent in = getIntent();
        int cols = in.getIntExtra("MapaColumnas", 99 );
        int rows = in.getIntExtra("MapaFilas", 99 );
        int lId = in.getIntExtra("LevelId", 99 );
        filas = rows;
        columnas = cols;
        levelId = lId;
        int[][] inMatrix = null;
        Object[] objectArray = (Object[]) getIntent().getExtras().getSerializable("InitialMap");
        if(objectArray!=null){
            inMatrix = new int[objectArray.length][];
            for(int i=0;i<objectArray.length;i++){
                inMatrix[i]=(int[]) objectArray[i];
            }
        }

        return inMatrix;
    }

    private void ChangeButtonState(int k, int p, Button b){
        if(matrizEstado[k][p]==0){
            b.setBackgroundColor(Color.BLACK);
            matrizEstado[k][p]++;
            matrizEstadoGuardar[k][p]='N'; // NEGRO
        }
        else if(matrizEstado[k][p]==1){
            b.setBackgroundColor(Color.WHITE);
            matrizEstado[k][p]++;
            matrizEstadoGuardar[k][p]='B'; // BLANCO
        }
        else {
            b.setBackgroundColor(Color.GRAY); // GRIS
            matrizEstado[k][p]=0;
        }
    }

    private void ComprobarEstadoJuego(){
        if(comprobar(matrizEstado)) {
            if(Arrays.deepEquals(matrizInicial, matrizEstado)){
                Toast toast1 = Toast.makeText(getApplicationContext(),"Felicitaciones", Toast.LENGTH_SHORT);
                toast1.show();
                System.out.println("correcto");
            }

            else {
                Toast toast1 =Toast.makeText(getApplicationContext(),"Incorrecto", Toast.LENGTH_SHORT);
                toast1.show();
            }

            try {
                Pista(matrizInicial, matrizEstado);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private  void ButtonPressed(){

        if(Arrays.deepEquals(matrizInicial, matrizEstado)){
            Toast toast1 =Toast.makeText(getApplicationContext(),"Felicitaciones", Toast.LENGTH_SHORT);
            toast1.show();
            System.out.println("correcto");
        }
        else System.out.println("Incorrecto");

        ComprobarEstadoJuego();

    }

    private void CrearTablero(){
        for ( int i = 0; i< filas; i++){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            //Y POR CADA COLUMNA UNA VISTA QUE PUEDE SER UN BUTTON Y REPRESENTA EL ESPACIO
            for( int j = 0; j<columnas; j++){
                final int p = j;
                final int k=i;
                final Button b= new Button(this);
                matrizEstado[i][j]=0;
                matrizEstadoGuardar[i][j]='G'; // GRIS
                if(matrizInicial[i][j]!=0){
                    b.setEnabled(false);
                    b.setText(String.valueOf(matrizInicial[i][j]));
                    matrizEstado[i][j]=2;
                    matrizEstadoGuardar[i][j]='0';
                    b.setBackgroundColor(Color.WHITE);
                }
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChangeButtonState(k,p, b);
                        ButtonPressed();
                        PrintMatrizChar(matrizEstadoGuardar);
                    }
                });

                int buttonSize = screenWidth/columnas - 5;
                b.setLayoutParams(new LinearLayout.LayoutParams(buttonSize, buttonSize));
                // y se añade al layout
                layout.addView(b);
          }
            layoutPrincipal.addView(layout);
        }
    }

    //Termina el Main Activity

    //Comprueba matrizEstado completa o no, true si es completa
    private boolean comprobar(int matriz2[][]) {
        for (int i = 0; i < 2; i++) {    // El primer índice recorre las filas.
            for (int j = 0; j < 3; j++) {    // El segundo índice recorre las columnas.
                // Procesamos cada elemento de la matrizEstado.
                if (matriz2[i][j]==0) return false;
            }

        }
        return true;
    }

    private void Guardar(int mat[][], int resp[][]){

        for (int i = 0; i < mat.length; i++) {    // El primer índice recorre las filas.
            for (int j = 0; j < mat[0].length; j++) {    // El segundo índice recorre las columnas.
                // Procesamos cada elemento de la matrizEstado.
                resp[i][j]=mat[i][j];
            }
        }
    }

    private void Retomar(){
    }

    private void Pista(int mat[][], int resp[][]) throws InterruptedException {
        for (int i = 0; i < mat.length; i++) {    // El primer índice recorre las filas.
            for (int j = 0; j < mat[0].length; j++) {    // El segundo índice recorre las columnas.
                // Procesamos cada elemento de la matrizEstado.
                if( resp[i][j]!=mat[i][j]){
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "Debes corregir la celda ubicada en la fila "+i+"columna"+j+"gracias", Toast.LENGTH_SHORT);
                    toast1.show();
                    Thread.sleep(3000);
                }
            }
        }
    }
}
