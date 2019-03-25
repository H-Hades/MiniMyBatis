package com.hyh.sqlfactory;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 描述:
 *
 * @Author hyh
 * @Date 2018/11/11
 */
public class MySqlSessionFactoryBuilder {

    /**
     * 用户会从Mybatis.xml读取配置，放到一个InputStream输入对象里面，
     * 该方法主要是从InputStream里面获取
     * driver, url, name, password
     * 接口或者sql映射文件的路径
     * @param in
     * @return
     */
    public MySqlSessionFactory build(Reader in){
        MySqlSessionFactory mySqlSessionFactory = new MySqlSessionFactory();
        if(in!=null){
            Scanner sc = new Scanner(in);
            ArrayList<String> list = new ArrayList<>();
            while(sc.hasNext()){
                list.add(sc.nextLine());
            }
            mySqlSessionFactory.setDriver(list.remove(0));
            mySqlSessionFactory.setUrl(list.remove(0));
            mySqlSessionFactory.setName(list.remove(0));
            mySqlSessionFactory.setPassword(list.remove(0));
            ArrayList<String> xmlPath = new ArrayList<>();
            if(!list.isEmpty()){
                Iterator<String> iterator = list.iterator();
                while(iterator.hasNext()){
                    xmlPath.add(iterator.next());
                }
                mySqlSessionFactory.setSqlXmlFilePath(xmlPath);
            }
        }
        return mySqlSessionFactory;
    }

   /* public static void main(String[] args) {
        ArrayList list = new ArrayList();
        for (int i = 0; i <10 ; i++) {
            list.add(i);
        }
        list.remove(1);list.remove(1);
        Object remove = list.remove(1);
        System.out.println(remove);
    }*/
}
