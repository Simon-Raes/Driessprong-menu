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
    private float headerAnimationStartLocation;

    // Start moving from this location towards the location of the tabs on the next page. Next page
    // will always have the tabs at the same location because list position is reset.
    private float tabsAnimationStartLocation;

    // Store the location where the tabs WOULD be if they were not capped at the top of the screen.
    // This is used to make them move back down at the correct point when scrolling back up.
    private float tabsCalculationLocation;

    // Remember if the tab bar had a background color. Used when starting a swipe to a different page
    // (backgroundcolor goes away), but then cancelling that and going back to the previous page
    // (bar gets its color back).
    private boolean barColoredBeforeSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setupPlaceholderLists();

        pagerAdapter = new MenuPagerAdapter(getSupportFragmentManager(), pagerItems);
        viewPagerDays.setAdapter(pagerAdapter);

        tabs.setViewPager(viewPagerDays);
        // Listener needs to be set on the tabs, not on the viewpager itself. (Only when using tabs)
        tabs.setOnPageChangeListener(this);

        tabsAnimationStartLocation = getTabsDefaultLocation();
        updateUi();
    }


    private void updateUi() {

        setBackGroundColor();
        setImageDrawableWithFade(imgHeader, headerImages.get(viewPagerDays.getCurrentItem()));

    }

    /**
     * Set the background color based on the active photo.
     */
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
                    //noinspection deprecation
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

        // init tabs calc location
        if (tabsCalculationLocation == 0) {
            tabsCalculationLocation = tabs.getY();
        }

        // update real calculation location
        tabsCalculationLocation = tabsCalculationLocation - dy;


        if (dy > 0) {
            if (tabsCalculationLocation < 0) {
                // Cap the tabs at the top of the screen
                tabs.setY(0);
                setTabsBackGroundVisible(true);
                barColoredBeforeSwipe = true;
            } else {
                tabs.setY(tabsCalculationLocation);
                setTabsBackGroundVisible(false);
                barColoredBeforeSwipe = false;
            }
        } else {
            if (tabsCalculationLocation >= tabs.getY()) {
                // Move tabs back down if the calculation location is lower than the visual loc.
                tabs.setY(tabsCalculationLocation);
                setTabsBackGroundVisible(false);
                barColoredBeforeSwipe = false;
            }
        }


        // Update image position
        imgHeader.setY(imgHeader.getY() - dy / 2);

        // Rounding errors (?) can make the image scroll down a bit too far, fix that here.
        if (imgHeader.getY() > 0) {
            imgHeader.setY(0);
        }

        updateHeaderAlpha();

        // Save the current location of the header.
        headerOriginalImagePosition = imgHeader.getY();
        headerAnimationStartLocation = imgHeader.getY();
        // Same for tabs.
        tabsAnimationStartLocation = tabs.getY();

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
                headerAnimationStartLocation = imgHeader.getY();
            }
            imgHeader.setY(headerAnimationStartLocation + (adjustedOffset * (0 - headerAnimationStartLocation)));

            updateHeaderAlpha();

            // Tabs
            float moveTabsTo = tabsAnimationStartLocation + (adjustedOffset * (getTabsDefaultLocation() - tabsAnimationStartLocation));
            tabs.setY(moveTabsTo);
            // Remove background color when scrolling the tabs back down.
            setTabsBackGroundVisible(false);
        }
    }

    private void updateHeaderAlpha() {
        // Update image alpha
        float alpha = 1 - (-imgHeader.getY() / imgHeader.getMeasuredHeight());
        imgHeader.setAlpha(Math.max(0, alpha));
    }


    /**
     * Called when a new day is selected.
     */
    @Override
    public void onPageSelected(int position) {
        updateUi();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (viewPagerDays.getCurrentItem() != selectedPage && state == ViewPager.SCROLL_STATE_IDLE) {
            pagerAdapter.resetListPositions();
            imgHeader.setY(0);
            selectedPage = viewPagerDays.getCurrentItem();
            headerAnimationStartLocation = 0;
            headerOriginalImagePosition = 0;

            tabsAnimationStartLocation = tabs.getY(); // =tabsdefaultstartlocation??
            tabsCalculationLocation = tabs.getY();

        } else if (state == ViewPager.SCROLL_STATE_IDLE) {
            // reset imgheader position to its position before the pagescroll movement started.
            // If the image was very far off-screen then it was set to -img.height at the start of
            // the animation to create a smooth swipe animation to the next page.
            // If the user does not select the next page then the image should be set back to its
            // original offscreen position.
            imgHeader.setY(headerOriginalImagePosition);
            if(barColoredBeforeSwipe){
                setTabsBackGroundVisible(true);
            }
        }
    }

    private void setTabsBackGroundVisible(boolean visible) {

        // todo: quick animation from visible to transparent
        if (visible) {
            tabs.setBackgroundColor(getResources().getColor(R.color.tabbar));
        } else {
            tabs.setBackgroundColor(getResources().getColor(R.color.transparent));
        }




        /*if (makeVisible && !tabBarCurrentlyColored) {
            ColorDrawable[] colors = new ColorDrawable[2];

            colors[0] = new ColorDrawable(getResources().getColor(R.color.transparent));
            colors[1] = new ColorDrawable(getResources().getColor(R.color.tabbar));

            TransitionDrawable colorTrans = new TransitionDrawable(colors);
            colorTrans.setCrossFadeEnabled(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tabs.setBackground(colorTrans);
            } else {
                //noinspection deprecation
                tabs.setBackgroundDrawable(colorTrans);
            }

            colorTrans.startTransition(250);
            tabBarCurrentlyColored = true;
        } else if(!makeVisible && tabBarCurrentlyColored) {
            ColorDrawable[] colors = new ColorDrawable[2];

            colors[0] = new ColorDrawable(getResources().getColor(R.color.tabbar));
            colors[1] = new ColorDrawable(getResources().getColor(R.color.transparent));

            TransitionDrawable colorTrans = new TransitionDrawable(colors);
            colorTrans.setCrossFadeEnabled(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tabs.setBackground(colorTrans);
            } else {
                //noinspection deprecation
                tabs.setBackgroundDrawable(colorTrans);
            }

            colorTrans.startTransition(250);
            tabBarCurrentlyColored =false;
        }*/
    }


    private float getTabsDefaultLocation() {
        return getResources().getDimension(R.dimen.tabs_offset);
    }

    private void setupPlaceholderLists() {
        pagerItems.add("Maandag");
        pagerItems.add("Dinsdag");
        pagerItems.add("Woensdag");

        headerImages.add(getResources().getDrawable(R.drawable.pasta));
        headerImages.add(getResources().getDrawable(R.drawable.pizza));
        headerImages.add(getResources().getDrawable(R.drawable.spaghetti));
    }
}
