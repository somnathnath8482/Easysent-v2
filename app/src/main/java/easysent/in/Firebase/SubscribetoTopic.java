package easysent.in.Firebase;

import android.app.Application;
import android.content.Context;



import java.util.List;


public class SubscribetoTopic extends Application {
    Context context;
    public static String TAG = "aomnathnath";
    public static  String TOPIC_NEW_POST  = "new_post";
    public static  String TOPIC_NEW_COMMENT  = "new_comment";

    public SubscribetoTopic() {
    }

        public void  Subscribetotopic(String topic, List<String> tokens) {
       // FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }



    public void sendNoti(String topic){


    }




}
