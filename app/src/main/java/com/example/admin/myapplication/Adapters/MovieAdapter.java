package com.example.admin.myapplication.Adapters;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.myapplication.R;

import org.w3c.dom.Text;

/**
 * Created by admin on 01.04.2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageButton thumbnail;

        public ViewHolder(View itemView) {

            super(itemView);
            thumbnail = (ImageButton) itemView.findViewById(R.id.movie_item_image);

        }
    }
}
