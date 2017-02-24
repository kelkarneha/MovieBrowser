package administrator.example.org.moviebrowser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 3/9/2016.
 */
public class MoviePosterViewHolder extends RecyclerView.ViewHolder{
    protected ImageView poster;

    public MoviePosterViewHolder(View view) {
        super(view);
        this.poster = (ImageView) view.findViewById(R.id.poster);
    }


}
