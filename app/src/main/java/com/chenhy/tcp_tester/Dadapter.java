package com.chenhy.tcp_tester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class Dadapter extends BaseAdapter {

public LayoutInflater inflater;
public Context con;
private Button btn;
private CheckBox checkBox;
private TextView t1,t2;
//public List list=Dactivity.list3;
    public List list;
private List<Dinfo> mada;
public Dadapter(Context context, List relist){
    this.con=context;
    this.list=relist;
    inflater=LayoutInflater.from(con);
}

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View converview, ViewGroup parent) {
      View view=inflater.inflate(R.layout.item_post_activity,null);
         checkBox= (CheckBox) view.findViewById(R.id.checkbox);
       // converview=LayoutInflater.from(con).inflate(R.layout.item_post_activity,parent,false);
//    btn=view.findViewById(R.id.btn);
         t1= (TextView) view.findViewById(R.id.id);
         t2= (TextView) view.findViewById(R.id.postion);
        t1.setText(list.get(position).toString());

t2.setText(String.valueOf(position+1));
String s= "true";
if (list.get(position).toString().equals(s)){
    checkBox.setChecked(true);
}

     //   Log.e("list",""+list);
     //   Log.e("list1", String.valueOf(list.size()));
     //   Log.e("list2", String.valueOf(position));
        return view;


    }
}
