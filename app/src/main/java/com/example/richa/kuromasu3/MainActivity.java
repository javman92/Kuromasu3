package com.example.richa.kuromasu3;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {


    EditText et;
    String archivoBytes = "archivoBytes.txt";
    String archivoDatos = "archivoDatos.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int filas=8;
        int columnas=8;
        final int matriz[][]= new int[20][20];


        LinearLayout layoutPrincipal=(LinearLayout) this.findViewById(R.id.principal);

        for ( int i = 0; i< filas; i++){

            LinearLayout layout = new LinearLayout(this);

            layout.setOrientation(LinearLayout.HORIZONTAL);


//Y POR CADA COLUMNA UNA VISTA QUE PUEDE SER UN BUTTON Y REPRESENTA EL ESPACIO
            for( int j = 0; j<columnas; j++){
                final int p = j;
                final int k=i;
                final Button b= new Button(this);

                matriz[i][j]=0;

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(matriz[k][p]==0){
                        b.setBackgroundColor(Color.BLACK);
                            matriz[k][p]++;
                        }
                        else if(matriz[k][p]==1){

                            b.setBackgroundColor(Color.RED);
                            matriz[k][p]++;
                        }
                        else {b.setBackgroundColor(Color.GRAY); matriz[k][p]=0;}


                    }
                });

// y se añade al layout
                layout.addView(b);}
            layoutPrincipal.addView(layout);
        }
    }



//Termina el Main Activity


    public void onClickLoadData(View view) {
        try {
            FileInputStream fis = openFileInput(archivoDatos);
            DataInputStream dis = new DataInputStream(fis);
            Byte b = dis.readByte();
            Character c = dis.readChar();
            Double d = dis.readDouble();
            Integer i = dis.readInt();
            Long l = dis.readLong();
            Float f = dis.readFloat();
            String su = dis.readUTF();
            DisplayToast(b.toString() + " " + c.toString() + " " + d.toString() + " "
                    + i.toString() + l.toString() + " " + f.toString() + " " + su);
            dis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void onClickSaveData(View view) {
        try {
            FileOutputStream fos = openFileOutput(archivoDatos, MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeByte(10);
            dos.writeChar('A');
            dos.writeDouble(3.14159);
            dos.writeInt(123);
            dos.writeLong(1234567890);
            dos.writeFloat(3.1415926F);
            dos.writeUTF("Esto es UTF");
            dos.flush();
            dos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DisplayToast("Escribió datos en archivo");
    }

    private void DisplayToast(String mensaje) {
        Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_SHORT).show();
    }



}
