package com.redrock.liye.text_redrock.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.redrock.liye.text_redrock.R;

import java.util.List;

/**
 * Created by a on 2016/5/14.
 */
public class MyFragmentPagerAdapter1 extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"欧美榜单", "内地榜单", "港台榜单"};
    private int[] imageResId = {R.drawable.ic_logo,
            R.drawable.ic_logo,
            R.drawable.ic_logo};
    private Context context;
    private List<Fragment> fragments;


    public MyFragmentPagerAdapter1(FragmentManager fm, Context context,List<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 2 :
                fragment = PageFragment_gangtai.newInstance(position + 1);break;
            case 1 :
                fragment = PageFragment_neidi.newInstance(position + 1);break;
            case 0 :
                fragment = PageFragment_oumei.newInstance(position+1);break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //第一次的代码
        //return tabTitles[position];
        //第二次的代码

        Drawable image = context.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
    public View getTabView(int position){

        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(tabTitles[position]);
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(imageResId[position]);
        return view;
    }

}