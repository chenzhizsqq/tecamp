package com.example.tecamp.sql;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonFile {
    private File file;

    public JsonFile(Context context) {

        file = new File(context.getFilesDir(), "lastOrderList.json");
    }

    // ファイルを保存
    public void saveFile(String str) {
        // try-with-resources
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ファイルを読み出し
    public String readFile() {
        String text = null;

        // try-with-resources
        try(
                BufferedReader br = new BufferedReader(new FileReader(file));
        ){
            text = br.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }
}
