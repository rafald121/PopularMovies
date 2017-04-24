package com.example.admin.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.admin.myapplication.Interfaces.RecyclerItemClickListener;
import com.example.admin.myapplication.Model.Movie;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.NetworkHelper;


import java.util.List;

/**
 * Created by admin on 01.04.2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolderMovie>{

    private static final String TAG = ViewHolderMovie.class.getSimpleName();
    private List<Movie> movieList;
    private Context context;

    final private RecyclerItemClickListener mOnClickListener;

    public MovieAdapter(Context context, List<Movie> movieList, RecyclerItemClickListener listener) {
        this.movieList = movieList;
        this.context = context;
        this.mOnClickListener = listener;
    }

    @Override
    public ViewHolderMovie onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_movie, parent, false);
        ViewHolderMovie movieViewHolderMovie = new ViewHolderMovie(view);

        return movieViewHolderMovie;
    }

    @Override
    public void onBindViewHolder(ViewHolderMovie holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    //todo try to move it \/ to single class
    public class ViewHolderMovie extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private ImageView thumbnail;

        public ViewHolderMovie(View itemView) {

            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.movie_item_image);
            thumbnail.setOnClickListener(this);

        }

        public void bind(int position){

            String thumnbailImage = NetworkHelper.URL_PICTURE_BASE + movieList.get(position).getMoviePoster();

            Glide.with(context).load(thumnbailImage)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(thumbnail);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
