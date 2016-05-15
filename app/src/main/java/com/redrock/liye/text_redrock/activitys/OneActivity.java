package com.redrock.liye.text_redrock.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.redrock.liye.text_redrock.R;
import com.redrock.liye.text_redrock.fragment.MyFragmentPagerAdapter1;
import com.redrock.liye.text_redrock.fragment.PageFragment_gangtai;
import com.redrock.liye.text_redrock.fragment.PageFragment_neidi;
import com.redrock.liye.text_redrock.fragment.PageFragment_oumei;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2016/5/14.
 */
public class OneActivity extends AppCompatActivity {
    private MyFragmentPagerAdapter1 pagerAdapter;

    private ViewPager viewPager;

    private TabLayout tabLayout;
    private List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_bar);
        getSupportActionBar().hide();
        initToolBar();
        initFragment();
    }
    private void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setLogo(R.drawable.ic_logo);//设置app logo
        toolbar.setTitle("日靡难眼的APP");//设置主标题
        toolbar.setSubtitle("小也君的私人APP");//设置副标题
        toolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                switch(menuItemId){
                    case R.id.action_item1:
                        Toast.makeText(OneActivity.this, "榜单", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_item2:
                        Toast.makeText(OneActivity.this, "查询", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OneActivity.this,FindActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_item3:
                        Toast.makeText(OneActivity.this, "播放列表", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(OneActivity.this,ListActivity.class);
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });
    }
    private void initFragment(){
        fragments.add(new PageFragment_oumei());
        fragments.add(new PageFragment_neidi());
        fragments.add(new PageFragment_gangtai());

        pagerAdapter = new MyFragmentPagerAdapter1(getSupportFragmentManager(),this,fragments);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);    //绑定

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }
}
