package com.dum.dodam.Univ.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.devs.readmoreoption.ReadMoreOption;
import com.dum.dodam.Community.dataframe.FeedResult;
import com.dum.dodam.Community.dataframe.ImageData;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;

public class InstagramViewerAdapter extends RecyclerView.Adapter<InstagramViewerAdapter.Holder> {
    // debug
    private final String TAG = "InstagramViewerAdapter";

    // instagram
    public String field = "media_url";
    public String access_token = "IGQVJYVFRlT1VRMDg3QWRQQ3ZAOTGNlbGQ2VGE3YWVvYUNaLWF0MWJaZA1VvQjlwX3lPZAEF3cHVvZAXEzWlhzOHMwSG10SXpmX1lPYXlldGpYZAFhCZAS1aSVR6WTN1ci1oU2FyV2V6OXl3";
    public String id;

    private ArrayList<ImageData> list = new ArrayList<ImageData>();
    //    private ArrayList<ArrayList<ImageData>> imageDataList = new ArrayList<ArrayList<ImageData>>();
    private Context context;

    public InstagramViewerAdapter(Context context, ArrayList<ImageData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstagramViewerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.univ_instagram_viewer_item, parent, false);
        InstagramViewerAdapter.Holder holder = new InstagramViewerAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView content;
        protected ImageView photo;
        protected ViewPager2 viewPager2;
        protected InstagramViewPagerAdapter adapter;
        protected ArrayList<ImageData> imgList;

        public Holder(View view) {
            super(view);
            this.content = view.findViewById(R.id.content);
            this.photo = view.findViewById(R.id.photo);
            this.viewPager2 = view.findViewById(R.id.viewPager);
            imgList = new ArrayList<ImageData>();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final InstagramViewerAdapter.Holder holder, final int position) {
        holder.adapter = new InstagramViewPagerAdapter(context, holder.imgList);

        holder.viewPager2.setId(position + 1);
        holder.viewPager2.setAdapter(holder.adapter);

        getPhotos(list.get(position).id, holder);

        ReadMoreOption readMoreOption = new ReadMoreOption.Builder(context).textLength(50, ReadMoreOption.TYPE_CHARACTER) // OR
                .moreLabel("more")
                .lessLabel("..less")
                .moreLabelColor(Color.rgb(128, 128, 128))
                .lessLabelColor(Color.rgb(128, 128, 128))
                .labelUnderLine(false)
                .expandAnimation(true)
                .build();

        try {
            readMoreOption.addReadMoreTo(holder.content, list.get(position).caption);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        holder.itemView.setTag(position);
    }

    public void getPhotos(String feedID, final Holder holder) {
        // Retrofit 삽질 4시간 경험 한 후기
        // 1. Retrofit 의 Service는 BaseURL과는 연관이 없다.
        // 2. Retrofit은 여러개를 만들어서 사용해도 상관 없다.
        // 3. Retrofit의 Parameter는 반드시 URL encoded 되지 않은것으로 사용해야한다.
        // 4. Retrofit의 Parameter는 절대 getString(R.string.name) 으로 가지고 온것을 사용하면 안된다.
        RetrofitService service = RetrofitAdapter.getInstance(context, "https://graph.instagram.com/");
        Call<FeedResult> call = service.getFeedDetails(feedID, field, access_token);

        call.enqueue(new retrofit2.Callback<FeedResult>() {
            @Override
            public void onResponse(Call<FeedResult> call, retrofit2.Response<FeedResult> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Success " + response.body().data.size());
                    holder.imgList.clear();
                    holder.imgList.addAll(response.body().data);
                    holder.adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<FeedResult> call, Throwable t) {
                Toast.makeText(context, "Please reloading", Toast.LENGTH_SHORT).show();
            }
        });
    }
}