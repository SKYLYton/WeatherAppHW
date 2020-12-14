package com.weather.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.weather.Constants;
import com.weather.R;

public class NotificationBottomSheetDialog extends BottomSheetDialogFragment {
    public static NotificationBottomSheetDialog newInstance() {
        return new NotificationBottomSheetDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_dialog_notification, container, false);

        setCancelable(false);

        TextView textViewText = view.findViewById(R.id.textViewText);
        TextView textViewButton = view.findViewById(R.id.textViewButton);

        Bundle bundle = getArguments();
        if(bundle != null){
            textViewText.setText(bundle.getString(Constants.BOTTOM_SHEET_TEXT));
            textViewButton.setText(bundle.getString(Constants.BOTTOM_SHEET_BUTTON));
        }

        view.findViewById(R.id.bottom_lin_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;

    }

}
