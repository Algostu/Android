package com.dum.dodam.Collage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dum.dodam.Collage.dataframe.CollageFrame;
import com.dum.dodam.Collage.dataframe.CollageLogoFrame;
import com.dum.dodam.Collage.dataframe.CollageLogoResponse;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;

import retrofit2.Call;

public class Collage extends Fragment {
    private final String TAG = "RHC";
    private CollageFrame collage;

    private ImageView iv_collage_logo;
    private TextView tv_collage_name;
    private TextView collage_news;
    private TextView collage_community;


    public Collage(CollageFrame collage) {
        this.collage = collage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collage_page, container, false);
        view.setClickable(true);

        iv_collage_logo = view.findViewById(R.id.iv_collage_logo);
        tv_collage_name = view.findViewById(R.id.tv_collage_name);
        collage_news = view.findViewById(R.id.collage_news);
        collage_community = view.findViewById(R.id.collage_community);

        getUnivLogo(collage.univID);

        collage_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceCollagePageFragment(new CollageCommunity());
            }
        });

        collage_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "homepage" + collage.homePage);
                replaceCollagePageFragment(new CollageNews(collage.eduHomePage));
            }
        });

        tv_collage_name.setText(collage.univName);
        return view;
    }

    private void getUnivLogo(int univID) {

        RetrofitAdapter rAdapter = new RetrofitAdapter();
        RetrofitService service = rAdapter.getInstance(getActivity());
        Call<CollageLogoResponse> call = service.getUnivLogo(univID);

        call.enqueue(new retrofit2.Callback<CollageLogoResponse>() {
            @Override
            public void onResponse(Call<CollageLogoResponse> call, retrofit2.Response<CollageLogoResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().checkError(getContext())!= 0) return;
                    CollageLogoFrame result = response.body().body;
                    Bitmap bitmap_signiture = StringToBitmap(result.signiture);
                    iv_collage_logo.setImageBitmap(bitmap_signiture);
                    Log.d("RHC", "on response: " + iv_collage_logo.toString());
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<CollageLogoResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void replaceCollagePageFragment(Fragment fragment) {
        FragmentTransaction transaction = ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Log.d("RHC", "StringToBitmap: ");
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            Log.d("RHC", "ChangeFail");
            return null;
        }
    }
}

