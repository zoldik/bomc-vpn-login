package com.boco.bomc.vpn.db.impl.oracle;


import java.io.Reader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.boco.bomc.vpn.util.ClassUtils;


public class ResultSetUtils {

    protected static Log log = LogFactory.getLog(ResultSetUtils.class);
    public static Map<String,Object> getMap(ResultSet resultSet)throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols = metaData.getColumnCount();
        Map<String,Object> row = new HashMap<String,Object>();
        if (resultSet.next()) {
            for (int i = 1; i <= cols; i++) {
            	putEntry(row,metaData,resultSet,i);
            }
        }
        return row;
    } 

    public static List<Map<String,Object>> getMaps(ResultSet resultSet,int start,int count)throws SQLException {
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	if (moveAbsolute(resultSet, start+1)&&count>0) {
	        ResultSetMetaData metaData = resultSet.getMetaData();
	        int cols = metaData.getColumnCount();
	        
	        int pos=0;
	        do {
	        	pos++;
	        	Map<String,Object> row = new HashMap<String,Object>();
	            for (int i = 1; i <= cols; i++) {
	            	putEntry(row,metaData,resultSet,i);
	            }
	            list.add(row);
	        } while (resultSet.next()&&pos<count);
	        
	    }
    	return list;
    } // end getMaps
    
    public static List<Map<String,Object>> getMaps(ResultSet resultSet)throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols = metaData.getColumnCount();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        while (resultSet.next()) {
        	Map<String,Object> row = new HashMap<String,Object>();
            for (int i = 1; i <= cols; i++) {
            	putEntry(row,metaData,resultSet,i);
            }
            list.add(row);
        } // end while
        return list;
    } // end getMaps
    public static void putEntry(Map<String,Object> properties,ResultSetMetaData metaData,ResultSet resultSet,String columnName,int i)throws SQLException {
        if (resultSet.getObject(i) == null) {
            properties.put(columnName, null);
        } else {
        	
            switch (metaData.getColumnType(i)) {
                case Types.DATE:   	
                case Types.TIME:
                case Types.TIMESTAMP:
                	java.util.Date timestamp=new java.util.Date(resultSet.getTimestamp(i).getTime());
                    properties.put(columnName,timestamp);
                    break;
                case Types.NUMERIC:
                case Types.DECIMAL:
                case Types.DOUBLE:
                case Types.REAL:
                case Types.FLOAT:
                	String str=resultSet.getObject(i)+"";
                	if(str.indexOf(".")>=0){
                		properties.put(columnName,new Float(str));
                	}else{
                		properties.put(columnName,new Long(str));
                	}
                    break;
                case Types.SMALLINT:
                case Types.TINYINT:
                case Types.INTEGER:
                case Types.BIGINT:
                    properties.put(columnName,new Long(resultSet.getObject(i)+""));
                    break;
                case Types.CHAR:
                case Types.CLOB:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                case Types.BLOB:
                case Types.LONGVARBINARY:
                case Types.VARBINARY:
                    if(resultSet.getObject(i).getClass()==oracle.sql.CLOB.class){
                    	String back=dumpClob((oracle.sql.CLOB)resultSet.getObject(i));
                    	properties.put(columnName,back);
                    }if(resultSet.getObject(i).getClass()==oracle.sql.BLOB.class){
                    	properties.put(columnName,resultSet.getObject(i));
                    }else{
                    	log.debug("resultSet.getObject(i).getClass()="+resultSet.getObject(i).getClass().getName());
                    	properties.put(columnName,resultSet.getString(i));
                    }
                    break;
                case Types.NULL:
                	properties.put(columnName,null);
                	break;
                    /*
                    :FIXME: Add handlers for
                    ARRAY
                    BINARY
                    BIT
                    DISTINCT
                    JAVA_OBJECT
                    OTHER
                    REF
                    STRUCT
                    */

                    // Otherwise, pass as *String property to be converted
                default:
                    properties.put(columnName,resultSet.getObject(i));
                    break;
            } // end switch
        } // end if result == null

    } // end putEntry

	private static String dumpClob( oracle.sql.CLOB   clob){
		String str=null;
		try{
			Reader   instream   =   clob.getCharacterStream();    
			long   len=clob.length();   
			Long   _len=new   Long(len);   
			int   i_len=_len.intValue();   
			char[]   buffer   =   new   char[i_len];     
			instream.read(buffer);
			str=new   String(buffer);   
			instream.close();	
		}catch(Exception e){
			
		}
		   
		return str;
	}
 
    public static void putEntry(Map<String,Object> properties,ResultSetMetaData metaData,ResultSet resultSet,int i)throws SQLException {
        String columnName = metaData.getColumnName(i).toLowerCase();
        putEntry(properties,metaData,resultSet,columnName,i);
    } 
    @SuppressWarnings("unchecked")
	public static <T>List<T>  getList(Class<?> targetClass, ResultSet resultSet,int start,int count)throws Exception {
        if ((targetClass == null) || (resultSet == null))
            throw new SQLException("getCollection: Null parameter");
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols = metaData.getColumnCount();
        Map<String,Object> row = new HashMap<String,Object>();
        List<T> list = new ArrayList<T>();
        
    	if (moveAbsolute(resultSet, start+1)&&count>0) {
    		int pos=0;
	        do {
	        	pos++;
	            for (int i = 1; i <= cols; i++) {
	                putEntry(row, metaData, resultSet, i);
	            }
	            try {
	                Object bean = targetClass.newInstance();
	                ClassUtils.copyMapToBean(row, bean);                
	                list.add((T)bean);
	            } catch (Exception e) {
	                throw e;
	            }
	            row.clear();
	
	        } while (resultSet.next()&&pos<count);
    	}
        return list;
    } // end getCollection
    @SuppressWarnings("unchecked")
	public static <T>List<T>  getList(Class<?> targetClass, ResultSet resultSet)throws Exception {

        // Check prerequisites
        if ((targetClass == null) || (resultSet == null))
            throw new SQLException("getCollection: Null parameter");

        // Acquire resultSet MetaData
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols = metaData.getColumnCount();

        Map<String,Object> row = new HashMap<String,Object>();
        // Use ArrayList to maintain ResultSet sequence
        List<T> list = new ArrayList<T>();
 
        // Scroll to next record and pump into hashmap
        while (resultSet.next()) {
            for (int i = 1; i <= cols; i++) {
                putEntry(row, metaData, resultSet, i);
            }
            try {
                Object bean = targetClass.newInstance();
                ClassUtils.copyMapToBean(row, bean);                
                list.add((T)bean);
            } catch (Exception e) {
                throw e;
            }
            row.clear();

        } // end while
        return list;
    } // end getCollection
    public static boolean moveAbsolute(ResultSet rs, int index) throws SQLException {
        return rs.absolute(index);
    }
    public static int getResultCount(ResultSet rs) throws SQLException {

        int rows = 0;
        boolean beforeFirst = rs.isBeforeFirst();
        boolean afterLast = rs.isAfterLast();
        int currIndex = rs.getRow();

        if (rs.last()) {
            // set the scroller parameters
            rows = rs.getRow();
        }

        // now move the cursor at its original position;
        if (beforeFirst)
            rs.beforeFirst();

        else if (afterLast)
            rs.afterLast();

        else if (currIndex != 0)
            rs.absolute(currIndex);

        return rows;
    }
}  

