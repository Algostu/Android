package com.dum.dodam.Mypage;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.dum.dodam.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class RecommendDialog extends Dialog {
    private Context context;

    public RecommendDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public void callDialog() {
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mypage_recommmend_dialog);
        dialog.show();

        final Button btn_ok = dialog.findViewById(R.id.btn_ok);
        final EditText et_recommend = dialog.findViewById(R.id.et_recommend);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = et_recommend.getText().toString().trim();
                if (code.isEmpty()) {
                    et_recommend.setError("추천인 코드를 입력하세요.");
                } else {
                    Log.d("RHC", "RECODE: " + code);
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_recommend.getWindowToken(), 0);
                    dialog.dismiss();
                }

            }
        });
    }
}
