package com.boco.bomc.vpn.db.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * select 命令的标准格式如下
 * 
 * 在sql命令格式使用的先后顺序上group by 先于 order by 
 * SELECT select_list [ INTO new_table ] 
 * FROM table_source [
 * WHERE search_condition ] 
 * [ GROUP BY group_by_expression ] 
 * [ HAVING search_condition ]
 * 
 * @author sanghg
 * 
 */
public class QueryCondition implements java.io.Serializable{
	
	private Where where = new Where();
	private Group group = new Group();
	private Order order = new Order();

	private StringBuffer sb = new StringBuffer("");
	
	public QueryCondition(){
	}
	
	public Where where(){
		if(where.whereSet.size()==0){
			sb.append(" WHERE ");
		}
		return where;
	}
	
	public Group group(){
		if(group.groupSet.size()==0){
			sb.append(" GROUP BY ");
		}
		return group;
	}

	public Order order(){
		if(order.orderSet.size()==0){
			sb.append(" ORDER BY ");
		}
		return order;
	}
	
	public String toSQLString(){
		return sb.toString();
	}
	
	public class Where{
		private Set<String> whereSet = new HashSet<String>();
		public void and(String... sqls){
			if(sqls!=null){
				for(String str:sqls){
					if(whereSet.size()==0){
						sb.append(str);
					}else{
						sb.append(" AND "+str+" ");
					}
					whereSet.add(str);
				}
			}
		}
		public void or(String... sqls) throws Exception{
			if(sqls!=null){
				for(String str:sqls){
					if(whereSet.size()==0&&sqls.length==1){
						throw new Exception("当前Where中无内容不能直接使用OR条件!");
					}else{
						sb.append(" OR "+str+" ");
					}
					whereSet.add(str);
				}
			}
			
		}
	}
	
	private class Group{
		private Set<String> groupSet = new HashSet<String>();
		public void group(String... columns){
			if(columns!=null){
				for(String column:columns){
					if(groupSet.size()==0){
						sb.append(column+" ");
					}else{
						sb.append(","+column+" ");
					}
					groupSet.add(column);
				}
			}
		}
	}
	
	private class Order{
		private Set<String> orderSet = new HashSet<String>();
		public void desc(String... columns){
			if(columns!=null){
				for(String column:columns){
					if(orderSet.size()==0){
						sb.append(column+" ");
					}else{
						sb.append(","+column+" ");
					}
					orderSet.add(column);
				}
				sb.append(" DESC");
			}
		}
		
		public void asc(String...columns){
			if(columns!=null){
				for(String column:columns){
					if(orderSet.size()==0){
						sb.append(column+" ");
					}else{
						sb.append(","+column+" ");
					}
					orderSet.add(column);
				}
				sb.append(" ASC");
			}
		}
	}
}




