package com.hyh.sqlfactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * 描述:
 *
 * @Author hyh
 * @Date 2018/11/11
 */
public class MySqlSessionFactory {

    private String driver;
    private String url;
    private String name;
    private String password;
    private List<String> interfacePath;
    private List<String> sqlXmlFilePath;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getInterfacePath() {
        return interfacePath;
    }

    public void setInterfacePath(List<String> interfacePath) {
        this.interfacePath = interfacePath;
    }

    public List<String> getSqlXmlFilePath() {
        return sqlXmlFilePath;
    }

    public void setSqlXmlFilePath(List<String> sqlXmlFilePath) {
        this.sqlXmlFilePath = sqlXmlFilePath;
    }

    public MySqlSessionFactory() {
    }

    public MySqlSessionFactory(String driver, String url, String name, String password, List<String> interfacePath, List<String> sqlXmlFilePath) {
        this.driver = driver;
        this.url = url;
        this.name = name;
        this.password = password;
        this.interfacePath = interfacePath;
        this.sqlXmlFilePath = sqlXmlFilePath;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回一个SqlSession连接
     * @return
     */
    public MySqlSession openSession(){
        Connection connection =null;
        try {

             connection = DriverManager.getConnection(url,name,password);
            connection.setAutoCommit(false);//默认mybatis关闭了 自动提交
            MySqlSession mySqlSession = new MySqlSession(connection);
            return  mySqlSession;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param auto  表示框架是否自动提交事务
     * @return
     */
    public MySqlSession openSession(boolean auto){
        Connection connection =null;
        try {
            connection = DriverManager.getConnection(url,name,password);
            connection.setAutoCommit(auto);//默认mybatis关闭了 自动提交
            MySqlSession mySqlSession = new MySqlSession(connection);
            return  mySqlSession;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
