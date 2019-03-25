package com.hyh.sqlfactory;


import com.hyh.annotation.MyInsert;
import com.hyh.annotation.MySelect;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述:
 *
 * @Author hyh
 * @Date 2018/11/11
 */
public class MySqlSession {

    // 在这里实现MyBatis框架的一级缓存机制

    private Connection connection;
    private ConcurrentHashMap<String, Object> firstLevelCache;
    static{
        // 加载jdbc驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public MySqlSession(Connection connection) {
        firstLevelCache = new ConcurrentHashMap<>();
        this.connection = connection;
    }

    /**
     * 这个是框架动态代理的核心入口
     * @param type
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> type){
        if(!type.isInterface()){
            throw new IllegalArgumentException(type.toString() + " is not an interface!");
        }
        return (T) Proxy.newProxyInstance(MySqlSession.class.getClassLoader(),
                new Class[]{type}, new MyHandler());
    }
    /**
     * 关闭连接SqlSession
     */
    public void close(){
        try {
            if(connection != null) {
                this.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    public void commit(){
        try {
            if(connection != null){
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回滚事务
     */
    public void rollback(){
        try {
            if(connection != null){
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String  changeQuestionMark(String sql){
        for(;;){
                int beginIndex = sql.indexOf("#{");
                if(beginIndex == -1){
                    break;
                }
                int endIndex = sql.indexOf("}", beginIndex);
                sql = sql.substring(0, beginIndex) + "?" + sql.substring(endIndex+1);
        }
        return sql;

    }
    public Object select(Method method, String sql, Object[] args){
        boolean tag = false;
        sql = changeQuestionMark(sql);

        Iterator<Map.Entry<String, Object>> iterator = firstLevelCache.entrySet().iterator();


            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                if(args!=null){
                    for (int i = 0; i < args.length; i++) {
                        ps.setObject(1+i,args[i]);
                    }
                 }
                String s = ps.toString();

                String substring = s.substring(s.indexOf(":")+2);
                while(iterator.hasNext()){
                    Map.Entry<String, Object> next = iterator.next();
                    if (next.getKey().equals(substring)){
                        tag=true;
                    }
                }
               if(!tag){
                   System.out.println(substring);
                   ResultSet rs = ps.executeQuery();


                   Class<?> returnType = method.getReturnType();
                   Type genericReturnType = method.getGenericReturnType();
                   //判断其是否为泛型化的字段
                   if(genericReturnType instanceof ParameterizedType){
                       //获得其中的MyMyBatis.Student
                       returnType = (Class) ((ParameterizedType)genericReturnType).getActualTypeArguments()[0];                Field[] fields = returnType.getDeclaredFields();
                       ArrayList<Object> objects = new ArrayList<>();
                       while(rs.next()){
                           Object object = returnType.newInstance();
                           for (Field field : fields) {
                               field.setAccessible(true);
                               Object value = rs.getObject(field.getName());
                               field.set(object,value);
                           }
                           objects.add(object);
                       }
                       firstLevelCache.put(substring,objects);
                       return objects;
                   }

                   Field[] fields = returnType.getDeclaredFields();
                   Object object = returnType.newInstance();
                   if (rs.next()){
                       for (Field field : fields) {
                           field.setAccessible(true);
                           Object value = rs.getObject(field.getName());
                           field.set(object,value);
                       }
                   }

                   firstLevelCache.put(substring,object);
                   return object;
               }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }



        return firstLevelCache.get(sql);
    }
    private int alter(String sql, Object[] args){
        {
            if(!firstLevelCache.isEmpty()){
                firstLevelCache.clear();
            }
            sql = changeQuestionMark(sql);
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sql);
                for (int i = 0; i < args.length; i++) {
                    ps.setObject(1+i,args[i]);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                return ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }
    /*private  int insert(String sql, Object[] args){
        firstLevelCache.clear();
        sql = changeQuestionMark(sql);
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(1+i,args[i]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
        return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int update(String sql, Object[] args){
        firstLevelCache.clear();

        sql = changeQuestionMark(sql);
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(1+i,args[i]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;

    }
    public int delete(String sql, Object[] args){
        firstLevelCache.clear();
        sql = changeQuestionMark(sql);
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(1+i,args[i]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }*/

    public void selectOne(String s, Object i) {
    }

    class  MyHandler implements InvocationHandler{

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Annotation[] anns = method.getDeclaredAnnotations();
            for (Annotation ann : anns) {

                if(ann instanceof MySelect){
                    if(((MySelect) ann).value() == null){
                        return null;
                    }
                    return select(method,((MySelect) ann).value(),args);
                }else{
                        if(((MyInsert) ann).value() == null){
                            return null;
                        }
                        return alter(((MyInsert) ann).value(),args);
                }
                /*else if (ann instanceof MyInsert){
                    if(((MyInsert) ann).value() == null){
                        return null;
                    }
                    return insert(((MyInsert) ann).value(),args);
                }else if(ann instanceof MyDelete){
                    if(((MyDelete) ann).value() == null){
                        return null;
                    }
                    return delete(((MyDelete) ann).value(),args);
                }else if (ann instanceof MyUpdate){
                    if(((MyUpdate) ann).value() == null){
                        return null;
                    }
                    return update(((MyUpdate) ann).value(),args);
                }*/
            }
            return null;
        }
    }
    class MapperMethed {

    }
}
