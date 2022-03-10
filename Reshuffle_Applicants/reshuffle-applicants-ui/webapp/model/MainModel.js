sap.ui.define([
	"sap/ui/model/json/JSONModel"
],
/**
 * 
 * JSONModelをextendしているのでこのクラスのインスタンスをそのままviewにsetModel出来る。
 * 
 * このモデルのJSONの構造は以下の通り。
 * {
 * 	"startDateTime": string
 * }
 * 
 * 
 * @param {typeof sap.ui.model.json.JSONModel} JSONModel 
 * @returns 
 */
function(JSONModel) {

	return JSONModel.extend("com.sap.sfsf.reshuffle.applicants.model.EditCaseTableModel", {
		configBackendUrl: null,

		/**
		 * 
		 * @param {String} configBackendUrl 
		 */
		constructor: function(configBackendUrl) {
			JSONModel.call(this);
			this.configBackendUrl = configBackendUrl;

			// @ts-ignore
			this.setProperty("/startDateTime", "-");
			// @ts-ignore
			this._getStartDateTime();
		},

		/**
		 * config appのbackendからstartDateTimeの取得
		 * 
		 */
		_getStartDateTime() {
			var _this = this;

			$.get({
				url: this.configBackendUrl + "jpconfig",
				contentType: "application/json",
				success: (result, xhr, data) => {
					var startDateTime = result["startDateTime"];
					_this.setProperty("/startDateTime", startDateTime);
				},
				error: (error) => {
					// TODO: error handling
				}
			})
		},
	});
})