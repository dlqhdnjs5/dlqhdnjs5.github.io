window.onload = function() { 
		var mainApp = new Vue({
			el : '#mainApp',
			mixins: _mixins,
			vuetify: new Vuetify(),
			data : function(){
				return {
					hellow : 'sss'
				}
			},
			created: function(){
				console.log(this)
				console.log('_mixins' + _mixins)
			},
			
		})
	}