package com.example.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private Context ct;

    private SharedPreferences sharedPreferences;
    public LanguageManager(Context ctx){
        ct = ctx;
        sharedPreferences = ct.getSharedPreferences("LANG",Context.MODE_PRIVATE);
    }

    public void updateResource(String code){
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        SetLang(code);
    }

    public String GetLang(){
        return sharedPreferences.getString("lang","hi");
    }

    public void SetLang(String code){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang",code);
        editor.commit();
    }
}
