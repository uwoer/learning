package com.uwola;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * map reducer  helloword
 */
public class MapReduceWordCountApp {

    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
//            StringTokenizer itr = new StringTokenizer(value.toString());
            StringTokenizer itr = new StringTokenizer(value.toString()," ");
            while (itr.hasMoreTokens()) {
                String str = itr.nextToken();
                if(str.matches("\\w+")){
                    word.set(str);
                    context.write(word, one);
                }
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

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.err.println("Usage: MaxTemperature <input path> <output path>");
                System.exit(-1);
            }
            //得到一个Job 并设置名字
            Job job = Job.getInstance(Tools.conf, "hadoop map reduce word count app");
            //设置Jar 使本程序在Hadoop中运行
            job.setJarByClass(MapReduceWordCountApp.class);
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
            //删除输出目录
            Tools.delDir(args[1]);

            // 设置输出格式
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            // 等待作业完成后退出
            //System.exit(job.waitForCompletion(true) ? 0 : 1);
            if(job.waitForCompletion(true)){
                System.out.println("执行完成...");
                //显示output下文件目录
                Tools.listStatus(args[1]);
                // 显示output下指定文件的内容
                Tools.read(args[1] +"/part-r-00000");
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
