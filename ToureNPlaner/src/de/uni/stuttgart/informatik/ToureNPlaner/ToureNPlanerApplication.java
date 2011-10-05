package de.uni.stuttgart.informatik.ToureNPlaner;

import android.app.Application;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.SessionData;

public class ToureNPlanerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SessionData.createInstance(this);
    }
}
