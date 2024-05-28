package com.zja.java.lang.reflect;

import lombok.Getter;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用反射实现框架和库的可扩展性
 *
 * @Author: zhengja
 * @Date: 2024-05-28 10:54
 */
public class PluginTest {

    // 定义一个插件接口
    public interface Plugin {
        void execute(PluginContext context);
    }

    // 插件实现类
    public static class MyPlugin implements Plugin {
        @Override
        public void execute(PluginContext context) {
            System.out.println("Executing plugin with message: " + context.getMessage());
        }
    }

    // 插件参数
    @Getter
    public static class PluginContext {
        private final String message;

        public PluginContext(String message) {
            this.message = message;
        }
    }

    // 创建一个插件管理器类,负责加载和管理插件
    public static class PluginManager {
        private static final String PLUGIN_DIR = "plugins/";
        private static final Map<String, Plugin> plugins = new HashMap<>();

        public static void loadPlugins() {
            File pluginDir = new File(PLUGIN_DIR);
            if (!pluginDir.exists()) {
                pluginDir.mkdir();
            }

            for (File file : pluginDir.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    loadPlugin(file);
                }
            }
        }

        private static void loadPlugin(File pluginFile) {
            try (URLClassLoader classLoader = new URLClassLoader(new URL[]{pluginFile.toURI().toURL()})) {
                Class<?> pluginClass = classLoader.loadClass("com.example.plugin.MyPlugin");
                if (Plugin.class.isAssignableFrom(pluginClass)) {
                    Constructor<?> constructor = pluginClass.getConstructor();
                    Plugin plugin = (Plugin) constructor.newInstance();
                    plugins.put(pluginClass.getName(), plugin);
                    System.out.println("Loaded plugin: " + pluginClass.getName());
                }
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException | java.io.IOException e) {
                e.printStackTrace();
            }
        }

        public static void loadPlugin(File pluginFile, PluginContext context) {
            try (URLClassLoader classLoader = new URLClassLoader(new URL[]{pluginFile.toURI().toURL()})) {
                Class<?> pluginClass = classLoader.loadClass("com.example.plugin.MyPlugin");
                if (Plugin.class.isAssignableFrom(pluginClass)) {
                    Constructor<?> constructor = pluginClass.getConstructor(PluginContext.class);
                    Plugin plugin = (Plugin) constructor.newInstance(context);
                    plugins.put(pluginClass.getName(), plugin);
                    System.out.println("Loaded plugin: " + pluginClass.getName());
                }
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException | java.io.IOException e) {
                e.printStackTrace();
            }
        }

        public static void executePlugins() {
            for (Plugin plugin : plugins.values()) {
                plugin.execute(new PluginContext("Hello from PluginManager!"));
            }
        }
    }

    public static void main(String[] args) {
        PluginManager.loadPlugins();
    }
}
