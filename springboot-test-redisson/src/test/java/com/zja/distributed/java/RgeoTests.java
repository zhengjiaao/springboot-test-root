package com.zja.distributed.java;

import com.zja.BaseTests;
import org.junit.Test;
import org.redisson.api.GeoEntry;
import org.redisson.api.GeoUnit;
import org.redisson.api.RFuture;
import org.redisson.api.RGeo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-01 16:31
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：参考 https://github.com/redisson/redisson/wiki/6.-Distributed-objects
 */
public class RgeoTests extends BaseTests {

    @Test
    public void testGeo(){

        RGeo<String> geo = redisson.getGeo("test");

        geo.add(new GeoEntry(13.361389, 38.115556, "Palermo"),
                new GeoEntry(15.087269, 37.502669, "Catania"));

        //两个位置的距离 米
        Double distance = geo.dist("Palermo", "Catania", GeoUnit.METERS);
        System.out.println(distance);

        //半径 公里
//        List<String> radius = geo.radius(15, 37, 200, GeoUnit.KILOMETERS);
//        System.out.println(radius.toString());
//
//        Map<String, GeoPosition> stringGeoPositionMap = geo.radiusWithPosition(15.087269, 37.502669, 200, GeoUnit.KILOMETERS);


//        Map<String, GeoPosition> positions = geo.pos("test2", "Palermo", "test3", "Catania", "test1");
    }


    @Test
    public void testAsyncGeo() throws ExecutionException, InterruptedException {

        RGeo<String> geo = redisson.getGeo("test");

        RFuture<Long> addFuture = geo.addAsync(new GeoEntry(13.361389, 38.115556, "Palermo"),
                new GeoEntry(15.087269, 37.502669, "Catania"));

        RFuture<Double> distanceFuture = geo.distAsync("Palermo", "Catania", GeoUnit.METERS);

        distanceFuture.await(2, TimeUnit.SECONDS);
        System.out.println(distanceFuture.get());


//        RFuture<Map<String, GeoPosition>> positionsFuture = geo.posAsync("test2", "Palermo", "test3", "Catania", "test1");

//        RFuture<List<String>> citiesFuture = geo.searchAsync(GeoSearchArgs.from(15, 37).radius(200, GeoUnit.KILOMETERS));
//        RFuture<Map<String, GeoPosition>> citiesWithPositions = geo.searchWithPositionAsync(GeoSearchArgs.from(15, 37).radius(200, GeoUnit.KILOMETERS));
    }
}
