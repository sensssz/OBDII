package cn.edu.nju.software.obdii.ui;

import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;

/**
 */
public class ClickableItemizedOverlay extends ItemizedOverlay<OverlayItem> {
    private OnItemClickedListener mOnItemClickedListener;

    public ClickableItemizedOverlay(Drawable drawable, MapView mapView) {
        super(drawable, mapView);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        mOnItemClickedListener = onItemClickedListener;
    }

    @Override
    public boolean onTap(int index) {
        if (mOnItemClickedListener != null) {
            mOnItemClickedListener.onItemClicked(index);
            return true;
        } else {
            return false;
        }
    }

    public interface OnItemClickedListener {
        public void onItemClicked(int index);
    }
}
