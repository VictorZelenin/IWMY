package com.jacksonsmolenko.iwmy.data_binders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jacksonsmolenko.iwmy.R;
import com.jacksonsmolenko.iwmy.adapters.DataBindAdapter;

import java.util.List;

/**
 * Created by victor on 15.07.16.
 */
public class PhotosBinder extends DataBinder<PhotosBinder.ViewHolder> {

    private List<String> photosList;

    public PhotosBinder(DataBindAdapter adapter, List<String> photosList) {
        super(adapter);
        this.photosList = photosList;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_event_photos, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        // set up photos
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // should be list of photos
        private ImageView photo1;
        private ImageView photo2;
        private ImageView photo3;
        private ImageView photo4;
        private ImageView photo5;

        public ViewHolder(View itemView) {
            super(itemView);

            photo1 = (ImageView) itemView.findViewById(R.id.photo_1);
            photo2 = (ImageView) itemView.findViewById(R.id.photo_2);
            photo3 = (ImageView) itemView.findViewById(R.id.photo_3);
            photo4 = (ImageView) itemView.findViewById(R.id.photo_4);
            photo5 = (ImageView) itemView.findViewById(R.id.photo_5);
        }
    }

    private void debugFillListMethod() {

    }
}
