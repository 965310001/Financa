package tech.com.commoncore.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Desc:
 */
public class SpaceItemDecorationListHo extends RecyclerView.ItemDecoration {
    int mSpace;
    int rows;

    /**
     * Retrieve any offsets for the given item. Each field of <code>outRect</code> specifies
     * the number of pixels that the item view should be inset by, similar to padding or margin.
     * The default implementation sets the bounds of outRect to 0 and returns.
     * <p>
     * <p>
     * If this ItemDecoration does not affect the positioning of item views, it should set
     * all four fields of <code>outRect</code> (left, top, right, bottom) to zero
     * before returning.
     * <p>
     * <p>
     * If you need to access Adapter for additional data, you can call
     * {@link RecyclerView#getChildAdapterPosition(View)} to get the adapter position of the
     * View.
     *
     * @param outRect Rect to receive the output.
     * @param view    The child view to decorate
     * @param parent  RecyclerView this ItemDecoration is decorating
     * @param state   The current state of RecyclerView.
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//        outRect.left = mSpace;
//        outRect.right = mSpace;
//        outRect.bottom = mSpace;
//        if (parent.getChildAdapterPosition(view) == 0) {
//            outRect.top = mSpace;
//        }
        //不是第一个的格子都设一个左边和底部的间距
        if(parent.getChildAdapterPosition(view) != 0){
            outRect.left = mSpace;
        }
//        //由于每行都只有rows个，所以第一个都是rows的倍数，把左边距设为0
//        if (parent.getChildLayoutPosition(view) %rows==0) {
//            outRect.left = 0;
//        }
    }

    public SpaceItemDecorationListHo(int space) {
        this.mSpace = space;
    }
}