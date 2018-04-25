package com.example.hansangjin.froot.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.example.hansangjin.froot.Activities.CategoryActivity;
import com.example.hansangjin.froot.R;

/**
 * Created by sjhan on 2018-04-25.
 */

public class AgreementDialog extends Dialog implements View.OnClickListener {
    private SharedPreferences sp;
    private Button button_agree;
    CategoryActivity activity;

    public AgreementDialog(@NonNull Context context) {
        super(context);
    }

    public AgreementDialog(@NonNull CategoryActivity activity, SharedPreferences sp) {
        super(activity);
        this.activity = activity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_agreement);

        button_agree = findViewById(R.id.button_agree);

        button_agree.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == button_agree){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("agreement", true);
            dismiss();
            activity.goToNextActivity();
        }
    }
}
