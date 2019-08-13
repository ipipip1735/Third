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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static third.alipay.MainActivity.RSA_PRIVATE;

/**
 * Created by Administrator on 2019/7/13.
 */
public class SandBoxActivity extends AppCompatActivity {


    Handler handler;
    public static final String APPID = "2016101000655055";
    public static final String PID = "2088102178971635";
    public static final String TARGET_ID = "kkkkk091125";

    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCxN1yZ6J8nu0nkuenXzRLqhSwAIQFL3wSuhziUs0H848yXC+dJDASOp0psVCagCFZE2deVhgROrYCbk2H4/iTqaBcyDJWgqpNgH54rc6WNxP09lhF/p2JtNqnZm/9offKq7WFwFO05rH8HAtsLa0HFZYcEoVFYu0rPl+5FGtJn+MaCOkG7x8G05Z9px3OsPfxmhPx0YnXba3QsRHoiuemHhD8k12AdPpoG/zV7XjtsAIPdvtJRJSK1IZABIXHn+2YqHsxdmbbJkm4z0m1IrbRDtX3vEpo5dn/WauZLkndy6Xybxdv5Pu3JrJMYdr87zJRsB+6TmXPjvUyUipykmVrXAgMBAAECggEAQCzxPtcr06KSVt8wnLKqF/2T+pP5OTWRJ3bWeZsU2XTRIR3xatWMTPCuFd6/ghKi0xokZQR8SNWyDiToRNgcHDsHs1s1UFKVPikVCV0+5cEhiAzxV04RacVy6tgAPSHnIBkIwSMC6XDbK3nQQylbTxW+OGIG6GAi7lxxr9rPuVCWbiQADeEKJ9fXHU1SXcoQ++JRxSGDoP2nye3QgoHr0PTvewQGR4yggpF01XcneiHxCw9nmxA7WSbWVfFXlsiLADjDwkf9oZvo9qTu7sRiL4kdE12kIl4cc1VRTS/9cBxUrfXtFHCu8t2iyqf4jGJyuuC+DJrZQHOiVxExgD8B+QKBgQDgo42qXRke3mt73dGsJalNLxpv0/5XDTXs0G0mLBXhZgsIic5B8I/DnY57D9KDQahTJwpmjg/D5JzmQpV9eb8e9j8IM0BQQMZd0CY8TQkIgBKB0M4FuqsMNcb7+qXYXXwuobvf0trJIFcaj6RknmWBJyEOFqdL3ef5kp6YiRRkLQKBgQDJ9PRtq3424BPI3JXxhFqk0ZN0wvWUxhrxJel87X/yZZ2Uy5o/0qBGYBl1sDP7M5LIFT69849+Gj47TU/Nd8I38Sk8BsGDryuVpxRiY5+RwifZMlWKxsK/sLcFvbywg4zxtFQ6qmEhKSDFV9pP9CuuwWWAnMdKqpRN8OUS939JkwKBgHvAnmKkNxqSXZQ3dzLm7IXg1SeWGh/K31I+4GKPFt69YIarpD0fUZPqUHvrE4XLvfdRIqGs0XKRlv4i4EfnsipUbhUOZvfPN3inGulNZxSPuaJabaUqWOC5H43hX0v69FacMuvzNSRn9JRlXaMwv6qO697fC/r3nLwY0dYmbl1hAoGBALvMCtdIl29T41HuvYf+uYN2VxZGjLMxnLANvxcqisXO9D//LIqYw+1tQ3+KwGuhQ6bHCrb8G7z5jlD6zXCVIod+vAdTiPN5GqBo462yUhnqX7+67IzF0ycJnse57hJ94byJIaID+ZoqcozP6vRaa5xvvoFgSHoIMSxhC8MdXsH9AoGAMH5ty8kpvYNLL+CLa+zBFVc/kmUGxMFcIv44svJHW3IqQNdutLHHC7Io8G/rImk8EFunkrDwYTPahZupt04uXYs3d8P3f2xGdERho6b6eb3vkyZvyFQDefhGXzdt2NVf0TQ1g65ACYwHAaBqGCQ0aGcy6wRXOOUxa4dhfTz12h4=";
    public static final String ALIPAY_RSA2_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu0fFgKWgPf1ku6ii8zmYGxwP3d2cYUBU4yBQ9IqARAqx1ObLJ2FJRLNdLJm8B33dHw228c2f67+i/Hxg4tj6GG4PATZ8YLmyJvXAfqkEVIM2RpREz+GPr4FMm9CrX0kGQCt2/MCo8GFxA3XxZGKVmKmR18pX6TH1aFVXkyMGIlxbGudvR02bcJMhJ9s+OHIlytZUavTttpvZJjZ+wTp+u4viNaFpVtbo8A6Gm72izp0AHUy729eFmN51rdNcbxUUGqs7essSyDuT2nkydKypANdTr/vPvFFAyPHp2Rrm0540IAHykyHafXeoZHly/hxGArZ1cfHgvTB1e9JXbsxeVwIDAQAB";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("*********  " + getClass().getSimpleName() + ".onCreate  *********");
        setContentView(R.layout.activity_main);


        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

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
                Map<String, String> map = (Map<String, String>) msg.obj;

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
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);

        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        System.out.println("orderParam is " + orderParam);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        System.out.println("sign is " + sign);


        final String orderInfo = orderParam + "&" + sign;
        System.out.println(orderInfo);

