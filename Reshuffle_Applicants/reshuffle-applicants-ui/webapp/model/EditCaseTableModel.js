sap.ui.define([
	"sap/ui/model/json/JSONModel",
	"sap/base/util/deepClone",
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
 * 		"caseIds": [{"id": ""}],
 * 	},
 * 	"tableData": [
 * 		{
 * 			"candidatesID": "",
 * 			....			
 *    }
 * 	]
 * 	"competencyData": {
 * 		"selectedRowIndex": int,
 * 		"competencies": []
 *  },
 * 	"indicatorData": {
 * 		"percentValue": 70, // インジケーターに表示する％
 * 		"assignedCount": 5, // 割り当てられた人の数
 * 		"positionCount": 20 // ポジションの数
 * 	}
 * }
 * 
 * 
 * @param {typeof sap.ui.model.json.JSONModel} JSONModel 
 * @param {*} deepClone
 * @param {Object} ExportTableDataRepository
 * @param {Object} CompetenciesRepository
 * @returns 
 */
function(JSONModel, deepClone, ExportTableDataRepository, CompetenciesRepository) {
	return JSONModel.extend("com.sap.sfsf.reshuffle.applicants.model.EditCaseTableModel", {
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
		 * 初期のテーブルのデータ。最後に比較して差分抽出するのに利用
		 * @type {Object}
		 */
		_initialTableData: null,

		/**
		 * 
		 * @param {string} backendServiceBaseUrl 
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
		 * フィルター（CaseID）で利用するデータを取得＆セット
		 */
		_getFiltersData() {
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
						return {
							id: val["ID"]
						}
					});

					_this.setProperty("/filterData", {"caseids": items});
				},
				error: function(data) {
					_this.errorMessageToastCallback("failed to load filter: caseids");
				}
			});
		},
		/**
		 * Tableにセットするデータを読み込む。
		 * ODataModelを利用するとメタデータに無いデータを追加出来なかったりするので手動で読み込み
		 * 
		 * @param {String} caseID 
		 * @param {()=>void} startLoading APIコール初めのタイミングで呼ばれるcallback
		 * @param {()=>void} finishLoading APIコールが完了したタイミングで呼ばれるcallback
		 */
		getTableData(caseID, startLoading, finishLoading) {

			var _this = this;
			startLoading();
			$.get({
				url: this.backendServiceBaseUrl + "Candidates",
				contentType: 'application/json',
				data: {
					"$filter": `caseID eq '${caseID}'`
				},
				dataType: 'json',
				success: function(response) {
					finishLoading();
					var fetchedData = /** @type {Object[]} */ (response.value);
					_this._initialTableData = deepClone(fetchedData);
					_this.setProperty("/tableData", fetchedData);
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

			var assignedCount = tableData.filter((t)=> t["candidateID"] != null || t["candidateID"] != undefined).length;
			var positionCount = tableData.length;

			data["percentValue"] = Math.round((assignedCount/positionCount)*100);
			data["assignedCount"] = assignedCount;
			data["positionCount"] = positionCount;

			this.setProperty("/indicatorData", data);
		},
		/**
		 * テーブルをクリアしてフィルターもリロードする
		 */
		clearAndReloadFilter() {
			this._initialTableData = null;
			this.setData({});
			this._getFiltersData();
			this.updateIndicatorData();
		},
		/**
		 * 空きポストがあるかどうかをチェック
		 */
		hasEmptyPost() {
			var tableData = /** @type {Object[]} */ (this.getProperty("/tableData"));

			var nullCandidateIDs = tableData.filter((data)=> {
				return data["candidateID"] == null || data["candidateID"] == undefined;
			});
			return nullCandidateIDs.length != 0;
		},

		/**
		 * テーブルの行をアップデートするためのメソッド
		 * @param {int} rowIndex 
		 * @param {String} candidateID 
		 * @param {String} candidateName
		 * @returns {Boolean} validationも通って値のアップデートが出来たかどうか 
		 */
		updateRow(rowIndex, candidateID, candidateName) {
			var tableData = /** @type {Object[]} */ (this.getProperty("/tableData"));

			// ドロップされたcandidateのデータと同じ物が既に存在しないかチェック
			var sameCandidate = tableData.filter((data)=> {
				if(data["candidateID"] == null || data["candidateID"] == undefined) {
					return false;
				}
				return data["candidateID"] == candidateID;
			});
			if(sameCandidate.length != 0) {
				// 既に同じ人物がdropされて居る
				return false;
			}

			this.setProperty("/tableData/"+ rowIndex +"/candidateID", candidateID);
			this.setProperty("/tableData/"+ rowIndex +"/candidateName", candidateName);
			this.updateIndicatorData();
			return true;
		},
		/**
		 * ドラッグアンドドロップでドラッグされて成功したときにrowを更新するメソッド
		 * @param {Number} draggedIndex // DragされたRowのindex
		 */
		tableRowDragged(draggedIndex) {
			this.setProperty("/tableData/"+ draggedIndex + "/candidateID", null);
			this.setProperty("/tableData/"+ draggedIndex + "/candidateName", null);
			this.updateIndicatorData();
		},
		/**
		 * Indexで指定した行に割り当てられたCandidateのempIDを取得
		 * @param {int} index 
		 * @returns {string}
		 */
		getAssignedCandidateID(index) {
			var data = this.getProperty("/tableData/" + index + "/candidateID");
			if(data == null || data == undefined) {
				return null;
			}
			return data;
		},
		/**
		 * Caseの保存。
		 * テーブルに表示されているデータの保存。
		 * 全部削除→全作成という２段階で一旦実装
		 * @param {sap.ui.model.odata.v4.ODataModel} model 
		 * @param {()=>void} success
		 * @param {()=>void} failed
		 */
		saveCase(model, success, failed) {
			var _this = this;

			// ほんとはbatchで全部まとめてやりたい。
			// 全削除→全作成
			this._deleteAllInitialTableData(() => {
				_this._createAllCurrentTableData(model, ()=> {
					success();
				}, ()=> {
					failed();
				})
			}, () => {
				failed();
			})
		},
		/**
		 * 現在表示しているcaseの削除
		 * @param {()=>void} success 
		 * @param {()=>void} fail 
		 */
		deleteCurrentCase(success, fail) {
			this._deleteAllInitialTableData(success, fail);
		},
		/**
		 * 対象のcaseIDのデータを全て削除
		 * @param {()=>void} success 
		 * @param {()=>void} fail 
		 */
		_deleteAllInitialTableData(success, fail) {
			var _this = this;

			var deleteRequests = this._initialTableData.map((data) => {
				return $.ajax({
        	url: _this.backendServiceBaseUrl + `Candidates(caseID='${data["caseID"]}',positionID='${data["positionID"]}',candidateID='${data["candidateID"]}')`,
        	type: 'DELETE'
      	})
			})

			$.when.apply(null, deleteRequests).then(function(){
				var data = arguments;
				if(deleteRequests.length == 1) {
					// @ts-ignore
					data = [arguments];
				}
				for(var i=0; i < data.length; i++) {
					console.log(data[i]);
					var result = data[i][2];
					if(result.status <200 && result.status >= 300) {
						fail();
						break;
					}
				}
				success();
			});
		},

		/**
		 * 現在のtableに表示されて居るデータを全てサーバー側にcreateする
		 * @param {sap.ui.model.odata.v4.ODataModel} model 
		 * @param {()=>void} success
		 * @param {()=>void} failed
		 */
		_createAllCurrentTableData(model, success, failed) {
			var bindList = model.bindList("/Candidates", null, null, null,{"$$updateGroupId": "updateCandidatesBatchGroup"});
			var currentTableData = /** @type {Object[]} */ (this.getProperty("/tableData"));

			currentTableData.forEach((data)=> {
				if(data["candidateID"] == null) {
					return;
				}
				var postData = {}
				postData["caseID"] = data["caseID"];
				postData["candidateID"] = data["candidateID"];
				postData["positionID"] = data["positionID"];
				postData["incumbentEmpID"] = data["incumbentEmpID"]
				bindList.create(postData, true)
			});
			model.submitBatch("updateCandidatesBatchGroup").then(
				function() {
					if(model.hasPendingChanges("updateCandidatesBatchGroup")) {
						model.resetChanges("updateCandidatesBatchGroup");
						failed();
					} else {
						success();
					}
				},
				function(error) {
					failed();
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
				tmp["candidateID"] = data["candidateID"];
				tmp["positionID"] = data["positionID"];
				tmp["incumbentEmpID"] = data["incumbentEmpID"];
				entities.push(tmp);
			});

			ExportTableDataRepository.exportTableData(this.backendServiceApiBaseUrl, entities, filename, success, fail)
		}
	});
})