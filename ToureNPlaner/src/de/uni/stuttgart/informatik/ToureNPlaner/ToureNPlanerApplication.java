package de.uni.stuttgart.informatik.ToureNPlaner;

import android.app.Application;
import android.os.Build;
import android.util.Log;

public class ToureNPlanerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        disableConnectionReuseIfNecessary();
    }

    private void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        // Build.VERSION_CODES.FROYO
        if (Build.VERSION.SDK_INT < 8) {
            System.setProperty("http.keepAlive", "false");
	        Log.i("ToureNPlaner","HTTP keep-alive disabled");
        }
    }
}
