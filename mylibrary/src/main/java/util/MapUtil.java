package util;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geodatabase.ShapefileFeatureTable;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by admin on 2017/3/29.
 */

public class MapUtil {

    private static MapUtil mapUtil;
    private static MapView mapView;
    static final Object sInstanceSync = new Object();
    public static Point ptlineStart;
    public static Point ptGonStart;


    public static MapUtil getInstance(MapView mapView){
        synchronized (sInstanceSync){
            if(mapUtil==null){
                mapUtil = new MapUtil();
            }
            MapUtil.mapView = mapView;
            return mapUtil;
        }

    }

    /**
     * 加载本地切片底图
     * @param path
     * @param mapView
     * @return
     */
    public ArcGISLocalTiledLayer initTiled(String path,MapView mapView){
        ArcGISLocalTiledLayer arcGISLocalTiledLayer = new ArcGISLocalTiledLayer(path);
        mapView.addLayer(arcGISLocalTiledLayer);
        return arcGISLocalTiledLayer;
    }


    /**
     * 加载切片底图服务
     * @param serverUrl
     * @param mapView
     */
    public ArcGISTiledMapServiceLayer initTiledServer(String serverUrl, MapView mapView){

        ArcGISTiledMapServiceLayer arcGISTiledMapServiceLayer = new
                ArcGISTiledMapServiceLayer(serverUrl);
        mapView.addLayer(arcGISTiledMapServiceLayer);
        return arcGISTiledMapServiceLayer;

    }

    /**
     * 加载动态图层
     * @param serverUrl
     * @param mapView
     * @return
     */
    public ArcGISDynamicMapServiceLayer initDynamicServer(String serverUrl, MapView mapView){

        ArcGISDynamicMapServiceLayer arcGISDynamicMapServiceLayer = new
                ArcGISDynamicMapServiceLayer(serverUrl);
        mapView.addLayer(arcGISDynamicMapServiceLayer);
        return arcGISDynamicMapServiceLayer;

    }


    /**
     * 定位方法，在回调到传入的LocationListener
     * @param locationListener
     * @return
     */

    public LocationDisplayManager setupLocationListener(LocationDisplayManager ldm, LocationListener locationListener){
        if(mapView!=null&&mapView.isLoaded()){
            ldm.setLocationListener(locationListener);
            return ldm;
        }
        return null;
    }

    /**
     * 定位成功后，从定位坐标系4326转为mapView坐标系的点
     * @param loc
     * @return
     */
    public Point getAsPoint(Location loc) {
        Point wgsPoint = new Point(loc.getLongitude(), loc.getLatitude());
        return (Point) GeometryEngine.project(wgsPoint, SpatialReference.create(4326),mapView.getSpatialReference());
    }


    /**
     * 对点标记进行渲染
     * @param x 点击地图后获取到的屏幕x坐标
     * @param y 点击地图后获取到的屏幕y坐标
     * @return
     */
    public Graphic simpleMarker(float x, float y, SimpleMarkerSymbol simpleMarkerSymbol){

        Point point = mapView.toMapPoint(x,y);
        Graphic graphic = new Graphic(point,simpleMarkerSymbol);
        return graphic;
    }

    public void simpleMarker(float x, float y, GraphicsLayer graphicsLayer, SimpleMarkerSymbol simpleMarkerSymbol){

        Point point = mapView.toMapPoint(x,y);
        Graphic graphic = new Graphic(point,simpleMarkerSymbol);
        if(graphicsLayer==null){
            graphicsLayer = new GraphicsLayer();
            mapView.addLayer(graphicsLayer);
        }
        graphicsLayer.addGraphic(graphic);
    }

    public Graphic simpleMarker(Point point, SimpleMarkerSymbol simpleMarkerSymbol){

        Graphic graphic = new Graphic(point,simpleMarkerSymbol);
        return graphic;
    }

    public void simpleMarker(Point point, GraphicsLayer graphicsLayer, SimpleMarkerSymbol simpleMarkerSymbol){

        Graphic graphic = new Graphic(point,simpleMarkerSymbol);
        if(graphicsLayer==null){
            graphicsLayer = new GraphicsLayer();
            mapView.addLayer(graphicsLayer);
        }
        graphicsLayer.addGraphic(graphic);
    }



