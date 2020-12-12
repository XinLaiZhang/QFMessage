package top.fluffcotton.annotation;


import top.fluffcotton.handle.Handle;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Description 扫描注解工具类
 * @Author zyc
 * @Date 2020/11/25 22:00
 */
public class AnnotationScanner {

    /**
     * 从包package中获取所有的Class
     *
     * @param packageName 包名
     * @return 包下class
     */
    public static Set<Class<?>> getClasses(String packageName) throws Exception {
        // 第一个class类的集合
        Set<Class<?>> classes = new HashSet<>();
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    addClass(classes, filePath, packageName);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                // 如果是一个.class文件 而且不是目录
                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    // 去掉后面的".class" 获取真正的类名
                                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                                    try {
                                        // 添加到classes
                                        classes.add(Class.forName(packageName + '.' + className));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * 将扫描到的文件class保存
     *
     * @param classes     保存集合
     * @param filePath    文件目录
     * @param packageName 包名
     * @throws Exception 异常
     */
    public static void addClass(Set<Class<?>> classes, String filePath, String packageName) throws Exception {
        File[] files = new File(filePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        assert files != null;
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String classesName = fileName.substring(0, fileName.lastIndexOf("."));
                if (!packageName.isEmpty()) {
                    classesName = packageName + "." + classesName;
                }
                doAddClass(classes, classesName);
            }
        }
    }

    /**
     * 将扫描到的包加载到虚拟机然后添加到结果
     *
     * @param classes    类集合
     * @param classsName 类名
     * @throws Exception 异常
     */
    public static void doAddClass(Set<Class<?>> classes, final String classsName) throws Exception {
//        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        classes.add(Class.forName(classsName));
    }

    /**
     * 查找含有注解的类集合
     *
     * @param packageName     包名
     * @param annotationClass 注解类
     * @return 类集合
     * @throws Exception 异常
     */
    public static <T extends Annotation> Set<Class<?>> getAnnotationClasses(String packageName, Class<T> annotationClass) throws Exception {
        //找用了annotationClass注解的类
        Set<Class<?>> controllers = new HashSet<>();
        Set<Class<?>> clsList = getClasses(packageName);
        for (Class<?> cls : clsList) {
            if (cls.getAnnotation(annotationClass) != null) {
                controllers.add(cls);
            }
        }
        return controllers;
    }

    /**
     * 查找含有接口的类集合
     *
     * @param packageName    包名
     * @param interfaceClass 接口类
     * @return 类集合
     * @throws Exception 异常
     */
    public static <T> Set<Class<T>> getInterfaceClasses(String packageName, Class<Handle> interfaceClass) throws Exception {
        //找用了annotationClass注解的类
        Set<Class<T>> controllers = new HashSet<>();
        Set<Class<?>> clsList = getClasses(packageName);
        for (Class<?> cls : clsList) {
            if (interfaceClass.isAssignableFrom(cls)) {
                controllers.add((Class<T>) cls);
            }
        }
        return controllers;
    }

}
