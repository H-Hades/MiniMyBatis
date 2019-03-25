package com.hyh.test.Utils;

import com.hyh.parsexml.Resources;
import com.hyh.sqlfactory.MySqlSession;
import com.hyh.sqlfactory.MySqlSessionFactory;
import com.hyh.sqlfactory.MySqlSessionFactoryBuilder;

import java.io.Reader;

public class MyBatisTestUtils {

    public void hyh(){
        String resourse = "com/hyh/test/config/mybatis.xml";
        Reader recourceAsReader = Resources.getRecourceAsReader(resourse);
        MySqlSessionFactory sqlMapper = (MySqlSessionFactory) new MySqlSessionFactoryBuilder().build(recourceAsReader);
        MySqlSession sqlSession = sqlMapper.openSession();
        sqlSession.selectOne("User.selectById",1);
    }
    public static MySqlSession openSession(){
        String resourse = "com/hyh/test/config/mybatis.xml";
        Reader recourceAsReader = Resources.getRecourceAsReader(resourse);
        MySqlSessionFactory sqlMapper = (MySqlSessionFactory) new MySqlSessionFactoryBuilder().build(recourceAsReader);
        MySqlSession sqlSession = sqlMapper.openSession();
        return  sqlSession;
    }
}
