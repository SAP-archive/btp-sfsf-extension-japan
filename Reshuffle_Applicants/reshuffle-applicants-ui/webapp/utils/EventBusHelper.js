sap.ui.define(
function() {
	"use strict";
	return {
		/**
		 * 
		 * @param {sap.ui.core.mvc.Controller} controller 
		 * @param {Function} fn イベントが来た時に動かすfunction
		 */
		subscribeOpenNextPositionTableEvent: function(controller, fn) {
			var oEventBus = controller.getOwnerComponent().getEventBus();
      oEventBus.subscribe("viewEventChannel","OpenNextPositionEvent",fn,controller);
		},
		/**
		 * NextPositionTable(右側のテーブル)を表示するイベントをpublishする
		 * @param {sap.ui.core.mvc.Controller} controller 
		 */
		publishOpenNextPositionTableEvent: function(controller) {
			var oEventBus = controller.getOwnerComponent().getEventBus();
      oEventBus.publish("viewEventChannel","OpenNextPositionEvent");
		},
		/**
		 * NextPositionTableを非表示にするイベントのsubscribe
		 * @param {sap.ui.core.mvc.Controller} controller 
		 * @param {Function} fn イベントが来た時に動かすfunction
		 */
		subscribeCloseNextPositionTableEvent: function(controller, fn) {
			var oEventBus = controller.getOwnerComponent().getEventBus();
      oEventBus.subscribe("viewEventChannel","CloseNextPositionEvent",fn,controller);
		},
		/**
		 * NextPositionTable(右側のテーブル)を非表示にするイベントをpublishする
		 * @param {sap.ui.core.mvc.Controller} controller 
		 */
		publishCloseNextPositionTableEvent: function(controller) {
			var oEventBus = controller.getOwnerComponent().getEventBus();
      oEventBus.publish("viewEventChannel","CloseNextPositionEvent");
		},
		/**
		 * EditCaseを非表示にするイベントのsubscribe
		 * @param {sap.ui.core.mvc.Controller} controller 
		 * @param {Function} fn イベントが来た時に動かすfunction
		 */
		subscribeOpenEditCaseTableEvent: function(controller, fn) {
			var oEventBus = controller.getOwnerComponent().getEventBus();
      oEventBus.subscribe("viewEventChannel","OpenEditCaseEvent",fn,controller);
		},
		/**
		 * EditCaseTable(右側のテーブル)を表示するイベントをpublishする
		 * @param {sap.ui.core.mvc.Controller} controller 
		 */
		publishOpenEditCaseTableEvent: function(controller) {
			var oEventBus = controller.getOwnerComponent().getEventBus();
      oEventBus.publish("viewEventChannel","OpenEditCaseEvent");
		},
		/**
		 * EditCaseを表示するイベントのsubscribe
		 * @param {sap.ui.core.mvc.Controller} controller 
		 * @param {Function} fn イベントが来た時に動かすfunction
		 */
		subscribeCloseEditCaseTableEvent: function(controller, fn) {
			var oEventBus = controller.getOwnerComponent().getEventBus();
      oEventBus.subscribe("viewEventChannel","CloseEditCaseEvent",fn,controller);
		},
		/**
		 * EditCaseTable(右側のテーブル)を非表示にするイベントをpublishする
		 * @param {sap.ui.core.mvc.Controller} controller 
		 */
		publishCloseEditCaseTableEvent: function(controller) {
			var oEventBus = controller.getOwnerComponent().getEventBus();
      oEventBus.publish("viewEventChannel","CloseEditCaseEvent");
		}
	}
})