package util;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;

import java.util.List;

/**
 * Created by Administrator on 2017-03-28.
 */
public class Calculate {
    private static MapView mapView;
    static final Object sInstanceSync = new Object();
    private static Calculate calculate;

    public static Calculate getInstance(MapView mapView){
        synchronized (sInstanceSync){
            if(calculate==null){
                calculate = new Calculate();
            }
            Calculate.mapView = mapView;
            return calculate;
        }
    }

    /**
     * 测量线距离并绘制图层
     *
     * @param graphicsLayer    加载图层
     * @param points           点集
     * @param simpleLineSymbol 线型渲染
     * @return 折线距离
     */

    public double getlength(GraphicsLayer graphicsLayer, List<Point> points, SimpleLineSymbol simpleLineSymbol) {
        double length = 0.0;
        if (points.size() > 1) {
            Polyline polyline = new Polyline();
            if (graphicsLayer == null) {
                graphicsLayer = new GraphicsLayer();
                mapView.addLayer(graphicsLayer);
            }
            Point startPoint = null;
            Point endPoint = null;
            for (int i = 1; i < points.size(); i++) {
                startPoint = points.get(i - 1);
                endPoint = points.get(i);
                Line line1 = new Line();
                line1.setStart(startPoint);
                line1.setEnd(endPoint);
                polyline.addSegment(line1, false);
            }
            Graphic graphic = new Graphic(polyline, simpleLineSymbol);
            graphicsLayer.removeAll();
            graphicsLayer.addGraphic(graphic);
            Polyline polyline1 = (Polyline) GeometryEngine.project(polyline, mapView.getSpatialReference(), SpatialReference.create(SpatialReference.WKID_WGS84_WEB_MERCATOR_AUXILIARY_SPHERE));
            length = Double.valueOf(Math.round(polyline1.calculateLength2D()));
        }
        return length;
    }


    public double getLength(Polyline polyline){
        Polyline polyline1 = (Polyline) GeometryEngine.project(polyline, mapView.getSpatialReference(), SpatialReference.create(SpatialReference.WKID_WGS84_WEB_MERCATOR_AUXILIARY_SPHERE));
        double length = Double.valueOf(Math.round(polyline1.calculateLength2D()));
        return length;
    }

    /**
     * 测量区域面积并绘制图形
     *
     * @param graphicsLayer    加载图层
     * @param points           点集
     * @param simpleFillSymbol 渲染图形
     * @return 区域面积
     */
    public double getArea(GraphicsLayer graphicsLayer, List<Point> points, SimpleFillSymbol simpleFillSymbol) {
        double sArea = 0.0;
        if (points.size() > 2) {
            Polygon polygon = new Polygon();
            if (graphicsLayer == null) {
                graphicsLayer = new GraphicsLayer();
                mapView.addLayer(graphicsLayer);
            }
            Point startPoint = null;
            Point endPoint = null;
            for (int i = 1; i < points.size(); i++) {
                startPoint = points.get(i - 1);
                endPoint = points.get(i);
                Line line1 = new Line();
                line1.setStart(startPoint);
                line1.setEnd(endPoint);
                polygon.addSegment(line1, false);
            }
            Graphic graphic = new Graphic(polygon, simpleFillSymbol);
            graphicsLayer.removeAll();
            graphicsLayer.addGraphic(graphic);
            Polygon polygonNow = (Polygon) GeometryEngine.project(polygon, mapView.getSpatialReference(), SpatialReference.create(SpatialReference.WKID_WGS84_WEB_MERCATOR_AUXILIARY_SPHERE));
            sArea = Math.abs(polygonNow.calculateArea2D());
        }
        return sArea;
    }


    public double getArea(Polygon polygon){
        Polygon polygonNow = (Polygon) GeometryEngine.project(polygon, mapView.getSpatialReference(), SpatialReference.create(SpatialReference.WKID_WGS84_WEB_MERCATOR_AUXILIARY_SPHERE));
        double area = Math.abs(polygonNow.calculateArea2D());
        return area;
    }

    /**
     * 创建点覆盖物，并且在图层中显示
     * @param graphicsLayer 图层
     * @param points 点集合
     * @param simpleLineSymbol 渲染
     * @return
     */
    public Polyline getPolyline(GraphicsLayer graphicsLayer, List<Point> points, SimpleLineSymbol simpleLineSymbol){
        Polyline polyline = null;
        if(points.size()>1){
            polyline = new Polyline();
            Point startPoint = null;
            Point endPoint = null;
            for (int i = 1; i < points.size(); i++) {
                startPoint = points.get(i - 1);
                endPoint = points.get(i);
                Line line1 = new Line();
                line1.setStart(startPoint);
                line1.setEnd(endPoint);
                polyline.addSegment(line1, false);
            }
            if (graphicsLayer == null) {
                graphicsLayer = new GraphicsLayer();
                mapView.addLayer(graphicsLayer);
            }
            Graphic graphic = new Graphic(polyline,simpleLineSymbol);
            graphicsLayer.removeAll();
            graphicsLayer.addGraphic(graphic);
        }
        return polyline;
    }

    /**
     * 创建点覆盖物，不在图层中显示
     * @param points 点集合
     * @return polyline 线要素
     */
    public Polyline getPolyline(List<Point> points){
        Polyline polyline = null;
        if(points.size()>1){
            polyline = new Polyline();
            Point startPoint = null;
            Point endPoint = null;
            for (int i = 1; i < points.size(); i++) {
                startPoint = points.get(i - 1);
                endPoint = points.get(i);
                Line line1 = new Line();
                line1.setStart(startPoint);
                line1.setEnd(endPoint);
                polyline.addSegment(line1, false);
            }
        }
        return polyline;
    }


    /**
     * 创建面覆盖物
     * @param points 点集
     * @return 面要素
     */
    public Polygon getPolygon(List<Point> points) {
        Polygon polygon = null;
        if (points.size()>2){
            polygon=new Polygon();
            Point startPoint = null;
            Point endPoint = null;
            for (int i = 1; i < points.size(); i++) {
                startPoint = points.get(i - 1);
                endPoint = points.get(i);
                Line line1 = new Line();
                line1.setStart(startPoint);
                line1.setEnd(endPoint);
                polygon.addSegment(line1, false);
            }
        }
        return polygon;
    }

    /**
     * 创建面覆盖物，并且在图层中显示
     * @param graphicsLayer 图层
     * @param points 点集合
     * @param simpleFillSymbol 自定义渲染
     * @return 面要素
     */
    public Polygon getPolygon(GraphicsLayer graphicsLayer, List<Point> points, SimpleFillSymbol simpleFillSymbol) {
        Polygon polygon = null;
        if (points.size()>2){
            polygon=new Polygon();
            Point startPoint = null;
            Point endPoint = null;
            for (int i = 1; i < points.size(); i++) {
                startPoint = points.get(i - 1);
                endPoint = points.get(i);
                Line line1 = new Line();
                line1.setStart(startPoint);
                line1.setEnd(endPoint);
                polygon.addSegment(line1, false);
            }
            if (graphicsLayer == null) {
                graphicsLayer = new GraphicsLayer();
                mapView.addLayer(graphicsLayer);
            }
            Graphic graphic = new Graphic(polygon,simpleFillSymbol);
            graphicsLayer.removeAll();
            graphicsLayer.addGraphic(graphic);

        }
        return polygon;
    }
}
