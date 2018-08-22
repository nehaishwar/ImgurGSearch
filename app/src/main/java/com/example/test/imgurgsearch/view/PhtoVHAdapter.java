package com.example.test.imgurgsearch.view;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.imgurgsearch.MainActivity;
import com.example.test.imgurgsearch.R;
import com.example.test.imgurgsearch.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhtoVHAdapter extends RecyclerView.Adapter<PhtoVHAdapter.PhotoVH>{

    List<Photo> photos = null;

    public static class PhotoVH extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView title;
        TextView imageCount;
        TextView postDate;

        public PhotoVH(View itemView) {
            super(itemView);
        }
    }

    @Override
    public PhotoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.item, parent, false);
        Log.d("IMGSearch","onCreateViewHolder");
        PhotoVH vh = new PhotoVH(v);
        vh.photo = (ImageView) vh.itemView.findViewById(R.id.photo);
        vh.title = (TextView) vh.itemView.findViewById(R.id.title);
        vh.postDate = (TextView) vh.itemView.findViewById(R.id.post_date);
        vh.imageCount = (TextView) vh.itemView.findViewById(R.id.image_count);
        return vh;
    }

    @Override
    public void onBindViewHolder(PhotoVH holder, int position) {

        Log.d("IMGSearch","onBindViewHolder");
        Log.d("IMGSearch","onBindViewHolder photos.size  getItemCount "+photos.size()+"---"+getItemCount());
        Picasso.with(holder.itemView.getContext()).load("https://i.imgur.com/" +
                photos.get(position).getId() + ".jpg").into(holder.photo);

        holder.title.setText(photos.get(position).getTitle());
        holder.postDate.setText(photos.get(position).getPostDate());
        holder.imageCount.setText(photos.get(position).getImageCount());

    }

    @Override
    public int getItemCount() {
        if(photos == null)
            return 0;
        else
            return photos.size();
    }

    public void setData(List<Photo> source){
        photos = source;
        //notifyDataSetChanged();
    }

    public List<Photo> getData(){
        return photos;
    }

}