    /**
     * 对点标记进行自定义图标渲染,不加载点，返回Graphic
     * @param x 点击地图后获取到的屏幕x坐标
     * @param y 点击地图后获取到的屏幕y坐标
     * @param drawable 标记点的自定义图标
     * @return
     */
    public Graphic pictureMarker(float x, float y, Drawable drawable){
        Point point = mapView.toMapPoint(x,y);

        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(drawable);

        Graphic graphic = new Graphic(point,pictureMarkerSymbol);

        return graphic;
    }

    /**
     * 对点标记进行渲染，在界面上显示，把点加入到graphicsLayer
     * @param x
     * @param y
     * @param graphicsLayer
     * @param drawable
     */

    public  void pictureMarker(float x, float y, GraphicsLayer graphicsLayer, Drawable drawable){
        Point point = mapView.toMapPoint(x,y);

        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(drawable);

        Graphic graphic = new Graphic(point,pictureMarkerSymbol);

        if(graphicsLayer==null){
            graphicsLayer = new GraphicsLayer();
            mapView.addLayer(graphicsLayer);
        }
        graphicsLayer.addGraphic(graphic);

    }

    /**
     * 同上，不加载点
     * @param point
     * @param drawable
     * @return
     */

    public Graphic pictureMarker(Point point, Drawable drawable){

        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(drawable);

        Graphic graphic = new Graphic(point,pictureMarkerSymbol);

        return graphic;
    }

    /**
     * 同上，记载点在graphicsLayer，graphicsLayer为null，new出来并且加载进去
     * @param point
     * @param graphicsLayer
     * @param drawable
     */
    public  void pictureMarker(Point point, GraphicsLayer graphicsLayer, Drawable drawable){

        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(drawable);

        Graphic graphic = new Graphic(point,pictureMarkerSymbol);

        if(graphicsLayer==null){
            graphicsLayer = new GraphicsLayer();
            mapView.addLayer(graphicsLayer);
        }
        graphicsLayer.addGraphic(graphic);

    }


    /**
     * 对点标记进行自定义图标渲染
     * @param x 点击地图后获取到的屏幕x坐标
     * @param y 点击地图后获取到的屏幕y坐标
     * @param drawable 标记点的自定义图标
     * @param offsetX  图标的偏移量X
     * @param offsetY  图标的偏移量Y
     * @return
     */
    public Graphic pictureMarker(float x, float y, Drawable drawable, float offsetX, float offsetY){
        Point point = mapView.toMapPoint(x,y);

        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(drawable);
        pictureMarkerSymbol.setOffsetX(offsetX);
        pictureMarkerSymbol.setOffsetY(offsetY);
        Graphic graphic = new Graphic(point,pictureMarkerSymbol);

        return graphic;
    }

    public  void pictureMarker(float x, float y, Drawable drawable, GraphicsLayer graphicsLayer, float offsetX, float offsetY){
        Point point = mapView.toMapPoint(x,y);

        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(drawable);
        pictureMarkerSymbol.setOffsetX(offsetX);
        pictureMarkerSymbol.setOffsetY(offsetY);
        Graphic graphic = new Graphic(point,pictureMarkerSymbol);
        if(graphicsLayer==null){
            graphicsLayer = new GraphicsLayer();
            mapView.addLayer(graphicsLayer);
        }
        graphicsLayer.addGraphic(graphic);
    }

    public Graphic pictureMarker(Point point, Drawable drawable, float offsetX, float offsetY){

        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(drawable);
        pictureMarkerSymbol.setOffsetX(offsetX);
        pictureMarkerSymbol.setOffsetY(offsetY);
        Graphic graphic = new Graphic(point,pictureMarkerSymbol);

        return graphic;
    }
    public  void pictureMarker(Point point, Drawable drawable, GraphicsLayer graphicsLayer, float offsetX, float offsetY){

        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(drawable);
        pictureMarkerSymbol.setOffsetX(offsetX);
        pictureMarkerSymbol.setOffsetY(offsetY);
        Graphic graphic = new Graphic(point,pictureMarkerSymbol);
        if(graphicsLayer==null){
            graphicsLayer = new GraphicsLayer();
            mapView.addLayer(graphicsLayer);
        }
        graphicsLayer.addGraphic(graphic);
    }

