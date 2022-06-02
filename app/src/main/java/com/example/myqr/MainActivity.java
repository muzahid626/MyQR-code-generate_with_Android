package com.example.myqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity {
    public EditText QrValue;
    public Button GenerateQR,ScanQR,SaveQR;
    public ImageView DemoQR;

    BitmapDrawable drawable;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Generate QR Code");

        QrValue=findViewById(R.id.editTextID);
        GenerateQR=findViewById(R.id.generateBtn);
        ScanQR=findViewById(R.id.scanBtn);
        SaveQR=findViewById(R.id.saveQR);
        DemoQR=findViewById(R.id.demoQr);

        SaveQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawable = (BitmapDrawable)DemoQR.getDrawable();
                bitmap=drawable.getBitmap();

                FileOutputStream outputStream=null;

                File sdcard = Environment.getExternalStorageDirectory();
                File directory = new File(sdcard.getAbsolutePath()+"/MyQR");
                directory.mkdir();
                String fileName = String.format("%d.jpg",System.currentTimeMillis());
                File outfile = new File(directory,fileName);

                Toast.makeText(MainActivity.this, "QR Code Save Successfully", Toast.LENGTH_SHORT).show();

                try {
                    outputStream =new FileOutputStream(outfile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,200,outputStream);
                    outputStream.flush();
                    outputStream.close();

                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outfile));
                    sendBroadcast(intent);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        GenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = QrValue.getText().toString();
                QRGEncoder qrgEncoder = new QRGEncoder(data,null, QRGContents.Type.TEXT,2500);
                try {
                    Bitmap qrBits = qrgEncoder.encodeAsBitmap();
                    DemoQR.setImageBitmap(qrBits);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });

        ScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(myIntent);
                QrValue.setText("");
            }
        });
    }
}
