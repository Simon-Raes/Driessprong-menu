package be.driessprong.menu.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import be.driessprong.menu.R;
import be.driessprong.menu.adapter.MenuPagerAdapter;
import be.driessprong.menu.fragment.DayMenuFragment;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity implements DayMenuFragment.ScrollListener, ViewPager.OnPageChangeListener {




    // TODO: ideas
    // Star rating the food




    @InjectView(R.id.imageView_header)
    ImageView imgHeader;
    @InjectView(R.id.viewPager_main)
    ViewPager viewPagerDays;
    @InjectView(R.id.layout_main)
    FrameLayout layMain;
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabs;
//    @InjectView(R.id.toolBar_main)
//    Toolbar toolbar;

    private ColorDrawable previousColor = new ColorDrawable(0);

    private ArrayList<String> pagerItems = new ArrayList<String>();
    private ArrayList<Drawable> headerImages = new ArrayList<>();

    MenuPagerAdapter pagerAdapter;
    private int selectedPage;

    // Store the off-screen location of the header image. This is only used when starting a swipe to
    // the next screen and then cancelling that action so the pager scrolls back to the previous
    // page. This value then sets the image back to the correct position.
    private float headerOriginalImagePosition;

    // Start moving from this location towards the location of the image on the next page. Next page
    // will always have the image at 0 because the list position is reset when changing page.
    private float headerPositionPreScroll;

    // Start moving from this location towards the location of the tabs on the next page. Next page
    // will always have the tabs at the same location because list position is reset.
    private float tabsStartingLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("test");

        pagerItems.add("ok");
        pagerItems.add("cool");
        pagerItems.add("nice");

        headerImages.add(getResources().getDrawable(R.drawable.pasta));
        headerImages.add(getResources().getDrawable(R.drawable.pizza));
        headerImages.add(getResources().getDrawable(R.drawable.spaghetti));

        pagerAdapter = new MenuPagerAdapter(getSupportFragmentManager(), pagerItems);
        viewPagerDays.setAdapter(pagerAdapter);

        tabs.setViewPager(viewPagerDays);
        // Listener needs to be set on the tabs, not on the viewpager itself. (Only when using tabs)
        tabs.setOnPageChangeListener(this);

        tabsStartingLocation = getTabsDefaultLocation();
        updateUi();
    }

    private void updateUi() {

        setBackGroundColor();
        setImageDrawableWithFade(imgHeader, headerImages.get(viewPagerDays.getCurrentItem()));

    }

    /**Set the background color based on the active photo.*/
    private void setBackGroundColor() {
        Bitmap activePhoto = ((BitmapDrawable) headerImages.get(viewPagerDays.getCurrentItem())).getBitmap();
        Palette.generateAsync(activePhoto, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {

                ColorDrawable[] colors = new ColorDrawable[2];
                ColorDrawable newColor = new ColorDrawable(palette.getMutedColor(0));
                colors[0] = previousColor;
                colors[1] = newColor;

                TransitionDrawable colorTrans = new TransitionDrawable(colors);
                colorTrans.setCrossFadeEnabled(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layMain.setBackground(colorTrans);
                } else {
                    layMain.setBackgroundDrawable(colorTrans);
                }

                colorTrans.startTransition(250);

                previousColor = newColor;
            }
        });
    }


    public static void setImageDrawableWithFade(final ImageView imageView, final Drawable drawable) {
        Drawable currentDrawable = imageView.getDrawable();
        if ((currentDrawable != null) && (currentDrawable instanceof TransitionDrawable)) {
            currentDrawable = ((TransitionDrawable) currentDrawable).getDrawable(1);
        }

        if (currentDrawable != null) {
            Drawable[] arrayDrawable = new Drawable[2];
            arrayDrawable[0] = currentDrawable;
            arrayDrawable[1] = drawable;
            TransitionDrawable transitionDrawable = new TransitionDrawable(arrayDrawable);
            transitionDrawable.setCrossFadeEnabled(true);
            imageView.setImageDrawable(transitionDrawable);
            transitionDrawable.startTransition(250);
        } else {
            imageView.setImageDrawable(drawable);
        }
    }

    /**
     * Called when a menu listview is scrolled.
     */
    @Override
    public void onFragmentListScrolled(int dy) {

        // Update tab position
        tabs.setY(tabs.getY() - dy);

        // Update image position
        imgHeader.setY(imgHeader.getY() - dy / 2);

        // Rounding errors (?) can make the image scroll down a bit too far, fix that here.
        if (imgHeader.getY() > 0) {
            imgHeader.setY(0);
        }

        updateHeaderAlpha();

        // Save the current location of the header.
        headerOriginalImagePosition = imgHeader.getY();
        headerPositionPreScroll = imgHeader.getY();
        // Same for tabs.
        tabsStartingLocation = tabs.getY();
    }

    /**
     * Called when moving between day pages.
     * Return the header and tabs back to their starting positions while moving.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (positionOffset > 0) {

            // Need to reverse the direction when scrolling to the left.
            float adjustedOffset = 0;
            if (position == selectedPage) {
                // going right
                adjustedOffset = positionOffset;
            } else if (position == selectedPage - 1) {
                // going left
                adjustedOffset = 1 - positionOffset;
            }

            // Header
            // Set the header to be just above the screen so it doesn't have to animate from 300km away.
            if (imgHeader.getY() < -imgHeader.getMeasuredHeight()) {
                imgHeader.setY(-imgHeader.getMeasuredHeight());
                headerPositionPreScroll = imgHeader.getY();
            }
            imgHeader.setY(headerPositionPreScroll + (adjustedOffset * (0 - headerPositionPreScroll)));

            updateHeaderAlpha();


            // Tabs
            float moveTabsTo = tabsStartingLocation + (adjustedOffset * (getTabsDefaultLocation() - tabsStartingLocation));
            tabs.setY(moveTabsTo);
        }
    }

    private void updateHeaderAlpha(){
        // Update image alpha
        float alpha = 1 - (-imgHeader.getY() / imgHeader.getMeasuredHeight());
        imgHeader.setAlpha(Math.max(0, alpha));
    }

    /**
     * Called when a new day is selected.
     */
    @Override
    public void onPageSelected(int position) {
        System.out.println("page selected");

        updateUi();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (viewPagerDays.getCurrentItem() != selectedPage && state == ViewPager.SCROLL_STATE_IDLE) {
            pagerAdapter.resetListPositions();
            imgHeader.setY(0);
            selectedPage = viewPagerDays.getCurrentItem();
            headerPositionPreScroll = 0;
            headerOriginalImagePosition = 0;

            tabsStartingLocation = tabs.getY();

        } else if (state == ViewPager.SCROLL_STATE_IDLE) {
            // reset imgheader position to its position before the pagescroll movement started.
            // If the image was very far off-screen then it was set to -img.height at the start of
            // the animation to create a smooth swipe animation to the next page.
            // If the user does not select the next page then the image should be set back to its
            // original offscreen position.
            imgHeader.setY(headerOriginalImagePosition);
        }
    }


    private float getTabsDefaultLocation(){
        float start = getResources().getDimension(R.dimen.tabs_offset);
        return start;
    }
}
