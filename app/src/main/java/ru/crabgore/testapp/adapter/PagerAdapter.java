package ru.crabgore.testapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import ru.crabgore.testapp.R;
import ru.crabgore.testapp.restModel.Url;
import ru.crabgore.testapp.tools.RecyclingPagerAdapter;
import ru.crabgore.testapp.tools.TextureVideoView;

public class PagerAdapter extends RecyclingPagerAdapter {
    private Context context;
    private List<Url> urls;
    private int size;
    private boolean isInfiniteLoop;

    public PagerAdapter(Context context, List<Url> urls) {
        this.context = context;
        this.urls = urls;
        this.size = urls.size();
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        return isInfiniteLoop ? Integer.MAX_VALUE : urls.size();
    }

    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup container) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = Objects.requireNonNull(layoutInflater).inflate(R.layout.page_layout, container, false);
        ImageView imageView = view.findViewById(R.id.image);
        TextureVideoView videoView = view.findViewById(R.id.video);

        if (urls.get(getPosition(position)).getMimeType().contains("video")) {
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setDataSource(urls.get(getPosition(position)).getUrl());
            videoView.setScaleType(TextureVideoView.ScaleType.CENTER_CROP);
        } else if (urls.get(getPosition(position)).getMimeType().contains("image")){
            videoView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(urls.get(getPosition(position)).getUrl()).fit().centerCrop().into(imageView);
        }

        view.setTag(position);
        return view;
    }

    public PagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}