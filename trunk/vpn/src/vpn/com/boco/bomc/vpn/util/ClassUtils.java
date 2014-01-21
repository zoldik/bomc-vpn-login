package com.boco.bomc.vpn.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;


public class ClassUtils {
	
	/**
	 * 对象属性转换为字段  例如：userName to user_name
	 * @param property 字段名
	 * @return String
	 */
	public static String propertyToField(String property) {
		if (null == property) {
			return "";
		}
		char[] chars = property.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (char c : chars) {
			if (CharUtils.isAsciiAlphaUpper(c)) {
				sb.append("_" + StringUtils.lowerCase(CharUtils.toString(c)));
			} else {
				sb.append(c);
			}
		}
		return StringUtils.upperCase(sb.toString());
	}
	
	/**获取对象属性将其作为插入数据库中的属性标识,子类可以覆盖该方法*/
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getBeanProperties(Object entity){
		try {
			Map<String,Object> props = BeanUtils.describe(entity);
			/*
			Object obj = getId(entity);
			if(obj==null)
				throw new Exception("Exception when Fetching fields id of class["+entity.getClass()+"]");
			
			if(obj instanceof Number){
				if((Integer)obj<=0)
					props.remove("id");
			}
			*/
			props.remove("tableName");
			
			props.remove("class");
			return props;
		} catch (Exception e) {
			throw new RuntimeException("Exception when Fetching fields of " + e);
		}
	}
	
	//判断该类是否定义Id属性
	@SuppressWarnings("unchecked")
	public static Object getId(Object obj){
		return null; 
	}
	//获取该类定义的表名
	
	public static PropertyDescriptor[] propertyDescriptors(Class<?> c){
		// Introspector caches BeanInfo classes for better performance
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(c);

		} catch (IntrospectionException e) {
//			throw new Exception("Bean introspection failed: "
//					+ e.getMessage());
		}

