package com.company.project.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.company.project.util.FileUtils;

public class OpenFileReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String path = intent.getStringExtra("path");
        Intent dealIntent = FileUtils.getFileOpenIntent(path);
        context.startActivity(dealIntent);
    }
}
