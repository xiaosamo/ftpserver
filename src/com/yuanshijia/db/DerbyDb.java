package com.yuanshijia.db;

import com.yuanshijia.entity.FileInfo;

import java.sql.*;
import java.util.*;

public class DerbyDb {
//	private static String driver = "org.apache.derby.jdbc.ClientDriver";
//	private static String url = "jdbc:derby://localhost:1527/ftpserver";

		private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private static String url = "jdbc:derby:ftpserver;create=true";

	private DerbyDb(){
	}

	private static Connection connection;
	static {
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建表
	 */
	public static void create() {
		try {
			Statement statement = connection.createStatement();
			String sql = "create table tb_file (\n" +
					"    uuid varchar(40),\n" +
					"    size bigint,\n" +
					"    type varchar(20),\n" +
					"    name varchar(100),\n" +
					"    create_time TIMESTAMP,\n" +
					"    save_path varchar(100),\n" +
					"    primary key(uuid)\n" +
					")";

			statement.executeUpdate(sql);
			System.err.println("创建tb_file表");
		} catch (SQLException e) {
			System.err.println("tb_file表已经存在，不需创建");
		}
	}

	/**
	 * 根据uuid查找记录
	 * @param uuid
	 * @return
	 */
	public static FileInfo queryById(String uuid) {
		try {
			String sql = "select * from tb_file  where uuid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, uuid);

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				FileInfo fileInfo = new FileInfo(rs.getString(1), rs.getLong(2), rs.getString(3), rs.getString(4), rs.getDate(5), rs.getString(6));
				return fileInfo;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据uuid删除记录
	 * @param uuid
	 */
	public static void deleteById(String uuid) {
		try {
			String sql = "delete from tb_file  where uuid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, uuid);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除所有记录
	 */
	public static void deleteAll() {
		try {
			PreparedStatement statement = connection.prepareStatement("delete  from tb_file");
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询所有记录
	 * @return
	 */
	public static List<FileInfo> queryAll() {
		List<FileInfo> result = new ArrayList<>();
		try {

			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM tb_file");
			while (rs.next()) {
				FileInfo fileInfo = new FileInfo(rs.getString(1), rs.getLong(2), rs.getString(3), rs.getString(4), rs.getDate(5), rs.getString(6));
				result.add(fileInfo);
				System.out.println(fileInfo);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 添加一条记录
	 * @param fileInfo
	 */
	public static void add(FileInfo fileInfo) {
		try {
			String sql = "insert into tb_file(uuid,size,type,name,create_time,save_path) values(?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, fileInfo.getUuid());
			statement.setLong(2, fileInfo.getSize());
			statement.setString(3, fileInfo.getType());
			statement.setString(4, fileInfo.getName());
			statement.setDate(5, new java.sql.Date(fileInfo.getCreateTime().getTime()));
			statement.setString(6, fileInfo.getSavePath());

			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
//		queryAll();

//		create();

//		add(new FileInfo(UUID.randomUUID().toString(), 1024, "image", "ysj.txt", new java.util.Date(), "d:"));

//		deleteAll();
//		queryAll();

//		System.out.println(queryById("4575d029-e9e2-44a7-8022-64c508550e59"));
//		deleteById("4575d029-e9e2-44a7-8022-64c508550e59");
//		System.out.println(queryById("4575d029-e9e2-44a7-8022-64c508550e59"));
	}



}
