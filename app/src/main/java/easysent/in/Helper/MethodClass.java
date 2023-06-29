package easysent.in.Helper;

import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.Constants.CATCH_DIR;
import static easysent.in.Helper.Constants.CATCH_DIR2;
import static easysent.in.Helper.Constants.GET_IP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import easysent.in.Activity.LoginActivity;
import easysent.in.Firebase.Data;
import easysent.in.Firebase.Sender;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.AllInterFace;
import easysent.in.Interface.OnDateSelect;
import easysent.in.Interface.OnMenuItemClick;
import easysent.in.Interface.OnMobileExist;
import easysent.in.Interface.Response;
import easysent.in.Networking.Interface.OnError;
import easysent.in.Networking.Interface.OnSuccess;
import easysent.in.Networking.Main;
import easysent.in.R;
import easysent.in.Response.Error.Error;
import easysent.in.Response.getIp.GetIp;
import easysent.in.Room.Block.BlockViewModel;
import easysent.in.Room.GroupChat.Group_Chat;
import easysent.in.Room.GroupChat.Groups_chat_ViewModel;
import easysent.in.Room.Groups.Groups_ViewModel;
import easysent.in.Room.Messages.Chats;
import easysent.in.Room.Messages.Message_View_Model;
import easysent.in.Room.Threads.Thread_ViewModel;
import easysent.in.Room.Users.UserVewModel;
import easysent.in.Room.Users.Users;
import easysent.in.databinding.ReportLayBinding;
import io.github.muddz.styleabletoast.StyleableToast;


public class MethodClass {

    public static HashMap<String, Object> Json_rpc_format_hashmap(HashMap<String, String> params) {
        HashMap<String, Object> main_param = new HashMap<String, Object>();
        main_param.put("jsonrpc", "2.0");
        main_param.put("params", params);
        Log.e("request", main_param.toString());
        return main_param;
    }

