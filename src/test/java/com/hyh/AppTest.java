package com.hyh;

import static org.junit.Assert.assertTrue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

/**
 * Unit test for simple App.
 * 项目二：实现一个Mini版的myBaits框架
 * 要求如下：
 * 1. 实现SqlSessionFactoryBuilder SqlSessionFactory SqlSession三个类的实现
 * 2. 实现基于注解的MyBatis框架
 * 3. 实现MyBatis框架一级缓存
 * 4. 熟练掌握XML的解析过程
 *
 * 扩展：
 * 1. 实现基于解析Sql映射文件
 * 2. 基于Mapper的二级缓存机制
 */
public class AppTest
{
    public static void main(String[] args) throws Exception{
//        String resource = "mybatis.xml";

        String resource = "src/main/com.hyh.test.config/mybatis.xml";
        InputStream in = null;
        //读取数据库配置文件config.xml的数据，
        //包含了事务配置，连接池配置，mysqld连接信息配置，mybatis xml文件路径信息
        in = AppTest.class.getClassLoader().getResourceAsStream(resource);

        // java 处理xml文件
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document d = builder.parse(in);

        Element root = d.getDocumentElement();

        System.out.println(root.getTagName());

    }
}
