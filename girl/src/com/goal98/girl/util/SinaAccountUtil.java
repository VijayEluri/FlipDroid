package com.goal98.girl.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import com.goal98.girl.activity.WeiPaiWebViewClient;
import com.goal98.girl.db.AccountDB;
import com.goal98.girl.model.Account;
import com.goal98.girl.model.sina.SinaToken;
import com.goal98.tika.common.TikaConstants;

/**
 * Created by IntelliJ IDEA.
 * User: ITS
 * Date: 11-7-5
 * Time: 下午1:29
 * To change this template use File | Settings | File Templates.
 */
public class SinaAccountUtil {

    public static boolean alreadyBinded(Context context) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return (preferences.getString(WeiPaiWebViewClient.SINA_ACCOUNT_PREF_KEY, null) != null);
    }

    public static SinaToken getToken(Context context) {
        String token = null;
        String tokenSecret = null;

        AccountDB accountDB = new AccountDB(context);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String userId = preferences.getString(WeiPaiWebViewClient.SINA_ACCOUNT_PREF_KEY, null);
        if (userId == null)
            userId = preferences.getString(WeiPaiWebViewClient.PREVIOUS_SINA_ACCOUNT_PREF_KEY, null);

        Cursor cursor = accountDB.findByTypeAndUsername(TikaConstants.TYPE_MY_SINA_WEIBO, userId);
        try {
            cursor.moveToFirst();
            token = cursor.getString(cursor.getColumnIndex(Account.KEY_PASSWORD));
            tokenSecret = cursor.getString(cursor.getColumnIndex(Account.KEY_PASSWORD_SECRET));
        } finally {
            cursor.close();
            accountDB.close();
        }

        SinaToken sinaToken = new SinaToken();
        sinaToken.setToken(token);
        sinaToken.setTokenSecret(tokenSecret);

        return sinaToken;
    }
}
