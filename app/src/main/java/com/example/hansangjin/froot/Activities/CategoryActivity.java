package com.example.hansangjin.froot.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansangjin.froot.Adapter.CategoryExpandableListViewAdapter;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.BackPressCloseHandler;
import com.example.hansangjin.froot.CustomView.AgreementDialog;
import com.example.hansangjin.froot.Data.CategoryDetail;
import com.example.hansangjin.froot.Data.CategoryMain;
import com.example.hansangjin.froot.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;


public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {
    private Button register_button;
    private ImageView exit_button;
    private TextView textView_title;

    private ExpandableListView categoryListView;
    private CategoryExpandableListViewAdapter categoryExpandableListViewAdapter;

    private BackPressCloseHandler backPressCloseHandler;

    private ArrayList<CategoryMain> categories;

    private static Toast toast = null;

    final int CATEGORY_RELIGION = 1;

    private Locale locale;
    private String locale_str;

    private SharedPreferences sp;

    private boolean agreement = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        init();
    }

    private void init() {
        createObjects();
        setUpToolbar();
        setUpData();
//        setUpUI();
        setUpListenter();
        setUpToast();
    }

    private void createObjects() {
        register_button = findViewById(R.id.button_register);

        textView_title = findViewById(R.id.toolbar_textView_title);
        exit_button = findViewById(R.id.toolbar_button_left);

        categoryListView = findViewById(R.id.expandableListView);

        categories = new ArrayList<>();

        backPressCloseHandler = new BackPressCloseHandler(this);

        categoryExpandableListViewAdapter = new CategoryExpandableListViewAdapter(this, categoryListView);

        locale = getResources().getConfiguration().locale;
        Log.d("language", locale.getLanguage());

        if (locale.getLanguage().equals("ko")) {
            locale_str = "ko";
        } else if (locale.getLanguage().equals("ja")) {
            locale_str = "ja";
        } else if (locale.getLanguage().equals("rCN")) {
            locale_str = "rCN";
        } else if (locale.getLanguage().equals("rTW")) {
            locale_str = "rTW";
        } else {
            locale_str = "en";
        }

        sp = getSharedPreferences("frootAgreement", Activity.MODE_PRIVATE);

        agreement = sp.getBoolean("agreement", false);

    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView_title.setVisibility(View.VISIBLE);
        textView_title.setText(R.string.title_category_activity);

        exit_button.setVisibility(View.VISIBLE);
        exit_button.setImageResource(R.drawable.ic_clear_black_36dp);
    }

    private void setUpData() {
        getData("http://"+ ApplicationController.getServerIP() + "category_activity.php");

//        setUpUI();
    }

    private void setUpUI() {
        register_button.setEnabled(false);
//        categoryExpandableListViewAdapter.notifyDataSetChanged();
        categoryListView.setAdapter(categoryExpandableListViewAdapter);

        categoryListView.setOnGroupExpandListener(categoryExpandableListViewAdapter);
        categoryListView.setOnGroupCollapseListener(categoryExpandableListViewAdapter);
//        register_button.setBackground(ApplicationController.setUpDrawable(R.drawable.button_recommend_restaurant));

    }

    private void setUpListenter() {
        exit_button.setOnClickListener(this);
        register_button.setOnClickListener(this);
    }

    private void setUpToast() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.layout_toast));
        TextView textView = view.findViewById(R.id.textView_toast);
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        textView.setText(R.string.toast_multiple_choice);
        toast.setView(view);
    }

    public void showToast() {
        if (toast != null) {
            toast.show();
        }
    }

    public void dismissToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    @Override
    public void onClick(View v) {

        if (v == exit_button) {
            Intent intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
            intent.putExtra("selected_info", "[]");
            ApplicationController.startActivity(this, intent);
        } else if (v == register_button) {
            if (!agreement) {
                AgreementDialog agreementDialog = new AgreementDialog(this, sp);

                agreementDialog.show();
            } else {
                goToNextActivity();
            }


        }

    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public void setRegisterEnable(boolean isEnable) {
        register_button.setEnabled(isEnable);
    }

    private void setLists(String strJSON) {
        try {
            JSONObject jsonObj = new JSONObject(strJSON);
            JSONArray jsonArray = jsonObj.getJSONArray("category");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                ArrayList<CategoryDetail> tmpDetailList = new ArrayList<>();


                int main_id = c.getInt("main_id");
                String main_str = c.getString("category_main");
                int sel_type = c.getInt("sel_type");


                JSONArray tmpJSONArray = c.getJSONArray("category_details");

                for (int j = 0; j < tmpJSONArray.length(); j++) {
                    JSONObject tmpObj = tmpJSONArray.getJSONObject(j);
                    CategoryDetail detail = new CategoryDetail(tmpObj.getInt("detail_id"), tmpObj.getString("detail_str"));
                    tmpDetailList.add(detail);
                }

                categories.add(new CategoryMain(main_id, main_str, sel_type, tmpDetailList));
            }

            categoryExpandableListViewAdapter.setCategories(categories);

            setUpUI();

            categoryExpandableListViewAdapter.notifyDataSetChanged();
            categoryListView.invalidate();
        } catch (Exception e) {

        }
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(10000);
                    con.setUseCaches(false); // 캐시 사용 안 함
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    OutputStream os = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8")); //캐릭터셋 설정
                    writer.write("lang=" + locale_str); //요청 파라미터를 입력
                    writer.flush();
                    writer.close();
                    os.close();

                    con.connect();

                    int retCode = con.getResponseCode();

                    Log.d("ResponseCode", retCode + "");

                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String result) {
                setLists(result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void goToNextActivity() {
        Intent intent;
        if (categoryExpandableListViewAdapter.getSelectedCategory().getId() == CATEGORY_RELIGION) {
            intent = new Intent(getApplicationContext(), ReligionRestaurantActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
        }

        intent.putExtra("selected_info", categoryExpandableListViewAdapter.getSelectedCategory().getDetails().toString());
        ApplicationController.startActivity(this, intent);
    }
}
