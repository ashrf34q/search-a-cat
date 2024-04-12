package com.example.project3;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class StringsAdapter extends ArrayAdapter<String> {

    public StringsAdapter(Context context, ArrayList<String> list) {
        super(context, 0, list);
    }
}
