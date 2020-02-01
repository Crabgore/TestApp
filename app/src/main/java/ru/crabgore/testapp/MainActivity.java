package ru.crabgore.testapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;

import java.util.List;

import ru.crabgore.testapp.restModel.Cell;
import ru.crabgore.testapp.restModel.RestModel;
import ru.crabgore.testapp.restModel.Url;
import ru.crabgore.testapp.adapter.PagerAdapter;
import ru.crabgore.testapp.tools.TextureVideoView;

import static ru.crabgore.testapp.ResultBase.LOWER;
import static ru.crabgore.testapp.ResultBase.UPPER;
import static ru.crabgore.testapp.ResultBase.result;

public class MainActivity extends AppCompatActivity {
    private RestModel restModel;
    private ViewPager upperPager;
    private ViewPager lowerPager;

    private Handler handler = new Handler();

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
        remove(UPPER);
        remove(LOWER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cont(UPPER);
        cont(LOWER);
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
        PagerAdapter upperAdapter = new PagerAdapter(this, upperUrls).setInfiniteLoop(true);
        PagerAdapter lowerAdapter = new PagerAdapter(this, lowerUrls).setInfiniteLoop(true);

        initPager(upperAdapter, upperPager, upperUrls, UPPER);
        initPager(lowerAdapter, lowerPager, lowerUrls, LOWER);
    }

    private void initPager(PagerAdapter adapter, ViewPager pager, List<Url> urls, int id) {
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new MyPageListener(pager, urls, id));
        pager.setPageTransformer(true, new FadeOutTransformation());
    }

    private void start(int id) {
        if (id == UPPER) handler.postDelayed(upperSliderRunnable, upperCell.getSliderInterval());
        else if (id == LOWER) handler.postDelayed(lowerSliderRunnable, lowerCell.getSliderInterval());
    }

    private void cont(int id) {
        if (id == UPPER) handler.post(upperSliderRunnable);
        else if (id == LOWER) handler.post(lowerSliderRunnable);
    }

    private void remove(int id) {
        if (id == UPPER) handler.removeCallbacks(upperSliderRunnable);
        else if (id == LOWER) handler.removeCallbacks(lowerSliderRunnable);
    }

    private Runnable upperSliderRunnable = new Runnable() {
        @Override
        public void run() {
            upperPager.setCurrentItem(upperPager.getCurrentItem() + 1);
        }
    };

    private Runnable lowerSliderRunnable = new Runnable() {
        @Override
        public void run() {
            lowerPager.setCurrentItem(lowerPager.getCurrentItem() + 1);
        }
    };

    public class MyPageListener implements ViewPager.OnPageChangeListener {
        private ViewPager pager;
        private List<Url> list;
        private int id;

        MyPageListener(ViewPager pager, List<Url> list, int id) {
            this.pager = pager;
            this.list = list;
            this.id = id;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            remove(id);
            if (list.get(pager.getCurrentItem() % list.size()).getMimeType().contains("video")){
                View myView = pager.findViewWithTag(position);
                if (myView != null) {
                    TextureVideoView videoView = myView.findViewById(R.id.video);
                    videoView.play();
                    videoView.setListener(new TextureVideoView.MediaPlayerListener() {
                        @Override
                        public void onVideoPrepared() {
                        }

                        @Override
                        public void onVideoEnd() {
                            cont(id);
                        }
                    });
                } else cont(id);
            } else {
                start(id);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    }

    public class FadeOutTransformation implements ViewPager.PageTransformer{
        @Override
        public void transformPage(View page, float position) {
            page.setTranslationX(-position*page.getWidth());
            page.setAlpha(1-Math.abs(position));
        }
    }
}
