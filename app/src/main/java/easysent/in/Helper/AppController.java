package easysent.in.Helper;

import android.app.Application;


import easysent.in.Helper.SharePref.PreferenceFile;


public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new PreferenceFile(this);
    }
}
