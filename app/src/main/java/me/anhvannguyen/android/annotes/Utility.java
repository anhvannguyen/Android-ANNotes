package me.anhvannguyen.android.annotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Anh on 1/4/2016.
 */
public class Utility {
    public static final int SORT_DATE = 0;
    public static final int SORT_TITLE = 1;

    public static int getSortingPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(preferences.getString(
                context.getString(R.string.pref_sortorder_key),
                context.getString(R.string.pref_sortorder_date_value)
        ));
    }
}
