package com.minecraft.launcher.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import com.minecraft.launcher.DeviceAdminReceiver;

public class SecurityUtils {

    public static void setAppHidden(Context context, String packageName, boolean hidden) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminComponent = new ComponentName(context, DeviceAdminReceiver.class);
        
        if (dpm.isAdminActive(adminComponent)) {
            try {
                // Nota: setApplicationHidden requiere que la app sea propietaria del dispositivo o perfil
                // En un entorno normal de usuario, esto podría no funcionar sin privilegios elevados.
                // Se implementa como se solicitó para el "sistema seguro para ocultar".
                dpm.setApplicationHidden(adminComponent, packageName, hidden);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
