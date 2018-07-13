package com.uwola;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.StringTokenizer;

/**
 * map reducer  helloword
 */
public class WordCount2 {

    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }

    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();
        @Override
        public void reduce(Text key, Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    private static final String IP_MASTER = "192.168.40.130";
    public static void main(String[] args) {
        System.setProperty("HADOOP_USER_NAME","root");
        try {
            if (args.length != 2) {
                System.err.println("Usage: MaxTemperature <input path> <output path>");
                System.exit(-1);
            }
            Configuration conf = new Configuration();

            // 每个人的maven仓库的路径不一样，这里使用你的MAVEN仓库路径，比如我的MAVEN就暴露出了盘很多，报名为me.j360.hadoop，F:\\Maven\\repo
            // 如果缺少这一句，会提示ClassNotFound
            conf.set("mapred.jar", "D:\\github\\hadoop\\target\\hadoop-1.0-SNAPSHOT.jar");

            // 以下都是默认配置，端口号和hadoop集群的配置必须要一致
            // 配置使用跨平台提交任务
            conf.setBoolean("mapreduce.app-submission.cross-platform", true);
            //指定namenode
            conf.set("fs.defaultFS", "hdfs://"+IP_MASTER+":9000");
            // 指定使用yarn框架
            conf.set("mapreduce.framework.name", "yarn");
            // 指定resourcemanager
            conf.set("yarn.resourcemanager.address", IP_MASTER+":8032");
            // 指定资源分配器
            conf.set("yarn.resourcemanager.scheduler.address", IP_MASTER+":8030");
            conf.set("mapreduce.jobhistory.address",IP_MASTER+":10020");



            //得到一个Job 并设置名字
            Job job = Job.getInstance(conf, "word count");
            //设置Jar 使本程序在Hadoop中运行
            job.setJarByClass(WordCount2.class);
            //设置Map处理类
            job.setMapperClass(TokenizerMapper.class);
            job.setCombinerClass(IntSumReducer.class);
            //设置Reduce处理类
            job.setReducerClass(IntSumReducer.class);
            //设置输入和输出目录
            //在运行作业之前，输出目录不能存在，这是为了避免覆盖数据导致数据丢失
            // 运行之前如果检测到目录已经存在，作业将无法运行
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            // 设置输出格式
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            // 等待作业完成后退出
//            System.exit(job.waitForCompletion(true) ? 0 : 1);
            if(job.waitForCompletion(true)){
                System.out.println("执行完成...");

                //使用hdfs的api打印出结果
                FileSystem fs = FileSystem.get(URI.create("hdfs://"+IP_MASTER+":9000"), conf);

                // 列出hdfs上/tmp/output/目录下的所有文件和目录
                FileStatus[] statuses = fs.listStatus(new Path(args[1]));
                for (FileStatus status : statuses) {
                    System.out.println(status);
                    System.out.println(status.getPath());

                }

                // 显示在hdfs的/tmp/output下指定文件的内容
                InputStream is = fs.open(new Path(args[1] +"/part-r-00000"));
                IOUtils.copyBytes(is, System.out, 1024, true);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
