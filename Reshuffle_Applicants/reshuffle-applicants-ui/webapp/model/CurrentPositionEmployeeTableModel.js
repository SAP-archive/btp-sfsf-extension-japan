sap.ui.define([
	"sap/ui/model/json/JSONModel",
	"sap/base/util/deepClone",
	"../repository/FilterDataRepository",
	"../repository/ExportTableDataRepository"
],
/**
 * 
 * JSONModelをextendしているのでこのクラスのインスタンスをそのままviewにsetModel出来る。
 * 
 * このモデルのJSONの構造は以下の通り。
 * {
 * 	"filterData": {
 * 		"companies": [{id: "", parentId:"", name: ""}],
 * 		"businessunits": [],
 * 		"divisions": [],
 * 		"departments": [],
 * 		"positions": []
 * 	},
 * 	"tableData": [
 * 		{
 * 			"empName": "",
 * 			"divisionName": "",
 * 			...
 * 			"transferedFlag": "", // 追加
 * 			"photoData": "" // 追加
 * 		}
 * 	],
 * 	"indicatorData": {
 * 		"percentValue": 70, // インジケーターに表示する％
 * 		"transferedCount": 5, // 右側に移された人の数
 * 		"applicantsCount": 20 // 人の数
 * 	}
 * }
 * 
 * 
 * @param {typeof sap.ui.model.json.JSONModel} JSONModel 
 * @param {*} deepClone
 * @param {Object} FilterDataRepository
 * @param {Object} ExportTableDataRepository
 * @returns 
 */
function(JSONModel, deepClone, FilterDataRepository, ExportTableDataRepository) {
	return JSONModel.extend("com.sap.sfsf.reshuffle.applicants.model.CurrentPositionemployeeTableModel", {
		/** 
		 * バックエンドサービスのURL
		 * @type {string}  
		 * */
		backendServiceBaseUrl: null,
		/**
		 * /odataではなく /apiのURL
		 * @type {string}
		 */
		backendServiceApiBaseUrl: null,
		/** 
		 * エラー発生時にToastを表示するためのcallback
		 * @type {(text: string)=>void} 
		 * */
		errorMessageToastCallback: null,
		/** 
		 * フィルターに使う、サーバーから取ってきた値をすべて格納するオブジェクト。
		 * ここから必要な物だけ取得して, this.setDataすることでViewに反映させる
		 * {
		 * 	"companies": [{id: "", parentId:"", name: ""}],
		 * 	"businessunits": [],
		 * 	"divisions": [],
		 * 	"departments": [],
		 * 	"positions": []
		 * }
		 * @type {object} 
		 * */
		filterData: {},

		/**
		 * 
		 * @param {string} backendServiceBaseUrl 
		 * @param {string} backendServiceApiBaseUrl
		 * @param {(text: string) => void} errorMessageToastCallback 
		 */
		constructor: function(backendServiceBaseUrl, backendServiceApiBaseUrl, errorMessageToastCallback) {
			JSONModel.call(this);
			this.backendServiceBaseUrl = backendServiceBaseUrl;
			this.backendServiceApiBaseUrl = backendServiceApiBaseUrl;
			this.errorMessageToastCallback = errorMessageToastCallback;

			// @ts-ignore
			this._getFiltersData();
			// @ts-ignore
			this.updateIndicatorData();
		},
		/**
		 * CurrentPositionEmployeeTableのフィルター（事業部、部門、ポジションなど）で利用するデータを取得＆セット
		 */
		_getFiltersData() {
			var _this = this;

			var filterNames = ["companies", "businessunits", "divisions", "departments", "positions"]
			FilterDataRepository.getFilterData(this.backendServiceBaseUrl, filterNames, (filterData)=> {
				_this.filterData = filterData;
				_this.setProperty("/filterData", deepClone(_this.filterData))
			});
		},
		/**
		 * Tableにセットするデータを読み込む。
		 * ODataModelを利用するとメタデータに無いデータを追加出来なかったりするので手動で読み込み
		 * 
		 * @param {com.sap.sfsf.reshuffle.applicants.model.CurrentPositionEmployeeTableFilterValues} filters 
		 * @param {()=>void} startLoading APIコール初めのタイミングで呼ばれるcallback
		 * @param {()=>void} finishLoading APIコールが完了したタイミングで呼ばれるcallback
		 */
		getTableData(filters, startLoading, finishLoading) {
			var _this = this;
			
			var filterStrings = [];

			// "ALL"でない物のみピックアップ
			if(filters.company != "") {
				filterStrings.push(`(companyID eq '${filters.company}')`)
			}
			if(filters.businessUnit != "") {
				filterStrings.push(`(businessUnitID eq '${filters.businessUnit}')`)
			}
			if(filters.division != "") {
				filterStrings.push(`(divisionID eq '${filters.division}')`)
			}
			if(filters.department != "") {
				filterStrings.push(`(departmentID eq '${filters.department}')`)
			}
			if(filters.position != "") {
				filterStrings.push(`(positionID eq '${filters.position}')`)
			}
			if(filters.tenureUL != null) {
			  filterStrings.push(`(empJobTenure le ${filters.tenureUL})`)
			}
			if(filters.tenureLL != null) {
			  filterStrings.push(`(empJobTenure ge ${filters.tenureLL})`)
			}
			if(filters.willingness != "") {
			  filterStrings.push(`(willingness eq '${filters.willingness}')`)
			}

			startLoading();
			$.get({
				url: this.backendServiceBaseUrl + "CurrentPositionEmployees",
				contentType: 'application/json',
				data: {
					"$filter": filterStrings.join(" and ")
				},
				dataType: 'json',
				success: function(response) {
					finishLoading();
					var fetchedData = /** @type {Object[]} */ (response.value);
					var resultTableData = [];
					fetchedData.forEach((data)=> {
						data["transferedFlag"] = false;
						data["photoData"] = null;
						resultTableData.push(data);
					});
					_this.setProperty("/tableData", resultTableData);
					_this._getPhotoData();
					_this.updateIndicatorData();
				},
				error: function(xhr) {
					finishLoading();
					_this.errorMessageToastCallback("Error")
				}
			});
		},

		/**
		 * 画像データを取得して/tableData/[index]/photDataに格納。
		 * URL(/api/v4/ReshuffleService/Photos/empID)を直接m:Imageのsrcに渡すと、スクロール時にリロードが走ってしまう。
		 * これを避けるプロパティを見つけられなかったので一旦ロードした画像データを手動管理。
		 * @returns 
		 */
		_getPhotoData() {
			var _this = this;

			var tableData = /** @type {Object[]} */ (this.getProperty("/tableData"));
			if(tableData == null || tableData == undefined) {
				// TODO: error handling
				return;
			}

			function getAndSetImage(empID, index) {
				fetch(_this.backendServiceApiBaseUrl + "Photos/" + empID).then((response)=>{
					if(response.status != 200) {
						return;
					}
					response.blob().then((blob) => {
						var reader = new FileReader();
    				reader.readAsDataURL(blob);
    				reader.onloadend = function () {
    					var base64String = reader.result;
							_this.setProperty("/tableData/"+ index +"/photoData", base64String);
        		}
					})
				});
			}

			for(var index = 0; index < tableData.length; index++) {
				var empID = tableData[index]["empID"];
				getAndSetImage(empID, index);
			}
		},
		/**
		 * Progress Indicatorに渡す値をセット
		 */
		updateIndicatorData() {
			var data = {};
			var tableData = /** @type {Object[]} */ (this.getProperty("/tableData"));
			if(tableData == null || tableData == undefined) {
				data["percentValue"] = 0
				data["transferedCount"] = 0
				data["applicantsCount"] = 0
				this.setProperty("/indicatorData", data);
				return;
			}

			var transferedCount = tableData.filter((t)=> t["transferedFlag"]).length;
			var applicantsCount = tableData.length;

			data["percentValue"] = Math.round((transferedCount/applicantsCount)*100);
			data["transferedCount"] = transferedCount;
			data["applicantsCount"] = applicantsCount;

			this.setProperty("/indicatorData", data);
		},
		/**
		 * empIDを指定して、そのempIDのtransferedFlagのステータスを変更する
		 * @param {String} empID 
		 * @param {Boolean} status 
		 */
		updateTransferedFlagStatusByEmpID(empID, status) {
			var tableData = /** @type {Object[]} */ (this.getProperty("/tableData"));
			// @ts-ignore
			var index = tableData.findIndex((data)=>{
				return data["empID"] == empID;
			});
			if(index == -1) {
				return;
			}
			this.setProperty("/tableData/"+ index +"/transferedFlag", status);
			this.updateIndicatorData();
		},
		/**
		 * ドラッグアンドドロップで、ドロップが完了した後に呼ぶ
		 * @param {int} rowIndex ドラッグされた行番号
		 */
		tableRowDragged(rowIndex) {
			this.setProperty("/tableData/"+ rowIndex +"/transferedFlag", true);
			this.updateIndicatorData();
		},
		/**
		 * ドラッグアンドドロップでドロップされた時に呼ばれるメソッド.
		 * 渡されたempIDのデータの”異動フラグ”をfalseにする.
		 * @param {String} empID ドラッグされてきたempID
		 */
		tableRowDropped(empID) {
			// ドラッグされてきたデータのempIDと一致するtableDataを検索
			var tableData = /** @type {Object[]} */ (this.getProperty("/tableData"));
			if(tableData == null || tableData == undefined) {
				return;
			}
			
			// @ts-ignore
			var index = tableData.findIndex((data)=>{
				return data["empID"] == empID;
			});
			if(index == -1) {
				return;
			}
			this.setProperty("/tableData/" + index + "/transferedFlag", false);
			this.updateIndicatorData();
		},
		/**
		 * BusineeUnitのIDを指定することでDivisionのリストを抽出して更新
		 * @param {string} businessunitId
		 */
		setDivisionFilterByBusinessUnit(businessunitId) {
			var divisions = this.filterData["divisions"]

			// ALLの場合全てをセットしてリターン
			if(businessunitId === "") {
				this.setProperty("/filterData/divisions", divisions);
				return;
			}
			var items = divisions.filter(function(val, index) {
				// parentIdが一致する物か、idが空文字（ALLの場合）の物を抽出
				return val.parentId ===  businessunitId || val.id === ""
			});
			this.setProperty("/filterData/divisions", items);
		},
		/**
		 * DivisionIDを指定することでDepartmentのリストを抽出して更新
		 * @param {string} divisionId
		 */
		setDepartmentFilterByDivision(divisionId) {
			var departments = this.filterData["departments"]
			if(divisionId === "") {
				this.setProperty("/filterData/departments", departments);
				return;
			}
			var items = departments.filter(function(val) {
				return val.parentId === divisionId || val.id === "";
			})
			this.setProperty("/filterData/departments", items);
		},

		/**
		 * DepartmentIDを指定することでPositionのリストを抽出して更新
		 * @param {string} departmentId
		 */
		setPositionFilterByDepartment(departmentId) {
			var positions = this.filterData["positions"]
			if(departmentId === "") {
				this.setProperty("/filterData/positions", positions);
				return;
			}
			var items = positions.filter(function(val) {
				return val.parentId === departmentId || val.id === "";
			})
			this.setProperty("/filterData/positions", items);
		},
		/**
		 * 
		 * @param {(password: String) => void} success 
		 * @param {()=>void} fail 
		 * @returns 
		 */
		exportTableData(success, fail) {

			// YYYYMMDDHHMMSS形式で時間を取得
			//var filename = new Date().toISOString().split('.')[0].replace(/[^\d]/gi,'');
			var filename = "exportedFile";

			var tableData = /** @type {Object[]} */ (this.getProperty("/tableData"));
			if(tableData == undefined || tableData == null) {
				// TODO error handling
				// tableDataが空の時のメッセージ
				return;
			}

			var entities = []
			tableData.map((data)=> {
				var tmp = {}
				tmp["candidateID"] = data["empID"];
				tmp["positionID"] = null;
				tmp["incumbentEmpID"] = null;
				entities.push(tmp);
			});

			ExportTableDataRepository.exportTableData(this.backendServiceApiBaseUrl, entities, filename, success, fail)
		}
	});
})