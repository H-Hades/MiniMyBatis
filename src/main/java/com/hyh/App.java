package com.hyh;

import com.hyh.sqlfactory.MySqlSession;
import com.hyh.test.Utils.MyBatisTestUtils;
import com.hyh.test.entity.User;
import com.hyh.test.dao.UserMapper;

import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        MySqlSession session = MyBatisTestUtils.openSession();

        try{
            UserMapper sm = session.getMapper(UserMapper.class);
//            sm.insert("hyh",8,100.0);
            User user = (User) sm.selectById(9);
            sm.selectById(9);
            System.out.println(user);
            ArrayList<User> select = sm.select();
            sm.select();
            System.out.println(select);

        }finally {
            session.commit();
            session.close();
        }
    }
}
