package com.dum.dodam.Univ.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dum.dodam.R;
import com.dum.dodam.Univ.dataframe.UnivFrame;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class UnivSearchDialog extends Dialog {

    private Context context;
    private myOnClickListener mListener;
    private UnivFrame univFrame;

    private ArrayList<String> logoNameList;

    public UnivSearchDialog(@NonNull Context context, UnivFrame univFrame, myOnClickListener mListener) {
        super(context);
        this.context = context;
        this.mListener = mListener;
        this.univFrame = univFrame;
    }

    public interface myOnClickListener {
        void onYoutubeClick();

        void onEduPageClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.univ_search_popup);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        MaterialButton btnEduPage = findViewById(R.id.btn_edu_page);
        MaterialButton btnYoutube = findViewById(R.id.btn_youtube);
        ImageView logo = findViewById(R.id.logo);
        TextView univName = findViewById(R.id.univ_name);

        univName.setText(univFrame.univName);

        // Find Univ Logo exist.
        try {
            AssetManager assetMgr = context.getAssets();
            logoNameList = new ArrayList<>(Arrays.asList(assetMgr.list("logo/")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String univEngName = univFrame.engname;
        if (univEngName != null) {
            for (String filename : logoNameList) {
                if (filename.split("\\.")[0].equals(univEngName)) {
                    AssetManager assetMgr = context.getAssets();
                    try {
                        InputStream is = assetMgr.open("logo/" + filename);
                        logo.setImageDrawable(Drawable.createFromStream(is, null));
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        btnEduPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEduPageClick();
                dismiss();
            }
        });
        btnYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onYoutubeClick();
                dismiss();
            }
        });
    }
}
