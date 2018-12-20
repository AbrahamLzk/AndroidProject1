package com.example.gahui.httptest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import android.widget.AdapterView;


public class NextActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private TextView txtshow;
    private String name;
    private String psw;

    private DrawerLayout drawer_layout;
    private ListView list_left_drawer;
    private ArrayList<Item> menuLists;
    private MyAdapter<Item> myAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        list_left_drawer = (ListView) findViewById(R.id.list_left_drawer);

        menuLists = new ArrayList<Item>();
        menuLists.add(new Item(R.mipmap.iv_menu_realtime,"实时信息"));
        menuLists.add(new Item(R.mipmap.iv_menu_alert,"提醒通知"));
        menuLists.add(new Item(R.mipmap.iv_menu_trace,"活动路线"));
        menuLists.add(new Item(R.mipmap.iv_menu_settings,"相关设置"));
        myAdapter = new MyAdapter<Item>(menuLists,R.layout.item_list) {
            @Override
            public void bindView(ViewHolder holder, Item obj) {
                holder.setImageResource(R.id.img_icon,obj.getIconId());
                holder.setText(R.id.txt_content, obj.getIconName());
            }
        };
        list_left_drawer.setAdapter(myAdapter);
        list_left_drawer.setOnItemClickListener(this);

        txtshow = (TextView) findViewById(R.id.txtshow);
        //获得Intent对象,并且用Bundle出去里面的数据
        Intent it = getIntent();
        Bundle bd = it.getExtras();

        //按键值的方式取出Bundle中的数据
        name = bd.getCharSequence("user").toString();
        psw = bd.getCharSequence("pass").toString();
        txtshow.setText("用户名：" + name + "；密码：" + psw);

        ListView listView = (ListView) this.findViewById(R.id.list);

        try {
            List<News> videos = VideoNewsService.sendRequestWithOkHttp();//需修改成你本机的Http请求路径
            List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
            for(News news : videos){
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("id", news.getId());
                item.put("title", news.getTitle());
                item.put("timelength", getResources().getString(R.string.timelength)
                        + news.getTimelength()+ getResources().getString(R.string.min));
                                data.add(item);
            }
            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item,
                    new String[]{"title", "timelength"}, new int[]{R.id.text1, R.id.text2});
                        listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            }


    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString("text", menuLists.get(position).getIconName());
        contentFragment.setArguments(args);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.ly_content,contentFragment).commit();
        drawer_layout.closeDrawer(list_left_drawer);
    }
}
