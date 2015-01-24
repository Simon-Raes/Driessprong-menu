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
import be.driessprong.menu.adapter.DayRecyclerAdapater;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Simon Raes on 24/01/2015.
 */
public class DayMenuFragment extends Fragment {

    private ArrayList<String> dayItems = new ArrayList<>();

    @InjectView(R.id.recycler_day)
    RecyclerView recyclerDay;

    public interface ScrollListener {
        public void onFragmentListScrolled(int dy);
    }

    private ScrollListener delegate;

    public static DayMenuFragment newInstance(String item) {


        DayMenuFragment f = new DayMenuFragment();

        Bundle args = new Bundle();
        args.putString("item", item);
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
        dayItems.add("menu item 1");
        dayItems.add("menu item 2");
        dayItems.add("menu item en 3");
        dayItems.add("menu item 1");
        dayItems.add("menu item 2");
        dayItems.add("menu item en 3");
        dayItems.add("menu item 1");
        dayItems.add("menu item 2");
        dayItems.add("menu item en 3");
        dayItems.add("menu item 1");
        dayItems.add("menu item 2");
        dayItems.add("menu item en 3");
        dayItems.add("menu item 1");
        dayItems.add("menu item 2");
        dayItems.add("menu item en 3");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_day_menu, container, false);
        ButterKnife.inject(this, view);

        DayRecyclerAdapater adapter = new DayRecyclerAdapater(dayItems);
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

}
