package com.zja.gis.supermap.util;

import com.supermap.data.*;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-18 13:45
 */
public class DataConversionUtil {

    public static Dataset convert(Dataset sourceDataset, PrjCoordSys targetPrjCoordSys, Datasource targetDatasource, String targetDatasetName) {
        return convert(sourceDataset, targetPrjCoordSys, targetDatasource, targetDatasetName, new CoordSysTransParameter(), CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION);
    }

    public static Dataset convert(Dataset sourceDataset, PrjCoordSys targetPrjCoordSys, Datasource targetDatasource, String targetDatasetName, CoordSysTransParameter coordSysTransParameter, CoordSysTransMethod coordSysTransMethod) {
        return CoordSysTranslator.convert(sourceDataset, targetPrjCoordSys, targetDatasource, targetDatasetName, coordSysTransParameter, coordSysTransMethod);
    }

    public static boolean convert(Dataset sourceDataset, PrjCoordSys targetPrjCoordSys) {
        return convert(sourceDataset, targetPrjCoordSys, new CoordSysTransParameter(), CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION);
    }

    public static boolean convert(Dataset sourceDataset, PrjCoordSys targetPrjCoordSys, CoordSysTransParameter coordSysTransParameter, CoordSysTransMethod coordSysTransMethod) {
        return CoordSysTranslator.convert(sourceDataset, targetPrjCoordSys, coordSysTransParameter, coordSysTransMethod);
    }

    public static boolean convert(Datasource sourceDatasource, Datasource targetDatasource) {
        return convert(sourceDatasource, targetDatasource, new CoordSysTransParameter(), CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION);
    }

    public static boolean convert(Datasource sourceDatasource, Datasource targetDatasource, CoordSysTransParameter coordSysTransParameter, CoordSysTransMethod coordSysTransMethod) {
        return CoordSysTranslator.convert(sourceDatasource, targetDatasource, coordSysTransParameter, coordSysTransMethod);
    }

    public static boolean convert(Datasource datasource, PrjCoordSys targetPrjCoordSys) {
        return convert(datasource, targetPrjCoordSys, new CoordSysTransParameter(), CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION);
    }

    public static boolean convert(Datasource datasource, PrjCoordSys targetPrjCoordSys, CoordSysTransParameter coordSysTransParameter, CoordSysTransMethod coordSysTransMethod) {
        return CoordSysTranslator.convert(datasource, targetPrjCoordSys, coordSysTransParameter, coordSysTransMethod);
    }

    public static boolean convert(Geometry geometry, PrjCoordSys targetPrjCoordSys) {
        PrjCoordSys sourcePrjCoordSys = getPrjByGeo(geometry);
        return convert(geometry, sourcePrjCoordSys, targetPrjCoordSys, new CoordSysTransParameter(), CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION);
    }

    public static boolean convert(Geometry geometry, PrjCoordSys sourcePrjCoordSys, PrjCoordSys targetPrjCoordSys) {
        return convert(geometry, sourcePrjCoordSys, targetPrjCoordSys, new CoordSysTransParameter(), CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION);
    }

    public static boolean convert(Geometry geometry, PrjCoordSys sourcePrjCoordSys, PrjCoordSys targetPrjCoordSys, CoordSysTransParameter coordSysTransParameter, CoordSysTransMethod coordSysTransMethod) {
        return CoordSysTranslator.convert(geometry, sourcePrjCoordSys, targetPrjCoordSys, coordSysTransParameter, coordSysTransMethod);
    }

    public static boolean convert(Point2Ds points, PrjCoordSys sourcePrjCoordSys, PrjCoordSys targetPrjCoordSys) {
        return convert(points, sourcePrjCoordSys, targetPrjCoordSys, new CoordSysTransParameter(), CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION);
    }

    public static boolean convert(Point2Ds points, PrjCoordSys sourcePrjCoordSys, PrjCoordSys targetPrjCoordSys, CoordSysTransParameter coordSysTransParameter, CoordSysTransMethod coordSysTransMethod) {
        return CoordSysTranslator.convert(points, sourcePrjCoordSys, targetPrjCoordSys, coordSysTransParameter, coordSysTransMethod);
    }

    public static PrjCoordSys getPrjByGeo(Geometry geometry) {
        Point2D innerPoint = geometry.getInnerPoint();
        if (innerPoint.getX() < 360.0 && innerPoint.getY() < 90.0) {
            return PrjCoordSys.fromEPSG(4490);
        } else if (innerPoint.getX() < 1000000.0 && innerPoint.getX() > 100000.0) {
            return PrjCoordSys.fromEPSG(4549);
        } else if (innerPoint.getX() < 1000000.0 && innerPoint.getX() > 100000.0 && String.valueOf(innerPoint.getX()).startsWith("40")) {
            return PrjCoordSys.fromEPSG(4528);
        } else {
            System.out.println("暂没新增该坐标系的支持-默认为4490");
            return PrjCoordSys.fromEPSG(4490);
        }
    }
}
