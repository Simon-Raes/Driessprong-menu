package be.driessprong.menu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import be.driessprong.menu.R;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Simon Raes on 24/01/2015.
 */
public class DayRecyclerAdapater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CONTENT = 1;

    private ArrayList<String> items;

    public DayRecyclerAdapater(ArrayList<String> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_CONTENT;
    }


    // ViewHolder

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {

        View v;

        if (type == TYPE_CONTENT) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_day_list, viewGroup, false);
            return new ContentViewHolder(v);
        } else if (type == TYPE_HEADER) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_day_list_header, viewGroup, false);
            return new HeaderViewHolder(v);
        } else {
            // exception
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HeaderViewHolder) {
            // empty header doesn't have anything to bind
        } else if (viewHolder instanceof ContentViewHolder) {
            ((ContentViewHolder) viewHolder).txtItem.setText(items.get(i-1));
        }
    }


    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.textView_item_title)
        TextView txtItem;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);

        }
    }

}
