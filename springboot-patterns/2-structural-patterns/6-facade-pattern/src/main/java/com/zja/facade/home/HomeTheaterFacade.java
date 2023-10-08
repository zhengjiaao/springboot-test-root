/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 17:45
 * @Since:
 */
package com.zja.facade.home;

import com.zja.facade.home.service.Amplifier;
import com.zja.facade.home.service.Lights;
import com.zja.facade.home.service.Projector;
import com.zja.facade.home.service.Screen;

/**
 * @author: zhengja
 * @since: 2023/10/08 17:45
 */
public class HomeTheaterFacade {
    private Projector projector;
    private Amplifier amplifier;
    private Lights lights;
    private Screen screen;

    public HomeTheaterFacade() {
        projector = new Projector();
        amplifier = new Amplifier();
        lights = new Lights();
        screen = new Screen();
    }

    public void watchMovie(String movie) {
        System.out.println("Get ready to watch a movie...");
        lights.dimLights();
        screen.down();
        projector.on();
        amplifier.on();
        amplifier.setVolume(10);
        projector.setInput(movie);
        projector.setAspectRatio("16:9");
        projector.start();
    }

    public void endMovie() {
        System.out.println("Shutting down the home theater...");
        projector.stop();
        projector.off();
        amplifier.off();
        screen.up();
        lights.on();
    }
}
