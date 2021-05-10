/* global QUnit */
QUnit.config.autostart = false;

sap.ui.getCore().attachInit(function () {
	"use strict";

	sap.ui.require([
		"com/sap/sfsf/configuration/Reshuffle_Configuration_UI/test/unit/AllTests"
	], function () {
		QUnit.start();
	});
});