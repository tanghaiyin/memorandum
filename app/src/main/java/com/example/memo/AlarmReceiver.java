package com.example.memo;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

public class AlarmReceiver extends BroadcastReceiver {
    private String TAG=this.getClass().getSimpleName();
    public static final String BC_ACTION="com.ex.action.BC_ACTION";
    private AlertDialog.Builder builder;
    CountDownTimer timer;
    @Override
    public void onReceive(Context context,Intent intent) {
        String msg=intent.getStringExtra("msg");
        Log.i(TAG,"get Receiver msg :"+msg);
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
        showConfirmDialog(context);
    }
    private void showConfirmDialog(Context context){
        notification(context);

//        builder=new AlertDialog.Builder(context);
//        builder.setTitle("提示")
//                .setMessage("将在30秒后关机")
//                .setCancelable(false)
//                .setPositiveButton("取消",
//                        new  DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(timer!=null)timer.cancel();
//            }
//        });
//        setShowDialogType(context,builder.create());
    }
    private void notification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);//successTest为你要跳转的页面所属类
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //8.0 以后需要加上channelId 才能正常显示NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        } else {
            Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent2.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent2);
        }
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //设置TaskStackBuilder
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);//successTest为你要跳转的页面所属类
            stackBuilder.addNextIntent(intent);
            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            int notifyId = (int) System.currentTimeMillis();
            pendingIntent = PendingIntent.getActivity(context, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Notification notification = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("标题")
                .setContentText("这是内容，点击我可以跳转")
                .setAutoCancel(true)//点击后自动取消通知
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_bwl)))//设置显示大图片
                .setDefaults(Notification.DEFAULT_ALL)
                //设置重要程度： PRIORITY_DEFAULT （表示默认）PRIORITY_MIN(表示最低) PRIORITY_LOW （较低）PRIORITY_HIGH （较高）PRIORITY_MAX（最高）
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();
//        notification.flags = Notification.FLAG_ONGOING_EVENT;//软件进程未被杀死则无法删除通知
        manager.notify(1, notification);
    }

    private void setShowDialogType(Context context,AlertDialog alertDialog){
        int type;
        if(Build.VERSION.SDK_INT>24){
            type= WindowManager.LayoutParams.TYPE_PHONE;
        }else if(Build.VERSION.SDK_INT>18){
            type= WindowManager.LayoutParams.TYPE_TOAST;
        }else{
            type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        alertDialog.getWindow().setType(type);
        alertDialog.show();
//开启倒计时，并设置倒计时时间（秒）
        startCountDownTimer(context,alertDialog,30);
    }
    private void startCountDownTimer(final Context context,final AlertDialog alertDialog,int time){
        timer=new CountDownTimer(time*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//倒计时提示文字
                Log.i(TAG,"onTick time :"+millisUntilFinished);
                alertDialog.setMessage("将在"+(millisUntilFinished/1000)+"关机");
            }
            @Override
            public void onFinish() {
//倒计时结束
                Log.i(TAG,"倒计时结束！");
                alertDialog.dismiss();
//倒计时结束执行定时的任务
// shutdown(context);
            }
        };
        timer.start();
    }
    //shoutDown需要 系统权限才能执行否则会提示权限异常
    public void shutDown(Context context) {
        String action ="android.intent.action.ACTION_REQUEST_SHUTDOWN";
        String extraName ="android.intent.extra.USER_REQUESTED_SHUTDOWN";
        Intent intent =new Intent(action);
        intent.putExtra(extraName, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
