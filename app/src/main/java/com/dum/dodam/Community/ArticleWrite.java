package com.dum.dodam.Community;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dum.dodam.R;
import com.dum.dodam.httpConnection.BaseResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class ArticleWrite extends Fragment {
    private static final String TAG = "RHC";

    private boolean is_anonymous = true;
    private int communityType;
    private int communityID;
    private String content;
    private String title;

    private CheckBox checkBox;
    private EditText et_title;
    private EditText et_content;
    private ImageButton btn_upload_article;

    private InputMethodManager imm;

    public ArticleWrite(int communityType, int communityID) {
        this.communityID = communityID;
        this.communityType = communityType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);

        View view = inflater.inflate(R.layout.article_write, container, false);
        view.setClickable(true);

        checkBox = view.findViewById(R.id.checkbox_isAnonymous);
        et_title = view.findViewById(R.id.et_title);
        et_content = view.findViewById(R.id.et_content);

        et_title.requestFocus();
        imm.showSoftInput(et_title, 0);
        btn_upload_article = view.findViewById(R.id.btn_upload_article);

        btn_upload_article.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if (TextUtils.isEmpty(et_title.getText().toString()) || TextUtils.isEmpty(et_content.getText().toString())) {
                    et_title.setError("제목 혹은 내용을 입력해주세요.");
                } else {
                    if (checkBox.isChecked()) {
                        is_anonymous = true;
                    } else {
                        is_anonymous = false;
                    }
                    title = et_title.getText().toString();
                    content = et_content.getText().toString();

                    JsonObject paramObject = new JsonObject();
                    paramObject.addProperty("communityType", communityType);
                    paramObject.addProperty("communityID", communityID);
                    paramObject.addProperty("isAnonymous", is_anonymous);
                    paramObject.addProperty("title", title);
                    paramObject.addProperty("content", content);

                    RetrofitAdapter adapter = new RetrofitAdapter();
                    com.dum.dodam.httpConnection.RetrofitService service = adapter.getInstance(getContext());

                    Call<BaseResponse> call = service.writeArticle(paramObject);

                    call.enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            BaseResponse response1 = response.body();
                            if (response1.checkError(getContext()) != 0) return;

                            imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
                            imm.hideSoftInputFromWindow(et_title.getWindowToken(), 0);

                            getActivity().getSupportFragmentManager().popBackStack();
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.toString());
                            Toast.makeText(getContext(), "게시 실패ㅠㅠ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }
}
