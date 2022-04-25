package org.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


import java.io.IOException;

public class HDFSDemo {
    private static Configuration configuration=null;
    private static FileSystem fs=null;

    public void connect(){

        try {
            configuration = new Configuration();
//            configuration.set("fs.defaultFS","hdfs://ns1");
            fs = FileSystem.get(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void mkdir(){
        try {
            if(!fs.exists(new Path("/test"))){
                fs.mkdirs(new Path("/test"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void fileUpToHDFS(String srcPath,String dePath){
        try {
            fs.copyFromLocalFile(new Path(srcPath),new Path(dePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void close(){
        try {
            if(fs!=null){
                fs.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
