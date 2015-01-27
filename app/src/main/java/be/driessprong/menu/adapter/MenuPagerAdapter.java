package be.driessprong.menu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import be.driessprong.menu.fragment.DayMenuFragment;
import be.driessprong.menu.model.Day;
import be.driessprong.menu.util.DateUtils;

/**
 * Created by Simon Raes on 24/01/2015.
 * Adapter for the ViewPager that holds the different days.
 */
public class MenuPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Day> content;
    private HashMap<Integer, Fragment> registeredFragments = new HashMap<>();

    public MenuPagerAdapter(FragmentManager fm, ArrayList<Day> content){
        super(fm);
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return DateUtils.getFormattedDayTitle(content.get(position).getDate());
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
