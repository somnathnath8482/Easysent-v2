package easysent.in.Firebase;

import static easysent.in.Helper.SyncData.SyncChat;
import static easysent.in.Helper.SyncData.SyncChatOFThread;
import static easysent.in.Helper.SyncData.SyncThread;
import static easysent.in.Helper.SyncData.SyncUser;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.ExecutionException;

import easysent.in.Encription.Encripter;
import easysent.in.Helper.Constants;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Room.GroupChat.Groups_chat_ViewModel;
import easysent.in.Room.Groups.Groups_ViewModel;
import easysent.in.Room.Messages.Message_View_Model;
import easysent.in.Room.Users.UserVewModel;
import easysent.in.Room.Users.Users;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class FirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.e("TAG", "showNotification: "+message.getData() );
        String type = message.getData().get("type");
        String thread = message.getData().get("thread");
        thread = thread==null?"":thread;
        Handler handler  =new Handler(Looper.getMainLooper());
        UserVewModel userVewModel = new UserVewModel(getApplication());
        Groups_ViewModel groups_viewModel = new Groups_ViewModel(getApplication());
        Message_View_Model message_view_model = new Message_View_Model(getApplication());
        Groups_chat_ViewModel groups_chat_viewModel = new Groups_chat_ViewModel(getApplication());
        type = type==null?"":type;
        //show notification
        if (!Constants.ACTIVE.equals(thread) && (type.equalsIgnoreCase("M") || type.equalsIgnoreCase("FM"))) {
            ShowNotification showNotification = new ShowNotification();
            if (PreferenceFile.isLogged()) {
                Users sender = userVewModel.selectUser(message.getData().get("sender"));
                Users reciver = userVewModel.selectUser(PreferenceFile.getUser().getUser().getId());
                if (sender != null && reciver != null) {
                    if (!Constants.ACTIVE.equalsIgnoreCase(sender.getId())) {
                        FutureTarget<Bitmap> futureTarget = Glide.with(this)
                                .asBitmap()
                                .load(Constants.BASE_URL + "profile_image/" + sender.getProfilePic())
                                .submit();
                        Bitmap bitmap = null;
                        try {
                            bitmap = futureTarget.get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Encripter encripter = new Encripter(message.getData().get("sender"));

                        showNotification.showNotification(this, Integer.parseInt(message.getData().get("sender")),
                                encripter.decrepit(message.getData().get("message")), sender.getName(), bitmap);
                    }





                }else{

                    SyncUser(getApplication(), new Handler(Looper.getMainLooper()), PreferenceFile.getUser().getUser().getEmail());
                }
            }
        }

        if (type.equalsIgnoreCase("FM")){
            SyncThread(getApplication(), handler);
            SyncChat(getApplication(), handler);
        }else if (type.equalsIgnoreCase("M")){
            SyncChatOFThread(thread,getApplication(),handler);
        }
    }
}
