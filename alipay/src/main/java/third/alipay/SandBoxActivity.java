package third.alipay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;

import java.util.HashMap;
import java.util.Map;

import static third.alipay.MainActivity.RSA_PRIVATE;

/**
 * Created by Administrator on 2019/7/13.
 */
public class SandBoxActivity extends AppCompatActivity {


    Handler handler;
    public static final String APPID = "2016101000655055";
    public static final String PID = "2088102178971635";
    public static final String TARGET_ID = "kkkkk091125";

    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDVv1c5DRvhyAvgevh4uaMMpww5jQT3X+eWehYsBSRNZXSv990pgGXdU0yRnJL6I8u0fODx8wtHBL5EUntBjddwCfTB2IByhcskY79CskxErPqgYmPAq7VEtq4KC1GFfIgVmx2wPcFpqab7tc5w2khUG63QWjeBj7+GefnQmFSTKhGMRzjuLU+Sy4U+gNA1vVYXR3miAPdT1DXgn1QGvo2R3UWiLZY5qN6lE5c1nz1zur8DhkLpw7u7A5iobfbkK9/1uYq/aFAnm57N92HsIaQmE1q7COofP74V0HhQ2xb5SU4CMIfJ8K2HWYuAKTMapDTBwcmJw348e3ruh7VcO7hfAgMBAAECggEBAJ2VtLuWA5FhCAiXAKsYybHhUmz3n8q1RSs2lTQdEleRTgcR6VbYl8El0ZSk5+NNErjdPIaEljLkt1XtrXt2FHhAjtd/Q6urIkqQ04hbpUkYcqpd/4cj0L95IzS6hX6xgi7Trn6p+PdqoXZ/4lRnSd3gjjPG35dAoIaHVPtJ40ie6Q1YgRG5qnjvOBtLH6OrVmtcZaJ7r0JsI9WLiTTCUwiQ0BEZvm8QeRRRYiSb1lih1pr9OzGmGb0nKQQyKmRgaz2JfHi9XOrsr5VRYhJOmDkn/c2Ry69Kx/HQFVOmlhKLdHoajVmbKqpZHfnDxsO0yAxZ7aZHRJad04KsR4XlHXkCgYEA9LXc+fjgnRSFjXTAnBuDBOTCvUgG8IedTHupReChG3FKt3z3aIMAFQo0EvVMDwJ72yB010fmuthC4lz8Ld+gtRadGp+BKASa+8MNkxMXlFZhhP4Ec7gt8mNs1kPQjWyRh8y5gho9gYMnmU7N8ZBTMMmqH9IRqVu45GB9MZa5tAsCgYEA35vKjTwU4M7k/1iafYMXvAN1qBzIz8Yb00EYPy2dWqwL/sXOLgxfAIX9aadUgZjsiWswSaDOr27YxrkVBMvLSORlHYRYMYTA8rgVokIz24oH6tP85SdFC5jUcHResT0mXc6upwPEFTLxiwIaFAvSKfUgH9kqKw+48Gw0/DZmzX0CgYEAqeay3CO935HmAAa1zC0V1In3429k0g92WSnqpweFFAaet7LeHAQIRJNnAFqrSiiRUdzBAs97FPMdzQh+VmNTsydWQKvKArzf1jjg7eJtlqI65xlugeG4lPgPEtzWqbpdeIndqsUJOyiSj9C1ECkCeXcq1RkHBi0WvAl6IrnhiW0CgYAc2RWFqRWsdyS2CLFNtgbu26dnO+dwXseiNoixRepCE2YsxUo3SKNNBvxNkCfn3FnP1MNTDGr92RggcypSBxS/369n4nYaVV0rMzKfT1kvXpxs4FKFIc7Xkyz9IRZCWXhEq/B+XY8DiSH+ZBQHOAsyHIy7byHwkkOyyNMBIueaZQKBgHdVYbJDiPgAMQwNG9g+A2bOia3KfdMpcmRXMTfb2+eDeqadDwoTYIWZUK2l021e6LksEbai1sSsrSpuxguZE6xHEDVQU/IjafO+B9jpmPL+NdUtLPUzd3euT/oB3ULah7UZSuAtJafbc7pdY7snA93sHz3PNw4yu8Vzt7bNFPbG";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        System.out.println("*********  " + getClass().getSimpleName() + ".onCreate  *********");
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    520);
        }

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                System.out.println("~~handleMessage~~");

                System.out.println("msg.arg1 is " + msg.arg1);
                System.out.println("msg.arg2 is " + msg.arg2);
                System.out.println("msg.what is " + msg.what);
                Map<String,String> map = (Map<String, String>) msg.obj;

                System.out.println(map);


                return true;
            }
        });


        //成功结果
