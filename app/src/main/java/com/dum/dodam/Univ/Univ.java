package com.dum.dodam.Univ;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.Univ.dataframe.UnivFrame;
import com.dum.dodam.Univ.dataframe.UnivLogoFrame;
import com.dum.dodam.Univ.dataframe.UnivLogoResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;

public class Univ extends Fragment {

    private static final String TAG = "RHC";

    private UnivFrame univ;
    private ImageView iv_univ_logo;
    private TextView go_web;
    private TabLayout tab_layout;
    private ViewPager pager;
    private UnivViewPageAdapter viewPageAdapter;

    public Univ(UnivFrame univ) {
        this.univ = univ;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collage_page, container, false);
        view.setClickable(true);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(univ.univName);

        iv_univ_logo = view.findViewById(R.id.iv_univ_logo);
        getUnivLogo(univ.univID);

        go_web = (TextView) view.findViewById(R.id.go_web);
        go_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(univ.eduHomePage));
            }
        });

        tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);

        pager = (ViewPager) view.findViewById(R.id.pager);
        viewPageAdapter = new UnivViewPageAdapter(getActivity().getSupportFragmentManager(), univ);
        pager.setAdapter(viewPageAdapter);

        tab_layout.setupWithViewPager(pager);
//        tab_layout.getTabAt(0).setIcon(R.drawable.ic_add_photo);

        return view;
    }

    private void getUnivLogo(int univID) {
        RetrofitAdapter rAdapter = new RetrofitAdapter();
        RetrofitService service = rAdapter.getInstance(getActivity());
        Call<UnivLogoResponse> call = service.getUnivLogo(univID);

        call.enqueue(new retrofit2.Callback<UnivLogoResponse>() {
            @Override
            public void onResponse(Call<UnivLogoResponse> call, retrofit2.Response<UnivLogoResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().checkError(getContext()) != 0) return;
                    UnivLogoFrame result = response.body().body;
                    Bitmap bitmap_signiture = StringToBitmap(result.signiture);
                    iv_univ_logo.setImageBitmap(bitmap_signiture);
                } else {
                    Log.d(TAG, "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<UnivLogoResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}

