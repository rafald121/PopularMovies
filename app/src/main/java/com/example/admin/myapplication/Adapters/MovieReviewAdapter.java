package com.example.admin.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.myapplication.Interfaces.MovieReviewClickListener;
import com.example.admin.myapplication.Interfaces.MovieReviewSiteClickListener;
import com.example.admin.myapplication.Model.MovieReview;
import com.example.admin.myapplication.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by admin on 24.04.2017.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolderMovieReview>{

    private List<MovieReview> movieReviewsList;
    private Context context;
    private MovieReviewSiteClickListener listener;
    public MovieReviewAdapter(List<MovieReview> movieReviewsList, Context context, MovieReviewSiteClickListener listener) {
        this.movieReviewsList = movieReviewsList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolderMovieReview onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_movie_review, parent, false);
        ViewHolderMovieReview viewHolderMovieReview = new ViewHolderMovieReview(view);

        return viewHolderMovieReview;
    }

    @Override
    public void onBindViewHolder(ViewHolderMovieReview holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movieReviewsList.size();
    }

    public class ViewHolderMovieReview extends RecyclerView.ViewHolder implements View.OnClickListener{

        //TODO udacity review how to bind by butterknife?

        TextView author;
        TextView content;
        TextView url;
        View itemView;

        public ViewHolderMovieReview(View itemView) {
            super(itemView);
            this.itemView = itemView;

            author = (TextView) itemView.findViewById(R.id.item_movie_review_author);
            content = (TextView) itemView.findViewById(R.id.item_movie_review_content);
            url = (TextView) itemView.findViewById(R.id.item_movie_review_url);

            itemView.setOnClickListener(this);

        }

        public void bind(int position) {
            itemView.setTag(movieReviewsList.get(position).getIdFromDBMovie());

            author.setText(movieReviewsList.get(position).getAuthor());
            content.setText(movieReviewsList.get(position).getContent());
            url.setText(context.getResources().getString(R.string.review_read_more));
            url.setTag(movieReviewsList.get(position).getUrl());

//            itemView.setTag();

        }

        @Override
        public void onClick(View v) {
            String _url =  url.getTag().toString();
            listener.onReviewClicked(_url);
        }
    }
}
