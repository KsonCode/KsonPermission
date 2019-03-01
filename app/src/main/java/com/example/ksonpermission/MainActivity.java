package com.example.ksonpermission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    private final int CALL_PHONE_CODE = 1;
    RxPermissions rxPermissions = new RxPermissions(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
    }

    /**
     * singletask，singletop默认的不到intent值
     */
    private void initData() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntent().getExtras().getString("userId");

    }

    /**
     * 拨打电话
     * @param view
     */
    public void call(View view) {

        rxPermissions.request(Manifest.permission.CALL_PHONE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean){
                    callPhone();
                }else{

                }
            }
        });


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){//设备的版本号（版本兼容适配，通过代码适配方式去适配）
            //检验是否拥有以下权限
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"没有授权",Toast.LENGTH_LONG).show();
                //未获得权限
                //请求权限，调用请求权限方法，会弹出系统授权窗口
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},CALL_PHONE_CODE);
            }else{
                //已授权
                Toast.makeText(this,"已授权",Toast.LENGTH_LONG).show();

                callPhone();
            }
        }else{//小于23
            callPhone();
        }
    }

    /**
     * 拨打电话逻辑
     */
    private void callPhone() {

        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "18612990000");
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 权限适配回调方法
     * @param requestCode 请求码
     * @param permissions 权限数组
     * @param grantResults 申请权限结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (CALL_PHONE_CODE==requestCode){//拨打电话
            if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED){//拒绝了
                //隐患，可能会崩溃
                //引导用户去开启（1.再申请一遍权限系统窗口 2.引导手动开启）

//                new AlertDialog.Builder(this).setTitle("权限").setMessage("如果没有权限问题")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                gotoPerssionSettinng();//跳转到权限设置页面，让用户手动开启
//                            }
//                        });

                Toast.makeText(this,"用户点击决绝",Toast.LENGTH_LONG).show();
                //是否点击不再询问的选择按钮
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){
                    gotoPerssionSettinng();
                }else{
                    //只点击拒绝，，不勾选不再询问，再次请求权限
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},CALL_PHONE_CODE);
                }


            }else{//通过，，直接拨打

                Toast.makeText(this,"用户点击允许",Toast.LENGTH_LONG).show();

                callPhone();

            }
        }
    }

    /**
     * 指引跳转设置页面
     */
    private void gotoPerssionSettinng() {
                        new AlertDialog.Builder(this).setTitle("权限").setMessage("如果没有权限问题")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gotoPerssionSettinng();//跳转到权限设置页面，让用户手动开启
                            }
                        }).show();

    }
}
