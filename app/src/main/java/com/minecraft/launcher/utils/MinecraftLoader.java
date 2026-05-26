package com.minecraft.launcher.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import java.io.File;

public class MinecraftLoader {

    private static final String TAG = "MinecraftLoader";
    private static final String MINECRAFT_PKG = "com.mojang.minecrafttrialpe";

    public static boolean injectMinecraft(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(MINECRAFT_PKG, 0);
            
            String nativeLibDir = appInfo.nativeLibraryDir;
            Log.d(TAG, "Detectando arquitectura del sistema: " + android.os.Build.SUPPORTED_ABIS[0]);
            Log.d(TAG, "Directorio de librerías de Minecraft: " + nativeLibDir);

            // Intentar cargar la librería principal de Minecraft
            // Nota: En Android 15, System.load puede fallar si la librería no tiene los permisos correctos
            // o si hay una discrepancia de arquitectura (32-bit vs 64-bit).
            File libMinecraft = new File(nativeLibDir, "libminecraftpe.so");
            
            if (libMinecraft.exists()) {
                Log.d(TAG, "Intentando carga nativa de: " + libMinecraft.getAbsolutePath());
                
                // Intentamos la carga. Si esto causa un crash, es por protección del kernel de Android.
                System.load(libMinecraft.getAbsolutePath());
                
                Log.d(TAG, "¡INYECCIÓN EXITOSA! La librería de Minecraft ha sido cargada en el proceso.");
                return true;
            } else {
                Log.e(TAG, "ERROR: No se encontró libminecraftpe.so en " + nativeLibDir);
                return false;
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Minecraft Trial no está instalado.");
            return false;
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "CRASH EVITADO: Error de enlace nativo. Probablemente incompatibilidad de arquitectura o protección de Android 15.");
            Log.e(TAG, "Detalle: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error inesperado durante la inyección: " + e.getMessage());
            return false;
        }
    }
}