    public static void Animate(Context context, View view) {
        //Transition transition = new Fade();
        //Transition transition = new CircularRevealTransition();
        Transition transition = new Slide(Gravity.END);
        transition.setDuration(600);
        transition.addTarget(view);

        TransitionManager.beginDelayedTransition((ViewGroup) view.getParent(), transition);
        view.setVisibility(view.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    public static void Animate(Context context, View hide, View show) {
        //Transition transition = new Fade();
        //Transition transition = new CircularRevealTransition();
        hide.setVisibility(View.GONE);
        Transition transition = new Slide(Gravity.END);
        transition.setDuration(600);
        transition.addTarget(show);

        TransitionManager.beginDelayedTransition((ViewGroup) show.getParent(), transition);
        show.setVisibility(View.VISIBLE);
    }

    public static void Animate(Context context, View show, int slideEdge) {

        Transition transition = new Slide(slideEdge);
        transition.setDuration(600);
        transition.addTarget(show);

        TransitionManager.beginDelayedTransition((ViewGroup) show.getParent(), transition);
        show.setVisibility(View.VISIBLE);
    }


    public static String isLogged() {
        if (PreferenceFile.isLogged()) {
            if (PreferenceFile.getUserType() != null && !PreferenceFile.getUserType().equalsIgnoreCase("")) {
                if (PreferenceFile.getUserType().equalsIgnoreCase("S")) {
                    return "S";
                } else if (PreferenceFile.getUserType().equalsIgnoreCase("F")) {
                    return "F";
                }
            } else {
                return "";
            }

        } else {
            return "";
        }
        return "";
    }


    public static boolean isStudent() {
        if (isLogged().equalsIgnoreCase("S")) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isFacilator() {
        if (isLogged().equalsIgnoreCase("F")) {
            return true;
        } else {
            return false;
        }
    }

    public static void logout(Activity activity, Handler handler) {

        Message_View_Model message_view_model = ViewModelProviders.of((FragmentActivity) activity).get(Message_View_Model.class);
        Thread_ViewModel thread_viewModel = ViewModelProviders.of((FragmentActivity) activity).get(Thread_ViewModel.class);
        BlockViewModel blockViewModel = new BlockViewModel(activity.getApplication());
        Groups_ViewModel groups_viewModel = new Groups_ViewModel(activity.getApplication());
        Groups_chat_ViewModel groups_chat_viewModel = new Groups_chat_ViewModel(activity.getApplication());
        new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_QUESTION)

                .setTitle("Logout")
                .setDescription("Are you sure you want to log out")
                .setPositiveText("Yes")
                .setNegativeText("No")
                .setPositiveTextColor(activity.getResources().getColor(R.color.white))
                .setNegativeTextColor(activity.getResources().getColor(R.color.white))
                .setPositiveListener(new ClickListener() {
                    @Override
                    public void onClick(@NonNull LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();

                        String type = "STATUS";
                        Data data = new Data(type, PreferenceFile.getUser().getUser().getId(), "off");
                        Sender se = new Sender(data, "/topics/EASY_ALL");
                        MethodClass.SendNotificationOnTopic(se, activity.getApplication(), handler);
                        thread_viewModel.deleteAll();
                        message_view_model.deleteAll();
                        blockViewModel.deleteAll();
                        groups_viewModel.deleteAll();
                        groups_chat_viewModel.deleteAll();
                        activity.finishAffinity();
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        PreferenceFile.clearSessionManager();
                    }
                }).setNegativeListener(new ClickListener() {
                    @Override
                    public void onClick(@NonNull LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();
                    }
                }).build().show();

    }

    public static int getResId(String resName, Class<?> c) {

        try {

            char[] initial = resName.toLowerCase(Locale.ROOT).toCharArray();
            String initial_char = "img_" + initial[0];

            Field idField = c.getDeclaredField(initial_char);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static List<String> emailValidator(List<String> email) {
        List<String> valid = new ArrayList<>();
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        for (String em : email) {
            matcher = pattern.matcher(em);
            if (matcher.matches()) {
                valid.add(em);
            }

        }
        return valid;
    }

    public static boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public static boolean isValidFile(String path) {

        return Pattern.compile("([a-zA-Z]:)?(\\\\[a-zA-Z0-9._-]+)+\\\\?").matcher(path).matches();
    }


    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean checkEditText(EditText editText) {
        if (editText != null && editText.getText().toString().trim().length() > 0) {

            return true;

        } else {
            return false;
        }
    }

    public static boolean hasError(Application activity, Error error, Handler handler) {

        if (error != null) {

            handler.post(() -> {

                try {
                    LottieAlertDialog lottieAlertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_ERROR)
                            .setPositiveButtonColor(Color.parseColor("#048B3A"))
                            .setNegativeButtonColor(Color.parseColor("#DA1606"))
                            .setNoneButtonColor(Color.parseColor("#038DFC"))
                            .setPositiveTextColor(Color.WHITE)
                            .setNegativeTextColor(Color.WHITE)
                            .setNoneTextColor(Color.WHITE)
                            .setTitle("Error")
                            .setDescription(error.getMessage())
                            .setNoneText("OK")
                            .setNoneListener(new ClickListener() {
                                @Override
                                public void onClick(@NonNull LottieAlertDialog lottieAlertDialog) {
                                    // activity.onBackPressed();
                                    lottieAlertDialog.dismiss();
                                }
                            })
                            .build();

                    lottieAlertDialog.setCancelable(false);
                    lottieAlertDialog.setCanceledOnTouchOutside(false);
                    lottieAlertDialog.show();
                } catch (Exception e) {
                    StyleableToast.makeText(activity.getApplicationContext(), "" + error.getMessage(), R.style.mytoast).show();
                }
            });


            return false;
        }


        return true;
    }

    public static boolean hasError2(Activity activity, Error error, Handler handler) {

        if (error != null) {

            handler.post(() -> {

                try {
                    LottieAlertDialog lottieAlertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_ERROR)
                            .setPositiveButtonColor(Color.parseColor("#048B3A"))
                            .setNegativeButtonColor(Color.parseColor("#DA1606"))
                            .setNoneButtonColor(Color.parseColor("#038DFC"))
                            .setPositiveTextColor(Color.WHITE)
                            .setNegativeTextColor(Color.WHITE)
                            .setNoneTextColor(Color.WHITE)
                            .setTitle("Error")
                            .setDescription(error.getMessage())
                            .setNoneText("OK")
                            .setNoneListener(new ClickListener() {
                                @Override
                                public void onClick(@NonNull LottieAlertDialog lottieAlertDialog) {
                                    // activity.onBackPressed();
                                    lottieAlertDialog.dismiss();
                                }
                            })
                            .build();

                    lottieAlertDialog.setCancelable(false);
                    lottieAlertDialog.setCanceledOnTouchOutside(false);
                    lottieAlertDialog.show();
                } catch (Exception e) {
                    StyleableToast.makeText(activity.getApplicationContext(), "" + error.getMessage(), R.style.mytoast).show();
                }
            });


            return false;
        }


        return true;
    }


    public static void OnDataExist(OnMobileExist onMobileExist, String url, Application context, Handler handler, HashMap<String, Object> map) {


        if (!NetWorkChecker.check(context, handler))
            return;

        Main main = new Main(context, handler, new OnError() {
            @Override
            public void OnEror(String url, String code, String message) {

                int a = 1 + 1;

            }
        }, new OnSuccess() {
            @Override
            public void OnSucces(String url, String code, String res) {
                int a = 1 + 1;
            }


        });

        main.CallPostRequestFormdata(BASE_URL + "register_user.php", "", map);


    }

    public static String getDate(String inputPattern, String outputPattern, String data) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            Date date = inputFormat.parse(data);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ParseException", e.toString());
        }
        return data;
    }

