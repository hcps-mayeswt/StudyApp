package com.example.willm.study;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

public class AppSettingsHandler {
    //Keys for question settings preferences
    public static final String KEY_REQUIRE_PIN = "require_pin";
    public static final String KEY_PIN = "pin";

    public static void promptForPassword(Context context, final Runnable onCorrect, final Runnable onIncorrect) {
        final String pin = getPreference(context, KEY_PIN);
        boolean requiresPin = getBoolPreference(context, KEY_REQUIRE_PIN);
        if(!pin.equals("") && requiresPin){
            //Build the adapter to ask for password
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Enter password");
            // Set up the input
            final EditText input = new EditText(context);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            //Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String userInput = input.getText().toString();
                    try {
                        if (userInput.equals(pin)) {
                            onCorrect.run();
                        } else {
                            onIncorrect.run();
                        }
                    }catch(Exception e){}
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        else{
            onCorrect.run();
        }
    }

    public static String getPreference (Context context,String prefKey){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(prefKey, "");
    }

    public static boolean getBoolPreference(Context context, String prefKey){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(prefKey, false);
    }
}