    /**
     * 点击覆盖物后显示点击范围内的所有点标记，返回点标记集合
     * @param graphicsLayer
     * @param x
     * @param y
     * @return
     */
    public List<Graphic> getClickGraphic(GraphicsLayer graphicsLayer, float x, float y){


        if(graphicsLayer!=null) {
            int[] graphicIds = graphicsLayer.getGraphicIDs(x, y, 20);
            if (graphicIds != null && graphicIds.length > 0) {
                List<Graphic> graphicList = new ArrayList<>();
                for (int i = 0; i < graphicIds.length; i++) {
                    Graphic graphic = graphicsLayer.getGraphic(graphicIds[i]);
                    if(graphic!=null){
                        graphicList.add(graphic);
                    }
                }
                return graphicList;
            }
        }
        return null;
    }

    /**
     * 删除单个标注
     * @param graphicsLayer
     * @param graphic
     */
    public void removeGraphic(GraphicsLayer graphicsLayer, Graphic graphic){

        if(graphicsLayer!=null&&graphic!=null){
            Point point = (Point) graphic.getGeometry();
            Point point1 = mapView.toScreenPoint(point);

            int[] ids = graphicsLayer.getGraphicIDs((float)point1.getX(),(float)point1.getY(),20);
            if(ids.length>0){
                graphicsLayer.removeGraphic(ids[0]);
            }
        }

    }

    /**
     * 删除graphicsLayer图层中的全部标注
     * * @param graphicsLayer
     */
    public void removeAllGraphic(GraphicsLayer graphicsLayer){

        if(graphicsLayer!=null){
            graphicsLayer.removeAll();
        }

    }

    /**
     * 删除graphicsLayer中的多个graphic
     * @param graphicsLayer
     * @param graphicList
     */
    public void removeGraphicList(GraphicsLayer graphicsLayer, List<Graphic> graphicList){

        if(graphicsLayer!=null&&graphicList!=null&&graphicList.size()!=0){

            for(int i=0;i<graphicList.size();i++){
                Graphic graphic = graphicList.get(i);
                Point point = (Point) graphic.getGeometry();
                Point point1 = mapView.toScreenPoint(point);

                int[] ids = graphicsLayer.getGraphicIDs((float)point1.getX(),(float)point1.getY(),5);
                if(ids.length>0){
                    graphicsLayer.removeGraphic(ids[0]);
                }
            }
        }

    }

    /**
     *批量删除标注或者覆盖物
     * @param graphicsLayer
     * @param graphiIds 该参数为graphic的ID集合
     */
    public void removeGraphicList(GraphicsLayer graphicsLayer, int[] graphiIds){

        if(graphicsLayer!=null&&graphiIds.length>0){
            for(int i=0;i<graphiIds.length;i++){
                graphicsLayer.removeGraphic(graphiIds[i]);
            }
        }
    }


    /**
     * 根据提供的点集合获取最佳图
     * @param points
     */
    public void zoomBestView(List<Point> points){

        Polygon polygon = Calculate.getInstance(mapView).getPolygon(points);

        mapView.setExtent(polygon,50);

    }

