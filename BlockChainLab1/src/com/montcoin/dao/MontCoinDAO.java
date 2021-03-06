/*Shivam Agarwal
 * shivamag@buffalo.edu
 * UBIT 50289132
 * */
package com.montcoin.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.montcoin.pojo.Customer;
import com.montcoin.pojo.Items;
import com.mysql.cj.jdbc.MysqlDataSource;

public class MontCoinDAO {

	JdbcTemplate temp;
	
	public static DataSource getMysqlDataSource() {
	    MysqlDataSource dataSource = new MysqlDataSource();

	    // Set dataSource Properties
	    dataSource.setServerName("localhost");
	    dataSource.setPortNumber(3306);
	    dataSource.setDatabaseName("sqldb");
	    dataSource.setUser("root");
	    dataSource.setPassword("123456");
	    return dataSource;
	  }
	
	public void setTemplate(JdbcTemplate template) {    
	    this.temp = template;    
	}   
	
	public int createCustomer(String name, String pass){
		MontCoinDAO dao = new MontCoinDAO();
		JdbcTemplate temp = new JdbcTemplate();
		temp.setDataSource(dao.getMysqlDataSource());
		
		String sql = "select MAX(accountNumber) from sqldb.member";
		Integer cust = temp.queryForObject(sql,Integer.class);
		int accNo = cust.intValue() + 1;
	    sql="insert into sqldb.member(accountNumber,name,token,password) values("+accNo+",'"+name+"',500,'"+pass+"')";    
	    temp.update(sql);
	    return accNo;
	}    
	
	public Customer getCustomer(int accno, String pass) throws SQLException {
		MontCoinDAO dao = new MontCoinDAO();
		JdbcTemplate temp = new JdbcTemplate();
		temp.setDataSource(dao.getMysqlDataSource());
		String sql = "select * from sqldb.member where accountNumber ="+accno+" and password ='"+pass+"'";
		return temp.queryForObject(sql, new Object[] {},new BeanPropertyRowMapper<Customer>(Customer.class));
	}
	
	public List<Items> getItems() {
		MontCoinDAO dao = new MontCoinDAO();
		JdbcTemplate temp = new JdbcTemplate();
		temp.setDataSource(dao.getMysqlDataSource());
		
		String sql = "select * from sqldb.items";
		return temp.query(sql, new RowMapper<Items>() {
			 public Items mapRow(ResultSet rs, int row) throws SQLException {    
		            Items item = new Items();    
		            item.setItemId(rs.getInt(1));    
		            item.setDetails(rs.getString(2));    
		            item.setPrice(rs.getDouble(3));    
		            item.setSold(rs.getString(4));
		            item.setSeller(rs.getInt(5));
		            return item;    
		        }    
		});
	}
	
	public int purchaseItem(Customer cust, Items item){  
		
		MontCoinDAO dao = new MontCoinDAO();
		JdbcTemplate temp = new JdbcTemplate();
		temp.setDataSource(dao.getMysqlDataSource());
		
		try {
			//subtract amount from buyer
			String sql = "update sqldb.member set token ="+(cust.getToken() - item.getPrice())+" where accountNumber="+cust.getAccountNumber();
			temp.update(sql);
			//mark item as sold
			sql = "update sqldb.items set sold = 'yes', buyer = "+cust.getAccountNumber()+" where itemid="+item.getItemId();
			temp.update(sql);
			//add token to the seller
			sql = "select * from sqldb.member where accountNumber ="+item.getSeller();
			Customer seller = temp.queryForObject(sql, new BeanPropertyRowMapper<Customer>(Customer.class));
			sql = "update sqldb.member set token ="+(seller.getToken() + item.getPrice())+" where accountNumber="+seller.getAccountNumber();
			return temp.update(sql);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	    return 0;    
	}   
	
	public int sellItem(Items item){
		MontCoinDAO dao = new MontCoinDAO();
		JdbcTemplate temp = new JdbcTemplate();
		temp.setDataSource(dao.getMysqlDataSource());
		
		try {
			String sql = "select MAX(itemid) from sqldb.items";
			Integer itemId = temp.queryForObject(sql,Integer.class);
			item.setItemId(itemId.intValue() + 1);
		    sql="insert into sqldb.items(itemid,detail,price,sold,seller) values("+item.getItemId()+",'"+item.getDetails()+"',"+item.getPrice()+",'no',"+item.getSeller()+")";    
		    return temp.update(sql);    
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public int buyTokens(int accountNumber, double token) {
		MontCoinDAO dao = new MontCoinDAO();
		JdbcTemplate temp = new JdbcTemplate();
		temp.setDataSource(dao.getMysqlDataSource());
		
		try {
			String sql = "update sqldb.member set token ="+token+" where accountNumber="+accountNumber;
			return temp.update(sql);    
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}    
	
	public List<Items> listProducts(int accountNumber) {
		MontCoinDAO dao = new MontCoinDAO();
		JdbcTemplate temp = new JdbcTemplate();
		temp.setDataSource(dao.getMysqlDataSource());
		
		String sql = "select * from sqldb.items where buyer="+accountNumber;
		return temp.query(sql, new RowMapper<Items>() {
			 public Items mapRow(ResultSet rs, int row) throws SQLException {    
		            Items item = new Items();    
		            item.setItemId(rs.getInt(1));    
		            item.setDetails(rs.getString(2));    
		            item.setPrice(rs.getDouble(3));    
		            item.setSold(rs.getString(4));
		            item.setSeller(rs.getInt(5));
		            return item;    
		        }    
		});
	}
	
	public int deleteAccount(int accno) throws SQLException {
		MontCoinDAO dao = new MontCoinDAO();
		JdbcTemplate temp = new JdbcTemplate();
		temp.setDataSource(dao.getMysqlDataSource());
		String sql = "delete from sqldb.items where seller ="+accno+" and sold ='no'";
		temp.execute(sql);
		sql = "delete from sqldb.member where accountNumber ="+accno;
		temp.execute(sql);
		return 1;
	}
}
