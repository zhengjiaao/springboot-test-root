package com.zja.gdal;

import org.gdal.gdal.gdal;
import org.gdal.ogr.ogr;
import org.junit.jupiter.api.Test;

/**
 *
 * @Author: zhengja
 * @Date: 2026-03-04 14:29
 */
public class GdalTest {

    @Test
    public void testVersion() {
        // 加载GDAL原生库，确保路径正确或在system PATH中
        // 如果环境变量PATH已配置，通常只需调用 registerAll
        try {
            gdal.AllRegister();
            ogr.RegisterAll();
            System.out.println("GDAL initialized successfully: " + gdal.VersionInfo());
            // GDAL initialized successfully: 3090100
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load GDAL native library. Check PATH and DLL files.");
            e.printStackTrace();
        }
    }
}
