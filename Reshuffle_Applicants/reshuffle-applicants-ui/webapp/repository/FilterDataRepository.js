sap.ui.define(function () {
	"use strict";
	return {

		/**
		 * フィルターデータの取得
		 * 
		 * finishの引数↓
		 * {
		 * 	"companies": [
		 * 		{"id": "", parentId: null, name:"ALL"},
		 * 		{}...
		 * 	],
		 * 	"businessunits": [
		 * 		...
		 * 	]
		 * }
		 * 
		 * @param {String} backendServiceBaseUrl //アクセス先のURLのベース
		 * @param {typeof String[]} filterNames // フィルターの名前のリスト（"companies", "businessuntis",...)
		 * @param {(filterData: Object) => void} finish 
		 */
		getFilterData: function(backendServiceBaseUrl, filterNames, finish) {
			var filterData = {};

			var requests = filterNames.map(function(filterName) {
				return $.post({
					url: backendServiceBaseUrl + "getFilter",
					contentType: 'application/json',
					dataType: 'json',
					data: JSON.stringify({
						"arg": filterName
					})
				});
			});

			$.when.apply(null, requests).then(function(){
				for(var i=0; i < arguments.length; i++) {
					var result = arguments[i][2];
					if(result.status != 200) {
						// TODO: error handling
						continue;
					} 
					var items = result.responseJSON.value.map((val)=> {
						return {
							id: val["ID"],
							parentId: val["parentID"],
							name: val["name"]
						}
					})
					// 先頭に"ALL"を追加
					items.unshift({
						id: "",
						parentId: null,
						name: "ALL"
					});

					filterData[filterNames[i]] = items;
				}

				finish(filterData);
			});
		}
	};
});