    /**
     * 在离线的shpe数据里面查询数据根据adminCode获取点
     * @param adminCode
     * @param shapefileFeatureTable
     * @param callBack
     */
    public void adminCodeToPoint(String adminCode, ShapefileFeatureTable shapefileFeatureTable, final AdminCodeCallBack callBack) {

        final List<Feature> features = new ArrayList<>();
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setOutFields(new String[]{"*"});
        queryParameters.setWhere("AdminCode = '" + adminCode + "'");
        shapefileFeatureTable.queryFeatures(queryParameters, new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult objects) {

                Iterator<Object> objectIterator = objects.iterator();
                boolean isNext = objectIterator.hasNext();
                while (objectIterator.hasNext()) {
                    Feature feature = (Feature) objectIterator.next();
                    features.add(feature);

                }

                Feature feature = features.get(0);
                double centerX = (double) feature.getAttributeValue("CenterX");
                double centerY = (double) feature.getAttributeValue("CenterY");
                Point point = new Point(centerX, centerY);

                callBack.success(point);

            }

            @Override
            public void onError(Throwable throwable) {

                callBack.fail();

            }
        });
    }
    public void adminCodeToPoint(String adminCode, ShapefileFeatureTable shapefileFeatureTable, final AdminCodeCallBack2 callBack) {

        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setOutFields(new String[]{"*"});
        queryParameters.setWhere("ADMINCODE = '" + adminCode + "'");
        shapefileFeatureTable.queryFeatures(queryParameters, new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult objects) {

                Iterator<Object> objectIterator = objects.iterator();
                Feature feature = null;
                if (objectIterator.hasNext()) {
                    feature = (Feature) objectIterator.next();
                    callBack.success(feature);
                } else {
                    callBack.fail("没有查询到结果");
                }

            }

            @Override
            public void onError(Throwable throwable) {

                callBack.fail(throwable.getMessage());

            }
        });

    }

    public void adminCodeToPoint(String adminCode, String serverUrl, final AdminCodeCallBack2 callBack) {

        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setOutFields(new String[]{"*"});
        queryParameters.setWhere("ADMINCODE = '" + adminCode + "'");

        QueryTask queryTask = new QueryTask(serverUrl);
        try {
            queryTask.execute(queryParameters, new CallbackListener<FeatureResult>() {
                @Override
                public void onCallback(FeatureResult objects) {

                    Iterator<Object> objectIterator = objects.iterator();
                    Feature feature = null;
                    if (objectIterator.hasNext()) {
                        feature = (Feature) objectIterator.next();
                        callBack.success(feature);
                    } else {
                        callBack.fail("没有查询到结果");
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    callBack.fail(throwable.getMessage());
                }
            });
        }catch (Exception e){
            callBack.fail(e.getMessage());


        }

    }



    /**
     *  通过经纬度坐标去查询行政区的编码
     * @param serverUrl 该URL为单个图层的URL，比如：http://192.168.11.114:6080/arcgis/rest/services/server/MyMapService/MapServer/0
     * @param x
     * @param y
     * @param polygonCallBack
     */

    public void pointToAdminPolygon(String serverUrl, double x, double y, final PolygonCallBack polygonCallBack){


        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setGeometry(new Point(x,y));
        queryParameters.setReturnGeometry(true);
        queryParameters.setOutFields(new String[]{"*"});
        queryParameters.setOutSpatialReference(mapView.getSpatialReference());
        queryParameters.setWhere("1=1");
        QueryTask queryTask = new QueryTask(serverUrl);
        try {
            queryTask.execute(queryParameters, new CallbackListener<FeatureResult>() {
                @Override
                public void onCallback(FeatureResult objects) {
                    Iterator<Object> objectIterator = objects.iterator();
                    Feature feature = null;
                    if (objectIterator.hasNext()) {
                        feature = (Feature) objectIterator.next();
                        String adminCode = (String) feature.getAttributeValue("AdminCode");
                        polygonCallBack.success(adminCode);
                    }else{
                        polygonCallBack.fail("没有查询到结果");
                    }


                }

                @Override
                public void onError(Throwable throwable) {
                    polygonCallBack.fail(throwable.getMessage());

                }
            });
        } catch (Exception e) {
            polygonCallBack.fail(e.getMessage());
        }

    }

    /**
     * 通过点point去查询行政区的编码
     * @param serverUrl 该URL为单个图层的URL，比如：http://192.168.11.114:6080/arcgis/rest/services/server/MyMapService/MapServer/0
     * @param point
     * @param polygonCallBack
     */
    public void pointToAdminPolygon(String serverUrl, Point point, final PolygonCallBack polygonCallBack){


        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setGeometry(point);
        queryParameters.setReturnGeometry(true);
        queryParameters.setOutFields(new String[]{"*"});
        queryParameters.setOutSpatialReference(mapView.getSpatialReference());
        queryParameters.setWhere("1=1");
        QueryTask queryTask = new QueryTask(serverUrl);

        try {
            queryTask.execute(queryParameters, new CallbackListener<FeatureResult>() {
                @Override
                public void onCallback(FeatureResult objects) {

                    Iterator<Object> objectIterator = objects.iterator();
                    Feature feature = null;
                    if (objectIterator.hasNext()) {
                        feature = (Feature) objectIterator.next();
                        String adminCode = (String) feature.getAttributeValue("AdminCode");
                        polygonCallBack.success(adminCode);
                    }else{
                        polygonCallBack.fail("没有查询到结果");
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    polygonCallBack.fail(throwable.getMessage());

                }
            });
        } catch (Exception e) {
            polygonCallBack.fail(e.getMessage());
        }

    }


    public Polyline drawLineOnly(float v, float v1, GraphicsLayer pointLayer, GraphicsLayer drawLayer, List<Point> points, SimpleMarkerSymbol simpleMarkerSymbolfirst,
                                 SimpleMarkerSymbol simpleMarkerSymbol, SimpleLineSymbol lineSymbol){

        Point ptCurrent = mapView.toMapPoint(new Point(v, v1));
        points.add(ptCurrent);
        if (ptlineStart == null) {//画线或多边形的第一个点
            pointLayer.removeAll();//第一次开始前，清空全部graphic
            ptlineStart = ptCurrent;

            //绘制第一个点
            Graphic graphic = new Graphic(ptlineStart, simpleMarkerSymbolfirst);
            pointLayer.addGraphic(graphic);
            return null;
        } else {      //画线或多边形的其他点
            //绘制其他点
            Graphic graphic = new Graphic(ptCurrent, simpleMarkerSymbol);
            pointLayer.addGraphic(graphic);

//                    SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.BLACK, 1, SimpleLineSymbol.STYLE.SOLID);

            Polyline polyline  = Calculate.getInstance(mapView).getPolyline(drawLayer,points,lineSymbol);
//            Log.i("TAG","length:"+length);
//                text_result.setText(length+"米");

//            }
            return polyline;
        }

    }
    public double drawLine(float v, float v1, GraphicsLayer pointLayer, GraphicsLayer drawLayer, List<Point> points, SimpleMarkerSymbol simpleMarkerSymbolfirst,
                           SimpleMarkerSymbol simpleMarkerSymbol, SimpleLineSymbol lineSymbol){

        Point ptCurrent = mapView.toMapPoint(new Point(v, v1));


        points.add(ptCurrent);
        if (ptlineStart == null) {//画线或多边形的第一个点
            pointLayer.removeAll();//第一次开始前，清空全部graphic
            ptlineStart = ptCurrent;

            //绘制第一个点
            Graphic graphic = new Graphic(ptlineStart, simpleMarkerSymbolfirst);
            pointLayer.addGraphic(graphic);
            return 0;
        } else {      //画线或多边形的其他点
            //绘制其他点
            Graphic graphic = new Graphic(ptCurrent, simpleMarkerSymbol);
            pointLayer.addGraphic(graphic);

//                    SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.BLACK, 1, SimpleLineSymbol.STYLE.SOLID);

            double length = Calculate.getInstance(mapView).getlength(drawLayer,points,lineSymbol);
            Log.i("TAG","length:"+length);
//                text_result.setText(length+"米");

//            }
            return length;
        }

    }
    public Polygon drawGonOnly(float v, float v1, GraphicsLayer pointLayer, GraphicsLayer drawLayer, List<Point> points, SimpleMarkerSymbol simpleMarkerSymbolfirst,
                               SimpleMarkerSymbol simpleMarkerSymbol, SimpleFillSymbol simpleFillSymbol){

        Point ptCurrent = mapView.toMapPoint(new Point(v, v1));


        points.add(ptCurrent);
        if (ptGonStart == null) {//画线或多边形的第一个点
            pointLayer.removeAll();//第一次开始前，清空全部graphic
            ptGonStart = ptCurrent;

            //绘制第一个点
            Graphic graphic = new Graphic(ptGonStart, simpleMarkerSymbolfirst);
            pointLayer.addGraphic(graphic);
            return null;
        } else {      //画线或多边形的其他点
            //绘制其他点
            Graphic graphic = new Graphic(ptCurrent, simpleMarkerSymbol);
            pointLayer.addGraphic(graphic);

            //绘制临时多边形
            Polygon polygon  = Calculate.getInstance(mapView).getPolygon(drawLayer,points,simpleFillSymbol);

            return polygon;
        }

    }
    public double drawGon(float v, float v1, GraphicsLayer pointLayer, GraphicsLayer drawLayer, List<Point> points, SimpleMarkerSymbol simpleMarkerSymbolfirst,
                          SimpleMarkerSymbol simpleMarkerSymbol, SimpleFillSymbol simpleFillSymbol){

        Point ptCurrent = mapView.toMapPoint(new Point(v, v1));


        points.add(ptCurrent);
        if (ptGonStart == null) {//画线或多边形的第一个点
            pointLayer.removeAll();//第一次开始前，清空全部graphic
            ptGonStart = ptCurrent;

            //绘制第一个点
            Graphic graphic = new Graphic(ptGonStart, simpleMarkerSymbolfirst);
            pointLayer.addGraphic(graphic);
            return 0;
        } else {      //画线或多边形的其他点
            //绘制其他点
            Graphic graphic = new Graphic(ptCurrent, simpleMarkerSymbol);
            pointLayer.addGraphic(graphic);

            //绘制临时多边形
            double area = Calculate.getInstance(mapView).getArea(drawLayer,points,simpleFillSymbol);

            Log.i("TAG","area:"+area);
//                text_result.setText(area+"平方米");

//            }
            return area;
        }

    }









}
