package easysent.in.Networking.Helper;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

public class MethodClass {



    public static JSONObject Json_rpc_format(HashMap<String, String> params) {
        HashMap<String, Object> main_param = new HashMap<String, Object>();
        main_param.put("params", new JSONObject(params));
        main_param.put("jsonrpc", "2.0");
        Log.e("request", new JSONObject(main_param).toString());
        return new JSONObject(main_param);
    }

    public static HashMap<String, Object> Json_rpc_format_hashmap(HashMap<String, String> params) {
        HashMap<String, Object> main_param = new HashMap<String, Object>();
        main_param.put("jsonrpc", "2.0");
        main_param.put("params", params);
        Log.e("request", main_param.toString());
        return main_param;
    }

    public static JSONObject Json_rpc_format_obj(HashMap<String, Object> params) {
        HashMap<String, Object> main_param = new HashMap<String, Object>();
        main_param.put("params", new JSONObject(params));
        main_param.put("jsonrpc", "2.0");
        Log.e("request", new JSONObject(main_param).toString());
        return new JSONObject(main_param);
    }
}
