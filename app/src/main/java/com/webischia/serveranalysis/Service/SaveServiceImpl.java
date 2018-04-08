package com.webischia.serveranalysis.Service;

// Dosyaya buradaki metotlardan yazdırıyoruz.

import android.content.Context;
import android.util.Log;

import com.webischia.serveranalysis.Controls.SaveControl;
import com.webischia.serveranalysis.Models.Graphic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SaveServiceImpl implements SaveService {

    SaveControl saveControl;
    static Context context;

    public SaveServiceImpl(SaveControl saveControl,Context context) {
        this.saveControl = saveControl;
        this.context = context;
    }

    @Override
    public void saveGraphics(Graphic graphObj) {
            //Java serialization ile yaz
        try {
            //grafik ismine göre kayıt yapılıyor. Aynı isimde iki grafik olamaz.
            File file = new File(context.getFilesDir(), graphObj.getName()+".ser");
            file.createNewFile();
            FileOutputStream outFile = context.openFileOutput(graphObj.getName()+".ser",Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(outFile);
            out.writeObject(graphObj); //objeyi yazdırdık
            out.close();
            outFile.close();
            Log.d("SAVE_GRAPH","SUCCESS");
            //saveNames(graphObj.getName());
            saveControl.successSave(graphObj.getName(),context);
        } catch (IOException i) {
            i.printStackTrace();
            Log.d("SAVE_GRAPH","NOPE");

        }
    }

    @Override
    public Graphic loadGraphics(String name) {
        //Java deserialization ile oku graphic nesnesine aç ve döndür
        Graphic temp;
        try {
            FileInputStream fileIn = context.openFileInput(name + ".ser"); //bu isimi nereden okuyacağız
            ObjectInputStream in = new ObjectInputStream(fileIn);             // ayrı bir text gibi birşeye kayıt ?
            temp = (Graphic) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Graphic not found");
            c.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public void saveNames(String name,String username) {

        File file = new File(context.getFilesDir(), username+".dat");
        int id;
        if(file.exists() != true) {
            id=Context.MODE_PRIVATE;
        }
        else
            id=Context.MODE_APPEND;

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(username+".dat", id);
            outputStream.write(name.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public void loadNames(String username) {
    List<Graphic> graphicList = new ArrayList<Graphic>();
        try {
            BufferedReader in
                    = new BufferedReader(new FileReader(username+".dat"));
            while(in.ready())
            {
                String line = in.readLine();
                graphicList.add(loadGraphics(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveControl.loadGraphs(graphicList,context);
        //return graphicList;
    }
}
