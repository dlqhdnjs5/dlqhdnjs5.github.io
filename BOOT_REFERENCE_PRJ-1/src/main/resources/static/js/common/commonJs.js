
var commonJs = {
		
	getCommonCdbyUpper : function(upperCd){
		
		var cd = upperCd;
		var resultCd;
		
		$.ajax({
			type : 'GET',
			url  : '/common/getCdbyUpperCd/'+cd
		})
		 .done(function(data,stat,xhr){
			resultCd =  data;
			return resultCd;
		})
		.fail(function(stat,xhr,err){
			console.log(stat)
			console.log('getCommonCdbyUpper request fail')
			console.log(xhr)
			alert('시스템오류가 발생했습니다.');
		});
		
	}
	
		
};

