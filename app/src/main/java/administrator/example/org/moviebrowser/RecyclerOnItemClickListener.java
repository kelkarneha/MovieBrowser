package administrator.example.org.moviebrowser;

import android.view.View;

/**
 * Created by Administrator on 3/14/2016.
 */
public interface RecyclerOnItemClickListener {
    public void onItemClick(View view, int position);
}

    //implements RecyclerView.OnItemTouchListener
    /*private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    public static interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick (View view, int position);
    }

    public RecyclerOnItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildPosition(childView));
            return true;
        }
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }*/

