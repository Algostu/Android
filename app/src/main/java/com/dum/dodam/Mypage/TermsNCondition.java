package com.dum.dodam.Mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dum.dodam.MainActivity;
import com.dum.dodam.R;

public class TermsNCondition extends Fragment {

    private TextView contract1;
    private TextView contract2;
    private TextView contract3;
    private TextView contract4;

    private Button btn_ok;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.terms_and_conditions, container, false);
        view.setClickable(true);

        contract1 = view.findViewById(R.id.contract1);
        contract2 = view.findViewById(R.id.contract2);
        contract3 = view.findViewById(R.id.contract3);
        contract4 = view.findViewById(R.id.contract4);
//        btn_ok = view.findViewById(R.id.btn_ok);

        contract1.setText(getText(R.string.terms_and_conditions1));
        contract2.setText(getText(R.string.terms_and_conditions2));
        contract3.setText(getText(R.string.terms_and_conditions3));
        contract4.setText(getText(R.string.terms_and_conditions4));

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });
        return view;
    }
}
