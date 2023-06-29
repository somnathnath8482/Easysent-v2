package easysent.in.Helper;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import easysent.in.Activity.MainActivity;
import easysent.in.R;

import io.github.muddz.styleabletoast.StyleableToast;

public class NetWorkChecker {
    static NetworkInfo wifi, mobile;

    public static Boolean check(Context c,Handler handler) {

        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (wifi != null && wifi.isConnected() && wifi.isAvailable()) {
         handler.post(new Runnable() {
             @Override
             public void run() {
                 NoInternet(c,false);

             }
         });
            return true;
        } else if (mobile != null && mobile.isAvailable() && mobile.isConnected()) {
         handler.post(new Runnable() {
             @Override
             public void run() {
                 NoInternet(c,false);
             }
         });
            return true;
        } else {
           handler.post(new Runnable() {
               @Override
               public void run() {
                   NoInternet(c,true);
               }
           });
            //Toast.makeText(c, "No Network Connection", Toast.LENGTH_SHORT).show();
           // ((Activity) c).finish();
            //displayMobileDataSettingsDialog(c,"No Network","No Network Connection");
            return false;
        }

    }

    public static void NoInternet(Context context,boolean is){
        if (MainActivity.binding!=null)
        try {

            Transition transition = new Slide(Gravity.TOP);
            transition.setDuration(600);
            transition.addTarget(MainActivity.binding.noInternet);

            TransitionManager.beginDelayedTransition((ViewGroup) MainActivity.binding.noInternet.getParent(), transition);
           if (is){
               MainActivity.binding.noInternet.setVisibility(View.VISIBLE);
           }else{
               MainActivity.binding.noInternet.setVisibility(View.GONE);
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void displayMobileDataSettingsDialog(final Context context, String title, String message) {
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
               /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setCancelable(false);
                builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                        context.startActivity(intent);
                    }
                });

                builder.show();*/
                StyleableToast.makeText(context, message, R.style.mytoast).show();
            }
        });

    }

}
