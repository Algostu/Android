package com.dum.dodam.Login;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ViewTarget;
import com.dum.dodam.Login.Adapter.SearchAdapter;
import com.dum.dodam.Login.Data.School;
import com.dum.dodam.Login.Data.SearchResponse;
import com.dum.dodam.Login.Data.UserInfo;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.BaseResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class SIgnUP2 extends Fragment {
    private static final String TAG = "KHK";

    private final int GET_GALLERY_IMAGE = 200;
    private static final int REQUEST_CODE = 0;
    Uri selectedImageUri;
    String filePath;
    File localImgFile;

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
    private ImageView loadingView;
    private EditText userName;

    private int grade;
    private boolean nickNamePossibleNot;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup2, container, false);

        userName = view.findViewById(R.id.name);
        loadingView = view.findViewById(R.id.loadingView);

        Glide.with((startUpActivity) getContext()).load(R.raw.loading_circle).into(loadingView);
        loadingView.setVisibility(GONE);


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
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
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
                loadingView.setVisibility(View.VISIBLE);

                JsonObject paramObject = new JsonObject();
                paramObject.addProperty("schoolID", school.schoolID);
                paramObject.addProperty("nickName", nickName.getText().toString());
                paramObject.addProperty("grade", grade);
                paramObject.addProperty("userID", user.userID);
                paramObject.addProperty("userName", userName.getText().toString());
                paramObject.addProperty("accessToken", user.accessToken);
                paramObject.addProperty("email", user.email);
                paramObject.addProperty("gender", user.gender);
                paramObject.addProperty("ageRange", user.ageRange);

//                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), localImgFile);

//                MultipartBody.Part image =
//                        MultipartBody.Part.createFormData("image", localImgFile.getName(), requestFile);

                Log.d(TAG, "JSON " + paramObject.toString());

//                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), paramObject.toString());

                RetrofitAdapter rAdapter = new RetrofitAdapter();
                RetrofitService service = rAdapter.getInstance(getActivity());
                Call<BaseResponse> call = service.registerKAKAO(paramObject);
                call.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (!response.isSuccessful()) return;
                        BaseResponse response1 = response.body();
                        if (response1.checkError(getContext()) != 0) return;
                        if (response1.status.equals("<success>")) {
                            String info = "";
                            info = info + "Name: " + userName + "\n";
                            info = info + "School: " + school.schoolName + "\n";

                            GMailSender gMailSender = new GMailSender("dum.dodamdodam@gmail.com", "ehekaeheka16#");

                            try {
                                gMailSender.sendMail2("인증 이메일", info, "dum.dodamdodam@gmail.com", localImgFile);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ((startUpActivity) getActivity()).replaceFragment(new Login());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
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

        RetrofitAdapter rAdapter = new RetrofitAdapter();
        RetrofitService service = rAdapter.getInstance(getActivity());
        Call<SearchResponse> call = service.searchSchoolName(query);

        call.enqueue(new retrofit2.Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, retrofit2.Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    SearchResponse result = response.body();
                    list.clear();
                    list.addAll(result.body);
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onResponse: Success " + response.body());
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imgUri = data.getData();
        String imgPath = getPath(getActivity().getApplicationContext(), imgUri);

        if (imgUri != null && imgPath != null) {

            InputStream in = null;//src
            try {
                in = getActivity().getContentResolver().openInputStream(imgUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String extension = imgPath.substring(imgPath.lastIndexOf("."));
            localImgFile = new File(getActivity().getApplicationContext().getFilesDir(), "localImgFile" + extension);


            if (in != null) {
                try {
                    OutputStream out = new FileOutputStream(localImgFile);//dst
                    try {
                        // Transfer bytes from in to out
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                    } finally {
                        out.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //InternalStorage로 복사된 localImgFile을 통하여 File에 접근가능
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    Toast.makeText(context, "Could not get file path. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    contentUri = MediaStore.Files.getContentUri("external");
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}
