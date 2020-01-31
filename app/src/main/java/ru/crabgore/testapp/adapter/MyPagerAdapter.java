package ru.crabgore.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import ru.crabgore.testapp.R;
import ru.crabgore.testapp.restModel.Url;
import ru.crabgore.testapp.tools.TextureVideoView;

public class MyPagerAdapter extends PagerAdapter {
    private List<Url> urls;
    private Context context;

    public MyPagerAdapter(List<Url> urls, Context context) {
        this.urls = urls;
        this.context = context;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(layoutInflater).inflate(R.layout.page_layout, container, false);
        ImageView imageView = view.findViewById(R.id.image);
        TextureVideoView videoView = view.findViewById(R.id.video);

        if (urls.get(position).getMimeType().contains("video")) {
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setDataSource(urls.get(position).getUrl());
            videoView.setScaleType(TextureVideoView.ScaleType.CENTER_CROP);
        } else if (urls.get(position).getMimeType().contains("image")){
            videoView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(urls.get(position).getUrl()).fit().centerCrop().into(imageView);
        }

        view.setTag(position);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
