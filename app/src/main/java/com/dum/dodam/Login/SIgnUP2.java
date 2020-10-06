package com.dum.dodam.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dum.dodam.Login.Adapter.SearchAdapter;
import com.dum.dodam.Login.Data.School;
import com.dum.dodam.Login.Data.UserInfo;
import com.dum.dodam.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import static android.app.Activity.RESULT_OK;

public class SIgnUP2 extends Fragment {
    private static final String TAG = "KHK";

    private final int GET_GALLERY_IMAGE = 200;
    Uri selectedImageUri;

    private List<School> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터

    private CheckBox checkBox;
    private EditText nickName;
    private TextView nickNameOkay;
    private TextView selectedSchool;
    private Spinner gradeSpinner;
    private School school;
    private Button submit;
    private ImageView studentCard;
    private int grade;
    private boolean nickNamePossibleNot;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup2, container, false);

        nickName = view.findViewById(R.id.nickName);
        nickNameOkay = view.findViewById(R.id.nickNameOkay);
        nickNameOkay.setText("다시 입력 하세요.");
        nickNameOkay.setTextColor(Color.parseColor("#F46F60"));
        nickName.requestFocus();
        nickNamePossibleNot = true;
        nickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Pattern.matches("^[가-힣a-zA-Z0-9]*$", charSequence.toString()) && charSequence.length() <= 15 && charSequence.length() >= 5) {
                    nickNameOkay.setText("사용 가능한 닉네임 입니다.");
                    nickNameOkay.setTextColor(Color.parseColor("#52D84D"));
                    nickNamePossibleNot = false;
                    Log.d(TAG, "nickname: true");
                } else {
                    nickNameOkay.setText("다시 입력 하세요.");
                    nickNameOkay.setTextColor(Color.parseColor("#F46F60"));
                    nickNamePossibleNot = true;
                    Log.d(TAG, "nickname: false");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        grade = 0;
        gradeSpinner = view.findViewById(R.id.grade);
        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                grade = i + 9;
                Log.d(TAG, "GRADE " + grade);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        selectedSchool = view.findViewById(R.id.selected_school);
        editSearch = view.findViewById(R.id.editSearch);
        listView = view.findViewById(R.id.listView);
        list = new ArrayList<School>();
        adapter = new SearchAdapter(list, getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                school = list.get(i);
                selectedSchool.setText(school.schoolName);
                editSearch.setText("");
                list.clear();
                adapter.notifyDataSetChanged();
            }
        });
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        studentCard = view.findViewById(R.id.im_student_card);
        studentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo user = ((startUpActivity) getActivity()).user;
                Date date = new Date();
                if (date.getTime() > user.expTime) {
                    Toast.makeText(getActivity(), "가입 시간이 만료 되었습니다. 처음부터 다시 시작해 주세요.", Toast.LENGTH_SHORT).show();
                    ((startUpActivity) getActivity()).replaceFragment(new Login());
                    return;
                }
                if (nickNamePossibleNot) {
                    Toast.makeText(getActivity(), "닉네임을 제대로 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (grade == 0) {
                    Toast.makeText(getActivity(), "학년을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (school == null) {
                    Toast.makeText(getActivity(), "학교를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!school.is_okay_to_enroll(user.gender)) {
                    Toast.makeText(getActivity(), "성별이 다른 학교 입니다. 학교를 다시 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                JsonObject paramObject = new JsonObject();
                paramObject.addProperty("schoolID", school.schoolID);
                paramObject.addProperty("nickName", nickName.getFreezesText());
                paramObject.addProperty("grade", grade);
                paramObject.addProperty("userID", user.userID);
                paramObject.addProperty("accessToken", user.accessToken);
                paramObject.addProperty("email", user.email);
                paramObject.addProperty("gender", user.gender);
                paramObject.addProperty("ageRange", user.ageRange);

                File file = new File(String.valueOf(selectedImageUri));
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                MultipartBody.Part image =
                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                Log.d(TAG, "JSON " + paramObject.toString());

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://49.50.164.11:5000/auth/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                RetrofitService service = retrofit.create(RetrofitService.class);
                Call<String> call = service.registerKAKAO(paramObject, image);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Register Kakao response: " + response.body());
                        if (!response.isSuccessful()) return;
                        String[] res = response.body().split(":");
                        if (res.length == 1) {
                            getActivity().getSupportFragmentManager().popBackStack();
                            Context context = getActivity();
                            SharedPreferences sharedPref = context.getSharedPreferences(
                                    "auto", Context.MODE_PRIVATE);
                            SharedPreferences.Editor autoLogin = sharedPref.edit();
                            autoLogin.putString("autoLogin", "true");
                            autoLogin.commit();
                            ((startUpActivity) getActivity()).replaceFragment(new Login());
                        }


                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.toString());
                        Toast.makeText(getContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

    private void search(String query) {

        list.clear();
        if (query.equals("")) {
            adapter.notifyDataSetChanged();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.50.164.11:5000/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<ArrayList<School>> call = service.searchSchoolName(query);

        call.enqueue(new retrofit2.Callback<ArrayList<School>>() {
            @Override
            public void onResponse(Call<ArrayList<School>> call, retrofit2.Response<ArrayList<School>> response) {
                if (response.isSuccessful()) {
                    ArrayList<School> result = response.body();
                    list.clear();
                    list.addAll(result);
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onResponse: Success " + response.body());
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<School>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public interface RetrofitService {

        @Multipart
        @POST("kakaoSignup")
        Call<String> registerKAKAO(@Body JsonObject body, @Part MultipartBody.Part image);

        @GET("schoolList")
        Call<ArrayList<School>> searchSchoolName(@Query("schoolName") String schoolName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
        }
    }

}
