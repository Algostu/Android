package com.example.myapplication.School;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.School.dataframe.CafeteriaFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class School extends Fragment {
    ArrayList<CafeteriaFrame> menu = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.school, container, false);
        getCafeteriaMenu();

//        https://lktprogrammer.tistory.com/178

        return view;
    }

    public void getCafeteriaMenu() {
        long now = System.currentTimeMillis();
        Date data = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yymm");
        String date = dateFormat.format(data);
        String filename = date + "_cafeteria_menu.tmp";
        File file = new File(getContext().getFilesDir() + "/" + filename);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream ois = new ObjectInputStream(fis);
                menu = (ArrayList) ois.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            try {
                FileOutputStream fos = getActivity().openFileOutput(filename, getContext().MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(menu);
                oos.close();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}