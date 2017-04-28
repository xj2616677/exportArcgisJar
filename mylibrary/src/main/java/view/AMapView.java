package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.esri.android.map.MapView;
import com.example.admin.mylibrary.R;

/**
 * Created by admin on 2017/3/31.
 */

public class AMapView extends RelativeLayout {


    private MapView mapView;
    private LinearLayout lin_zoom;
    private ImageButton imgbt_zoomin,imgbt_zoomout;
    private Compass compass;
    public  int zoomlevel;

    public AMapView(Context context) {
        super(context);
        initView(context);
    }

    public AMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){

        View.inflate(context, R.layout.layout_group, AMapView.this);
        mapView = (MapView) findViewById(R.id.mapview);
        lin_zoom = (LinearLayout) findViewById(R.id.lin_zoombt);
        imgbt_zoomin = (ImageButton) findViewById(R.id.imgbt_zoomin);
        imgbt_zoomout = (ImageButton) findViewById(R.id.imgbt_zoomout);
        lin_zoom.setVisibility(GONE);
        imgbt_zoomin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    mapView.zoomin();
            }
        });
        imgbt_zoomout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.zoomout();
            }
        });

        compass = new Compass(context,null,mapView);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15,15,0,0);
        mapView.addView(compass,layoutParams);
        compass.setVisibility(GONE);
    }

    /**
     * 设置是否允许地图旋转
     * @param isAllow
     */
    public void setAllowRotationByPinch(boolean isAllow){
        mapView.setAllowRotationByPinch(isAllow);
    }

    /**
     * 设置是否显示指北针
     * @param enable
     */
    public void setCompassVisibility(boolean enable){
        if(enable){
            compass.setVisibility(VISIBLE);
        }else{
            compass.setVisibility(GONE);
        }
    }

    /**
     * 设置是否显示缩放按钮
     * @param enable
     */
    public void setZoomButtonVisibility(boolean enable){

        if(enable){
            lin_zoom.setVisibility(VISIBLE);
        }else{
            lin_zoom.setVisibility(GONE);
        }
    }




    public MapView getMapView() {
        return mapView;
    }
}
