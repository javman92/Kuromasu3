package com.example.richa.kuromasu3;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;

public class Game extends AppCompatActivity {

    int[][]juego = {{1,2,2}, {1, 1, 1}};
    int filas=2;
    int columnas=3;
    final int matriz[][]= new int[2][3];
    LinearLayout linearLTablero;


    //gris=0,negro=1, blanco=2

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        linearLTablero =(LinearLayout) this.findViewById(R.id.lLTablero);


        CreacionTablero();
    }


    private void CreacionTablero(){
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


                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        System.out.println("hola2");
                        imprime(matriz);
                        if(comprobar(matriz)) System.out.println("Completo");

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

/*
                        for(int h=0;h<2;h++)    {
                            for(int k=0;k<3;k++){System.out.println("matriz 1"+matriz[k][h]+"matriz2"+juego[k][h]);}
                        }*/

                    }
                });


// y se añade al layout
                layout.addView(b);}
            linearLTablero.addView(layout);
        }
    }

//Termina el Main Activity

    private void imprime(int matriz2[][]) {
        System.out.println("hola");
        for (int i = 0; i < 2; i++) {    // El primer índice recorre las filas.
            for (int j = 0; j < 3; j++) {    // El segundo índice recorre las columnas.
                // Procesamos cada elemento de la matriz.
                System.out.println("el numero es"+matriz2[i][j]);

            }

        }
    }

    private boolean comprobar(int matriz2[][]) {
        for (int i = 0; i < 2; i++) {    // El primer índice recorre las filas.
            for (int j = 0; j < 3; j++) {    // El segundo índice recorre las columnas.
                // Procesamos cada elemento de la matriz.

                if (matriz2[i][j]==0) return false;
            }
        }
        return true;
    }
}
