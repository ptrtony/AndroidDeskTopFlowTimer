package com.example.timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends Activity implements ProxyTimeLogic.ActivityProvider {
    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ProxyTimeLogic(this, savedInstanceState);
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tabAlarm").setIndicator("ALARM").setContent(R.id.tabAlarm));
        tabHost.addTab(tabHost.newTabSpec("tabTimer").setIndicator("TIMER").setContent(R.id.tabTimer));

        if (!PermissionUtil.checkFloatPermission(this)){
            new AlertDialog.Builder(this)
                    .setTitle("温馨提示")
                    .setMessage("请到手机系统设置界面打开悬浮框相关权限")
                    .setNegativeButton("确定", (dialog, which) -> {
                        PermissionUtil.requestSettingCanDrawOverlays(MainActivity.this);
                        dialog.dismiss();
                    })
                    .setPositiveButton("取消", (dialog, which) -> dialog.dismiss())
                    .show();


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionUtil.REQUEST_DIALOG_PERMISSION && !PermissionUtil.checkFloatPermission(this)){
            Toast.makeText(this,"请你打开悬浮框相关权限",Toast.LENGTH_LONG).show();
        }
    }
}
