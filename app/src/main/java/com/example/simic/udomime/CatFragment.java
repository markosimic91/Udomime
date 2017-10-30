package com.example.simic.udomime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Simic on 20.10.2017..
 */

public class CatFragment  extends Fragment{
    public static final  String TITLE = "Mačka";

    public CatFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cat_fragment,container,false);
        ArrayList<Cat> listCats = getCats();
        ListView lvCats = (ListView) view.findViewById(R.id.lvCats);
        lvCats.setAdapter(new CatAdapter(getActivity(),listCats));
        return view;

    }

    private ArrayList<Cat> getCats() {
        ArrayList<Cat> catList = new ArrayList<>();

        String[] name = getResources().getStringArray(R.array.cat);
        String[] image = getResources().getStringArray(R.array.catimage);
        int[] contact = getResources().getIntArray(R.array.catcontact);
        String[] desription = getResources().getStringArray(R.array.catdesription);

        for (int i=0; i<name.length;i++){
            catList.add(new Cat(image[i],name[i],desription[i],contact[i]));
        }

        return catList;
    }


}
