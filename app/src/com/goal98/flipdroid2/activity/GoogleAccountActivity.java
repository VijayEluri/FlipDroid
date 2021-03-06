package com.goal98.flipdroid2.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.goal98.flipdroid2.R;
import com.goal98.flipdroid2.db.AccountDB;
import com.goal98.flipdroid2.db.SourceDB;
import com.goal98.flipdroid2.model.google.GReader;
import com.goal98.flipdroid2.util.AlarmSender;
import com.goal98.tika.common.TikaConstants;
import com.srz.androidtools.util.DeviceInfo;


public class GoogleAccountActivity extends SinaAccountActivity {

    protected AccountDB accountDB;
    protected SourceDB sourceDB;

    private static final String GOOGLE_ACCOUNT_PREF_KEY = "google_account";
    public static final String GOOGLE_ACCOUNT_SID = "GOOGLE_ACCOUNT_SID";
    public static final String GOOGLE_ACCOUNT_AUTH = "GOOGLE_ACCOUNT_AUTH";

    public DeviceInfo getDeviceInfoFromApplicationContext(){
        return DeviceInfo.getInstance(this);
    }

    protected void initView() {
        setContentView(R.layout.google_account);

        sourceDB = new SourceDB(this);
        accountDB = new AccountDB(this);

        usernameView = (TextView) findViewById(R.id.sina_username);
        passwordView = (TextView) findViewById(R.id.sina_password);

        usernameView.setText("@gmail.com");
        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setMaxWidth(getDeviceInfoFromApplicationContext().getWidth());
        Button button = (Button) findViewById(R.id.sina_login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    saveAccount();
                    goToNextActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(GoogleAccountActivity.class.getName(), e.getMessage());
                }

            }
        });
    }

    protected void saveAccount() {

        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        GReader gr = new GReader();
        try {
            gr.getSidAndAuth(username, password);
        } catch (Exception e) {
            new AlarmSender(GoogleAccountActivity.this.getApplicationContext()).sendInstantMessage(R.string.credentialInCorrect);
            return;
        }
        if (gr.isLogin()) {
            sourceDB.insert(TikaConstants.TYPE_GOOGLE_READER, getString(R.string.my_feed),  getString(R.string.my_feed_desc), null,"mygr", "http://www.google.com/images/logos/google_logo_41.png");
            accountDB.insertOrUpdate(username, password, TikaConstants.TYPE_GOOGLE_READER);
            preferences.edit().putString(GOOGLE_ACCOUNT_PREF_KEY, username).commit();
            preferences.edit().putString(GOOGLE_ACCOUNT_SID, gr.getSid()).commit();
            preferences.edit().putString(GOOGLE_ACCOUNT_AUTH, gr.getAuth()).commit();
        } else {
            new AlarmSender(GoogleAccountActivity.this.getApplicationContext()).sendInstantMessage(R.string.credentialInCorrect);
        }
    }

    protected void goToNextActivity() {
        Intent intent = new Intent(this, IndexActivity.class);
        startActivity(intent);
        finish();
    }
}