package com.boco.bomc.vpn.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.boco.bomc.vpn.db.exception.DBException;
import com.boco.bomc.vpn.db.exception.DaoException;

/**
 * 数据库管理
 */
public class DBManager {

	private final static Log log = LogFactory.getLog(DBManager.class);
	private static ThreadLocal<Connection> conns = new ThreadLocal<Connection>();
	private static DataSource dataSource;
	private static boolean show_sql = false;
	
	private static int count = 0;
	
	static {
		initDataSource(null);
	}

	/**
	 * 初始化连接池
	 * @param props
	 * @param show_sql
	 */
	private final static void initDataSource(Properties dbProperties) {
		try {
			if(dbProperties == null){
				dbProperties = new Properties();
				dbProperties.load(DBManager.class.getResourceAsStream("/vpn.properties"));
			}
			Properties cp_props = new Properties();
			for(Object key : dbProperties.keySet()) {
				String skey = (String)key;
				if(skey.startsWith("jdbc.")){
					String name = skey.substring(5);
					cp_props.put(name, dbProperties.getProperty(skey));
					if("show_sql".equalsIgnoreCase(name)){
						show_sql = "true".equalsIgnoreCase(dbProperties.getProperty(skey));
					}
				}
			}
			dataSource = (DataSource)Class.forName(cp_props.getProperty("datasource")).newInstance();
			if(dataSource.getClass().getName().indexOf("c3p0")>0){
				//Disable JMX in C3P0
				System.setProperty("com.mchange.v2.c3p0.management.ManagementCoordinator", 
						"com.mchange.v2.c3p0.management.NullManagementCoordinator");
			}
			log.info("Using DataSource : " + dataSource.getClass().getName());
			BeanUtils.populate(dataSource, cp_props);

			Connection conn = getConnection();
			DatabaseMetaData mdm = conn.getMetaData();
			log.info("Connected to " + mdm.getDatabaseProductName() + 
                              " " + mdm.getDatabaseProductVersion());
			closeConnection();
		} catch (Exception e) {
			throw new DBException(e);
		}
	}
	
	/** 开启事务 */  
    public static void beginTransaction() throws DaoException{  
        try {  	
        	getConnection().setAutoCommit(false);  //把事务提交方式改为手工提交  
        } catch (SQLException e) {  
            throw new DBException("开户事务时出现异常");  
        }  
    }  
    
    /** 提交事务并关闭连接 */  
    public static void commitAndClose() throws DaoException{  
        try {  
        	getConnection().commit(); //提交事务  
        } catch (SQLException e) {  
            throw new DBException("提交事务时出现异常");  
        }finally{  
        	closeConnection();  
        }  
    }  
      
    /** 回滚并关闭连接 */  
    public static void rollbackAndClose()throws DaoException{  
        try {  
        	getConnection().rollback();  
        } catch (SQLException e) {  
            throw new DBException("回滚事务时出现异常",e);  
        }finally{  
        	closeConnection();  
        }  
    }  
	
	/**
	 * 断开连接池
	 */
	public final static void closeDataSource(){
		try {
			dataSource.getClass().getMethod("close").invoke(dataSource);
		} catch (NoSuchMethodException e){ 
		} catch (Exception e) {
			log.error("Unabled to destroy DataSource!!! ", e);
		}
	}

 	public final static Connection getConnection() throws SQLException {
		Connection conn = conns.get();
		
		if(conn ==null || conn.isClosed()){
			conn = dataSource.getConnection();
			count++;
			
			conns.set(conn);
			log.info("Connection :"+conn);
		}
		log.debug("获取连接["+conn.getClass()+"]，当前连接数:"+count);
		return (show_sql && !Proxy.isProxyClass(conn.getClass()))?new _DebugConnection(conn).getConnection():conn;
	}
	
	/**
	 * 关闭连接
	 */
	public final static void closeConnection() {
		Connection conn = conns.get();
		try {
			if(conn != null && !conn.isClosed()){
				conn.setAutoCommit(true);
				count--;
				conn.close();
			}
		} catch (SQLException e) {
			log.error("Unabled to close connection!!! ", e);
		}
		log.debug("释放连接"+conn.getClass()+"]，当前连接数:"+count);
		conns.set(null);
	}

	/**
	 * 用于跟踪执行的SQL语句
	 * @author Winter Lau
	 */
	static class _DebugConnection implements InvocationHandler {
		
		private final static Log log = LogFactory.getLog(_DebugConnection.class);
		
		private Connection conn = null;

		public _DebugConnection(Connection conn) {
			this.conn = conn;
		}

		/**
		 * Returns the conn.
		 * @return Connection
		 */
		public Connection getConnection() {
			return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), 
                             conn.getClass().getInterfaces(), this);
		}
		
		public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
			try {
				String method = m.getName();
				if("prepareStatement".equals(method) || "createStatement".equals(method)){
					log.info("[SQL] >>> " + Arrays.deepToString(args));
				}
				return m.invoke(conn, args);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}

	}
	
	public static void main(String[] args){
		DBManager m =new DBManager();
	}
}