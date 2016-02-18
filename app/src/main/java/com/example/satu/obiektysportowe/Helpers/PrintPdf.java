package com.example.satu.obiektysportowe.Helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Piotrek on 2015-05-24.
 */

public class PrintPdf {

    public PrintPdf() {

    }

    @TargetApi(19)
    public void generatePDF(Context context, View v){

            PdfDocument document = new PdfDocument();

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();

            PdfDocument.Page page = document.startPage(pageInfo);

            View content = v;
            content.draw(page.getCanvas());

            document.finishPage(page);

            //data wstawiana do nazwy pliku
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy_HH-mm-ss");
            Calendar calendar = Calendar.getInstance();


            try {
                //zamiast context.getFilesDir() mozna podac sciezke na sztywno, np. do folderu Download uzytkownika
                //obecnie zapisuje do folderu systemowego aplikacji: /data/data/com.example.satu.obiektysportowe/files/
                // lub, w zależności od systemu, /Android/data/<nazwa_pakietu>/files/

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Potwierdzenie_" + df.format(calendar.getTime()) + ".pdf");
                FileOutputStream os = new FileOutputStream(file);
                document.writeTo(os);
                document.close();
                os.close();

            } catch (IOException e) {
                throw new RuntimeException("Error saving file", e);
            } finally {
                Toast.makeText(context, "Zapisano plik w folderze Downloads.", Toast.LENGTH_LONG).show();
            }

    }
}