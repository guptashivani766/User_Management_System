package com.xadmin.usermanagement.dao;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xadmin.usermanagement.bean.User;

public class UserDao {

	private String jdbcURL= "jdbc:mysql://localhost:3306/userdb?useSSL=false";
	private String jdbcUsername="root";
	private String jdbcPassword="shivani@1814";
	private String jdbcDriver="com.mysql.jdbc.Driver";
	
	private static final String INSERT_USERS_SQL ="INSERT INTO users"+"(name,email,country) VALUES"+"(?,?,?);";
	
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id=?";
	private static final String SELECT_ALL_USERS ="select * from users";
	private static final String DELETE_USERS_SQL ="delete from users where id=?;";
	private static final String UPDATE_USERS_SQL = "update users set name =?,email=?,country=? where id=?;";
	public UserDao() {
		
	}
	protected Connection getConnection() {
		Connection connection =null;
		try {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
			
		} catch (SQLException e) {
		  e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
		
	}
	
	//insert user
	public void insertUser(User user)throws SQLException{
		System.out.println(INSERT_USERS_SQL);
		try (
			Connection connection =getConnection();
			PreparedStatement prepareStatement = connection.prepareStatement(INSERT_USERS_SQL);){
			prepareStatement.setString(1,user.getName());
			prepareStatement.setString(2,user.getEmail());
			prepareStatement.setString(3, user.getCountry());
			System.out.println(prepareStatement);
			prepareStatement.executeUpdate();	
		} catch (SQLException e) {
		   printSQLException (e);
		}
	}
		
	//select user by id
	public User selectUser(int id) {
		User user =null;
		//step-1 Establishing a Connection
		try (Connection connection = getConnection();
			  //step-2: create a statement using connection object
			PreparedStatement prepareStatement = connection.prepareStatement(SELECT_USER_BY_ID);){
			prepareStatement.setInt(1,id);
			System.out.println(prepareStatement);
			//step-3: Execute the query of update query
			ResultSet rs = prepareStatement.executeQuery();
			
			//step-4 Process the ResultSet object
			while(rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id,name,email,country);
			}
;		} catch (SQLException e) {
		  printSQLException(e);
		}
		return user;
	}
	//select all users
	public List<User> selectAllusers(){
		//using try with ressources to avoid closing resources (boiler plate code)
		List<User> users = new ArrayList<>();
		//step1: Establishing a connection
		try (Connection connection = getConnection();
				//step 2: Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);){
		  System.out.println(preparedStatement);
		  //step-3 Execute the query or update query
		  ResultSet rs = preparedStatement.executeQuery()
;
		  
		  //step-4 Process the ResultSet object
		  while(rs.next()) {
			  int id = rs.getInt("id");
			  String name = rs.getString("name");
			  String email = rs.getString("email");
			  String country = rs.getString("country");
			  users.add(new User(id,name,email,country));
		  }
		} catch (SQLException e) {
			printSQLException(e);
		}
		return users;
		
		
	}
	
	//update user method
	public boolean updateuser(User user)throws SQLException {
		boolean rowUpdated;
		try(Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);){
			System.out.println("updated User:"+statement);
			statement.setString(1, user.getName());
			statement.setString(2,user.getEmail());
			statement.setString(3, user.getCountry());
			statement.setInt(4, user.getId());
			
			rowUpdated = statement.executeUpdate()>0 ;			
			
		} 
		return rowUpdated;
		
	}
	
	// delete user
	public boolean deleteUser(int id) throws SQLException{
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);){
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() >0;
			
		} 
		return rowDeleted;
	}
	private void printSQLException(SQLException ex) {
		for(Throwable e : ex) {
			if(e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState "+((SQLException) e).getSQLState());
				System.err.println("Error Code: "+((SQLException) e).getErrorCode());
				System.err.println("Message: "+ e.getMessage());
				Throwable t = ex.getCause();
				while(t !=null) {
					System.out.println("cause" + t);
					t=t.getCause();
				}
			}
		}
		
	}

	
}
