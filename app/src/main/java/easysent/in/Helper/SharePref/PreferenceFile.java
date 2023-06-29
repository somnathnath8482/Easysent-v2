package easysent.in.Helper.SharePref;



import android.content.Context;
import android.content.SharedPreferences;


import easysent.in.Helper.ShareData;
import easysent.in.Response.Login.LoginResponse;
import easysent.in.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class PreferenceFile {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public PreferenceFile(Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        editor = preferences.edit();
    }

    public static void clearSessionManager() {
        if (editor!=null) {
            editor.clear().apply();
        }
    }

    public static void setLogged(boolean is) {
        editor.putBoolean("USER_LOGGED", is);
        editor.apply();
    }
    public static void setData(String Data,String key) {
        editor.putString(key,Data);
        editor.apply();
    }
      public static String getData(String key) {
       return preferences.getString(key, "");
    }


  public static void setFingirAuth(boolean is) {
        editor.putBoolean("2_STEP", is);
        editor.apply();
    }


    public static boolean getFingirAuth() {
        return preferences != null && preferences.getBoolean("2_STEP", false);
    }

    public static boolean isLogged() {
        return preferences == null ? false : preferences.getBoolean("USER_LOGGED", false);
    }
 public static String getUserType() {
        return preferences == null ? "" : preferences.getString(PreferenceKey.KEY_USER_TYPE,"");
    }







    public static LoginResponse getUser() {
        LoginResponse res = null;
        try {
            Gson gson = new Gson();
            String json = preferences.getString("ARTIX_USER", "");
            res = gson.fromJson(json, LoginResponse.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static boolean setUser(LoginResponse value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString("ARTIX_USER",json);
        return  editor.commit();
    }
 public static boolean setShare(ShareData value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString("SHARE_DATA",json);
        return  editor.commit();
    }
    public static ShareData getShare() {
        ShareData res = null;
        try {
            Gson gson = new Gson();
            String json = preferences.getString("SHARE_DATA", "");
            res = gson.fromJson(json, ShareData.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return res;
    }

}
