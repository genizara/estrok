<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
	<title>stockList</title>
	<link rel="stylesheet" type="text/css" href="/resources/css/main.css">
</head>
<body>
	<h1>Stock List</h1>
<section>
	<input type="hidden" id="priceData" value='${listJson }'/>
	<div>
		<form action="" id="sumbitForm" >
			<input id="stockCode" type="text"  alt="종목코드를 입력해주세요." name="stockCode">
			<input type="button" id="searchStock" value="찾기">
		</form>		
	</div>
	<br/>
	<div>
	<canvas id="myChart" width="400" height="400"></canvas>
	</div>
	<br/>
	<div>
		<table class="simple-table">
			<thead>
		        <tr>
		            <th>코드</th>
		            <th>날짜</th>
		            <th>가격</th>
		            <th>거래량</th>
		            <th>종목 52주 평균</th>
		            <th>시장52주 평균</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<c:choose>
		    	
					<c:when test="${ not empty list}">
				    	<c:forEach items="${list }" var="data" >
					        <tr>
					            <td>${data.stock_code }</td>
					            <td>${data.reg_dttm }</td>
					            <td>${data.price }</td>
					            <td>${data.volume }</td>
					            <td>${data.sdp_rs_52w }</td>
					            <td>${data.si_rs_52w }</td>
					        </tr>
				    	</c:forEach>
					</c:when>
					<c:otherwise>
					        <tr>
					            <td colspan="4">no data</td>
					        </tr>
					</c:otherwise>	    	 	
		    	</c:choose>
		    </tbody>
		</table>
	</div>
</section>
</body>
<script src="/resources/js/jquery-3.4.1.min.js"></script>
<script src="/resources/js/chartLib/Chart.js"></script>
<script type="text/javascript">

	$(document).ready(function(){
	
		bindButtons();
		let chartData = document.getElementById('priceData').value;
		if(chartData!=null && chartData.trim()!=''){
			ChartClass.initChart(chartData);
			
		}
	});

	function bindButtons() {
		
		$('#searchStock').on('click', function(){
			let code = $('#stockCode').val();
			if(code===undefined || code === null || code.trim()===''){
				alert("코드를 입력해주세요.");
				return;
			} 
			let submitForm = document.getElementById('sumbitForm');
			submitForm.action ="/data1";
			submitForm.method ="get";
			
			submitForm.submit();
			
		});
		
	}
	const ChartClass = {
			ctx : null
		, 	myChart : null
		,   initChart : function(dataSet){
			ChartClass.ctx = document.getElementById('myChart');
			let jsonData = JSON.parse(dataSet);
			let datasets = [];
			let labels = [];
			for(let data in jsonData){
				labels.push(jsonData[data].reg_dttm);
				datasets.push(jsonData[data].price);
			}
// 			let labels = dataSet.labels;
// 			let datasets = dataSet.datas;
			ChartClass.myChart = new Chart(ChartClass.ctx, {
			    type: 'line',
			    data: {
			        labels: labels,
			        datasets: [{
			            label: 'price',
			            data: datasets,
// 			            backgroundColor: [
// 			                'rgba(255, 99, 132, 0.2)',
// 			                'rgba(54, 162, 235, 0.2)',
// 			                'rgba(255, 206, 86, 0.2)',
// 			                'rgba(75, 192, 192, 0.2)',
// 			                'rgba(153, 102, 255, 0.2)',
// 			                'rgba(255, 159, 64, 0.2)'
// 			            ],
// 			            borderColor: [
// 			                'rgba(255, 99, 132, 1)',
// 			                'rgba(54, 162, 235, 1)',
// 			                'rgba(255, 206, 86, 1)',
// 			                'rgba(75, 192, 192, 1)',
// 			                'rgba(153, 102, 255, 1)',
// 			                'rgba(255, 159, 64, 1)'
// 			            ],
			            borderWidth: 1
			        }]
			    },
			    options: {
			        scales: {
			            yAxes: [{
			                ticks: {
			                    beginAtZero: true
			                }
			            }]
			        }
			    }
			})
		}
	}

</script>
</html>
