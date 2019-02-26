package com.chenhy.tcp_tester;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;


public class MQTTService extends Service {

//    private ContextWrapper context;
//
//    public MQTTService(ContextWrapper context){
//        this.context=context;
//    }
              private Context context;
    public static final String TAG = MQTTService.class.getSimpleName();
public static String str1;
    private static MqttAndroidClient client;
    private  MqttClient mqttClient;
    private MqttConnectOptions conOpt;


    private String host = "tcp://www2.boohersmart.com:1883";
    private String userName = "admin";
    private String passWord = "password";
   // private static String []myTopic = new String[]{"home/garden/fountain/1","home/garden/fountain/2"};
    private String [] myTopic={"Android_000123987","Android_ceshi","Android_chenggong"};
    private String clientId = "Android_000123";
    public  static String broadcast;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return super.onStartCommand(intent, flags, startId);
    }

    public static void publish(String msg){
     //   String topic = myTopic;
        Integer qos = 0;
        Boolean retained = false;
       // try {
         //   client.publish(String.valueOf(topic), msg.getBytes(), qos.intValue(), retained.booleanValue());
   //     } catch (MqttException e) {
       //     e.printStackTrace();
      //  }
    }

    private void init() {
        // 服务器地址（协议+地址+端口号）
        String uri = host;
        client = new MqttAndroidClient(this, uri, clientId);
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);

        conOpt = new MqttConnectOptions();
        // 清除缓存
        conOpt.setCleanSession(true);
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(20);
        // 用户名
        conOpt.setUserName(userName);
        // 密码
        conOpt.setPassword(passWord.toCharArray());

        // last will message
        boolean doConnect = true;
        String message = "{\"terminal_uid\":\"" + clientId + "\"}";
        String[] topic = myTopic;
        Integer qos = 0;
        Boolean retained = false;
        if ((!message.equals("")) || (!topic.equals(""))) {
            // 最后的遗嘱
            try {
                for (int i=0;i<myTopic.length;i++) {


                    conOpt.setWill(topic[i], message.getBytes(), qos.intValue(), retained.booleanValue());
                }
            } catch (Exception e) {
                Log.i(TAG, "Exception Occured", e);
                doConnect = false;
                iMqttActionListener.onFailure(null, e);
            }
        }

        if (doConnect) {
            doClientConnection();
        }

    }

    @Override
    public void onDestroy() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /** 连接MQTT服务器 */
    private void doClientConnection() {
        if (!client.isConnected() && isConnectIsNomarl()) {
            try {
                client.connect(conOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            try {

                // 订阅myTopic话题
               for (int i=0;i<myTopic.length;i++)
               client.subscribe(new String[]{myTopic[i]}, new int[]{1});

              //  Log.e("mytopic", Arrays.toString(myTopic));
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            // 连接失败，重连
        }
    };

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {
//private ContextWrapper context;
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

           str1 = new String(message.getPayload());
            MQTTMessage msg = new MQTTMessage();
            msg.setMessage(str1);
            EventBus.getDefault().post(msg);
            String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
            Log.i(TAG, "messageArrived:" + str1);
            Log.i(TAG, str2);
//







            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(MQTTService.this)
                    .setContentTitle("这是标题")
                    .setContentText("内容;"+message)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //  .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    //        .setContentIntent(pi)
                    .setDefaults(Notification.DEFAULT_ALL)
                    //   .setStyle(new NotificationCompat.BigTextStyle().bigText(""+message))
                    .build();
            notificationManager.notify(1, notification);


            zz();
        }
        class MQTTMessage {
    private String message;
private Context context;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}


        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
        }
    };

    /** 判断网络是否连接 */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT当前网络名称：" + name);

            return true;
        } else {
            Log.i(TAG, "MQTT 没有可用网络");
            return false;
        }
    }

    @Nullable
    @Override
    public  IBinder onBind(Intent intent) {

        return  new MyBinder();
    }

    public   class MyBinder extends Binder {

        public void callBanZheng(int money){
            //调用办证的方法
            banZheng(money);



        }


    }

    public   void banZheng(int money){
        if (money>1000) {
            Toast.makeText(getApplicationContext(), "我是领导 把证给你办了", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "这点钱 还想办事....", Toast.LENGTH_SHORT).show();
        }
    }

    public  void  zz(){

        String on="on";
        if (str1.equals(on) ){

              broadcast="case1";

        }else {
            broadcast="case2";

        }


        Intent intent=new Intent("android.net");
       // intent.putExtra("zhi1","ceshiyuju");

        sendBroadcast(intent);
    }






}
