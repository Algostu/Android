package com.dum.dodam.Univ;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.LocalDB.Todo;
import com.dum.dodam.LocalDB.TodoModule;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.Univ.Fragment.UnivSearchDialog;
import com.dum.dodam.Univ.dataframe.LiveShowFrame;
import com.dum.dodam.Univ.dataframe.LiveShowResponse;
import com.dum.dodam.Univ.dataframe.UnivFrame;
import com.dum.dodam.Univ.dataframe.UnivResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveShow extends Fragment implements LiveShowAdapter.OnListItemSelectedInterface {

    private static String TAG = LiveShow.class.getName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView no_liveshow;

    private ArrayList<LiveShowFrame> list = new ArrayList<LiveShowFrame>();
    private UnivFrame collage = new UnivFrame();
    private int cnt_readLiveShow = 0;

    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.univ_liveshow, container, false);
        view.setClickable(true);
        setHasOptionsMenu(true);
        RealmTodoListInit();

        no_liveshow = view.findViewById(R.id.no_liveshow);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("라이브 쇼");
        actionBar.setSubtitle("대학생의 생생한 경험담을 듣고싶다면?!");
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.instagram.com/d.___.dam";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(Intent.createChooser(intent, "Browse with"));
            }
        });

        adapter = new LiveShowAdapter(getContext(), list, this);

        readLiveShowList();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_liveshow);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu_live_show, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save2Todo:
                saveLiveShow2Todo();
                break;
            case R.id.delete2Todo:
                deleteLiveShowFromTodo(true);
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void readLiveShowList() {
        com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<LiveShowResponse> call = service.readLiveShowList("latest");
        call.enqueue(new Callback<LiveShowResponse>() {
            @Override
            public void onResponse(Call<LiveShowResponse> call, Response<LiveShowResponse> response) {
                if (response.isSuccessful()) {
                    LiveShowResponse result = response.body();
                    if (result.checkError(getActivity()) != 0) return;

                    list.addAll(result.body);
                    adapter.notifyDataSetChanged();
                    if (list.size() < 1)
                        no_liveshow.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "onResponse: Fail");
                }
            }

            @Override
            public void onFailure(Call<LiveShowResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readLiveShow < 5) readLiveShowList();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readLiveShow++;
            }
        });
    }

    @Override
    public void onItemSelected2(View v, int position) {
        LiveShowAdapter.Holder holder = (LiveShowAdapter.Holder) recyclerView.findViewHolderForAdapterPosition(position);

        search(holder.school.getText().toString());
    }

    public void search(String query) {
        list.clear();
        if (query.equals("")) {
            adapter.notifyDataSetChanged();
            return;
        }

        RetrofitAdapter rAdapter = new RetrofitAdapter();
        RetrofitService service = rAdapter.getInstance(getActivity());
        Call<UnivResponse> call = service.searchCollageName(query);

        call.enqueue(new retrofit2.Callback<UnivResponse>() {
            @Override
            public void onResponse(Call<UnivResponse> call, retrofit2.Response<UnivResponse> response) {
                if (response.isSuccessful()) {
                    UnivResponse result = response.body();
                    collage = result.body.get(0);
                    UnivSearchDialog dialog = new UnivSearchDialog(getContext(), collage, new UnivSearchDialog.myOnClickListener() {
                        @Override
                        public void onYoutubeClick() {
                            if (collage.youtube == null | collage.youtube.equals("")) {
                                Toast.makeText(getContext(), "관련 정보가 없어 검색페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                                String youtubeUrl = "https://www.youtube.com/results?search_query=" + collage.univName;
                                ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(youtubeUrl));
                            } else {
                                ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(collage.youtube));
                            }
                        }

                        @Override
                        public void onEduPageClick() {
                            if (collage.admission == null | collage.admission.equals("")) {
                                Toast.makeText(getContext(), "입학처 정보가 없어 홈페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                                ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(collage.homePage));
                            } else {
                                ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(collage.admission));
                            }
                        }
                    });
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);

                    DisplayMetrics dm = getActivity().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                    final int width = dm.widthPixels; //디바이스 화면 너비
                    final int height = dm.heightPixels; //디바이스 화면 높이

                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
                    wm.copyFrom(dialog.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
                    wm.width = (int) (width * 0.9);  //화면 너비의 절반
                    wm.height = height / 2;  //화면 높이의 절반

                    dialog.show();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<UnivResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void deleteLiveShowFromTodo(final boolean isToast) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (LiveShowFrame frame : list) {
                    String id = frame.writtenTime.substring(0, 7).replace("-", "");
                    Todo todo = realm.where(Todo.class).equalTo("ID", id).equalTo("title", String.format("%s %s", frame.univTitle, frame.major)).findFirst();
                    if (todo != null) todo.deleteFromRealm();
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (isToast)
                    Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveLiveShow2Todo() {
        deleteLiveShowFromTodo(false);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String id, title, year, month, date;

                Calendar calendar = Calendar.getInstance();
                for (LiveShowFrame frame : list) {
                    Todo todo = realm.createObject(Todo.class);
                    year = frame.writtenTime.substring(0, 4);
                    month = frame.writtenTime.substring(5, 7);
                    date = frame.writtenTime.substring(8, 10);

                    id = year + month;
                    title = String.format("%s %s", frame.univTitle, frame.major);

                    todo.ID = id;
                    calendar.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date));
                    todo.end = calendar.getTimeInMillis();
                    todo.start = calendar.getTimeInMillis();
                    todo.title = title;
                    todo.done = false;
                    todo.color = Color.parseColor("#ffcc80");
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "일정에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
            }
        });
    }

    private void RealmTodoListInit() {
        RealmConfiguration config = new RealmConfiguration.Builder().name("TodoList.realm").schemaVersion(1).modules(new TodoModule()).build();
        realm = Realm.getInstance(config);
    }
}
