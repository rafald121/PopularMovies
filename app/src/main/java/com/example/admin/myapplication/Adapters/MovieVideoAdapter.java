package com.example.admin.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.admin.myapplication.Interfaces.MovieVideoClickListener;
import com.example.admin.myapplication.Model.MovieVideo;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.NetworkHelper;

import java.util.List;
/**
 * Created by admin on 04.05.2017.
 */
public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.ViewHolderMovieVideo>{

    private List<MovieVideo> movieVideoList;
    private Context context;

    final private MovieVideoClickListener movieVideoClickListener;

    public MovieVideoAdapter(List<MovieVideo> movieVideoList, Context context, MovieVideoClickListener listener) {
        this.movieVideoList = movieVideoList;
        this.context = context;
        this.movieVideoClickListener = listener;
    }

    @Override
    public ViewHolderMovieVideo onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_movie_video, parent, false);
        ViewHolderMovieVideo viewHolderMovieVideo = new ViewHolderMovieVideo(view);

        return viewHolderMovieVideo;
    }


    @Override
    public void onBindViewHolder(ViewHolderMovieVideo holder, int position) {

        String YTkey = movieVideoList.get(position).getYTkey();

        holder.itemView.setTag(YTkey);

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movieVideoList.size();
    }

    public class ViewHolderMovieVideo extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView movieTitle;
        ImageView movieImage;
        View itemView;

        public ViewHolderMovieVideo(View itemView) {
            super(itemView);

            this.itemView = itemView;

            movieTitle = (TextView) itemView.findViewById(R.id.item_movie_video_title);
            movieImage = (ImageView) itemView.findViewById(R.id.item_movie_video_image);


            itemView.setOnClickListener(this);
        }

        public void bind(int position) {

            movieTitle.setText(movieVideoList.get(position).getYTtitle());

            String videoKey = movieVideoList.get(position).getYTkey();
            String movieImageUrl = NetworkHelper.getUriMovieVideoImage(videoKey).toString();

            Glide.with(context).load(movieImageUrl).crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(movieImage);

        }

        @Override
        public void onClick(View v) {
            String tag = v.getTag().toString();
            movieVideoClickListener.onClickMovieVideoListener(tag);
        }
    }
}
