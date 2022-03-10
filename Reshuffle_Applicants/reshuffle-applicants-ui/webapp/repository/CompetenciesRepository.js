sap.ui.define(function () {
	"use strict";
	return {

		/**
		 * Competencyの取得
		 * 
		 * @param {String} backendServiceBaseUrl //アクセス先のURLのベース
		 * @param {String} selectedPositionID 
		 * @param {(value: Object) => void} success 
		 * @param {() => void} fail 
		 */
		getCompetencies: function(backendServiceBaseUrl, selectedPositionID, success, fail) {
			
			$.get({
				url: backendServiceBaseUrl + "Competencies",
				contentType: 'application/json',
				data: {
					"$filter": `currentPositionID  eq '${selectedPositionID}'`
				},
				dataType: 'json',
				success: function(response) {
					success(response.value);
				},
				error: function(xhr) {
					fail();
				}
			});
			
		}
	};
});