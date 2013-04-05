package com.agarwal.routepath;
/**
 * @author Agarwal
 *
 */
import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem> {

    private final ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();


    public CustomItemizedOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
    }

    @Override
    protected OverlayItem createItem(int i) {
        return mapOverlays.get(i);
    }

    @Override
    public int size() {
        return mapOverlays.size();
    }

    public void addOverlay(OverlayItem overlay) {
        mapOverlays.add(overlay);
        this.populate();
    }

}