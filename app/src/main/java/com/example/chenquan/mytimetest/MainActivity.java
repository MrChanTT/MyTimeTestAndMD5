package com.example.chenquan.mytimetest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity Log";
    private static final int GETTIME = 0x100001;
    private Myhandler myhandler;
    private MyThread myThread;
    private Timer timer;
    private TextView tv;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        myhandler = new Myhandler();
        myThread = new MyThread();
        new Thread(myThread).start();
//        new Timer().schedule(timerTask,0,1000);


    }
    public class Myhandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GETTIME:
                    try {
                        byte[] result = eccrypt(getTime());
                        byte[] ss = getTime().getBytes();
                        tv.setText("" + getTime() + ",密文：" + new String(result)+",明文："+new String(ss));
                          tv.setText("" + getTime());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    break;
                default:
                    break;
            }

        }
    }
    public class MyThread implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(1000);
                    Message message = new Message();
                    message.what = GETTIME;
                    myhandler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
    private void initView(){
        tv = (TextView) findViewById(R.id.tv);
    }
    private String getTime(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        time = sf.format(date);
        return time;
    }
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = GETTIME;
            myhandler.sendMessage(message);
        }
    };
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = GETTIME;
            myhandler.sendMessage(message);
        }
    };
    public byte[] eccrypt(String string) throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] srcbyte = string.getBytes();
        md.update(srcbyte);
        byte[] newbyte = md.digest();
        return newbyte;
    }
}
