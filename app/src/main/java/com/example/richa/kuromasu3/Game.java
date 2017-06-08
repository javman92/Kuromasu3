package com.example.richa.kuromasu3;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;

public class Game extends AppCompatActivity {

    int[][] matrizInicial = {{1,2,0}, {1, 1, 1}};
    int[][] matrizEstado;
    int filas=2,columnas=3;
    LinearLayout layoutPrincipal;
    private GridLayout gridMap;
    private Button btnRestartGame, btnRules;

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

        matrizInicial = GetInitialMap();
        matrizEstado = new int[filas][columnas];
        //gridMap = (GridLayout) findViewById(R.id.gridMap);

        layoutPrincipal=(LinearLayout) this.findViewById(R.id.lLTablero);
        CrearTablero();

    }
    private void PrintMap(int rows, int cols, int[][] m){
        String line;
        for (int i =0; i< rows; i++){
            line ="";
            for (int j =0; j < cols; j++){
                line += m[i][j] + ",";
            }
        }
    }

    private int[][] GetInitialMap(){
        Intent in = getIntent();
        int cols = in.getIntExtra("MapaColumnas", 99 );
        int rows = in.getIntExtra("MapaFilas", 99 );
        filas = rows;
        columnas = cols;
        //Log.d("SETTINGS", cols+"x"+rows);
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

    private  void CheckSolution(int k, int p, Button b){

        if(matrizEstado[k][p]==0){
            b.setBackgroundColor(Color.BLACK);


            matrizEstado[k][p]++;
        }
        else if(matrizEstado[k][p]==1){
            b.setBackgroundColor(Color.WHITE);
            matrizEstado[k][p]++;
        }
        else {b.setBackgroundColor(Color.GRAY); matrizEstado[k][p]=0;}



        if(Arrays.deepEquals(matrizInicial, matrizEstado)){
            Toast toast1 =Toast.makeText(getApplicationContext(),"Felicitaciones", Toast.LENGTH_SHORT);
            toast1.show();
            System.out.println("correcto");
        }
        else System.out.println("Incorrecto");

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
                if(matrizInicial[i][j]!=0){
                    b.setEnabled(false);
                    b.setText(String.valueOf(matrizInicial[i][j]));
                    matrizEstado[i][j]=2;
                    b.setBackgroundColor(Color.WHITE);
                }
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckSolution(k,p, b);
                    }
                });

                int buttonSize = screenWidth/columnas - 5;
                Log.d("BUTTONSIZE",buttonSize + "");
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
