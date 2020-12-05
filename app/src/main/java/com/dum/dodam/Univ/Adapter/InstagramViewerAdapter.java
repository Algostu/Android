package com.dum.dodam.Univ.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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

    private ArrayList<ImageData> list = new ArrayList<ImageData>();
    private ArrayList<ImageData> imageDataList = new ArrayList<ImageData>();
    private Context context;
    public String field = "media_url";
    public String access_token = "IGQVJYVFRlT1VRMDg3QWRQQ3ZAOTGNlbGQ2VGE3YWVvYUNaLWF0MWJaZA1VvQjlwX3lPZAEF3cHVvZAXEzWlhzOHMwSG10SXpmX1lPYXlldGpYZAFhCZAS1aSVR6WTN1ci1oU2FyV2V6OXl3";

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
        protected TextView contestID;
        protected ImageView imageView;

        protected String imageUrl;
        protected String feedID;

        public Holder(View view) {
            super(view);
            this.contestID = (TextView) view.findViewById(R.id.contestID);
            this.imageView = (ImageView) view.findViewById(R.id.image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull InstagramViewerAdapter.Holder holder, final int position) {
        holder.imageUrl = list.get(position).media_url;
        MultiTransformation multiOption = new MultiTransformation(new CenterCrop(), new RoundedCorners(20));
        Glide.with(context).load(holder.imageUrl).apply(RequestOptions.bitmapTransform(multiOption)).into(holder.imageView);
        holder.feedID = list.get(position).id;
        holder.itemView.setTag(position);
    }

    public void getPhotos(String feedID){
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
                    imageDataList.clear();
                    imageDataList.addAll(response.body().data);
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