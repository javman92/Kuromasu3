package com.example.richa.kuromasu3;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;

public class Game extends AppCompatActivity {

    int[][] matrizInicial = {{1,2,0}, {1, 1, 1}};
    int matrizSolucion[][]= new int[2][3];
    int filas=2,columnas=3;
    LinearLayout layoutPrincipal;

    //gris=0, negro=1, blanco=2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //matrizInicial = GetInitialMap();

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
            //Log.d("ROW", line);
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

        if(matrizSolucion[k][p]==0){
            b.setBackgroundColor(Color.BLACK);
            matrizSolucion[k][p]++;
        }
        else if(matrizSolucion[k][p]==1){
            b.setBackgroundColor(Color.WHITE);
            matrizSolucion[k][p]++;
        }
        else {b.setBackgroundColor(Color.GRAY); matrizSolucion[k][p]=0;}



        if(Arrays.deepEquals(matrizInicial, matrizSolucion)){
            Toast toast1 =Toast.makeText(getApplicationContext(),"Felicitaciones", Toast.LENGTH_SHORT);
            toast1.show();
            System.out.println("correcto");
        }
        else System.out.println("Incorrecto");

        if(comprobar(matrizSolucion)) {

            if(Arrays.deepEquals(matrizInicial, matrizSolucion)){
                Toast toast1 = Toast.makeText(getApplicationContext(),"Felicitaciones", Toast.LENGTH_SHORT);
                toast1.show();
                System.out.println("correcto");
            }

            else {
                Toast toast1 =Toast.makeText(getApplicationContext(),"Incorrecto", Toast.LENGTH_SHORT);
                toast1.show();
            }

            try {
                Pista(matrizInicial, matrizSolucion);
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
                b.setText(String.valueOf(matrizInicial[i][j]));
                matrizSolucion[i][j]=0;
                if(matrizInicial[i][j]!=0){
                    matrizSolucion[i][j]=2;
                    b.setBackgroundColor(Color.WHITE);}
                    b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    CheckSolution(k,p, b);
/*
                        for(int h=0;h<2;h++)    {
                            for(int k=0;k<3;k++){System.out.println("matrizSolucion 1"+matrizSolucion[k][h]+"matriz2"+matrizInicial[k][h]);}
                        }*/
                    }
                });
                // y se añade al layout
                layout.addView(b);}
                layoutPrincipal.addView(layout);
        }
    }

    //Termina el Main Activity

    //Comprueba matrizSolucion completa o no, true si es completa
    private boolean comprobar(int matriz2[][]) {
        for (int i = 0; i < 2; i++) {    // El primer índice recorre las filas.
            for (int j = 0; j < 3; j++) {    // El segundo índice recorre las columnas.
                // Procesamos cada elemento de la matrizSolucion.

                if (matriz2[i][j]==0) return false;
            }

        }
        return true;
    }

    private void Guardar(int mat[][], int resp[][]){

        for (int i = 0; i < mat.length; i++) {    // El primer índice recorre las filas.
            for (int j = 0; j < mat[0].length; j++) {    // El segundo índice recorre las columnas.
                // Procesamos cada elemento de la matrizSolucion.
                resp[i][j]=mat[i][j];

            }

        }
    }

    private void Retomar(){



    }

    private void Pista(int mat[][], int resp[][]) throws InterruptedException {
        for (int i = 0; i < mat.length; i++) {    // El primer índice recorre las filas.
            for (int j = 0; j < mat[0].length; j++) {    // El segundo índice recorre las columnas.
                // Procesamos cada elemento de la matrizSolucion.
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
