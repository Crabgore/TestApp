package ru.crabgore.testapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;

import java.util.List;

import ru.crabgore.testapp.adapter.MyPagerAdapter;
import ru.crabgore.testapp.restModel.Cell;
import ru.crabgore.testapp.restModel.RestModel;
import ru.crabgore.testapp.restModel.Url;
import ru.crabgore.testapp.tools.AutoScrollViewPager;
import ru.crabgore.testapp.tools.TextureVideoView;

import static ru.crabgore.testapp.ResultBase.LOWER;
import static ru.crabgore.testapp.ResultBase.UPPER;
import static ru.crabgore.testapp.ResultBase.result;

public class MainActivity extends AppCompatActivity {
    private RestModel restModel;
    private AutoScrollViewPager upperPager;
    private AutoScrollViewPager lowerPager;

    private Cell upperCell;
    private Cell lowerCell;
    private List<Url> upperUrls;
    private List<Url> lowerUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        initRestModel();

        initLists();

        initPagers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause(UPPER);
        pause(LOWER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resume(UPPER);
        resume(LOWER);
    }

    private void initUI() {
        upperPager = findViewById(R.id.upper);
        lowerPager = findViewById(R.id.lower);
    }

    private void initRestModel() {
        restModel = new Gson().fromJson(result, RestModel.class);
    }

    private void initLists() {
        upperCell = restModel.getData().get(0).getCells().get(0);
        lowerCell = restModel.getData().get(0).getCells().get(1);
        upperUrls = upperCell.getUrls();
        lowerUrls = lowerCell.getUrls();
    }

    private void initPagers() {
        MyPagerAdapter upperAdapter = new MyPagerAdapter(upperUrls, this);
        MyPagerAdapter lowerAdapter = new MyPagerAdapter(lowerUrls, this);

        initPager(upperAdapter, upperPager, upperUrls, UPPER);
        initPager(lowerAdapter, lowerPager, lowerUrls, LOWER);
    }

    private void initPager(MyPagerAdapter adapter, AutoScrollViewPager pager, List<Url> urls, int id) {
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new MyPageListener(pager, urls, id));
        pager.setBorderAnimation(false);
        pager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
        pager.setCycle(false);
    }

    private void constructPager(AutoScrollViewPager pager, Cell cell, int direction) {
        pager.startAutoScroll();
        pager.setInterval(cell.getSliderInterval());
        pager.setDirection(direction);
        pager.setScrollDurationFactor(5);
    }

    public void pause(int id) {
        if (id == UPPER) upperPager.stopAutoScroll();
        else if (id == LOWER) lowerPager.stopAutoScroll();
    }

    public void resume(int id) {
        if (id == UPPER) {
            constructPager(upperPager, upperCell, AutoScrollViewPager.RIGHT);
        } else if (id == LOWER) {
            constructPager(lowerPager, lowerCell, AutoScrollViewPager.LEFT);
        }
    }

    public class MyPageListener implements ViewPager.OnPageChangeListener {
        private AutoScrollViewPager pager;
        private List<Url> list;
        private int id;

        MyPageListener(AutoScrollViewPager pager, List<Url> list, int id) {
            this.pager = pager;
            this.list = list;
            this.id = id;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (list.get(position).getMimeType().contains("video")){
                pause(id);
                View myView = pager.findViewWithTag(position);
                TextureVideoView videoView = myView.findViewById(R.id.video);
                videoView.play();
                videoView.setListener(new TextureVideoView.MediaPlayerListener() {
                    @Override
                    public void onVideoPrepared() {}

                    @Override
                    public void onVideoEnd() {
                        if (position == list.size()-1) {
                            pager.setCurrentItem(0);
                        } else
                            pager.setCurrentItem(pager.getCurrentItem()+1);
                    }
                });
            } else resume(id);
        }

        @Override
        public void onPageSelected(int position) {}

        @Override
        public void onPageScrollStateChanged(int state) {}
    }
}
