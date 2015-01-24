package be.driessprong.menu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import be.driessprong.menu.fragment.DayMenuFragment;

/**
 * Created by Simon Raes on 24/01/2015.
 */
public class MenuPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> content;
    private HashMap<Integer, Fragment> registeredFragments = new HashMap<>();

    public MenuPagerAdapter(FragmentManager fm, ArrayList<String> content){
        super(fm);
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Fragment getItem(int position) {
        return DayMenuFragment.newInstance(content.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        registeredFragments.remove(position);
    }

    public void resetListPositions(){
        for(Fragment frag : registeredFragments.values()){
            if (frag instanceof DayMenuFragment){
                ((DayMenuFragment) frag).resetScrollPosition();
            }
        }
    }

}
