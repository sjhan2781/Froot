package com.example.hansangjin.froot.CustomView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.example.hansangjin.froot.R;

/**
 * Created by sjhan on 2018-07-20.
 */

public class NoDataDialog extends Dialog implements View.OnClickListener{
    Activity activity;
    private Button button_ok;

    public NoDataDialog(@NonNull Context context) {
        super(context);
    }

    public NoDataDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_no_data);

        button_ok = findViewById(R.id.button_ok);

        button_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == button_ok){
            backToPreviousActivity();
        }
    }

    @Override
    public void onBackPressed() {
        backToPreviousActivity();
    }

    public void backToPreviousActivity(){
        dismiss();
        activity.finish();
    }
}
