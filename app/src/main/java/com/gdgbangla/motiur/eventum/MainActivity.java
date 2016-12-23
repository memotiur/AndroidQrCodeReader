package com.gdgbangla.motiur.eventum;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity  implements ZXingScannerView.ResultHandler{
    private ZXingScannerView zXingScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ok(View view) {
        zXingScannerView= new ZXingScannerView(this);
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }
    protected void onPause(){
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        /*AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(result.getText());
        AlertDialog alertDialog=builder.create();
        alertDialog.show();*/
        zXingScannerView.resumeCameraPreview(this);
        message("Result is "+result);

    }
    public void message(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

}


//Add dependency compile 'me.dm7.barcodescanner:zxing:1.8.4'
//Add Camera Permission
//Edit code in main Java file