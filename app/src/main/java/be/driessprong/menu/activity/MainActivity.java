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
    private float originalHeaderImagePosition;

    // Start moving from this location towards the location of the image on the next page. Next page
    // will always have the image at 0 because the list position is reset when changing page.
    private float positionPreStartScroll;

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

        headerImages.add(getResources().getDrawable(R.drawable.pizza));
        headerImages.add(getResources().getDrawable(R.drawable.pasta));
        headerImages.add(getResources().getDrawable(R.drawable.pizza));

        pagerAdapter = new MenuPagerAdapter(getSupportFragmentManager(), pagerItems);
        viewPagerDays.setAdapter(pagerAdapter);
        viewPagerDays.setOnPageChangeListener(this);

        updateUi();
    }


    private void updateUi() {

        generatePaletteForImage();
        setImageDrawableWithFade(imgHeader, headerImages.get(viewPagerDays.getCurrentItem()));

    }

    private void generatePaletteForImage() {
        Bitmap activePhoto = ((BitmapDrawable) headerImages.get(viewPagerDays.getCurrentItem())).getBitmap();
        Palette.generateAsync(activePhoto, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {

                ColorDrawable[] colors = new ColorDrawable[2];
                ColorDrawable newColor = new ColorDrawable(palette.getMutedColor(000000));
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

        imgHeader.setY(imgHeader.getY() - dy / 2);

        // Rounding errors (?) can make the image scroll down too far, fix that here.
        if (imgHeader.getY() > 0) {
            imgHeader.setY(0);
        }

        originalHeaderImagePosition = imgHeader.getY();
        positionPreStartScroll = imgHeader.getY();
    }

    /**
     * Called when moving between days.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (positionOffset > 0) {

            if (imgHeader.getY() < -imgHeader.getMeasuredHeight()) {
                imgHeader.setY(-imgHeader.getMeasuredHeight());
                positionPreStartScroll = imgHeader.getY();
            }

            // Need to reverse the direction when scrolling to the left.
            float adjustedOffset = 0;
            if (position == selectedPage) {
                // going right
                adjustedOffset = positionOffset;
            } else if (position == selectedPage - 1) {
                // going left
                adjustedOffset = 1 - positionOffset;
            }

            imgHeader.setY(positionPreStartScroll + (adjustedOffset * -positionPreStartScroll));
        }
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
            positionPreStartScroll = 0;
            originalHeaderImagePosition = 0;
        } else if (state == ViewPager.SCROLL_STATE_IDLE) {
            // reset imgheader position to its position before the scroll movement started
            // it was set to -img.height to create a smooth swipe animation to the next day,
            // but this will create problems if the user does not select the new day/page.
            imgHeader.setY(originalHeaderImagePosition);
        }
    }
}