//        {
//            "alipay_trade_app_pay_response": {
//            "code": "10000",
//                    "msg": "Success",
//                    "app_id": "2016101000655055",
//                    "auth_app_id": "2016101000655055",
//                    "charset": "utf-8",
//                    "timestamp": "2019-07-16 17:08:25",
//                    "out_trade_no": "071617072842319",
//                    "total_amount": "0.01",
//                    "trade_no": "2019071622001435711000035050",
//                    "seller_id": "2088102178971635"},
//            "sign": "oAprqsV+qI67HoLxQTPU7XJpjs/WBx6RVKZBNPO+GByaiOdKAY1ZL64JYANet4bYDBElqWL4FsxpJTOoZEnUGqp3SUm3aRx6PstJqAEgqazPYQXa45qyImM3VL044HntBxTtKOn/JrA/CRSYH9oI23ZP2UNZ2nq/rB6QVeH9tw1z7gTM6Bojvey/glGrMStChSLS33KPN0SEcZJTjaAbexE5022lmJ+3jvzd+n54D/zgPAonSbhKJrc8Dxz6UeSoTeHo5ay7MKBf+GdMVxpJei7ez+9FfM1PN08wypF5uTV3Up7y8iEr+7q4L8Vd3J00nBwR5O+AtNmqNraN1inE1Q==",
//            "sign_type": "RSA2"
//        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("~~onRequestPermissionsResult~~");

        System.out.println("requestCode is " + requestCode);


        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("permissions =" + "PERMISSION_GRANTED");
            } else {
                System.out.println("permissions =" + "PERMISSION_DENIED");
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("*********  " + getClass().getSimpleName() + ".onStart  *********");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        System.out.println("*********  " + getClass().getSimpleName() + ".onRestoreInstanceState  *********");

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("*********  " + getClass().getSimpleName() + ".onRestart  *********");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("*********  " + getClass().getSimpleName() + ".onResume  *********");
    }

    @Override
    protected void onPause() {
        System.out.println("*********  " + getClass().getSimpleName() + ".onPause  *********");

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        System.out.println("*********  " + getClass().getSimpleName() + ".onBackPressed  *********");

        super.onBackPressed();
    }


    @Override
    protected void onStop() {
        System.out.println("*********  " + getClass().getSimpleName() + ".onStop  *********");

        super.onStop();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("*********  " + getClass().getSimpleName() + ".onSaveInstanceState  *********");

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        System.out.println("*********  " + getClass().getSimpleName() + ".onDestroy  *********");

        super.onDestroy();
    }


    public void start(View view) {
        System.out.println("~~button.start~~");


        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = buildOrderParamMap();



        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        System.out.println("orderParam is " + orderParam);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        System.out.println("sign is " + sign);


        final String orderInfo = orderParam + "&" + sign;


        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(SandBoxActivity.this);
                Map<String,String> result = alipay.payV2(orderInfo,true);//异步操作

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };



        // 必须异步调用
        new Thread(payRunnable).start();



    }


    public void stop(View view) {
        System.out.println("~~button.stop~~");

    }

    public void bind(View view) {
        System.out.println("~~button.bind~~");

        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
        System.out.println("info is "+ info);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        System.out.println("sign is "+ sign);

        final String authInfo = info + "&" + sign;

        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(SandBoxActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();

    }

    public void unbind(View view) {
        System.out.println("~~button.unbind~~");

    }

    public void reloading(View view) {
        System.out.println("~~button.reloading~~");

    }


    public void del(View view) {
        System.out.println("~~button.del~~");

    }

    public void query(View view) {
        System.out.println("~~button.query~~");

        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        System.out.println("version is " + version);
    }




    public static Map<String, String> buildOrderParamMap() {
        Map<String, String> keyValues = new HashMap<String, String>();

        //公有参数
        keyValues.put("app_id", APPID);
        keyValues.put("charset", "utf-8");
        keyValues.put("method", "alipay.trade.app.pay");
        keyValues.put("sign_type", "RSA2");
        keyValues.put("timestamp", "2016-07-29 16:55:53");
        keyValues.put("version", "1.0");

        //自定义参数
        keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"0.01\",\"subject\":\"1\",\"body\":\"我是测试数据\",\"out_trade_no\":\"9527\"}");

        return keyValues;
    }




}
