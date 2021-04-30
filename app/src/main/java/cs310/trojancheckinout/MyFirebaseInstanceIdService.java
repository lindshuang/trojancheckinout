package cs310.trojancheckinout;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

//public class MyFirebaseInstanceIdService extends Service {
//    public MyFirebaseInstanceIdService() {
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//}

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "userABC";

    //    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MyActivity.this,  new OnSuccessListener<InstanceIdResult>() {
//        @Override
//        public void onSuccess(InstanceIdResult instanceIdResult) {
//            String newToken = instanceIdResult.getToken();
//            Log.e("newToken",newToken);
//
//        }
//    });

    @Override
    public void onNewToken(String token) {
        //Log.d(TAG, "Refreshed token: " + token);
        // Once the token is generated, subscribe to topic with the userId
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        Log.d(TAG, "onTokenRefresh completed with token: " + token);
        //sendRegistrationToServer(token);
    }
}