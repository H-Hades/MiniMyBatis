package com.hyh.parsexml;

import
        org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


/**
 * 描述: 实现xml文件的解析类
 *
 * @Author hyh
 * @Date 2018/11/11
 */
public class Resources {
    private static void write(String resource){
        File temp = null;
        Writer w = null;
        try {
            temp = new File("temp");
            temp.createNewFile();
            w = new FileWriter(temp);
            String  filepath = Resources.class.getClassLoader().getResource(resource).getPath();
            try {
                filepath = URLDecoder.decode(filepath,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            SAXReader reader = new SAXReader();

            Document document = null;
            try {
                document = reader.read(new File(filepath));
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            String xpathtable = "//configuration//environments//environment//dataSource//property";
           write(document,w,xpathtable,"value");
           xpathtable="//configuration//mappers//mapping";
           write(document,w,xpathtable,"resource");
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void write(Document document,Writer writer,String xpathtable,String goal){
        List<String> list = document.selectNodes(xpathtable);

        if(list!=null){
            Iterator iter = list.iterator();
            while(iter.hasNext()){
                Element element = (Element) iter.next();
                String value = element.attributeValue(goal);
                try {
                    writer.write(value+"\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static Reader getRecourceAsReader(String resource){
        File temp = null;
        File f = null;
        Reader r = null;
        try {
            f = File.createTempFile("temp","txt");
                temp = new File("temp");
                temp.createNewFile();
                write(resource);
                r  = new InputStreamReader(new FileInputStream(temp));
//

        return  r;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static void main(String[] args) {
        Reader recourceAsReader = Resources.getRecourceAsReader("com/hyh/test/config/mybatis.xml");
        Scanner sc = new Scanner(recourceAsReader);
        while(sc.hasNext()){
            System.out.println(sc.nextLine());
        }

    }

}
