package com.androsoft.killshot.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.androsoft.killshot.R;

// Tasarım değişikliğinde bütün dialogların değişmesi için.
public class CustomDialog {
    AlertDialog.Builder alertDialogBuilder;

    public CustomDialog(Context context) {
        alertDialogBuilder = new AlertDialog.Builder(context);
    }

    public static void showConnectionErrorDialog(Context context){
        new CustomDialog(context)
                .setTitle(context.getString(R.string.warning))
                .setMessage(context.getString(R.string.connect_apply_error))
                .setPositiveButton(context.getString(R.string.ok), null)
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
