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
import java.util.ArrayList;
import java.util.Map;

public class GetStockPrices {

	public static void main(String[] args) {
		
		// 종료 년
		String endYear = "2020";
		// 종료 월
		String endMonth = "01";
		// 종료 일
		String endDate = "30";
		

		//		String domain = 
		Connection conn = null;
		PreparedStatement  pstmt = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			
			System.out.println("시작11");
			Class.forName("com.mysql.cj.jdbc.Driver");
			// mysql은 "jdbc:mysql://localhost/사용할db이름" 이다.
			String dbUrl = "jdbc:mysql://localhost/estrok?characterEncoding=UTF-8&serverTimezone=UTC";
			// @param getConnection(url, userName, password);
			// @return Connection
			conn = DriverManager.getConnection(dbUrl, "root", "wjdekf$033");
			conn.setAutoCommit(false);
			
			System.out.println("연결 성공");
			
//			String sql = "select si.stock_code from stock_info si LEFT OUTER JOIN "+ 
//					 "(SELECT stock_code FROM stock_daily_price GROUP BY stock_code) d1 " +
//					 "ON  si.stock_code = d1.stock_code " +
//					 "WHERE d1.stock_code IS NULL ORDER BY si.stock_code asc" ;
			String sql = "select stock_code from stock_info";
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			int totalCodeCnt = 0;
			ArrayList<String> codeList = new ArrayList<String>();
			while(rs.next()) {
				totalCodeCnt ++;
				codeList.add(rs.getString(1));
			}
			rs.close();
			
			System.out.println("total codes : " + totalCodeCnt);
			int codeCount = 0;
			
//			codeList = new ArrayList<>();
//			codeList.add("005930");
			
			for(String code : codeList) {
				codeCount ++;
				System.out.println("nowCode : " + codeCount + " // totalCodes : " + totalCodeCnt);
				HttpURLConnection httpConn = null;
				String urlParameters = ""; // 파라메타값
				
				for(int page = 1;page<2;page++) {
					System.out.println("----------------------------------" + page);
					String targetURL = "http://vip.mk.co.kr/newSt/price/daily.php?p_page="+page+"&y1=2020&m1=12&d1=15&y2="+endYear+"&m2="+endMonth+"&d2="+endDate+"&stCode="+code;
					URL url = new URL(targetURL);
					httpConn = (HttpURLConnection) url.openConnection();
					
					// 헤더 선언
					httpConn.setRequestMethod("POST");
					httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
					httpConn.setRequestProperty("Cookie", "cookievalue=" + "");
					
					httpConn.setUseCaches(false);
					httpConn.setDoInput(true);
					httpConn.setDoOutput(true);
					
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(httpConn.getOutputStream(), "utf-8"));
					pw.write(urlParameters);
					pw.flush();
					pw.close();
					
					// Get Response
					InputStream is = httpConn.getInputStream();
					BufferedReader rd = new BufferedReader(new InputStreamReader(is));
					String line;
					
					String insertSql = "insert into stock_daily_price (stock_code, reg_dttm, price, volume) values (?,STR_TO_DATE(?, '%Y/%m/%d'),?,?) on duplicate key update volume = values(volume)";
					pstmt = conn.prepareStatement(insertSql);
					
					
					Map<String, String> data = null;
					boolean dataArea1 = false;
					boolean dataArea2 = false;
					boolean dataArea3 = false;
					boolean dataArea4 = false;
					int effectLines = 0;
					String sCode = code;
					String sDate = "";
					int sPrice = 0;
					int sVolume = 0;
					
					
					while ((line = rd.readLine()) != null) {
						if(dataArea1) {
							
							if(dataArea2) {
								
								if(dataArea3) {
									
									if(line.indexOf("</table>")>-1) {
										break;
									}
									
									if(dataArea4) {
										effectLines ++;
										line = line.trim();
										if(effectLines==1) {
											sDate = line.substring(19, 27);
										} else if(effectLines==2) {
											line = line.replaceAll(",", "");
											sPrice = Integer.parseInt(line.substring(4, line.indexOf("</td>")));
										} else if(effectLines==8) {
											line = line.replaceAll(",", "");
											sVolume = Integer.parseInt(line.substring(4, line.indexOf("</td>")));
											
											pstmt.setString(1, sCode);
											pstmt.setString(2, sDate);
											pstmt.setInt(3, sPrice);
											pstmt.setInt(4, sVolume);
											pstmt.execute();
											pstmt.clearParameters();
											sDate = "";
											sPrice = 0;
											sVolume = 0;
											effectLines = 0;
											dataArea4 = false;

										}
										
										
									}
									
									if(line.indexOf("<tr")>-1) {
										dataArea4 = true;
									}
									
								}
								
								if(line.indexOf("</thead>")>-1) {
									dataArea3 = true;
								}
								
							}
							
							if(line.indexOf("table")>-1 && line.indexOf("table_3")>-1) {
								dataArea2 = true;
							}
							
						}
						
						if (line.indexOf("dailyForm")>-1) {
							dataArea1=true;
						}
	
					}
					
					
//					System.out.println("code : " + codeCount + " // " + totalCodeCnt);
//					System.out.println("page : " + page + " per 3");
//					System.out.println("==============================");
//					httpConn.disconnect();
					Thread.sleep(10);
				}
				conn.commit();
			}
			
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
