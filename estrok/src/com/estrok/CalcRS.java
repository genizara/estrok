package com.estrok;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalcRS {

	public static void main(String[] args) {

//		String domain = 
		Connection conn = null;
		PreparedStatement  pstmt = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			
			System.out.println("시작 CalcRs");
			Class.forName("com.mysql.cj.jdbc.Driver");
			// mysql은 "jdbc:mysql://localhost/사용할db이름" 이다.
			String dbUrl = "jdbc:mysql://localhost/estrok?characterEncoding=UTF-8&serverTimezone=UTC";
			// @param getConnection(url, userName, password);
			// @return Connection
			conn = DriverManager.getConnection(dbUrl, "root", "wjdekf$033");
			conn.setAutoCommit(false);
			
			System.out.println("연결 성공");

			String getFirstRegDttm = "select reg_dttm from stock_index order by reg_dttm asc limit 1";
			
			String firstDate = "";
			
			st = conn.createStatement();
			rs = st.executeQuery(getFirstRegDttm);
			while(rs.next()) {
				firstDate = rs.getString(1);
				System.out.println(firstDate);
			}
			
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			
			Calendar c = Calendar.getInstance();
			c.set(2020, 0, 8);
//			c.set(Integer.parseInt(firstDate.substring(0, 4)), Integer.parseInt(firstDate.substring(5, 7))-1, Integer.parseInt(firstDate.substring(7, 9)));
			long startDateMil = c.getTime().getTime();
			int oneDayMil = 60 * 60 * 24 * 1000;
			
			long todayMil = new Date().getTime();
			
			String insertRsSQL =" update stock_index si,(		" +
								" SELECT 						" +
								" avg_sum/cnt AS rs_52w			" +
								" FROM 							" +
								" (								" +
								" SELECT 						" +
								" COUNT(reg_dttm) AS cnt		" +
								" , SUM(average) AS avg_sum 	" +
								" FROM stock_index 				" +
								" WHERE reg_dttm > FROM_UNIXTIME(UNIX_TIMESTAMP(STR_TO_DATE(?, '%Y-%m-%d')) - 365 *  60 * 60 * 24, '%Y-%m-%d' ) " + 
								" AND reg_dttm <= STR_TO_DATE(?, '%Y-%m-%d') " +
								" ) datas						" +
								" ) datas2						" +
								" SET si.rs_52w = datas2.rs_52w	" +
								" WHERE si.reg_dttm = STR_TO_DATE(?, '%Y-%m-%d')	";
			pstmt = conn.prepareStatement(insertRsSQL);

			
			while(startDateMil < todayMil) {
				String targetDate = f.format(new Date(startDateMil));
				System.out.println("date : " + targetDate + " ( " + startDateMil + " / " + todayMil + " )");
				pstmt.setString(1, targetDate);
				pstmt.setString(2, targetDate);
				pstmt.setString(3, targetDate);
				
				pstmt.execute();
				pstmt.clearParameters();
				
				startDateMil += oneDayMil;
				Thread.sleep(10);
			}
			conn.commit();
			
//			String get = "select * from ";
//			pstmt = conn.prepareStatement(insertSql);
//			
//			Map<String, Object> params = null;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null) {
					pstmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}
