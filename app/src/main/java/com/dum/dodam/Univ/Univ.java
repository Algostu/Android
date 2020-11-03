package com.dum.dodam.Univ;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.Univ.dataframe.UnivFrame;
import com.dum.dodam.Univ.dataframe.UnivLogoFrame;
import com.dum.dodam.Univ.dataframe.UnivLogoResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;

public class Univ extends Fragment {

    private static final String TAG = "RHC";

    private UnivFrame univ;
    private ImageView iv_univ_bg;
    private TextView go_web;
    private TabLayout tab_layout;
    private ViewPager pager;
    private UnivViewPageAdapter viewPageAdapter;
    private CollapsingToolbarLayout toolBarLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    public Univ(UnivFrame univ) {
        this.univ = univ;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.univ_page, container, false);
        view.setClickable(true);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolBarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(univ.univName);

        tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar);
        if (univ.univName.equals("아주대학교")) {
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
                    //Initialize the size of the scroll
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    //Check if the view is collapsed
                    if (scrollRange + verticalOffset == 0) {
                        tab_layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.classic_blue));
                        tab_layout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    } else {
                        tab_layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
                    }
                }
            });
        }


        iv_univ_bg = view.findViewById(R.id.iv_univ_bg);
//      Temp Color
        if (univ.univName.equals("아주대학교")) {
            iv_univ_bg.setImageResource(R.drawable.ajou_univ);
            Drawable alpha = iv_univ_bg.getDrawable();
            alpha.setAlpha(150);
        } else {
            iv_univ_bg.setImageResource(R.color.classic_blue);
            tab_layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.classic_blue));
        }
//        getUnivLogo(univ.univID);

        go_web = (TextView) view.findViewById(R.id.go_web);
        go_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(univ.eduHomePage));
            }
        });

        pager = (ViewPager) view.findViewById(R.id.pager);
        viewPageAdapter = new UnivViewPageAdapter(getChildFragmentManager(), univ);
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
//                    iv_univ_bg.setImageBitmap(bitmap_signiture);
//                    Drawable d = new BitmapDrawable(getResources(), bitmap_signiture);
//                    toolBarLayout.setContentScrim(d);
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

