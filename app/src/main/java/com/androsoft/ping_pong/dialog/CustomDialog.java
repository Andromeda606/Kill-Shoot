package com.androsoft.ping_pong.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

// Tasarım değişikliğinde bütün dialogların değişmesi için.
public class CustomDialog {
    AlertDialog.Builder alertDialogBuilder;

    public CustomDialog(Context context) {
        alertDialogBuilder = new AlertDialog.Builder(context);
    }

    public static void showConnectionErrorDialog(Context context){
        new CustomDialog(context)
                .setTitle("Uyarı")
                .setMessage("Oyuncuya Bağlanılamadı")
                .setPositiveButton("OK", null)
                .show();
    }

    public CustomDialog setMessage(String message){
        alertDialogBuilder.setMessage(message);
        return this;
    }

    public CustomDialog setTitle(String title){
        alertDialogBuilder.setTitle(title);
        return this;
    }

    public CustomDialog setPositiveButton(String label, DialogInterface.OnClickListener dialogInterface){
        alertDialogBuilder.setPositiveButton(label, dialogInterface);
        return this;
    }

    public CustomDialog setNeutralButton(String label, DialogInterface.OnClickListener dialogInterface){
        alertDialogBuilder.setNeutralButton(label, dialogInterface);
        return this;
    }

    public void show(){
        alertDialogBuilder.show();
    }
}
