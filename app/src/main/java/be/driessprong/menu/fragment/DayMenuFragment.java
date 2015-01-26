package be.driessprong.menu.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import be.driessprong.menu.R;
import be.driessprong.menu.adapter.MenuRecyclerAdapter;
import be.driessprong.menu.model.Day;
import be.driessprong.menu.model.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Simon Raes on 24/01/2015.
 */
public class DayMenuFragment extends Fragment {

    private ArrayList<String> dayItems = new ArrayList<>();
    private Day day;

    @InjectView(R.id.recycler_day)
    RecyclerView recyclerDay;

    private ScrollListener delegate;
    public interface ScrollListener {
        public void onFragmentListScrolled(int dy);
    }

    public static DayMenuFragment newInstance(Day item) {
        DayMenuFragment f = new DayMenuFragment();
        Bundle args = new Bundle();
        args.putParcelable("day", item);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ScrollListener) {
            this.delegate = (ScrollListener) activity;
        } else {
            throw new ClassCastException("Activity must implement ScrollListener interface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.day = getArguments().getParcelable("day");

        dayItems.add(day.getSoup().getName());
        dayItems.add(day.getSoup().getName());
        dayItems.add(day.getSoup().getName());
        dayItems.add(day.getSoup().getName());
        dayItems.add(day.getSoup().getName());

        System.out.println(dayItems.get(0));

        dayItems.add(day.getMainCourse().getName());
        System.out.println(dayItems.get(5));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_day_menu, container, false);
        ButterKnife.inject(this, view);
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(day.getSoup());
        menuItems.add(day.getMainCourse());
        MenuRecyclerAdapter adapter = new MenuRecyclerAdapter(menuItems);
        recyclerDay.setAdapter(adapter);
        recyclerDay.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerDay.setOnScrollListener(new RecyclerScrollListener());

        return view;

    }

    public class RecyclerScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            delegate.onFragmentListScrolled(dy);
        }
    }

    public void resetScrollPosition(){
        ((LinearLayoutManager)recyclerDay.getLayoutManager()).scrollToPosition(0);
    }

    public void updateListContent(){
        recyclerDay.getAdapter().notifyDataSetChanged();
    }

}
