package util;

import com.esri.core.geometry.Point;

/**
 * Created by admin on 2017/3/30.
 */

public interface AdminCodeCallBack {

    public void success(Point point);

    public void fail();

}
