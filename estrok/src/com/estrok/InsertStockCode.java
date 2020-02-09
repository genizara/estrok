package com.estrok;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InsertStockCode {

	public static void main(String[] args) {

		String stockCodesCsvPath = "D:/jobs/estrok/";
		String stockCodesCsvFileName = "stockCodes.csv";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		FileReader fr = null;
		BufferedReader br = null;
		try {

			System.out.println("시작");

			Class.forName("com.mysql.cj.jdbc.Driver");
			// mysql은 "jdbc:mysql://localhost/사용할db이름" 이다.
			String url = "jdbc:mysql://localhost/estrok?characterEncoding=UTF-8&serverTimezone=UTC";

			// @param getConnection(url, userName, password);
			// @return Connection
			conn = DriverManager.getConnection(url, "root", "wjdekf$033");
			conn.setAutoCommit(false);

			System.out.println("연결 성공");
			String sql = "insert into stock_info (stock_name, stock_code, ipo_dttm, reg_dttm) values (?,?,STR_TO_DATE(?, '%Y-%m-%d'),now()) on duplicate key update reg_dttm = now()";
			pstmt = conn.prepareStatement(sql);

			fr = new FileReader(new File(stockCodesCsvPath + stockCodesCsvFileName));
			br = new BufferedReader(fr);

			String line = null;
			int lineNum = 0;
			while ((line = br.readLine()) != null) {
				lineNum++;
				if (lineNum == 1) {
					continue; // 첫줄 지나가기~
				}
//				System.out.println(lineNum + " : " + line);

				String[] data = line.split("\\|");
				if (data.length < 8) {
					continue;
				}
				pstmt.setString(1, data[0]);
				pstmt.setString(2, data[1]);
				pstmt.setString(3, data[4]);
				pstmt.execute();
				if (lineNum % 100 == 0) {
					System.out.println(">> " + lineNum);
				}
			}
			conn.commit();
			System.out.println("TOTAL : " + (lineNum - 1));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (fr != null) {
					fr.close();
				}
				if(rs!=null) {
					rs.close();
				}
				
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

	}

}
