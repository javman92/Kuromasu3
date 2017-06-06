package com.example.richa.kuromasu3;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    int[][]juego = {{1,2,0}, {1, 1, 1}};
    int filas=2;
    int columnas=3;
    int matriz[][]= new int[2][3];
    private Button btnIrAMenu;

    //gris=0,negro=1, blanco=2

    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnIrAMenu = (Button) findViewById(R.id.btnMenu);
        btnIrAMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Menu.class));
            }
        });

        LinearLayout layoutPrincipal=(LinearLayout) this.findViewById(R.id.principal);

        for ( int i = 0; i< filas; i++){

            LinearLayout layout = new LinearLayout(this);

            layout.setOrientation(LinearLayout.HORIZONTAL);
            //Y POR CADA COLUMNA UNA VISTA QUE PUEDE SER UN BUTTON Y REPRESENTA EL ESPACIO
            for( int j = 0; j<columnas; j++){
                final int p = j;
                final int k=i;
                final Button b= new Button(this);
                b.setText(String.valueOf(juego[i][j]));
                matriz[i][j]=0;
                if(juego[i][j]!=0){

                    matriz[i][j]=2;
                    b.setBackgroundColor(Color.WHITE);}

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(matriz[k][p]==0){
                        b.setBackgroundColor(Color.BLACK);
                            matriz[k][p]++;
                        }
                        else if(matriz[k][p]==1){

                            b.setBackgroundColor(Color.WHITE);
                            matriz[k][p]++;
                        }
                        else {b.setBackgroundColor(Color.GRAY); matriz[k][p]=0;}



                       if(Arrays.deepEquals(juego, matriz)){Toast toast1 =
                               Toast.makeText(getApplicationContext(),
                                       "Felicitaciones", Toast.LENGTH_SHORT);

                           toast1.show();
                       System.out.println("correcto");
                       }
                       else System.out.println("Incorrecto");



                        if(comprobar(matriz)) {

                            if(Arrays.deepEquals(juego, matriz)){Toast toast1 =
                                    Toast.makeText(getApplicationContext(),
                                            "Felicitaciones", Toast.LENGTH_SHORT);

                                toast1.show();
                                System.out.println("correcto");
                            }

                            else {Toast toast1 =
                                    Toast.makeText(getApplicationContext(),
                                            "Incorrecto", Toast.LENGTH_SHORT);

                            toast1.show();}

                            try {
                                Pista(juego,matriz);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
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






