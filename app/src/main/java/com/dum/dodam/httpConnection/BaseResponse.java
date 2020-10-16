package com.dum.dodam.httpConnection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.dum.dodam.MainActivity;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    public String status;
    public String errorMessage;
    public int errorCode;

    public int checkError(Context context) {
        if (context == null) return 1;
        String[] errorArray = status.split(":");
        status = errorArray[0];
        if (errorArray[0].equals("<success>")) {
            return 0;
        } else {
            errorCode = Integer.parseInt(errorArray[1]);
            errorMessage = errorArray[2];
            switch (errorCode) {
                case 1:
                    SharedPreferences sharedPref = context.getSharedPreferences(
                            "auto", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPref.edit();
                    edit.clear();
                    edit.commit();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    return 1;
                case 2:
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    return 2;
                case 3:
                    return 3;
                case 4:
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    return 4;
            }
        }
        return 1;
    }
}
