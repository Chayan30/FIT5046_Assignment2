package edu.monash.MovieMemoir.ui.watchlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.monash.MovieMemoir.R;
import edu.monash.MovieMemoir.ui.movieSearch.ItemAdapter;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ItemAdapter> mList;
    private Context mContext;
    private RecyclerViewClickListener mRecyclerViewClickListener;
        public ListAdapter(List<ItemAdapter> list, Context context, RecyclerViewClickListener recyclerViewClickListener){
        super();
        mList = list;
        mContext = context;
        mRecyclerViewClickListener = recyclerViewClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_watchlist, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, mRecyclerViewClickListener);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        final ItemAdapter itemAdapter = mList.get(position);
        ((ViewHolder) viewHolder).movie_name.setText(itemAdapter.getText());
        ((ViewHolder) viewHolder).rel_year.setText(itemAdapter.getText2());
        ((ViewHolder) viewHolder).uaddDate.setText(itemAdapter.getImage());
        ((ViewHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                itemAdapter.setSelected(!itemAdapter.isSelected());
                viewHolder.itemView.setBackgroundColor(itemAdapter.isSelected() ? R.color.colorPrimary : Color.WHITE);
                //((ViewHolder) viewHolder).movie_name.setTextColor(itemAdapter.isSelected() ? Color.WHITE : R.color.colorPrimary);
            }
        });
        //((ViewHolder) viewHolder).mImg.setImageDrawable(itemAdapter.getImage());
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView movie_name;
        public TextView rel_year;
        public TextView uaddDate;
        RecyclerViewClickListener recyclerViewClickListener;
        public ViewHolder(View itemView, RecyclerViewClickListener recyclerViewClickListener) {
            super(itemView);
            movie_name = (TextView) itemView.findViewById(R.id.Movie_name_watch);
            uaddDate = (TextView) itemView.findViewById(R.id.UserAddDate_watch);
            rel_year = (TextView) itemView.findViewById(R.id.Rel_date_watch);
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