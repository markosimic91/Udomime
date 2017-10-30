package com.example.simic.udomime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simic on 20.10.2017..
 */

public class DogFragment extends Fragment{

    public static final String TITLE = "Pas";


    public DogFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dog_fragment,container,false);
        ArrayList<Dog> listDogs = getDogs();
        ListView lvDogs = (ListView) view.findViewById(R.id.lvDogs);
        lvDogs.setAdapter(new DogAdapter(getActivity(),listDogs));
        return view;
    }

    private ArrayList<Dog> getDogs() {
        ArrayList<Dog> dogList = new ArrayList<>();

        String[] name = getResources().getStringArray(R.array.dog);
        String[] image = getResources().getStringArray(R.array.dogimage);
        int[] contact = getResources().getIntArray(R.array.dogcontact);
        String[] desription = getResources().getStringArray(R.array.dogdesription);

        for (int i = 0; i<name.length;i++){
            dogList.add(new Dog(image[i],name[i],desription[i],contact[i]));
        }

        return dogList;
    }

}
