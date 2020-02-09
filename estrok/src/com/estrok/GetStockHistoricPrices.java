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

public class GetStockHistoricPrices {

	
	// 시장 주가 넣는다!!!!
	public static void main(String[] args) {
		
		// 종료 년
		String endYear = "2020";
		// 종료 월
		String endMonth = "1";
		// 종료 일
		String endDate = "30";
		
//		String domain = 
		Connection conn = null;
		PreparedStatement  pstmt = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			
			System.out.println("시작12");
			Class.forName("com.mysql.cj.jdbc.Driver");
			// mysql은 "jdbc:mysql://localhost/사용할db이름" 이다.
			String dbUrl = "jdbc:mysql://localhost/estrok?characterEncoding=UTF-8&serverTimezone=UTC";
			// @param getConnection(url, userName, password);
			// @return Connection
			conn = DriverManager.getConnection(dbUrl, "root", "wjdekf$033");
			conn.setAutoCommit(false);
			
			System.out.println("연결 성공");
			String urlParameters = ""; // 파라메타값

//			String targetURL = "http://vip.mk.co.kr/newSt/rate/kospikosdaq_2.php?stCode=KPS001&sty=2019&stm=1&std=2&eny=2020&enm=1&end=10&x=26&y=10";
			String targetURL = "http://vip.mk.co.kr/newSt/rate/kospikosdaq_2.php?stCode=KPS001&sty=2019&stm=12&std=7&eny="+endYear+"&enm="+endMonth+"&end="+endDate+"&x=38&y=14";
			URL url = new URL(targetURL);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

			
			// 헤더 선언
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			httpConn.setRequestProperty("Cookie", "cookievalue=" + "");
			
			httpConn.setUseCaches(false);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.setReadTimeout(20000);
			
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(httpConn.getOutputStream(), "utf-8"));
			pw.write(urlParameters);
			pw.flush();
			pw.close();
			
			// Get Response
			InputStream is = httpConn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			
			
			String line = null;
			
			String insertSql = "insert into stock_index (reg_dttm, average, volume, avg_200, ld_avg, max_avg, min_avg) "
					+ " values (STR_TO_DATE(?, '%Y%m%d'),?,?,null,?,?,?) on duplicate key update volume = values(volume)";
			pstmt = conn.prepareStatement(insertSql);
			
			boolean dataArea1 = false;
			boolean dataArea2 = false;
			int dataAreaLock = 0;
			int effectiveLine = 0;
			int inputCnt = 0;
			
			
			String yyyymmdd = "";
			double p1 = 0;
			double p2 = 0;
			double p3 = 0;
			double p4 = 0;
			double p5 = 0;
			
			while ((line = rd.readLine()) != null) {
				line = line.trim();
				if(dataArea1) {
					
					if(dataArea2) {
						effectiveLine ++;
						if(effectiveLine==1) {
//							System.out.println("날짜 : " + line.substring(19, line.indexOf("</td>")));
							String yymmdd = line.substring(19, line.indexOf("</td>"));
							yyyymmdd= "20" + yymmdd.substring(0,2) + yymmdd.substring(3,5) + yymmdd.substring(6,8);
							pstmt.setString(1, yyyymmdd);
						}else if(effectiveLine==2) {
							line = line.replaceAll(",", "");
//							System.out.println("종가 : " + line.substring(4, line.indexOf("</td>")));
							p1 = Double.parseDouble(line.substring(4, line.indexOf("</td>")));
							pstmt.setDouble(2, p1);
						}else if(effectiveLine==5) {
							line = line.replaceAll(",", "");
//							System.out.println("시작가 : " + line.substring(4, line.indexOf("</td>")));
							p2 = Double.parseDouble(line.substring(4, line.indexOf("</td>")));
							pstmt.setDouble(4, p2);

						}else if(effectiveLine==6){
							line = line.replaceAll(",", "");
//							System.out.println("고가 : " + line.substring(4, line.indexOf("</td>")));
							p3 = Double.parseDouble(line.substring(4, line.indexOf("</td>")));
							pstmt.setDouble(5, p3);

						}else if(effectiveLine==7) {
							line = line.replaceAll(",", "");
//							System.out.println("저가 : " + line.substring(4, line.indexOf("</td>")));
							p4 = Double.parseDouble(line.substring(4, line.indexOf("</td>")));
							pstmt.setDouble(6, p4);

						}else if(effectiveLine==8) {
							line = line.replaceAll(",", "");
//							System.out.println("거래량(천주) : " + line.substring(30, line.indexOf("</td>")));
							p5 = Double.parseDouble(line.substring(30, line.indexOf("</td>"))) * 1000 ;
							pstmt.setDouble(3, p5);
							
//							System.out.println("파라미터들 " + yyyymmdd);
//							System.out.println("파라미터들 " + p1);
//							System.out.println("파라미터들 " + p2);
//							System.out.println("파라미터들 " + p3);
//							System.out.println("파라미터들 " + p4);
//							System.out.println("파라미터들 " + p5);
//							System.out.println("날짜 : " + yyyymmdd + " / 총 입력수 : " + inputCnt);
							pstmt.execute();
							inputCnt ++;
							pstmt.clearParameters();
							if(inputCnt !=0 && inputCnt  %1 == 0) {
								conn.commit();
								System.out.println("총입력수  : " + inputCnt);
							}
							effectiveLine = 0;
							dataArea2 = false;
						}
					}
					
					
					if(line.indexOf("<tr>")>-1) {
						dataArea2 = true;
					}
					
				}
				
				if(dataAreaLock==2) {
					dataArea1 = true;
				}
				
				if(line.indexOf("class='sky'")>-1) {
					dataAreaLock ++;
				}
				

				Thread.sleep(5);
			}
			
			conn.commit();
			System.out.println("총입력수  : " + inputCnt);

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
