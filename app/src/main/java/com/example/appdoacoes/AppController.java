package com.example.appdoacoes;

import android.app.Application;
import android.util.Log;

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Log.e("AppCrash", "Crash não tratado", throwable);
            // Aqui você pode adicionar lógica para reiniciar o app ou enviar relatórios de erro
        });
    }
}