		return beanInfo.getPropertyDescriptors();
	}
	
	
	public static void main(String[] args){
/*		System.out.println(ClassUtils.getTableName(Test.class));
		Test t = new Test();
		t.setId(22);
		
		System.out.println(t.getTableName());*/

	}
	
	public static List<Object>convert(List<Map<String,Object>> mapObjectList,Class<?>clazz) throws Exception{
		List<Object>objectList=new ArrayList<Object>();
		if(mapObjectList!=null&&mapObjectList.size()>0){
			for(Map<String,Object>mapObject:mapObjectList){
				Object object =clazz.newInstance();
				ClassUtils.setProperties(object, mapObject);
				objectList.add(object);
			}
		}
		return objectList;
	}
	private static Map<String,Field> getAllFields(Class<?> _class){
		if(_class==null){
			return null;
		}
		Map<String,Field> map=new HashMap<String,Field>();
		Field []fields=_class.getDeclaredFields();
		if(fields!=null){
			for(int i=0;i<fields.length;i++){
				map.put(fields[i].getName().toLowerCase(),fields[i]);
			}
		}
		Class<?> superClass=_class.getSuperclass();
		Map<String,Field> superFields=getAllFields(superClass);
		if(superFields!=null){
			map.putAll(superFields);
		}
		if(map.size()==0){
			return null;
		}
		return map;
	}
	public static void setProperties(Object bean,Map<String,Object>map) throws Exception{
		if(bean==null){
			return;
		}
		Map<String,Field>fieldMap=getAllFields(bean.getClass());
 
		for(String key:map.keySet()){
			Object value=map.get(key);
			Field field=fieldMap.get(key.toLowerCase());
			if(field!=null){
				if(value!=null&&value.getClass()!=java.util.Date.class
						&&value.getClass()!=java.sql.Timestamp.class){	
					Class<?> fieldType=field.getType();
					value=convert(value,fieldType);
				}			
				boolean old=field.isAccessible();
				field.setAccessible(true);
				field.set(bean, value);
				field.setAccessible(old);
			}
		}
	}

	public static List<String> getProperties(Class<?> clazz){
		if(clazz==null){
			return null;
		}

		List<String>list=new ArrayList<String>();
		PropertyDescriptor pds[]=PropertyUtils.getPropertyDescriptors(clazz);
		if(pds!=null){
			for(PropertyDescriptor pd:pds){
				String name=pd.getName().toLowerCase();
				if("class".equals(name)||"tablename".equals(name)||"s_codeflag".equals(name)){
					continue;
				}
				list.add(pd.getName());
				
			}
		}
		if(list.size()==0){
			return null;
		}
		return list;
	}
	public static List<Object>getValuesList(Object bean){
		if(bean==null){
			return null;
		}
		List<Object> valuesList=new ArrayList<Object>();
		List<String> namesList=getProperties(bean.getClass());
		for(String name:namesList){
			Object value=null;
			try {
				value = PropertyUtils.getProperty(bean, name);
			} catch (Exception e) {
			}  
			valuesList.add(value);
		}
 
		if(valuesList.size()>0){
			return valuesList;
		}else{
			return null;
		}
	}
	public static Map<String,Object>getValuesMap(Object bean,Boolean withNull){
		if(bean==null){
			return null;
		}
		Map<String,Object>valuesMap=getMap(bean,withNull);
		
		if(valuesMap!=null&&valuesMap.size()>0){
			return valuesMap;
		}else{
			return null;
		}
	}
	public static Map<String,Object> getValuesMapNullValue(Object bean){
		return getValuesMap(bean,null);
	}
	public static Map<String,Object> getValuesMapNotNullValue(Object bean){
		return getValuesMap(bean,false);
	}
	public static Map<String,Object> getValuesMapAllValue(Object bean){
		return getValuesMap(bean,true);
	}
	public static Map<String,Object> getTypesMap(Object bean){
		Map<String,Object>map=new HashMap<String,Object>();
		List<String> namesList=getProperties(bean.getClass());
		for(String name:namesList){
			String type=null;
			try {
				Class<?> clazz = PropertyUtils.getPropertyType(bean, name);
				type=clazz.getName();
			} catch (Exception e) {
			}  
 
			map.put(name, type);
		}
		if(map.size()>0){
			return map;
		}else{
			return null;
		}
	}
	private static String getIntStr(Object value){
		String str=value.toString();
		int end=str.indexOf(".");
		if(end!=-1){
			str=str.substring(0,end);
		}
		return str;
	}
	private static Object convert(Object value,Class<?>type){
		if(type==java.lang.Integer.class){
			return new Integer(getIntStr(value));
		}else if(type==java.lang.Long.class){
			return new Long(getIntStr(value));
		}else if(type==java.lang.Byte.class){
			return new Byte(getIntStr(value));
		}else if(type==java.lang.Short.class){
			return new Short(getIntStr(value));
		}else if(type==java.lang.Boolean.class){
			Integer i=new Integer(getIntStr(value));
			if(i==0)
				return Boolean.FALSE;
			else
				return Boolean.TRUE;
			
		}else{
			return ConvertUtils.convert(value.toString(),type);
		}
		
	}
	public static void copyMapToBean(Map<String,Object>map,Object bean) throws Exception{
		List<String> namesList=getProperties(bean.getClass());
		for(String name:namesList){
			Class<?> type=PropertyUtils.getPropertyType(bean, name);
			Object value=map.get(name.toLowerCase());
			if(value!=null&&value.toString().trim().length()>0){
				if(value.getClass()!=type){
				value=convert(value,type);
				}
			}
			PropertyUtils.setProperty(bean, name, value);
		}
	}
	private static Map<String,Object> getMap(Object bean,Boolean withNull){
		if(bean instanceof HashMap){
			return (HashMap)bean;
		}else{
			Map<String,Object>map=new HashMap<String,Object>();
			List<String> namesList=getProperties(bean.getClass());
			for(String name:namesList){
				Object value=null;
				try {
					value = PropertyUtils.getProperty(bean, name);
				} catch (Exception e) {
				}  
				if(withNull!=null){
					if(withNull==false&&value==null){
						continue;
					}
				}else{
					if(value!=null){
						continue;
					}
				}
				map.put(name, value);
			}
			if(map.size()>0){
				return map;
			}else{
				return null;
			}			
		}
	}
}
