package util;

import com.esri.core.map.Feature;

/**
 * Created by admin on 2017/3/30.
 */

public interface AdminCodeCallBack2 {

    public void success(Feature feature);

    public void fail(String message);

}