//        final String orderInfo1 = "alipay_sdk=alipay-sdk-java-3.7.110.ALL&app_id=2016101000655055&biz_content=%7B%22body%22%3A%22my+body%22%2C%22out_trade_no%22%3A%221565595816574%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22xxx%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpc.yssp888.com%2Ftest.php&sign=RKIxV%2Bbuq9Kkl1o3g%2B0tlar16wimkOXboCMhR8tgFJJ8%2Fyk%2BLJ4QdP8ZLgFPi2qf0Di70%2FyNPxRJt0i2VpLPUBIMApk0aNDbjtPTFjARngNcx6Evn%2FyuMDIfSAqsDsvomQIc6cykVPaNkxHMNP2IoZHyI2No%2F%2BAwOWPdk18bEkLvDbgKgLmx0aVWaWHIcBzCY0RqD6dH9SuNBz9Mm6hvNf38grM%2BlGF%2FuQsKahtrH4PB2AnDWlKBkez6bW0EghKFiaCuUs8rA5qkJDdSrn9%2Fg1G3pffYAIALrE6FYirhBLGV9ZXW50qppGUInTJSc7TtEl%2B8aMJ7X2fec0KWCySjjQ%3D%3D&sign_type=RSA2&timestamp=2019-08-12+15%3A43%3A36&version=1.0";
//        final String orderInfo1 = "&lt;form id='alipaysubmit' name='alipaysubmit' action='https://openapi.alipaydev.com/gateway.do?charset=UTF-8' method='POST'&gt;&lt;input type='hidden' name='biz_content' value='{&quot;out_trade_no&quot;:1565659920,&quot;subject&quot;:&quot;XXXX&quot;,&quot;total_amount&quot;:10.1}'/&gt;&lt;input type='hidden' name='app_id' value='2016101000655055'/&gt;&lt;input type='hidden' name='version' value='1.0'/&gt;&lt;input type='hidden' name='format' value='json'/&gt;&lt;input type='hidden' name='sign_type' value='RSA2'/&gt;&lt;input type='hidden' name='method' value='alipay.trade.app.pay'/&gt;&lt;input type='hidden' name='timestamp' value='2019-08-13 01:32:00'/&gt;&lt;input type='hidden' name='alipay_sdk' value='alipay-sdk-php-20180705'/&gt;&lt;input type='hidden' name='notify_url' value='http://pc.yssp888.com/test.php'/&gt;&lt;input type='hidden' name='charset' value='UTF-8'/&gt;&lt;input type='hidden' name='sign' value='IFP9relApNxcDh/brh6ObHU3Aoh5z6vRIQj4MD3S4NE7sg1nyWy+e4tFuZr9B9oLXHJFGKw5OQM7+S5C2xwSsIcFbQu58qDzIDf2DUpbQxPH5iJL8uX/cM8uKOd4w6JByIG3mMXvhAqiU1eKVM4ULAt1VnVNNcC8+YjL9Lb1kqnnxeE096sKag4dyomFVV8C1/OfNUQVt10NO1ZxTU52zkhhrWhfiqcFqumu2w4ArJlAhyB11dtHaJ3WIGYHkqcChxa1ao5ubKnMm9U0qc3sIATN9jwP9CXLbX7lFgcId5ybciJ96KbOQc5BOYKHFgOjbyNN3qRbyD1AJKM7FsqQqA=='/&gt;&lt;input type='submit' value='ok' style='display:none;'&gt;&lt;/form&gt;&lt;script&gt;document.forms['alipaysubmit'].submit();&lt;/script&gt;";
        final String orderInfo1 = "alipay_sdk=alipay-sdk-java-3.7.110.ALL&app_id=2016101000655055&biz_content=%7B%22body%22%3A%22my+body%22%2C%22out_trade_no%22%3A%221565660319388%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22xxx%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpc.yssp888.com%2Ftest.php&sign=jlf61rMJ6eQ3p7LyCAHBwSZIPisAqLyZEcBnwSRAvDlEB48mSoqGsBCu1mutJjt%2BN41RIHpAjArNEfHZdYx1lICTs6G5v0Meqh0irUCYPmFK7BbWsetjsnNZHtntqh7f6xKL%2BjB36brJodUDa%2BQiP2JeCVPMVDjQ9880W%2FdxHMZoQK7N9BOm1w8NRNm97U6eYWCMqkpPcdt%2BWU3303uCbqyI4cwjqBM5lpjy%2FCY%2BdnHN9ZxckuPCZwehutRG5UnFLliT7caRf%2F6vtDAVf%2FNJT5okZSQtdqZ5js2n104gS808XBfzrHaTZ%2FgeMLa2bczZp%2FjIia8OiJpd3zxIMJUA1g%3D%3D&sign_type=RSA2&timestamp=2019-08-13+09%3A38%3A39&version=1.0";
        System.out.println(orderInfo1);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(SandBoxActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo1, true);//异步操作

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

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pubPKCS8 = new X509EncodedKeySpec(Base64.decode(ALIPAY_RSA2_PUBLIC));
            PublicKey pubKey = keyFactory.generatePublic(pubPKCS8);
            System.out.println(pubKey.toString());



            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(pubKey);
            String data = "{\"code\":\"10000\",\"msg\":\"Success\",\"app_id\":\"2016101000655055\",\"auth_app_id\":\"2016101000655055\",\"charset\":\"utf-8\",\"timestamp\":\"2019-08-07 04:22:46\",\"out_trade_no\":\"080704221820463\",\"total_amount\":\"0.01\",\"trade_no\":\"2019080722001435711000047941\",\"seller_id\":\"2088102178971635\"}";

            signature.update(data.getBytes());

            String sign = "jCu4D8AMkh3JUCOulG74CsbA0/jvyIyYZzOjwmVxPIKEMmuczCKMEACV4ujSTextCdh1EYCGZqednGrqyoAYVy8+2/oUvxUvS1TxepwkqusNTA5IoEEAlEWexgvqg5ZwLBV+igBYL9idDW8w16foOM3f4v8Y3IOmk/ebGlQ4Uv2MhiEDETWRkuVj7FIZ60KTETCeRiq4EfeInYy9ce4h2Vi9/grtMJbo4EJbkhMK6M89BZxeW9pT4+kbZQJkVKUm5BF0wsLdz0xZTP7cFev+InsH/A9yqY/zK8X7KGt0XFt0RhxbsVwQ+TQtKNACHI2edfg/wzjDmUplUY6pTpJHKg==";
            boolean verifies = signature.verify(Base64.decode(sign));
            System.out.println("signature verifies: " + verifies);



        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }


    }

    public void bind(View view) {
        System.out.println("~~button.bind~~");

        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
        System.out.println("info is " + info);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        System.out.println("sign is " + sign);

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


}
