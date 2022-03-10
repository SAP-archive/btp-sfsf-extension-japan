sap.ui.define([
	"sap/ui/model/json/JSONModel",
	"sap/base/util/deepClone",
	"../repository/FilterDataRepository",
	"../repository/ExportTableDataRepository",
	"../repository/CompetenciesRepository"
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
 * 			"candidateData": {empID:"", empName:""},  // 割り当てられたemployeeのデータ.割り当てられてなければnull
 * 			"empID": "",
 * 			"empName": "",
 * 			"division": "",
 * 			....			
 *    }
 * 	],
 * 	"competencyData": {
 * 		"selectedRowIndex": int,
 * 		"competencies": []
 *  },
*		"indicatorData": {
 * 		"percentValue": 70, // インジケーターに表示する％
 * 		"assignedCount": 5, // 割り当てられた人の数
 * 		"positionCount": 20 // ポジションの数
 * 	}
 * }
 * 
 * 
 * @param {typeof sap.ui.model.json.JSONModel} JSONModel
 * @param {*} deepClone 
 * @param {Object} FilterDataRepository
 * @param {Object} ExportTableDataRepository
 * @param {Object} CompetenciesRepository
 * @returns 
 */
function(JSONModel, deepClone, FilterDataRepository, ExportTableDataRepository, CompetenciesRepository) {
	return JSONModel.extend("com.sap.sfsf.reshuffle.applicants.model.NextPositionTableModel", {
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
		 * 	"companies": [],
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
		 * @param {com.sap.sfsf.reshuffle.applicants.model.NextPositionTableFilterValues} filters 
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
			if(filters.retire != "") {
				filterStrings.push(`(empRetire eq '${filters.retire}')`)
			}

			startLoading();
			$.get({
				url: this.backendServiceBaseUrl + "NextPositions",
				contentType: 'application/json',
				data: {
					"$filter": filterStrings.join(" and ")
				},
				dataType: 'json',
				success: function(response) {
					finishLoading();
					var fetchedData = /** @type {Object[]} */ (response.value);
					var items = fetchedData.map((item)=> {
						item["candidateData"] = null;
						return item;
					})
					_this.setProperty("/tableData", items);
					_this.updateIndicatorData();
				},
				error: function(xhr) {
					finishLoading();
					_this.errorMessageToastCallback("Error")
				}
			});
		},
		/**
		 * Progress Indicatorに渡す値をセット
		 */
		updateIndicatorData() {
			var data = {};
			var tableData = /** @type {Object[]} */ (this.getProperty("/tableData"));
			if(tableData == null || tableData == undefined) {
				data["percentValue"] = 0
				data["assignedCount"] = 0
				data["positionCount"] = 0
				this.setProperty("/indicatorData", data);
				return;
			}

			var assignedCount = tableData.filter((t)=> t["candidateData"] != null || t["candidateData"] != undefined).length;
			var positionCount = tableData.length;

			data["percentValue"] = Math.round((assignedCount/positionCount)*100);
			data["assignedCount"] = assignedCount;
			data["positionCount"] = positionCount;

			this.setProperty("/indicatorData", data);
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
			});
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
			});
			this.setProperty("/filterData/positions", items);
		},
		/**
		 * indexで指定した行に割り当てられたCandidateのempID
		 * @param {int} index 
		 * @returns {string} 割り当てられているCandidateのempID. 割り当てられないない場合null
		 */
		getAssignedCandidateEmpID(index) {
			var data = this.getProperty("/tableData/" + index + "/candidateData");
			if(data == null || data == undefined) {
				return null;
			}
			return data["empID"];
		},
		/**
		 * ドラッグアンドドロップでドロップされたときにrowを更新するメソッド
		 * @param {Number} droppedIndex DropされたRowのindex
		 * @param {String} draggedData DragされてきたRowのObject == CurrentPositionEmployeeのデータ
		 * @returns {Boolean} validationを通って値のアップデートが出来たかどうか
		 */
		tableRowDropped(droppedIndex, draggedData) {
			var result = this.updateRow(droppedIndex, draggedData["empID"], draggedData["empName"]);
			this.updateIndicatorData();
			return result;
		},
		/**
		 * ドラッグアンドドロップでドラッグされて成功したときにrowを更新するメソッド
		 * @param {Number} draggedIndex // DragされたRowのindex
		 */
		tableRowDragged(draggedIndex) {
			this.setProperty("/tableData/"+ draggedIndex + "/candidateData", null);
			this.updateIndicatorData();
		},
		/**
		 * 空きポストがあるかどうかをチェック
		 */
		hasEmptyPost() {
			var tableData = /** @type {Object[]} */ (this.getProperty("/tableData"));
			var nullCandidateData = tableData.filter((data) => {
				return data["candidateData"] == null;
			});
			return nullCandidateData.length != 0;
		},
		/**
		 * 行の更新を行う
		 * @param {int} rowIndex 更新したい行
		 * @param {String} empID 更新したい行に割り当てるemployeeのID(candidateID)
		 * @param {String} empName 更新したい行に割り当てるemployeeの名前(candidateName)
		 * @returns {Boolean} validationを通って値のアップデートが出来たかどうか
		 */
		updateRow(rowIndex, empID, empName) {
			var tableData = /** @type {Object[]} */ (this.getProperty("/tableData"));

			// 同じ人が既に割り当てられてないかチェック
			var sameCandidate = tableData.filter((data)=> {
				var candidateData = data["candidateData"];
				if(candidateData == null || candidateData == undefined) {
					return false;
				}
				return candidateData["empID"] == empID;
			});
			if(sameCandidate.length != 0) {
				// 既に同じ人物がdropされて居る
				return false;
			}

			var data = {
				"empID": empID,
				"empName": empName
			}
			return this.setProperty("/tableData/"+ rowIndex + "/candidateData", data);
		},
		/**
		 * 
		 * @param {sap.ui.model.odata.v4.ODataModel} model 
		 * @param {String} caseID
		 * @param {()=>void} success
		 * @param {(msg: String)=>void} failed
		 */
		saveCandidates(model, caseID, success, failed) {
			var _this = this;
			// caseIDのvalidation
			this._getCaseIds((ids)=> {
				var searchResult = ids.indexOf(caseID);
				if(searchResult != -1) {
					failed("CaseIDの重複"); // TODO i18n
					return;
				}
				this._createCandidates(model, caseID, success, failed);
			}, ()=> {
				failed(null);
			});
			
		},
		/**
		 * @param {(ids: String[]) => void} success
		 * @param {() => void} fail
		 */
		_getCaseIds(success, fail) {
			var _this = this;
			$.post({
				url: _this.backendServiceBaseUrl + "getFilter",
				contentType: 'application/json',
				dataType: 'json',
				data: JSON.stringify({
					"arg": "caseids"
				}),
				success: function(data, status, xhr) {
					if(xhr.status != 200) {
						_this.errorMessageToastCallback("failed to load filter: caseids");
						return;
					}
					var items = data.value.map((val) => {
						return val["ID"]
					});
					success(items);
				},
				error: function(data) {
					fail();
				}
			});
		},
		/**
		 * 
		 * @param {sap.ui.model.odata.v4.ODataModel} model 
		 * @param {String} caseID
		 * @param {()=>void} success
		 * @param {(msg: String)=>void} failed
		 */
		_createCandidates(model, caseID, success, failed) {
			//
			var bindList = model.bindList("/Candidates", null, null, null,{"$$updateGroupId": "updateCandidatesBatchGroup"});
			var tableData = /** @type {Object[]} */ (this.getProperty("/tableData"));
			
			// tableData[n].candidateDataがnullでないデータを抜き出す。
			var filteredTableData = tableData.filter((data) => data["candidateData"] != null);

			filteredTableData.forEach((data) => {
				var postData = {}
				postData["caseID"] = caseID;
				postData["candidateID"] = data["candidateData"]["empID"];
				postData["positionID"] = data["positionID"];
				postData["incumbentEmpID"] = data["empID"]

				bindList.create(postData, true)
			});
		
			model.submitBatch("updateCandidatesBatchGroup").then(
				function() {
					if(model.hasPendingChanges("updateCandidatesBatchGroup")) {
						model.resetChanges("updateCandidatesBatchGroup");
						failed(null);
					} else {
						success();
					}
				},
				function(error) {
					failed(null);
				}
			)
		},
		/**
		 * Compitencyの取得
		 * @param {int} selectedRowIndex // positionIDが押されたRowのindex
		 * @param {() => void} success 
		 * @param {()=>void} failed 
		 */
		getCompetencies(selectedRowIndex, success, failed) {
			this.setProperty("/competencyData", null);
			var selectedPositionID = this.getProperty("/tableData/" + selectedRowIndex + "/positionID");
			var _this = this;

			CompetenciesRepository.getCompetencies(this.backendServiceBaseUrl, selectedPositionID, (value) => {
				var val = {
					"competencies": value,
					"selectedRowIndex": selectedRowIndex
				}
				_this.setProperty("/competencyData", val);
				success();
			}, () => {
				_this.errorMessageToastCallback("Not Found"); // TODO: i18n, エラーハンドリング
				failed();
			})
		},

		/**
		 * Competency一覧から選ばれたemployeeを、テーブルに反映させる
		 * @param {Object} selectedCompetencyData 
		 * @returns {Boolean} rowの更新に成功したかどうか
		 */
		competencySelected(selectedCompetencyData) {
			var selectedEmpID = selectedCompetencyData["currentEmpID"];
			var selectedEmpName = selectedCompetencyData["currentEmpName"];
			var rowIndex = this.getProperty("/competencyData/selectedRowIndex");
			return this.updateRow(rowIndex, selectedEmpID, selectedEmpName);
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
				tmp["candidateID"] = data["candidateData"] ? data["candidateData"]["empID"] : null;
				tmp["positionID"] = data["positionID"];
				tmp["incumbentEmpID"] = data["empID"];
				entities.push(tmp);
			});

			ExportTableDataRepository.exportTableData(this.backendServiceApiBaseUrl, entities, filename, success, fail)
		}
	});
})