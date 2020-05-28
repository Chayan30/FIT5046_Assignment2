package edu.monash.MovieMemoir;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;



public class MemoirListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MemoirItemAdapter> mList;
    private Context mContext;
    private RecyclerViewClickListener mRecyclerViewClickListener;
        public MemoirListAdapter(List<MemoirItemAdapter> list, Context context, RecyclerViewClickListener recyclerViewClickListener){
        super();
        mList = list;
        mContext = context;
        mRecyclerViewClickListener = recyclerViewClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memoir_list_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, mRecyclerViewClickListener);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        MemoirItemAdapter itemAdapter = mList.get(position);
        ((MemoirListAdapter.ViewHolder) viewHolder).movie_name.setText(itemAdapter.getMname());
        ((MemoirListAdapter.ViewHolder) viewHolder).rel_year.setText(itemAdapter.getRyear());
        ((ViewHolder) viewHolder).uaddDate.setText(itemAdapter.getUaddDate());
        ((ViewHolder) viewHolder).user_opinion.setText(itemAdapter.getUopinion());
        ((ViewHolder) viewHolder).genre.setText(itemAdapter.getGenre());
        ((ViewHolder) viewHolder).cinema_postcode.setText(itemAdapter.getCpostcode());
        ((ViewHolder) viewHolder).user_rating.setRating(itemAdapter.getUserRating());
        ((ViewHolder) viewHolder).public_rating.setRating(itemAdapter.getPublicRating());
        String url= itemAdapter.getImage();
        Picasso.get()
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .resize(200, 200)
                .centerInside()
                .into(((MemoirListAdapter.ViewHolder) viewHolder).poster);
                //((ViewHolder) viewHolder).movie_name.setTextColor(itemAdapter.isSelected() ? Color.WHITE : R.color.colorPrimary);
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView movie_name;
        public TextView rel_year;
        public TextView uaddDate;
        public TextView user_opinion;
        public TextView cinema_postcode;
        public TextView genre;
        public RatingBar user_rating;
        public RatingBar public_rating;
        public ImageView poster;
        RecyclerViewClickListener recyclerViewClickListener;
        public ViewHolder(View itemView, RecyclerViewClickListener recyclerViewClickListener) {
            super(itemView);
            movie_name = (TextView) itemView.findViewById(R.id.movie_name_info_text);
            uaddDate = (TextView) itemView.findViewById(R.id.user_watch_date_info_text);
            rel_year = (TextView) itemView.findViewById(R.id.release_date_info_text);
            user_opinion = (TextView) itemView.findViewById(R.id.user_opinion_info_text);
            cinema_postcode = (TextView) itemView.findViewById(R.id.cinema_info_text);
            user_rating = (RatingBar) itemView.findViewById(R.id.ratingBar_user_rating);
            public_rating = (RatingBar) itemView.findViewById(R.id.ratingBar_public_rating);
            poster = (ImageView) itemView.findViewById(R.id.movie_image_memoir);
            genre = (TextView) itemView.findViewById(R.id.genre_info_text);
            user_rating.setMax(5);
            user_rating.setStepSize((float)0.5);
            public_rating.setMax(5);
            public_rating.setStepSize((float)0.5);
            this.recyclerViewClickListener = recyclerViewClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewClickListener.onItemClick(getAdapterPosition());
        }
    }
    public interface RecyclerViewClickListener {
        void onItemClick(int item);
    }
    public void clear() {
        int size = mList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mList.remove(0);
            }

            notifyItemRangeRemoved(0,size);
        }
    }
}