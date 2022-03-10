sap.ui.define(function () {
	"use strict";
	return {

		/**
		 * テーブルデータのエクスポート
		 * サーバーからダウンロードして来た上でブラウザから保存させるところまで行う
		 * 
		 * data=[
		 * 	{
		 * 		"candidateID": "",
		 * 		"positionID": "",
		 * 		"incumbentEmpID": ""
		 * 	},
		 * 	...
		 * ]
		 * 
		 * @param {String} backendServiceApiBaseUrl //アクセス先のURLのベース
		 * @param {typeof Object[]} data 
		 * @param {String} filename 
		 * @param {(password: String) => void} success 
		 * @param {() => void} fail 
		 */
		exportTableData: function(backendServiceApiBaseUrl, data, filename, success, fail) {

			// TODO: 一貫性が無いのでajaxで実装したい
			fetch(backendServiceApiBaseUrl + "exportFile", {
				method: 'POST',
        headers: {
					'Content-Type': 'application/json'
        },
        body: JSON.stringify({
					"filename": filename,
					"entities": data
				})
      }).then((response) => {
				if(response.status != 200) {
					fail();
					return;
				}
        response.blob().then((blob) => {
        	const downloadUrl = window.URL.createObjectURL(blob);
        	const link = document.createElement('a');
        	link.setAttribute('href', downloadUrl);
        	link.setAttribute('download', 'file');
        	link.style.display = 'none';
					link.download = filename + ".zip";
        	document.body.appendChild(link);
        	link.click();
        	window.URL.revokeObjectURL(link.href);
        	document.body.removeChild(link);
					success(response.headers.get("password"));
				})
			});
		}
	};
});