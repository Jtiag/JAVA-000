package com.company;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 自定义一个Classloader，加载一个Hello.xlass文件，执行hello方法，此文件内容是
 * 一个Hello.class文件所有字节(x=255-x)处理后的文件
 *
 * @author jasper
 */
public class MyClassLoader extends ClassLoader {
    /**
     * 文件所在绝对路径
     */
    private String absPath;

    public MyClassLoader(String path) {
        super(null);
        this.absPath = path;
    }

    /**
     * Finds the class with the specified <a href="#name">binary name</a>.
     * This method should be overridden by class loader implementations that
     * follow the delegation model for loading classes, and will be invoked by
     * the {@link #loadClass <tt>loadClass</tt>} method after checking the
     * parent class loader for the requested class.  The default implementation
     * throws a <tt>ClassNotFoundException</tt>.
     *
     * @param name The <a href="#name">binary name</a> of the class
     * @return The resulting <tt>Class</tt> object
     * @since 1.2
     */
    @Override
    protected Class<?> findClass(String name) {
        String absFileName = absPath + File.separator + name;
        byte[] data = findByteData(absFileName);
        assert data != null;
        String className = name.substring(0, name.indexOf("."));
        return defineClass(className, data, 0, data.length);
    }

    private byte[] findByteData(String absFileName) {
        File file = new File(absFileName);
        try (FileInputStream fileInputStream = new FileInputStream(file);
             FileChannel readChannel = fileInputStream.getChannel();
             ByteArrayOutputStream bufferedWriter = new ByteArrayOutputStream((int) readChannel.size());
             WritableByteChannel writableByteChannel = Channels.newChannel(bufferedWriter)) {
            int bufSize = 1024;
            //todo allocate 的值为多大较合适 1024？
            ByteBuffer buffer = ByteBuffer.allocate(bufSize);
            while (true) {
                int read = readChannel.read(buffer);
                boolean stop = (read == 0 || read == -1);
                if (stop) {
                    break;
                }
                buffer.flip();
                writableByteChannel.write(buffer);
                buffer.clear();
            }
            byte[] old = bufferedWriter.toByteArray();
            byte[] news = new byte[old.length];
            for (int i = 0; i < old.length; i++) {
                news[i] = (byte) (255 - old[i]);
            }
            return news;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        // /Users/jasper/work-space/git-warehouse/java/geektime/java_training_camp/week1/Hello/Hello.xlass
        String path = "/Users/jasper/work-space/git-warehouse/java/geektime/java_training_camp/week1/Hello";
        MyClassLoader myClassLoader = new MyClassLoader(path);
        Class<?> myClassLoaderClass = myClassLoader.findClass("Hello.xlass");
        Class<?> hello = myClassLoaderClass.getClassLoader().loadClass("Hello");
        Method helloMethod = hello.getMethod("hello");
        helloMethod.invoke(hello.newInstance());
    }
}