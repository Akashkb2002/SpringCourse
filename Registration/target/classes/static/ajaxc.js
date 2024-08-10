$(document).ready(function(){
		$('#country').change(function(){
			var id=$(this).val();
			
			$.ajax({
				type:'GET',
				url:'/states/'+id,
				success:function(data){
					$('#state').empty();
					$('#city').empty();
					
					$.each(data,function(index,state){
						$('#state').append('<option value="'+state.stateid+'">'+state.statename+'</option>');
					});
					
				}
				
				
			});
			
		});
		$('#state').change(function(){
			var stateid=$(this).val();
			$.ajax({
				type:'GET',
				url:'/cities/'+stateid,
				success:function(data){
					$('#city').empty();
					$.each(data,function(index,city){
						$('#city').append('<option value="'+city.cityid+'">'+city.cityname+'</option>');
					});
				}
			});
		});
	});