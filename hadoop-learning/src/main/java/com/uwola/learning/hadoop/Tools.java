package com.uwola.learning.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by uwoer on 2018/4/11.
 */
public class Tools {
    public static  final Configuration conf = new Configuration();
    static {
        conf.set("fs.defaultFS", "hdfs://master:9000");
        System.setProperty("HADOOP_USER_NAME","root");
    }

    public static void delDir(String dir){
        Path path = new Path(dir);
        try {
            FileSystem fs = path.getFileSystem(conf);
            fs.getFileStatus(path).getLen();
            if(fs.exists(path)){
                fs.delete(path,true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void listStatus(String dir) throws IOException {
        Path path = new Path(dir);
        //使用hdfs的api打印出结果
        FileSystem fs = path.getFileSystem(Tools.conf);
        // 列出所有文件和目录
        FileStatus[] statuses = fs.listStatus(path);
        for (FileStatus status : statuses) {
            System.out.println(status);
            System.out.println(status.getPath());
        }
    }

    public static void read(String outPutFilePath) throws IOException {
        Path path = new Path(outPutFilePath);
        //使用hdfs的api打印出结果
        FileSystem fs = path.getFileSystem(Tools.conf);
        InputStream is = fs.open(path);
        IOUtils.copyBytes(is, System.out, fs.getFileStatus(path).getLen(), true);
    }
}
