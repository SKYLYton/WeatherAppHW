package com.weather.bottomsheet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.weather.Constants;
import com.weather.R;

public class BottomSheetCreator {
    public static void show(Context context, String text){
        NotificationBottomSheetDialog dialogFragment =
                NotificationBottomSheetDialog.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.BOTTOM_SHEET_TEXT, text);
        bundle.putString(Constants.BOTTOM_SHEET_BUTTON, context.getString(android.R.string.ok));

        dialogFragment.setArguments(bundle);
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        dialogFragment.show(fragmentActivity.getSupportFragmentManager(),
                Constants.BOTTOM_SHEET_TAG);
    }
}
