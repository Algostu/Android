package com.example.myapplication.Community;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Community.dataframe.ArticleFrame;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kakao.usermgmt.response.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public class ArticleWrite extends Fragment {
    private static final String TAG = "RHC";

    private int user_id = 1;
    private boolean is_anonymous = true;
    private int article_type;
    private String content;
    private String title;

    private CheckBox checkBox;
    private EditText et_title;
    private EditText et_content;
    private ImageButton btn_upload_article;

    public ArticleWrite(int article_type) {
        this.article_type = article_type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        View view = inflater.inflate(R.layout.wrtie_article, container, false);

        checkBox = view.findViewById(R.id.checkbox_isAnonymous);
        et_title = view.findViewById(R.id.et_title);
        et_content = view.findViewById(R.id.et_content);

        et_title.requestFocus();

        btn_upload_article = view.findViewById(R.id.btn_upload_article);
        btn_upload_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    is_anonymous = true;
                } else {
                    is_anonymous = false;
                }
                title = et_title.getText().toString();
                content = et_content.getText().toString();


                JsonObject paramObject = new JsonObject();
                paramObject.addProperty("articleType", article_type);
                paramObject.addProperty("userId", user_id);
                paramObject.addProperty("isAnonymous", is_anonymous);
                paramObject.addProperty("title", title);
                paramObject.addProperty("content", content);

                Log.d(TAG, "JSON " + paramObject.toString());

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://49.50.164.11:5000/article/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                RetrofitService service = retrofit.create(RetrofitService.class);
                Call<String> call = service.writeArticle(paramObject);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getContext(), "게시 성공!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Write Article Response: " + response.body());
                        getActivity().getSupportFragmentManager().popBackStack();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.toString());
                        Toast.makeText(getContext(), "게시 실패ㅠㅠ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

    public interface RetrofitService {
        @POST("write")
        Call<String> writeArticle(@Body JsonObject body);
    }
}
