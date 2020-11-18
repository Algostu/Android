package com.dum.dodam.Mypage;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import androidx.annotation.NonNull;

import com.dum.dodam.R;

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

//        final Button btn_ok = dialog.findViewById(R.id.btn_ok);
//        final EditText et_recommend = dialog.findViewById(R.id.et_recommend);
//
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String code = et_recommend.getText().toString().trim();
//                if (code.isEmpty()) {
//                    et_recommend.setError("숫자를 입력하세요.");
//                } else {
//                    try {
//                        int x = Integer.parseInt(et_recommend.getText().toString());
//
//                        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(et_recommend.getWindowToken(), 0);
//                        dialog.dismiss();
//                    } catch (Exception e) {
//                        Toast.makeText(getContext(), "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    }
//
//                }
//
//            }
//        });
    }
}
