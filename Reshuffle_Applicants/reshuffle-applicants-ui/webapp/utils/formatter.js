sap.ui.define([], function () {
	"use strict";
	return {
		transferedFlagText: function (/** @type {Boolean} */ flag) {
			if(flag) {
				return "✓";
			}
			return "";
		}
	};
});