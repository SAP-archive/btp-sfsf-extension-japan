/* global QUnit */
QUnit.config.autostart = false;

sap.ui.getCore().attachInit(function () {
	"use strict";

	sap.ui.require([
		"com/sap/sfsf/comparison/Reshuffle_Comparison_UI/test/integration/AllJourneys"
	], function () {
		QUnit.start();
	});
});