    public static void GetSelectdDate(OnDateSelect onselect, Context context, Long max, Long min) {

        final Calendar c = Calendar.getInstance();
        // c.add(Calendar.YEAR, -18);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, year, monthOfYear, dayOfMonth) -> {
           /* fd_mDay = dayOfMonth + 1;
            fd_mMonth = monthOfYear;
            fd_mYear = year;*/
            String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            onselect.OnSelect(getDate("yyyy-M-d", "dd-MM-yyyy", date));

            //current_age = getAge(fd_mYear,fd_mMonth,fd_mDay);

        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(max);
        datePickerDialog.getDatePicker().setMinDate(min);
        datePickerDialog.show();


    }


    public static void Call(Response onReponse, String url, Activity context, Handler handler, HashMap<String, Object> map) {

        if (NetWorkChecker.check(context, handler)) {
            CustomProgressbar.showProgressBar(context, false);
            Main main = new Main(context.getApplication(), handler, new OnError() {
                @Override
                public void OnEror(String url, String code, String message) {
                    CustomProgressbar.hideProgressBar();
                    Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                }
            }, new OnSuccess() {
                @Override
                public void OnSucces(String url, String code, String res) {
                    CustomProgressbar.hideProgressBar();
                    if (onReponse != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject != null) {
                                if (jsonObject.has("error")) {
                                    Error error = new Error(jsonObject.getString("code"), jsonObject.getString("error"));
                                    hasError(context.getApplication(), error, handler);
                                } else {
                                    onReponse.onResponse(new JSONObject(res));
                                }
                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            });
            main.CallPostRequestFormdata(url, "", map);


        }


    }


    public static void CallMultipart_NO_ProgressBar(Response onReponse, String url, Application context, Handler handler, HashMap<String, Object> map, String file, String key) {

        if (NetWorkChecker.check(context, handler)) {
            Main main = new Main(context, handler, new OnError() {
                @Override
                public void OnEror(String url, String code, String message) {
                    Toast.makeText(context, "We are faild to load data", Toast.LENGTH_SHORT).show();
                    Log.e("Send Failed", "OnEror: " + message);
                }
            }, new OnSuccess() {
                @Override
                public void OnSucces(String url, String code, String res) {
                    if (onReponse != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject != null) {
                                if (jsonObject.has("error")) {
                                    Error error = new Error(jsonObject.getString("code"), jsonObject.getString("error"));
                                    hasError(context, error, handler);
                                } else {
                                    Log.e("TAG", "OnSucces: send message " + res);
                                    onReponse.onResponse(new JSONObject(res));
                                }
                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            });
            List<File> f = new ArrayList<>();
            f.add(new File(file));
            main.CAllMultipartRequest(url, "", map, f, key);


        }


    }

    public static void CallMultipart_NO_ProgressBarWithProgress(Response onReponse, String url, Application context,
                                                                Handler handler, HashMap<String, Object> map, String file,
                                                                String key, ProgressBar progress) {

        if (NetWorkChecker.check(context, handler)) {
            Main main = new Main(context, handler, new OnError() {
                @Override
                public void OnEror(String url, String code, String message) {
                    Toast.makeText(context, " " + message, Toast.LENGTH_SHORT).show();
                    Log.e("Send Failed", "OnEror: " + message);
                }
            }, new OnSuccess() {
                @Override
                public void OnSucces(String url, String code, String res) {
                    if (onReponse != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject != null) {
                                if (jsonObject.has("error")) {
                                    Error error = new Error(jsonObject.getString("code"), jsonObject.getString("error"));
                                    hasError(context, error, handler);
                                } else {
                                    onReponse.onResponse(new JSONObject(res));
                                }
                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            });

            main.CAllMultipartRequestWithprogress(url, "", map, new File(file), key, progress);


        }


    }


    public static void CallMultipart(Response onReponse, String url, Activity context, Handler handler, HashMap<String, Object> map, String file, String key) {

        if (NetWorkChecker.check(context, handler)) {
            CustomProgressbar.showProgressBar(context, false);
            Main main = new Main(context.getApplication(), handler, new OnError() {
                @Override
                public void OnEror(String url, String code, String message) {
                    CustomProgressbar.hideProgressBar();
                    Toast.makeText(context, "We are faild to load data", Toast.LENGTH_SHORT).show();
                }
            }, new OnSuccess() {
                @Override
                public void OnSucces(String url, String code, String res) {
                    CustomProgressbar.hideProgressBar();
                    if (onReponse != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject != null) {
                                if (jsonObject.has("error")) {
                                    Error error = new Error(jsonObject.getString("code"), jsonObject.getString("error"));
                                    hasError(context.getApplication(), error, handler);
                                } else {
                                    onReponse.onResponse(new JSONObject(res));
                                }
                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            });
            List<File> f = new ArrayList<>();
            f.add(new File(file));
            main.CAllMultipartRequest(url, "", map, f, key);


        }


    }


    public static void CallHeader(Response onReponse, String url, Application context, Handler handler, String header, HashMap<String, Object> map) {

        if (NetWorkChecker.check(context, handler)) {
            Main main = new Main(context, handler, new OnError() {
                @Override
                public void OnEror(String url, String code, String message) {
                    Toast.makeText(context, "We are faild to load data", Toast.LENGTH_SHORT).show();
                    Log.e("Send Faild", "OnEror: " + message);
                }
            }, new OnSuccess() {
                @Override
                public void OnSucces(String url, String code, String res) {
                    if (onReponse != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject != null) {
                                if (jsonObject.has("error")) {
                                    onReponse.onResponse(new JSONObject(res));
                                    Error error = new Error(jsonObject.getString("code"), jsonObject.getString("error"));
                                    // hasError(context,error);
                                } else {
                                    onReponse.onResponse(new JSONObject(res));
                                }
                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("TAG", "OnSucces: " + res);
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            });
            main.CallPostRequestFormdata(url, header, map);


        }


    }

    public static void CallHeader2(Response onReponse, String url, Application context, Handler handler, String header, HashMap<String, Object> map) {

        if (NetWorkChecker.check(context, handler)) {
            Main main = new Main(context, handler, new OnError() {
                @Override
                public void OnEror(String url, String code, String message) {
                    Toast.makeText(context, "We are faild to load data", Toast.LENGTH_SHORT).show();
                }
            }, new OnSuccess() {
                @Override
                public void OnSucces(String url, String code, String res) {
                    if (onReponse != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject != null) {
                                if (jsonObject.has("error")) {
                                    Error error = new Error(jsonObject.getString("code"), jsonObject.getString("error"));
                                    hasError(context, error, handler);
                                } else {
                                    onReponse.onResponse(new JSONObject(res));
                                }
                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            });
            main.CallPostRequestFormdata(url, header, map);


        }


    }

    public static String CheckEmpty(EditText editText) throws Exception {
        if (editText.getText().toString().trim().equals("")) {
            if (editText.isFocusable()) {
                editText.setError("Please Enter " + editText.getHint());
                editText.requestFocus();
            } else {
                Toast.makeText(editText.getContext(), "Please Enter" + editText.getHint(), Toast.LENGTH_SHORT).show();
            }

            throw new Exception(editText.getId() + "");
        } else {
            return editText.getText().toString().trim();
        }
    }


    public static void CallUpdate(Response onReponse, String url, Application context, Handler handler, String header, HashMap<String, Object> map) {

        if (NetWorkChecker.check(context, handler)) {
            Main main = new Main(context, handler, new OnError() {
                @Override
                public void OnEror(String url, String code, String message) {
                    Toast.makeText(context, "We are faild to load data", Toast.LENGTH_SHORT).show();
                }
            }, new OnSuccess() {
                @Override
                public void OnSucces(String url, String code, String res) {
                    if (onReponse != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject != null) {
                                if (jsonObject.has("error")) {
                                    Error error = new Error(jsonObject.getString("code"), jsonObject.getString("error"));
                                    // hasError(context,error);
                                } else {
                                    onReponse.onResponse(new JSONObject(res));
                                }
                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            });
            main.CallPostRequestFormdata(url, header, map);


        }


    }

    public static void SendNotification(Sender sender, Application activity, Handler handler) {
        Log.e("TAG", "SendNotification: " + sender.toString());
        if (!NetWorkChecker.check(activity, handler)) {
            return;
        }

        Main main = new Main(activity, handler, new OnError() {
            @Override
            public void OnEror(String url, String code, String message) {
                Log.d("TAG", "OnEror: SendNotification ");

            }
        }, new OnSuccess() {
            @Override
            public void OnSucces(String url, String code, String res) {
                Log.d("TAG", "OnSucces: SendNotification " + sender.data.getMessage() + " " + sender.data.getMessage());


            }
        });

        Gson gson = new Gson();
        String jsonString = gson.toJson(sender);
        try {
            JSONObject request = new JSONObject(jsonString);
            main.SendNotification(Constants.SEND_NOTIFICATION, Constants.FIREBASE_AUTH, "", jsonString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    public static String getTimeAgo(String dt) {
        if (dt == null || dt.equals("")) {
            return null;
        }
        String date = dt.replaceAll("-", "/");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date d = utcFormat.parse(date);

            DateFormat pstFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //DateFormat pstFormat = new SimpleDateFormat("hh:mm a");;
            pstFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
            assert d != null;
            String datess = pstFormat.format(d);
            String dd = datess.replaceAll("/", "-");

            return TimeAgo2.convertAM_PM(dd);


        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }


    public static void SendNotificationOnTopic(Sender sender, Application activity, Handler handler) {
        if (!NetWorkChecker.check(activity, handler)) {
            return;
        }

        Main main = new Main(activity, handler, new OnError() {
            @Override
            public void OnEror(String url, String code, String message) {
                Log.d("TAG", "OnEror: SendNotification topic");

            }
        }, new OnSuccess() {
            @Override
            public void OnSucces(String url, String code, String res) {
                Log.d("TAG", "OnSucces: SendNotification topic" + sender.data.getMessage() + " " + res);


            }
        });

        Gson gson = new Gson();
        String jsonString = gson.toJson(sender);
        try {
            JSONObject request = new JSONObject(jsonString);
            main.SendNotification(Constants.SEND_NOTIFICATION, Constants.FIREBASE_AUTH, "", jsonString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    public static void show_popup_menu(ImageView imageView, Activity activity, boolean block, OnMenuItemClick onMenuItemClick) {
        //Toast.makeText(activity, "clicked "+block, Toast.LENGTH_SHORT).show();
        Context wrapper = new ContextThemeWrapper(activity, R.style.pop_up_menu_style);
        PopupMenu popupMenu = new PopupMenu(wrapper, imageView, Gravity.BOTTOM | Gravity.END);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.message_menu, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.getMenu().findItem(R.id.menu_block).setVisible(!block);
        popupMenu.getMenu().findItem(R.id.menu_un_block).setVisible(block);
        popupMenu.getMenu().findItem(R.id.menu_background).setVisible(false);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                onMenuItemClick.OnClick(menuItem.getItemId());
                return true;
            }
        });
    }

    public static void show_popup_menu_Group(ImageView imageView, Activity activity, OnMenuItemClick onMenuItemClick) {

        Context wrapper = new ContextThemeWrapper(activity, R.style.pop_up_menu_style);
        PopupMenu popupMenu = new PopupMenu(wrapper, imageView, Gravity.BOTTOM | Gravity.END);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.group_message_menu, popupMenu.getMenu());
        popupMenu.show();


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                onMenuItemClick.OnClick(menuItem.getItemId());
                return true;
            }
        });
    }


    public static void show_popup_menu1(View imageView, Activity activity, OnMenuItemClick onMenuItemClick) {
        //Toast.makeText(activity, "clicked "+block, Toast.LENGTH_SHORT).show();
        Context wrapper = new ContextThemeWrapper(activity, R.style.pop_up_menu_style);
        PopupMenu popupMenu = new PopupMenu(wrapper, imageView, Gravity.BOTTOM | Gravity.END);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.message_item_menu, popupMenu.getMenu());
        popupMenu.show();


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                onMenuItemClick.OnClick(menuItem.getItemId());
                return true;
            }
        });
    }

    public static void show_popup_menu2(View imageView, Activity activity, OnMenuItemClick onMenuItemClick) {
        //Toast.makeText(activity, "clicked "+block, Toast.LENGTH_SHORT).show();
        Context wrapper = new ContextThemeWrapper(activity, R.style.pop_up_menu_style);
        PopupMenu popupMenu = new PopupMenu(wrapper, imageView, Gravity.BOTTOM | Gravity.END);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.messagemenu2, popupMenu.getMenu());
        popupMenu.show();


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                onMenuItemClick.OnClick(menuItem.getItemId());
                return true;
            }
        });
    }

    public static void show_popup_menu3(View imageView, Activity activity, OnMenuItemClick onMenuItemClick, boolean is_deletable) {
        //Toast.makeText(activity, "clicked "+block, Toast.LENGTH_SHORT).show();
        Context wrapper = new ContextThemeWrapper(activity, R.style.pop_up_menu_style);
        PopupMenu popupMenu = new PopupMenu(wrapper, imageView, Gravity.BOTTOM | Gravity.END);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.messagemenu2, popupMenu.getMenu());
        popupMenu.getMenu().findItem(R.id.menu_delete).setVisible(is_deletable);
        popupMenu.getMenu().findItem(R.id.menu_report).setVisible(!is_deletable);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                onMenuItemClick.OnClick(menuItem.getItemId());
                return true;
            }
        });
    }


    public static void downloadVideo(String url, Activity activity, String name) {
        try {
            File direct = new File(Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_DOWNLOADS + "/", name);

            if (direct.exists()) {
                name = Calendar.getInstance().getTimeInMillis() + "_" + name;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setMimeType(getMimeType(uri.toString()));
            request.setTitle(name);
            request.setDescription("Downloading attachment..");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
            DownloadManager dm = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
            dm.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getUserName(UserVewModel userVewModel, String sender) {
        String name = "";

        if (PreferenceFile.getUser().getUser().getId().equalsIgnoreCase(sender)) {
            name = "You";
        } else {
            Users users = userVewModel.selectUser(sender);
            name = users.getName();
        }
        return name;
    }

    public static void chengeBackground(LinearLayout mainLay) {
        mainLay.setBackgroundColor(mainLay.getContext().getColor(R.color.seleted_bg));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mainLay.setBackgroundColor(mainLay.getContext().getColor(R.color.transparent));
            }
        }, 1200);


    }


    public static String getLocalIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {


                        ip += inetAddress.getHostAddress();


                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    public static String getLocalIpAddress1() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void getPublicIp(Application activity, Handler handler, AllInterFace allInterFace) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Main main = new Main(activity, handler, new OnError() {
                    @Override
                    public void OnEror(String url, String code, String message) {
                        if (allInterFace != null) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    allInterFace.IsClicked("");
                                }
                            });
                        }
                    }
                },
                        new OnSuccess() {
                            @Override
                            public void OnSucces(String url, String code, String res) {
                                Gson gson = new Gson();
                                GetIp getIp = gson.fromJson(res, GetIp.class);
                                try {
                                    if (getIp.getIps().size() > 0) {
                                        if (allInterFace != null) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    allInterFace.IsClicked(getIp.getIps().get(0));
                                                }
                                            });
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (allInterFace != null) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                allInterFace.IsClicked("");
                                            }
                                        });
                                    }
                                }
                            }
                        });

                main.CALLGetRequest(BASE_URL + GET_IP, "", new HashMap<>());

            }
        }).start();
    }

    public static String getRightAngleImage(String photoPath) {

        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree;

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }

            return rotateImage(degree, photoPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoPath;
    }

    public static String rotateImage(int degree, String imagePath) {

        if (degree <= 0) {
            return imagePath;
        }
        try {
            Bitmap b = BitmapFactory.decodeFile(imagePath);

            Matrix matrix = new Matrix();
            if (b.getWidth() > b.getHeight()) {
                matrix.setRotate(degree);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
            }

            FileOutputStream fOut = new FileOutputStream(imagePath);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

            FileOutputStream out = new FileOutputStream(imagePath);
            if (imageType.equalsIgnoreCase("png")) {
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else if (imageType.equalsIgnoreCase("jpeg") || imageType.equalsIgnoreCase("jpg")) {
                b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            fOut.flush();
            fOut.close();

            b.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    public static void CashImage(String s, String s1, Drawable d) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(CATCH_DIR + "/" + s1);

                if (file.exists()) {
                    file.deleteOnExit();
                }

                try {
                    file.createNewFile();
                   /* Bitmap bm = ((BitmapDrawable) d).getBitmap();
                    FileOutputStream outStream = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);

                    outStream.flush();
                    outStream.close();*/

                    new GetFile(file, s).execute();

                } catch (Exception e) {
                    e.printStackTrace();
                    file.delete();
                }

            }
        });
        th.run();
    }

    public static void CashImage2(String s, String s1) {

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(CATCH_DIR2 + "/" + s1);

                /*if (file.exists()){
                    file.deleteOnExit();
                }

                try {
                    file.createNewFile();
                    URL url = new URL(s);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    FileOutputStream out = new FileOutputStream(file);
                    copyStream(input, out);
                    out.close();

                } catch (Exception e){
                    e.printStackTrace();
                    file.delete();
                }*/

                new GetFile(file, s).execute();
            }
        });
        th.start();

    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
                /*out.flush();
                out.close();*/
        }
        out.flush();
        out.close();
    }

    public static void report(Activity context, AllInterFace allInterFace, String name) {

        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.report_lay, null);

        ReportLayBinding binding = ReportLayBinding.bind(dialogView.getRootView());

        binding.heading.setText("Want To Report " + name + " ?");
        binding.tvBlock.setText(" Check to Block " + name);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

        builder.setView(dialogView);
        builder.setCancelable(true);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        binding.btCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        binding.btCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        binding.btreport.setOnClickListener(view -> {
            allInterFace.isClicked(binding.accept.isChecked());
            dialog.dismiss();
        });

    }

    public static void reportGroup(Activity context, AllInterFace allInterFace, String name) {

        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.report_lay, null);

        ReportLayBinding binding = ReportLayBinding.bind(dialogView.getRootView());

        binding.heading.setText("Want To Report " + name + "Group ?");
        binding.tvBlock.setText(" Check to Exit from  " + name);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

        builder.setView(dialogView);
        builder.setCancelable(true);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        binding.btCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        binding.btCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        binding.btreport.setOnClickListener(view -> {
            allInterFace.isClicked(binding.accept.isChecked());
            dialog.dismiss();
        });

    }

    public static void showStyleToast(Context context, String message) {
        StyleableToast.makeText(context, message, R.style.mytoast).show();
    }

    public static class GetFile extends AsyncTask<Void, Void, Void> {

        File file;
        String ur;

        public GetFile(File file, String ur) {
            this.file = file;
            this.ur = ur;
        }


        @Override
        protected Void doInBackground(Void... voids) {


            if (file.exists()) {
                file.delete();
            }

            try {
                file.createNewFile();

                URL url = new URL(ur);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                FileOutputStream out = new FileOutputStream(file);
                copyStream(input, out);
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
                file.delete();
            }


            return null;
        }
    }

    public static void GetPermission(FragmentActivity activity, String[] storage_permission, int i) {
        activity.requestPermissions(storage_permission, 122);

        LottieAlertDialog lottieAlertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_WARNING)
                .setPositiveButtonColor(Color.parseColor("#048B3A"))
                .setNegativeButtonColor(Color.parseColor("#DA1606"))
                .setNoneButtonColor(Color.parseColor("#038DFC"))
                .setPositiveTextColor(Color.WHITE)
                .setNegativeTextColor(Color.WHITE)
                .setNoneTextColor(Color.WHITE)
                .setTitle("WARNING")
                .setDescription("We need the Storage Permission To Store AND Share Media. Click Allow To give permission\n" +
                        "Without the permission Attachment might not show properly")
                .setNoneText("OK")
                .setNoneListener(new ClickListener() {
                    @Override
                    public void onClick(@NonNull LottieAlertDialog lottieAlertDialog) {
                        // activity.onBackPressed();


                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                        activity.startActivity(intent);
                        lottieAlertDialog.dismiss();
                    }
                })
                .build();

        lottieAlertDialog.setCancelable(false);
        lottieAlertDialog.setCanceledOnTouchOutside(false);
        lottieAlertDialog.show();


    }


    public static void showKeyboard(Activity activity, EditText editText) {

        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                editText.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);

        editText.requestFocus();
    }

    public static void hideKeyboard(Activity activity, View view) {

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static void updateToken(String token, Application activity, Handler handler) {
        Data data = new Data("TOKEN", PreferenceFile.getUser().getUser().getId(), token);
        Sender se = new Sender(data, "/topics/EASY_ALL");
        MethodClass.SendNotificationOnTopic(se, activity, handler);
    }


    public static Bitmap rotateBitmapByDeg(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        switch (i) {
            case 2:
                matrix.setScale(-1.0f, 1.0f);
                break;
            case 3:
                matrix.setRotate(180.0f);
                break;
            case 4:
                matrix.setRotate(180.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 5:
                matrix.setRotate(90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 6:
                matrix.setRotate(90.0f);
                break;
            case 7:
                matrix.setRotate(-90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 8:
                matrix.setRotate(-90.0f);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            bitmap.recycle();
            return createBitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void cashattachmentImage(Bitmap bm, Handler handler, Activity context, AllInterFace allInterFace) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = new File(context.getFilesDir(), "EasySent/Cash").getAbsolutePath();
                Random random = new Random();
                File myDir = new File(path + "/" + random.nextInt(10000) + ".jpg");

                if (myDir.exists()) {
                    myDir.delete();
                }
                try {
                    myDir.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    FileOutputStream out = new FileOutputStream(myDir);
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            allInterFace.IsClicked(myDir.getAbsolutePath());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            allInterFace.IsClicked(null);
                        }
                    });
                }


            }
        }).start();

    }

    public static void cashattachmentImage2(Bitmap bm, String filename, Handler handler, Activity context, AllInterFace allInterFace) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = CATCH_DIR2;
                File myDir = new File(path + "/" + filename + ".jpg");

                if (myDir.exists()) {
                    myDir.delete();
                }
                try {
                    myDir.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    FileOutputStream out = new FileOutputStream(myDir);
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            allInterFace.IsClicked(myDir.getAbsolutePath());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            allInterFace.IsClicked(null);
                        }
                    });
                }


            }
        }).start();

    }

    public static void cashattachmentFILE2(File bm, String filename, Handler handler, Activity context, AllInterFace allInterFace) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = CATCH_DIR2;
                File file = new File(path + "/" + filename + "_" + bm.getName());

                if (file.exists()) {
                    file.delete();
                }
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (file.exists()) {


                    try {
                        Uri returnUri = Uri.fromFile(bm);
                        InputStream inputStream = context.getContentResolver().openInputStream(returnUri);
                        FileOutputStream out = new FileOutputStream(file);
                        copyStream(inputStream, out);
                        out.close();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (file.exists()) {

                                    allInterFace.IsClicked(file.getAbsolutePath());
                                } else {
                                    allInterFace.IsClicked(null);
                                }

                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                        allInterFace.IsClicked(null);
                    }


                }


            }
        }).start();

    }

    public static String changeDateFormat(String date_str) {
        String date_output = "";
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("dd/MMM/yyyy KK:mm a");
        output.setTimeZone(TimeZone.getDefault());
        try {
            Date oneWayTripDate = input.parse(date_str);
            date_output = output.format(oneWayTripDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date_output;
    }

    public static String changeDateFormat2(String date_str) {
        String date_output = "";
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        output.setTimeZone(TimeZone.getDefault());
        try {
            Date oneWayTripDate = input.parse(date_str);
            date_output = output.format(oneWayTripDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date_output;
    }

    public static String getTime(String dt) {
        if (dt == null || dt.equals("")) {
            return null;
        }
        String date = dt.replaceAll("-", "/");

        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date d = utcFormat.parse(date);

            DateFormat pstFormat = new SimpleDateFormat("KK:mm a");
            //DateFormat pstFormat = new SimpleDateFormat("hh:mm a");;
            pstFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
            assert d != null;
            String datess = pstFormat.format(d);


            return datess;


        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }


}
