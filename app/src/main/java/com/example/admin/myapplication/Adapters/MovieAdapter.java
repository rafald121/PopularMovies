package com.example.admin.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.admin.myapplication.Model.Movie;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.NetworkHelper;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by admin on 01.04.2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private static final String TAG = MovieAdapter.ViewHolder.class.getSimpleName();
    private List<Movie> movieList;
    private Context context;

    final private RecyclerItemClickListener mOnClickListener;

    public interface RecyclerItemClickListener{
        void onListItemClick(int cickedItemIndex);
    }

    public MovieAdapter(Context context, List<Movie> movieList, RecyclerItemClickListener listener) {
        this.movieList = movieList;
        this.context = context;
        this.mOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        ViewHolder movieViewHolder = new ViewHolder(view);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private ImageButton thumbnail;

        public ViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageButton) itemView.findViewById(R.id.movie_item_image);

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
            Log.i(TAG, "onClick: " + movieList.get(getAdapterPosition()).getTitle());

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
