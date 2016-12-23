package com.gdgbangla.motiur.eventum;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity  implements ZXingScannerView.ResultHandler{
    private ZXingScannerView zXingScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    //menu Item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    //Select Items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About");
                builder.setMessage(getString(R.string.about));
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.help:
                message("Help");
                return true;
            case R.id.history:
                message("History");
                return true;
            case R.id.flash:
                message("History");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //This Method not working
    public void ok(View view) {
        zXingScannerView= new ZXingScannerView(this);
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }
    protected void onPause(){
        super.onPause();
        zXingScannerView.stopCamera();
        //message("onPause");
    }

    protected void onResume(){
        super.onResume();
        zXingScannerView= new ZXingScannerView(this);
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
        //message("onResume");
    }
    protected void onRestart(){
        super.onRestart();
        //message("onRestart");
    }
    protected void onStop(){
        super.onStop();
        //message("onStop");
    }protected void onDestroy(){
        super.onDestroy();
        //message("onDestroy");
    }



    @Override
    public void handleResult(Result result) {
        MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.sound);
        mediaPlayer.start();
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(200);

        String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(result.getText());//replace with string to compare
        if(m.find()) {
            String url=result.getText();
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            urlAlert(url);
        }else{
            textAlert(result.getText());
            message(result.getText());
        }
    }

    public void urlAlert(final String url){
        try{
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", url);
            clipboard.setPrimaryClip(clip);
            message(url+" copied to Clipboard");
        }catch (Exception e){
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("This is an URL");
        builder.setMessage(url);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Share", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent share=new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, url);
                startActivity(share);
                dialog.dismiss();
                message("Text Shared.");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                zXingScannerView.resumeCameraPreview(MainActivity.this);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void textAlert(final String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setTitle("This is Text");
        builder.setMessage(msg);
        builder.setPositiveButton("Copy It", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try{
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", msg);
                    clipboard.setPrimaryClip(clip);
                    //finish();
                    dialog.dismiss();
                    message("Text Copied.");
                    zXingScannerView.startCamera();
                }catch (Exception e){
                }
            }
        });
        builder.setNeutralButton("Share", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent share=new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, msg);
                startActivity(share);
                dialog.dismiss();
                message("Text Shared.");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                zXingScannerView.resumeCameraPreview(MainActivity.this);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void message(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1000);
    }

}


//Add dependency compile 'me.dm7.barcodescanner:zxing:1.8.4'
//Add Camera Permission
//Edit code in main Java file
//Have a look Transparent Actiobar http://stackoverflow.com/questions/29907615/android-transparent-status-bar-and-actionbar