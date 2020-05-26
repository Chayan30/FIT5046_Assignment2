package edu.monash.MovieMemoir.ui.movieSearch;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import edu.monash.MovieMemoir.R;
import edu.monash.MovieMemoir.ui.movieSearch.ItemAdapter;
import java.util.List;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, mRecyclerViewClickListener);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ItemAdapter itemAdapter = mList.get(position);
        ((ViewHolder) viewHolder).movie_name.setText(itemAdapter.getText());
        ((ViewHolder) viewHolder).rel_year.setText(itemAdapter.getText2());
        String url= itemAdapter.getImage();
        Picasso.get()
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .resize(200, 200)
                .centerInside()
                .into(((ViewHolder) viewHolder).mImg);
        //((ViewHolder) viewHolder).mImg.setImageDrawable(itemAdapter.getImage());
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView movie_name;
        public TextView rel_year;
        public ImageView mImg;
        RecyclerViewClickListener recyclerViewClickListener;
        public ViewHolder(View itemView, RecyclerViewClickListener recyclerViewClickListener) {
            super(itemView);
            movie_name = (TextView) itemView.findViewById(R.id.movie_name);
            mImg = (ImageView) itemView.findViewById(R.id.img_item);
            rel_year = (TextView) itemView.findViewById(R.id.release_date);
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