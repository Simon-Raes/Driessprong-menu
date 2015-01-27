package be.driessprong.menu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import be.driessprong.menu.R;
import be.driessprong.menu.model.Ingredient;
import be.driessprong.menu.model.MainCourse;
import be.driessprong.menu.model.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Simon Raes on 24/01/2015.
 */
public class MenuRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CONTENT = 1;

    // todo: use day object instead of string
    private ArrayList<MenuItem> items;

    public MenuRecyclerAdapter(ArrayList<MenuItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
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
            MenuItem menuItem = items.get(i - 1);

            // Dish name
            ((ContentViewHolder) viewHolder).txtItem.setText(menuItem.getName());
            // Dish photo
            Picasso.with(((ContentViewHolder) viewHolder).imgDish.getContext()).load(menuItem.getPhoto()).into(((ContentViewHolder) viewHolder).imgDish);
            // Dish ingredients
            if (menuItem instanceof MainCourse) {
                if (((MainCourse) menuItem).getIngredients() != null) {
                    ArrayList<Ingredient> ingredients = ((MainCourse) menuItem).getIngredients();
                    StringBuilder result = new StringBuilder();

                    String newline = "";
                    for (Ingredient ingredient : ingredients) {
                        result.append(newline).append(ingredient.getName());
                        newline = "\n";
                    }
                    ((ContentViewHolder) viewHolder).txtContent.setText(result);

                }
            }
        }
    }


    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.textView_item_title)
        TextView txtItem;
        @InjectView(R.id.textView_item_content)
        TextView txtContent;
        @InjectView(R.id.imageView_dish)
        ImageView imgDish;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
            // Empty view
        }
    }

}
