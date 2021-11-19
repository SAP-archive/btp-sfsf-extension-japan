{
	"contents": {
		"39d52fba-7f62-43d8-b25b-b072df2e60a9": {
			"classDefinition": "com.sap.bpm.wfs.Model",
			"id": "simulation_approval",
			"subject": "simulation_approval",
			"name": "simulation_approval",
			"documentation": "",
			"lastIds": "62d7f4ed-4063-4c44-af8b-39050bd44926",
			"events": {
				"11a9b5ee-17c0-4159-9bbf-454dcfdcd5c3": {
					"name": "StartEvent1"
				},
				"2798f4e7-bc42-4fad-a248-159095a2f40a": {
					"name": "EndEvent1"
				},
				"0feb4abe-061a-47f1-87a8-de59416f0183": {
					"name": "EndEvent4"
				},
				"747a49cb-681c-419a-a60d-71e0e655996f": {
					"name": "EndEvent5"
				},
				"7d0514a6-29f4-4883-bb2c-0c7381e4b142": {
					"name": "EndEvent7"
				}
			},
			"activities": {
				"55c648ed-fdd8-490d-b162-e645045f8738": {
					"name": "set Next Approver (Current Manager)"
				},
				"7042cf7c-7fc9-4ad3-a2ed-5a28c9858a66": {
					"name": "Current Manager Approval"
				},
				"6bda5a59-c008-439c-92dc-1aeb2d0e16e4": {
					"name": "statusCheck(current manager)"
				},
				"df4a588d-9932-4aad-aed1-8d537db502f7": {
					"name": "ExclusiveGateway1"
				},
				"9722cf21-cb25-4388-bf38-f8a9a1f14c74": {
					"name": "ExclusiveGateway2"
				},
				"f409d90a-2b95-4720-9401-a52ea4baa7b7": {
					"name": "ParallelGateway1"
				},
				"83b8345d-5071-4126-a309-afc0757fb0d2": {
					"name": "ExclusiveGateway3"
				},
				"29c5d740-25ce-4afb-b595-185d6c4c63e3": {
					"name": "set Next Approver(Next Manager)"
				},
				"ee98e134-e894-45e7-ae17-3217e777c3b1": {
					"name": "Next Manager Approval"
				},
				"47ce29ce-9bfb-4ea5-85b2-b284ca504c83": {
					"name": "statusCheck(next manager)"
				},
				"f055559e-b46f-4caa-b8c2-f9f09837b771": {
					"name": "ExclusiveGateway4"
				},
				"2196e5b6-cf15-4967-9433-1fd8caa6ed4e": {
					"name": "ExclusiveGateway5"
				},
				"b35d8f7d-0dfc-4408-a8a7-59f29951cb24": {
					"name": "HR Approval"
				},
				"86964ce9-e9ee-4935-88f7-e5d3dc91dae6": {
					"name": "updateCandidates"
				},
				"f08f8cd5-251a-4cd4-a6c9-2c2747346f9a": {
					"name": "define All Candidates"
				},
				"c3be53fe-c606-4dd7-a3a3-3b154d80f6e0": {
					"name": "ExclusiveGateway10"
				},
				"fc70f644-29af-41b4-ab93-fa2b92c7a391": {
					"name": "ExclusiveGateway11"
				},
				"f71cfd9f-6a0f-4446-b3ad-5b3f428c688c": {
					"name": "hr_deny"
				},
				"0b45570e-6733-463f-9565-e8002273ce12": {
					"name": "ExclusiveGateway16"
				},
				"245ec0a5-514d-4580-8ca0-cf8e30e942e9": {
					"name": "ParallelGateway5"
				},
				"f4322e19-9777-47fd-ba76-89e75eea7ffd": {
					"name": "ExclusiveGateway17"
				},
				"0b035e81-5023-4bb6-afcb-8b741fef8087": {
					"name": "denial(current manager)"
				},
				"f1d4306c-d559-42da-939f-c20e4a7202d5": {
					"name": "denial(next manager)"
				},
				"75c527ec-b455-406d-b922-9fdd24c0f619": {
					"name": "denial_HR"
				}
			},
			"sequenceFlows": {
				"c6b99f32-5fe6-4ab6-b60a-80fba1b9ae0f": {
					"name": "SequenceFlow1"
				},
				"f38a04a2-2624-426a-894c-5156c5dc7cf6": {
					"name": "SequenceFlow2"
				},
				"55cc0d99-b7d0-47b4-963a-0595d434304e": {
					"name": "SequenceFlow3"
				},
				"d2ee874b-4b71-42a8-9a59-54727727f46d": {
					"name": "SequenceFlow4"
				},
				"e94ceca4-d809-478f-818e-cdeef4d7a046": {
					"name": "SequenceFlow5"
				},
				"ff4ba49b-ba53-4729-8f1d-62d18b69ded9": {
					"name": "SequenceFlow6"
				},
				"36de9c4f-c905-48e5-b0c8-24c2f3234f5e": {
					"name": "SequenceFlow7"
				},
				"023d2efa-4f72-4d7b-b169-9488b61d7db1": {
					"name": "SequenceFlow8"
				},
				"4d01666c-9cae-4674-bf20-8ee615bd0a86": {
					"name": "SequenceFlow9"
				},
				"94093928-3ee3-4b2d-9ecd-9938f6d3960f": {
					"name": "SequenceFlow10"
				},
				"6e32b055-1863-49f7-8743-6a76ca05be7a": {
					"name": "SequenceFlow11"
				},
				"b6d52e3d-730d-4168-8b14-3d5b87182253": {
					"name": "SequenceFlow12"
				},
				"c1cf54a3-68b3-412d-a1a1-1aad4f593b7a": {
					"name": "SequenceFlow13"
				},
				"3cf628e4-4998-40f6-ba4d-44fe7a365635": {
					"name": "SequenceFlow15"
				},
				"7d3885f4-7dac-4154-9426-fc5cc40fbee4": {
					"name": "SequenceFlow19"
				},
				"9177a247-92f0-4160-ab37-973cfbebca8b": {
					"name": "SequenceFlow21"
				},
				"8d8601ae-df38-4010-b13c-55d6b179366d": {
					"name": "SequenceFlow27"
				},
				"4527d22c-ddf2-4526-8832-3b3847f7d30e": {
					"name": "SequenceFlow32"
				},
				"dcc638dd-e318-4ebc-9b6e-5df931c3755c": {
					"name": "SequenceFlow50"
				},
				"4a77b961-eb9b-48fe-8f04-081be7ce400a": {
					"name": "denialEvent"
				},
				"83794608-8a4f-4fcd-9c5f-12a0272b9663": {
					"name": "SequenceFlow52"
				},
				"4ce7918e-2632-4c54-b0e9-aa4f60a04bfe": {
					"name": "SequenceFlow56"
				},
				"90926328-e6be-42f7-b297-34e8b377a75e": {
					"name": "SequenceFlow63"
				},
				"5de32aa3-f2ef-4ae4-883a-cdd490c54b4d": {
					"name": "SequenceFlow64"
				},
				"9e060501-2592-4a35-aaa5-e08c888d462d": {
					"name": "SequenceFlow65"
				},
				"620f7c74-1da7-495a-9622-85bc2a3bab4b": {
					"name": "SequenceFlow66"
				},
				"d6926956-f048-4d74-a8b7-92abdf304823": {
					"name": "SequenceFlow67"
				},
				"91bc939d-8f35-44b7-b021-bc13a3acf456": {
					"name": "SequenceFlow68"
				},
				"507eae6c-b11d-44f7-82df-60449466e60e": {
					"name": "SequenceFlow69"
				},
				"3b357805-3bc2-45b1-9ac7-e50250f8ed8f": {
					"name": "SequenceFlow70"
				},
				"c86e0281-ea22-486b-8bb5-9fc81c06dc8b": {
					"name": "SequenceFlow71"
				},
				"33d614b1-4b4e-4da8-b1df-59f399be0f2b": {
					"name": "SequenceFlow72"
				}
			},
			"diagrams": {
				"42fa7a2d-c526-4a02-b3ba-49b5168ba644": {}
			}
		},
		"11a9b5ee-17c0-4159-9bbf-454dcfdcd5c3": {
			"classDefinition": "com.sap.bpm.wfs.StartEvent",
			"id": "startevent1",
			"name": "StartEvent1",
			"sampleContextRefs": {
				"f5b330af-33e4-4f9c-8983-9128dfd54a3e": {}
			}
		},
		"2798f4e7-bc42-4fad-a248-159095a2f40a": {
			"classDefinition": "com.sap.bpm.wfs.EndEvent",
			"id": "endevent1",
			"name": "EndEvent1"
		},
		"0feb4abe-061a-47f1-87a8-de59416f0183": {
			"classDefinition": "com.sap.bpm.wfs.EndEvent",
			"id": "endevent4",
			"name": "EndEvent4",
			"eventDefinitions": {
				"7fef017f-80d7-40a5-abbb-57da3516df8c": {}
			}
		},
		"747a49cb-681c-419a-a60d-71e0e655996f": {
			"classDefinition": "com.sap.bpm.wfs.EndEvent",
			"id": "endevent5",
			"name": "EndEvent5",
			"eventDefinitions": {
				"d2af257b-7cb6-4ac9-92d6-c3243a4b2477": {}
			}
		},
		"7d0514a6-29f4-4883-bb2c-0c7381e4b142": {
			"classDefinition": "com.sap.bpm.wfs.EndEvent",
			"id": "endevent7",
			"name": "EndEvent7",
			"eventDefinitions": {
				"ddefc4b0-dc0f-4919-9826-ac4af85c478b": {}
			}
		},
		"55c648ed-fdd8-490d-b162-e645045f8738": {
			"classDefinition": "com.sap.bpm.wfs.ScriptTask",
			"reference": "/scripts/simulation_approval/setNextApprover.js",
			"id": "scripttask1",
			"name": "set Next Approver (Current Manager)"
		},
		"7042cf7c-7fc9-4ad3-a2ed-5a28c9858a66": {
			"classDefinition": "com.sap.bpm.wfs.UserTask",
			"subject": "異動案（${context.caseID}） 承認画面",
			"priority": "MEDIUM",
			"isHiddenInLogForParticipant": false,
			"supportsForward": false,
			"userInterface": "sapui5://comsapbpmworkflow.comsapbpmwusformplayer/com.sap.bpm.wus.form.player",
			"recipientUsers": "${context.nextApprover_cm}",
			"formReference": "/forms/simulation_approval/CurrentManagerApproveUI.form",
			"userInterfaceParams": [{
				"key": "formId",
				"value": "currentmanagerapproveui"
			}, {
				"key": "formRevision",
				"value": "1.0"
			}],
			"customAttributes": [],
			"id": "usertask1",
			"name": "Current Manager Approval"
		},
		"6bda5a59-c008-439c-92dc-1aeb2d0e16e4": {
			"classDefinition": "com.sap.bpm.wfs.ScriptTask",
			"reference": "/scripts/simulation_approval/statusCheck.js",
			"id": "scripttask2",
			"name": "statusCheck(current manager)"
		},
		"df4a588d-9932-4aad-aed1-8d537db502f7": {
			"classDefinition": "com.sap.bpm.wfs.ExclusiveGateway",
			"id": "exclusivegateway1",
			"name": "ExclusiveGateway1"
		},
		"9722cf21-cb25-4388-bf38-f8a9a1f14c74": {
			"classDefinition": "com.sap.bpm.wfs.ExclusiveGateway",
			"id": "exclusivegateway2",
			"name": "ExclusiveGateway2",
			"default": "ff4ba49b-ba53-4729-8f1d-62d18b69ded9"
		},
		"f409d90a-2b95-4720-9401-a52ea4baa7b7": {
			"classDefinition": "com.sap.bpm.wfs.ParallelGateway",
			"id": "parallelgateway1",
			"name": "ParallelGateway1"
		},
		"83b8345d-5071-4126-a309-afc0757fb0d2": {
			"classDefinition": "com.sap.bpm.wfs.ExclusiveGateway",
			"id": "exclusivegateway3",
			"name": "ExclusiveGateway3"
		},
		"29c5d740-25ce-4afb-b595-185d6c4c63e3": {
			"classDefinition": "com.sap.bpm.wfs.ScriptTask",
			"reference": "/scripts/simulation_approval/setNextApprover_nm.js",
			"id": "scripttask3",
			"name": "set Next Approver(Next Manager)"
		},
		"ee98e134-e894-45e7-ae17-3217e777c3b1": {
			"classDefinition": "com.sap.bpm.wfs.UserTask",
			"subject": "異動案（${context.caseID}） 承認画面",
			"priority": "MEDIUM",
			"isHiddenInLogForParticipant": false,
			"supportsForward": false,
			"userInterface": "sapui5://comsapbpmworkflow.comsapbpmwusformplayer/com.sap.bpm.wus.form.player",
			"recipientUsers": "${context.nextApprover_nm}",
			"formReference": "/forms/simulation_approval/nextManagerApproveUI.form",
			"userInterfaceParams": [{
				"key": "formId",
				"value": "nextmanagerapproveui"
			}, {
				"key": "formRevision",
				"value": "1.0"
			}],
			"id": "usertask2",
			"name": "Next Manager Approval"
		},
		"47ce29ce-9bfb-4ea5-85b2-b284ca504c83": {
			"classDefinition": "com.sap.bpm.wfs.ScriptTask",
			"reference": "/scripts/simulation_approval/statusCheck_nm.js",
			"id": "scripttask4",
			"name": "statusCheck(next manager)"
		},
		"f055559e-b46f-4caa-b8c2-f9f09837b771": {
			"classDefinition": "com.sap.bpm.wfs.ExclusiveGateway",
			"id": "exclusivegateway4",
			"name": "ExclusiveGateway4",
			"default": "3cf628e4-4998-40f6-ba4d-44fe7a365635"
		},
		"2196e5b6-cf15-4967-9433-1fd8caa6ed4e": {
			"classDefinition": "com.sap.bpm.wfs.ExclusiveGateway",
			"id": "exclusivegateway5",
			"name": "ExclusiveGateway5"
		},
		"b35d8f7d-0dfc-4408-a8a7-59f29951cb24": {
			"classDefinition": "com.sap.bpm.wfs.UserTask",
			"subject": "異動案（${context.caseID}） 最終承認画面",
			"priority": "HIGH",
			"isHiddenInLogForParticipant": false,
			"supportsForward": false,
			"userInterface": "sapui5://comsapbpmworkflow.comsapbpmwusformplayer/com.sap.bpm.wus.form.player",
			"recipientUsers": "${context.sfadmin}",
			"recipientGroups": "",
			"formReference": "/forms/simulation_approval/HRApprove.form",
			"userInterfaceParams": [{
				"key": "formId",
				"value": "hrapprove"
			}, {
				"key": "formRevision",
				"value": "1.0"
			}],
			"id": "usertask4",
			"name": "HR Approval"
		},
		"86964ce9-e9ee-4935-88f7-e5d3dc91dae6": {
			"classDefinition": "com.sap.bpm.wfs.ServiceTask",
			"destination": "Reshuffle_Simulation_approve",
			"path": "",
			"httpMethod": "POST",
			"xsrfPath": "",
			"requestVariable": "${context.caseID}",
			"id": "servicetask1",
			"name": "updateCandidates",
			"principalPropagationRef": "b35d8f7d-0dfc-4408-a8a7-59f29951cb24"
		},
		"f08f8cd5-251a-4cd4-a6c9-2c2747346f9a": {
			"classDefinition": "com.sap.bpm.wfs.ScriptTask",
			"reference": "/scripts/simulation_approval/defAllCandidates.js",
			"id": "scripttask8",
			"name": "define All Candidates"
		},
		"c3be53fe-c606-4dd7-a3a3-3b154d80f6e0": {
			"classDefinition": "com.sap.bpm.wfs.ExclusiveGateway",
			"id": "exclusivegateway10",
			"name": "ExclusiveGateway10",
			"default": "dcc638dd-e318-4ebc-9b6e-5df931c3755c"
		},
		"fc70f644-29af-41b4-ab93-fa2b92c7a391": {
			"classDefinition": "com.sap.bpm.wfs.ExclusiveGateway",
			"id": "exclusivegateway11",
			"name": "ExclusiveGateway11",
			"default": "83794608-8a4f-4fcd-9c5f-12a0272b9663"
		},
		"f71cfd9f-6a0f-4446-b3ad-5b3f428c688c": {
			"classDefinition": "com.sap.bpm.wfs.ExclusiveGateway",
			"id": "exclusivegateway13",
			"name": "hr_deny",
			"default": "4ce7918e-2632-4c54-b0e9-aa4f60a04bfe"
		},
		"0b45570e-6733-463f-9565-e8002273ce12": {
			"classDefinition": "com.sap.bpm.wfs.ExclusiveGateway",
			"id": "exclusivegateway16",
			"name": "ExclusiveGateway16"
		},
		"c6b99f32-5fe6-4ab6-b60a-80fba1b9ae0f": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow1",
			"name": "SequenceFlow1",
			"sourceRef": "11a9b5ee-17c0-4159-9bbf-454dcfdcd5c3",
			"targetRef": "f08f8cd5-251a-4cd4-a6c9-2c2747346f9a"
		},
		"f38a04a2-2624-426a-894c-5156c5dc7cf6": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow2",
			"name": "SequenceFlow2",
			"sourceRef": "55c648ed-fdd8-490d-b162-e645045f8738",
			"targetRef": "7042cf7c-7fc9-4ad3-a2ed-5a28c9858a66"
		},
		"55cc0d99-b7d0-47b4-963a-0595d434304e": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow3",
			"name": "SequenceFlow3",
			"sourceRef": "7042cf7c-7fc9-4ad3-a2ed-5a28c9858a66",
			"targetRef": "6bda5a59-c008-439c-92dc-1aeb2d0e16e4"
		},
		"d2ee874b-4b71-42a8-9a59-54727727f46d": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow4",
			"name": "SequenceFlow4",
			"sourceRef": "6bda5a59-c008-439c-92dc-1aeb2d0e16e4",
			"targetRef": "c3be53fe-c606-4dd7-a3a3-3b154d80f6e0"
		},
		"e94ceca4-d809-478f-818e-cdeef4d7a046": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow5",
			"name": "SequenceFlow5",
			"sourceRef": "df4a588d-9932-4aad-aed1-8d537db502f7",
			"targetRef": "55c648ed-fdd8-490d-b162-e645045f8738"
		},
		"ff4ba49b-ba53-4729-8f1d-62d18b69ded9": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow6",
			"name": "SequenceFlow6",
			"sourceRef": "9722cf21-cb25-4388-bf38-f8a9a1f14c74",
			"targetRef": "2196e5b6-cf15-4967-9433-1fd8caa6ed4e"
		},
		"36de9c4f-c905-48e5-b0c8-24c2f3234f5e": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"condition": "${context.currentManagersLength != 0}",
			"id": "sequenceflow7",
			"name": "SequenceFlow7",
			"sourceRef": "9722cf21-cb25-4388-bf38-f8a9a1f14c74",
			"targetRef": "df4a588d-9932-4aad-aed1-8d537db502f7"
		},
		"023d2efa-4f72-4d7b-b169-9488b61d7db1": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow8",
			"name": "SequenceFlow8",
			"sourceRef": "f409d90a-2b95-4720-9401-a52ea4baa7b7",
			"targetRef": "df4a588d-9932-4aad-aed1-8d537db502f7"
		},
		"4d01666c-9cae-4674-bf20-8ee615bd0a86": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow9",
			"name": "SequenceFlow9",
			"sourceRef": "f409d90a-2b95-4720-9401-a52ea4baa7b7",
			"targetRef": "83b8345d-5071-4126-a309-afc0757fb0d2"
		},
		"94093928-3ee3-4b2d-9ecd-9938f6d3960f": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow10",
			"name": "SequenceFlow10",
			"sourceRef": "83b8345d-5071-4126-a309-afc0757fb0d2",
			"targetRef": "29c5d740-25ce-4afb-b595-185d6c4c63e3"
		},
		"6e32b055-1863-49f7-8743-6a76ca05be7a": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow11",
			"name": "SequenceFlow11",
			"sourceRef": "29c5d740-25ce-4afb-b595-185d6c4c63e3",
			"targetRef": "ee98e134-e894-45e7-ae17-3217e777c3b1"
		},
		"b6d52e3d-730d-4168-8b14-3d5b87182253": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow12",
			"name": "SequenceFlow12",
			"sourceRef": "ee98e134-e894-45e7-ae17-3217e777c3b1",
			"targetRef": "47ce29ce-9bfb-4ea5-85b2-b284ca504c83"
		},
		"c1cf54a3-68b3-412d-a1a1-1aad4f593b7a": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow13",
			"name": "SequenceFlow13",
			"sourceRef": "47ce29ce-9bfb-4ea5-85b2-b284ca504c83",
			"targetRef": "fc70f644-29af-41b4-ab93-fa2b92c7a391"
		},
		"3cf628e4-4998-40f6-ba4d-44fe7a365635": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow15",
			"name": "SequenceFlow15",
			"sourceRef": "f055559e-b46f-4caa-b8c2-f9f09837b771",
			"targetRef": "2196e5b6-cf15-4967-9433-1fd8caa6ed4e"
		},
		"7d3885f4-7dac-4154-9426-fc5cc40fbee4": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"condition": "${context.nextManagersLength != 0}",
			"id": "sequenceflow19",
			"name": "SequenceFlow19",
			"sourceRef": "f055559e-b46f-4caa-b8c2-f9f09837b771",
			"targetRef": "83b8345d-5071-4126-a309-afc0757fb0d2"
		},
		"9177a247-92f0-4160-ab37-973cfbebca8b": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow21",
			"name": "SequenceFlow21",
			"sourceRef": "b35d8f7d-0dfc-4408-a8a7-59f29951cb24",
			"targetRef": "f71cfd9f-6a0f-4446-b3ad-5b3f428c688c"
		},
		"8d8601ae-df38-4010-b13c-55d6b179366d": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow27",
			"name": "SequenceFlow27",
			"sourceRef": "86964ce9-e9ee-4935-88f7-e5d3dc91dae6",
			"targetRef": "2798f4e7-bc42-4fad-a248-159095a2f40a"
		},
		"4527d22c-ddf2-4526-8832-3b3847f7d30e": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow32",
			"name": "SequenceFlow32",
			"sourceRef": "f08f8cd5-251a-4cd4-a6c9-2c2747346f9a",
			"targetRef": "0b45570e-6733-463f-9565-e8002273ce12"
		},
		"dcc638dd-e318-4ebc-9b6e-5df931c3755c": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow50",
			"name": "SequenceFlow50",
			"sourceRef": "c3be53fe-c606-4dd7-a3a3-3b154d80f6e0",
			"targetRef": "9722cf21-cb25-4388-bf38-f8a9a1f14c74"
		},
		"4a77b961-eb9b-48fe-8f04-081be7ce400a": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"condition": "${usertasks.usertask1.last.decision == \"deny\"}",
			"id": "sequenceflow51",
			"name": "denialEvent",
			"sourceRef": "0b035e81-5023-4bb6-afcb-8b741fef8087",
			"targetRef": "0feb4abe-061a-47f1-87a8-de59416f0183"
		},
		"83794608-8a4f-4fcd-9c5f-12a0272b9663": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow52",
			"name": "SequenceFlow52",
			"sourceRef": "fc70f644-29af-41b4-ab93-fa2b92c7a391",
			"targetRef": "f055559e-b46f-4caa-b8c2-f9f09837b771"
		},
		"4ce7918e-2632-4c54-b0e9-aa4f60a04bfe": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow56",
			"name": "SequenceFlow56",
			"sourceRef": "f71cfd9f-6a0f-4446-b3ad-5b3f428c688c",
			"targetRef": "86964ce9-e9ee-4935-88f7-e5d3dc91dae6"
		},
		"90926328-e6be-42f7-b297-34e8b377a75e": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow63",
			"name": "SequenceFlow63",
			"sourceRef": "0b45570e-6733-463f-9565-e8002273ce12",
			"targetRef": "f409d90a-2b95-4720-9401-a52ea4baa7b7"
		},
		"42fa7a2d-c526-4a02-b3ba-49b5168ba644": {
			"classDefinition": "com.sap.bpm.wfs.ui.Diagram",
			"symbols": {
				"df898b52-91e1-4778-baad-2ad9a261d30e": {},
				"53e54950-7757-4161-82c9-afa7e86cff2c": {},
				"6bb141da-d485-4317-93b8-e17711df4c32": {},
				"43633823-6f17-49c6-8110-4c4559417d6a": {},
				"7880622e-bf13-4e4e-83a5-68da0b814453": {},
				"3d7639cc-3426-4a8d-bfad-c8656ea587bf": {},
				"337dbba4-c936-44ba-b39b-8263f07ea4ae": {},
				"93fac5fc-a140-43cc-a7bc-2071ae27bf7f": {},
				"a726859c-fed2-437b-894f-faf5e2a3db26": {},
				"aa507045-81df-4358-9d85-d68085caee61": {},
				"a626b797-c750-48aa-ab2f-71615ec515fe": {},
				"4c52042d-56fc-4fc2-adb9-fdc1acc40897": {},
				"7d3739b5-ae14-4706-86c6-727d02e8bf69": {},
				"08636d7d-cc07-4abe-b418-9e20bb2ffbdd": {},
				"d21c5e3e-e069-41df-a586-651b942104b4": {},
				"e7840887-9fc0-4333-85b1-b5f1dc83e089": {},
				"a69e8dde-cf9b-4997-b281-ddc31e5b2787": {},
				"f2fb34eb-ad88-476b-942a-bccd692d44db": {},
				"cbf8165c-81f3-43af-803b-36b3cc4e632b": {},
				"809a664b-1e06-48a2-92a9-2f219568b972": {},
				"a9e1dfdd-696c-4d3f-bbcc-4fe88aa402db": {},
				"fa68c92e-780c-4d0a-a121-42df3ac0fd67": {},
				"b661c108-1dda-4274-8675-6fd5c5925eba": {},
				"e07c358b-c74d-4cba-9018-221e2b6af185": {},
				"fbc69f69-74c1-495d-b437-4fe8c2f566f8": {},
				"b5d7f437-c107-449b-88b9-f9f351afc6db": {},
				"eef597a9-ebe1-4add-b941-1dc43a7f0ad5": {},
				"7ce430d2-aa6c-4feb-b001-c6110446a42f": {},
				"b30e5e4b-893f-44f7-bae9-08ed8524d12c": {},
				"b6ab27c4-5a73-463a-bb67-93b16013db69": {},
				"87698911-fc01-4c47-9120-a154be3ffec7": {},
				"3aec1617-5ad1-48ad-8917-0bd3ddeedc64": {},
				"a302048d-373a-461c-944b-bacf913ef8a5": {},
				"15650dc5-2ac0-4b76-9993-77af77353136": {},
				"2bc60639-9316-4bda-a010-ce1b6bcecd32": {},
				"512087a0-a046-4ef1-935e-5f2b41debf3a": {},
				"5e3eae32-dc1d-4b01-95eb-63b02fccdadf": {},
				"db0300b1-8fb6-4caa-b2e2-d9661cfde18d": {},
				"62a073fe-33c2-42c6-a17f-52d18ab09c91": {},
				"2cbe801c-9e58-4bfa-a874-8cef7b5d4325": {},
				"6c39a6e9-2de7-4b34-b737-9b0543686341": {},
				"5a816e56-7e0d-4c38-8201-27db5f5238a5": {},
				"c30d7f53-4284-4310-8736-825bd8e0d531": {},
				"4be06e4a-d5e8-408b-b289-f425434e110b": {},
				"796ab1ce-a8cc-4a94-838a-f3ac46c82b35": {},
				"7f8cfec9-7488-43ef-b52b-9fa3aaec1c05": {},
				"9fbc5c10-bcf1-4f31-a0bb-b5bcd55291b4": {},
				"62588939-a80d-4e29-b522-0737b5a5040e": {},
				"62087cfa-fe81-4a02-8e95-cdbeb366dbe5": {},
				"75833ffc-8cef-4f64-b6b9-34670d09fd88": {},
				"aa5ab274-07de-43d5-a3be-561e9b9e3a59": {},
				"d751a7e7-3ebb-438a-93bb-429b3d7007c6": {},
				"17c149bb-08f6-4419-838c-73bd018b3bba": {},
				"b6a536cf-9cfa-4d9c-90b7-8025bce0375b": {},
				"e5c4e452-fa78-4ca4-9365-0fd9c6f719a7": {},
				"eeeb07ab-e0da-4338-a1f5-9e836a207df5": {},
				"e0335ee9-1ce4-4fc9-b296-30b4aadf891f": {},
				"fcd5f9d7-1ca4-4e21-9635-29960d83ad20": {},
				"9113f55b-8e0b-4287-ba17-b5c5ccbc6c82": {},
				"971b9d27-c889-4a09-a8a7-3d4101b30f8b": {},
				"c556b1f2-6260-4169-a78b-9e1595c65d15": {}
			}
		},
		"f5b330af-33e4-4f9c-8983-9128dfd54a3e": {
			"classDefinition": "com.sap.bpm.wfs.SampleContext",
			"reference": "/sample-data/simulation_approval/entrySample.json",
			"id": "default-start-context"
		},
		"7fef017f-80d7-40a5-abbb-57da3516df8c": {
			"classDefinition": "com.sap.bpm.wfs.TerminateEventDefinition",
			"id": "terminateeventdefinition2"
		},
		"d2af257b-7cb6-4ac9-92d6-c3243a4b2477": {
			"classDefinition": "com.sap.bpm.wfs.TerminateEventDefinition",
			"id": "terminateeventdefinition3"
		},
		"ddefc4b0-dc0f-4919-9826-ac4af85c478b": {
			"classDefinition": "com.sap.bpm.wfs.TerminateEventDefinition",
			"id": "terminateeventdefinition4"
		},
		"df898b52-91e1-4778-baad-2ad9a261d30e": {
			"classDefinition": "com.sap.bpm.wfs.ui.StartEventSymbol",
			"x": -186,
			"y": 161,
			"width": 32,
			"height": 32,
			"object": "11a9b5ee-17c0-4159-9bbf-454dcfdcd5c3"
		},
		"53e54950-7757-4161-82c9-afa7e86cff2c": {
			"classDefinition": "com.sap.bpm.wfs.ui.EndEventSymbol",
			"x": 1704,
			"y": 122,
			"width": 35,
			"height": 35,
			"object": "2798f4e7-bc42-4fad-a248-159095a2f40a"
		},
		"6bb141da-d485-4317-93b8-e17711df4c32": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "-170,180.5 -44,180.5",
			"sourceSymbol": "df898b52-91e1-4778-baad-2ad9a261d30e",
			"targetSymbol": "15650dc5-2ac0-4b76-9993-77af77353136",
			"object": "c6b99f32-5fe6-4ab6-b60a-80fba1b9ae0f"
		},
		"43633823-6f17-49c6-8110-4c4559417d6a": {
			"classDefinition": "com.sap.bpm.wfs.ui.ScriptTaskSymbol",
			"x": 263,
			"y": 94,
			"width": 100,
			"height": 60,
			"object": "55c648ed-fdd8-490d-b162-e645045f8738"
		},
		"7880622e-bf13-4e4e-83a5-68da0b814453": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "313,124 445,124",
			"sourceSymbol": "43633823-6f17-49c6-8110-4c4559417d6a",
			"targetSymbol": "3d7639cc-3426-4a8d-bfad-c8656ea587bf",
			"object": "f38a04a2-2624-426a-894c-5156c5dc7cf6"
		},
		"3d7639cc-3426-4a8d-bfad-c8656ea587bf": {
			"classDefinition": "com.sap.bpm.wfs.ui.UserTaskSymbol",
			"x": 395,
			"y": 94,
			"width": 100,
			"height": 60,
			"object": "7042cf7c-7fc9-4ad3-a2ed-5a28c9858a66"
		},
		"337dbba4-c936-44ba-b39b-8263f07ea4ae": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "445,124 569,124",
			"sourceSymbol": "3d7639cc-3426-4a8d-bfad-c8656ea587bf",
			"targetSymbol": "93fac5fc-a140-43cc-a7bc-2071ae27bf7f",
			"object": "55cc0d99-b7d0-47b4-963a-0595d434304e"
		},
		"93fac5fc-a140-43cc-a7bc-2071ae27bf7f": {
			"classDefinition": "com.sap.bpm.wfs.ui.ScriptTaskSymbol",
			"x": 519,
			"y": 94,
			"width": 100,
			"height": 60,
			"object": "6bda5a59-c008-439c-92dc-1aeb2d0e16e4"
		},
		"a726859c-fed2-437b-894f-faf5e2a3db26": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "569,122.75 714.25,122.75",
			"sourceSymbol": "93fac5fc-a140-43cc-a7bc-2071ae27bf7f",
			"targetSymbol": "512087a0-a046-4ef1-935e-5f2b41debf3a",
			"object": "d2ee874b-4b71-42a8-9a59-54727727f46d"
		},
		"aa507045-81df-4358-9d85-d68085caee61": {
			"classDefinition": "com.sap.bpm.wfs.ui.ExclusiveGatewaySymbol",
			"x": 184,
			"y": 102,
			"object": "df4a588d-9932-4aad-aed1-8d537db502f7"
		},
		"a626b797-c750-48aa-ab2f-71615ec515fe": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "205,123.5 313,123.5",
			"sourceSymbol": "aa507045-81df-4358-9d85-d68085caee61",
			"targetSymbol": "43633823-6f17-49c6-8110-4c4559417d6a",
			"object": "e94ceca4-d809-478f-818e-cdeef4d7a046"
		},
		"4c52042d-56fc-4fc2-adb9-fdc1acc40897": {
			"classDefinition": "com.sap.bpm.wfs.ui.ExclusiveGatewaySymbol",
			"x": 829,
			"y": 98,
			"object": "9722cf21-cb25-4388-bf38-f8a9a1f14c74"
		},
		"7d3739b5-ae14-4706-86c6-727d02e8bf69": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "850,120 909,120 909,186",
			"sourceSymbol": "4c52042d-56fc-4fc2-adb9-fdc1acc40897",
			"targetSymbol": "eef597a9-ebe1-4add-b941-1dc43a7f0ad5",
			"object": "ff4ba49b-ba53-4729-8f1d-62d18b69ded9"
		},
		"08636d7d-cc07-4abe-b418-9e20bb2ffbdd": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "850,98.5 850,-116 205,-116 205,102.5",
			"sourceSymbol": "4c52042d-56fc-4fc2-adb9-fdc1acc40897",
			"targetSymbol": "aa507045-81df-4358-9d85-d68085caee61",
			"object": "36de9c4f-c905-48e5-b0c8-24c2f3234f5e"
		},
		"d21c5e3e-e069-41df-a586-651b942104b4": {
			"classDefinition": "com.sap.bpm.wfs.ui.ParallelGatewaySymbol",
			"x": 90,
			"y": 161,
			"object": "f409d90a-2b95-4720-9401-a52ea4baa7b7"
		},
		"e7840887-9fc0-4333-85b1-b5f1dc83e089": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "111,182 205,182 205,143.5",
			"sourceSymbol": "d21c5e3e-e069-41df-a586-651b942104b4",
			"targetSymbol": "aa507045-81df-4358-9d85-d68085caee61",
			"object": "023d2efa-4f72-4d7b-b169-9488b61d7db1"
		},
		"a69e8dde-cf9b-4997-b281-ddc31e5b2787": {
			"classDefinition": "com.sap.bpm.wfs.ui.ExclusiveGatewaySymbol",
			"x": 179,
			"y": 238,
			"object": "83b8345d-5071-4126-a309-afc0757fb0d2"
		},
		"f2fb34eb-ad88-476b-942a-bccd692d44db": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "111,182 200,182 200,238.5",
			"sourceSymbol": "d21c5e3e-e069-41df-a586-651b942104b4",
			"targetSymbol": "a69e8dde-cf9b-4997-b281-ddc31e5b2787",
			"object": "4d01666c-9cae-4674-bf20-8ee615bd0a86"
		},
		"cbf8165c-81f3-43af-803b-36b3cc4e632b": {
			"classDefinition": "com.sap.bpm.wfs.ui.ScriptTaskSymbol",
			"x": 263,
			"y": 228,
			"width": 100,
			"height": 60,
			"object": "29c5d740-25ce-4afb-b595-185d6c4c63e3"
		},
		"809a664b-1e06-48a2-92a9-2f219568b972": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "220.5,258.5 313,258.5",
			"sourceSymbol": "a69e8dde-cf9b-4997-b281-ddc31e5b2787",
			"targetSymbol": "cbf8165c-81f3-43af-803b-36b3cc4e632b",
			"object": "94093928-3ee3-4b2d-9ecd-9938f6d3960f"
		},
		"a9e1dfdd-696c-4d3f-bbcc-4fe88aa402db": {
			"classDefinition": "com.sap.bpm.wfs.ui.UserTaskSymbol",
			"x": 395,
			"y": 228,
			"width": 100,
			"height": 60,
			"object": "ee98e134-e894-45e7-ae17-3217e777c3b1"
		},
		"fa68c92e-780c-4d0a-a121-42df3ac0fd67": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "313,258 395.5,258",
			"sourceSymbol": "cbf8165c-81f3-43af-803b-36b3cc4e632b",
			"targetSymbol": "a9e1dfdd-696c-4d3f-bbcc-4fe88aa402db",
			"object": "6e32b055-1863-49f7-8743-6a76ca05be7a"
		},
		"b661c108-1dda-4274-8675-6fd5c5925eba": {
			"classDefinition": "com.sap.bpm.wfs.ui.ScriptTaskSymbol",
			"x": 519,
			"y": 228,
			"width": 100,
			"height": 60,
			"object": "47ce29ce-9bfb-4ea5-85b2-b284ca504c83"
		},
		"e07c358b-c74d-4cba-9018-221e2b6af185": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "445,258 519.5,258",
			"sourceSymbol": "a9e1dfdd-696c-4d3f-bbcc-4fe88aa402db",
			"targetSymbol": "b661c108-1dda-4274-8675-6fd5c5925eba",
			"object": "b6d52e3d-730d-4168-8b14-3d5b87182253"
		},
		"fbc69f69-74c1-495d-b437-4fe8c2f566f8": {
			"classDefinition": "com.sap.bpm.wfs.ui.ExclusiveGatewaySymbol",
			"x": 829,
			"y": 238,
			"object": "f055559e-b46f-4caa-b8c2-f9f09837b771"
		},
		"b5d7f437-c107-449b-88b9-f9f351afc6db": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "569,258.25 724.25,258.25",
			"sourceSymbol": "b661c108-1dda-4274-8675-6fd5c5925eba",
			"targetSymbol": "2cbe801c-9e58-4bfa-a874-8cef7b5d4325",
			"object": "c1cf54a3-68b3-412d-a1a1-1aad4f593b7a"
		},
		"eef597a9-ebe1-4add-b941-1dc43a7f0ad5": {
			"classDefinition": "com.sap.bpm.wfs.ui.ExclusiveGatewaySymbol",
			"x": 888,
			"y": 178,
			"object": "2196e5b6-cf15-4967-9433-1fd8caa6ed4e"
		},
		"7ce430d2-aa6c-4feb-b001-c6110446a42f": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "870.5,259 909,259 909,219.5",
			"sourceSymbol": "fbc69f69-74c1-495d-b437-4fe8c2f566f8",
			"targetSymbol": "eef597a9-ebe1-4add-b941-1dc43a7f0ad5",
			"object": "3cf628e4-4998-40f6-ba4d-44fe7a365635"
		},
		"b30e5e4b-893f-44f7-bae9-08ed8524d12c": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "850,279.5 850,592 200,592 200,279.5",
			"sourceSymbol": "fbc69f69-74c1-495d-b437-4fe8c2f566f8",
			"targetSymbol": "a69e8dde-cf9b-4997-b281-ddc31e5b2787",
			"object": "7d3885f4-7dac-4154-9426-fc5cc40fbee4"
		},
		"b6ab27c4-5a73-463a-bb67-93b16013db69": {
			"classDefinition": "com.sap.bpm.wfs.ui.UserTaskSymbol",
			"x": 1274,
			"y": 169,
			"width": 100,
			"height": 60,
			"object": "b35d8f7d-0dfc-4408-a8a7-59f29951cb24"
		},
		"87698911-fc01-4c47-9120-a154be3ffec7": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "1324,199 1441.25,199",
			"sourceSymbol": "b6ab27c4-5a73-463a-bb67-93b16013db69",
			"targetSymbol": "c30d7f53-4284-4310-8736-825bd8e0d531",
			"object": "9177a247-92f0-4160-ab37-973cfbebca8b"
		},
		"3aec1617-5ad1-48ad-8917-0bd3ddeedc64": {
			"classDefinition": "com.sap.bpm.wfs.ui.ServiceTaskSymbol",
			"x": 1535,
			"y": 102,
			"width": 100,
			"height": 60,
			"object": "86964ce9-e9ee-4935-88f7-e5d3dc91dae6"
		},
		"a302048d-373a-461c-944b-bacf913ef8a5": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "1635,137 1705.5,137",
			"sourceSymbol": "3aec1617-5ad1-48ad-8917-0bd3ddeedc64",
			"targetSymbol": "53e54950-7757-4161-82c9-afa7e86cff2c",
			"object": "8d8601ae-df38-4010-b13c-55d6b179366d"
		},
		"15650dc5-2ac0-4b76-9993-77af77353136": {
			"classDefinition": "com.sap.bpm.wfs.ui.ScriptTaskSymbol",
			"x": -94,
			"y": 154,
			"width": 100,
			"height": 60,
			"object": "f08f8cd5-251a-4cd4-a6c9-2c2747346f9a"
		},
		"2bc60639-9316-4bda-a010-ce1b6bcecd32": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "-44,183.5 48.25,183.5",
			"sourceSymbol": "15650dc5-2ac0-4b76-9993-77af77353136",
			"targetSymbol": "7f8cfec9-7488-43ef-b52b-9fa3aaec1c05",
			"object": "4527d22c-ddf2-4526-8832-3b3847f7d30e"
		},
		"512087a0-a046-4ef1-935e-5f2b41debf3a": {
			"classDefinition": "com.sap.bpm.wfs.ui.ExclusiveGatewaySymbol",
			"x": 693.25,
			"y": 100.5,
			"object": "c3be53fe-c606-4dd7-a3a3-3b154d80f6e0"
		},
		"5e3eae32-dc1d-4b01-95eb-63b02fccdadf": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "714.25,120.25 850,120.25",
			"sourceSymbol": "512087a0-a046-4ef1-935e-5f2b41debf3a",
			"targetSymbol": "4c52042d-56fc-4fc2-adb9-fdc1acc40897",
			"object": "dcc638dd-e318-4ebc-9b6e-5df931c3755c"
		},
		"db0300b1-8fb6-4caa-b2e2-d9661cfde18d": {
			"classDefinition": "com.sap.bpm.wfs.ui.EndEventSymbol",
			"x": 693.25,
			"y": -80,
			"width": 35,
			"height": 35,
			"object": "0feb4abe-061a-47f1-87a8-de59416f0183"
		},
		"62a073fe-33c2-42c6-a17f-52d18ab09c91": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "712.65625,-3 712.65625,-45.5",
			"sourceSymbol": "b6a536cf-9cfa-4d9c-90b7-8025bce0375b",
			"targetSymbol": "db0300b1-8fb6-4caa-b2e2-d9661cfde18d",
			"object": "4a77b961-eb9b-48fe-8f04-081be7ce400a"
		},
		"2cbe801c-9e58-4bfa-a874-8cef7b5d4325": {
			"classDefinition": "com.sap.bpm.wfs.ui.ExclusiveGatewaySymbol",
			"x": 703.25,
			"y": 237.5,
			"object": "fc70f644-29af-41b4-ab93-fa2b92c7a391"
		},
		"6c39a6e9-2de7-4b34-b737-9b0543686341": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "724.25,258.75 850,258.75",
			"sourceSymbol": "2cbe801c-9e58-4bfa-a874-8cef7b5d4325",
			"targetSymbol": "fbc69f69-74c1-495d-b437-4fe8c2f566f8",
			"object": "83794608-8a4f-4fcd-9c5f-12a0272b9663"
		},
		"5a816e56-7e0d-4c38-8201-27db5f5238a5": {
			"classDefinition": "com.sap.bpm.wfs.ui.EndEventSymbol",
			"x": 706.25,
			"y": 493,
			"width": 35,
			"height": 35,
			"object": "747a49cb-681c-419a-a60d-71e0e655996f"
		},
		"c30d7f53-4284-4310-8736-825bd8e0d531": {
			"classDefinition": "com.sap.bpm.wfs.ui.ExclusiveGatewaySymbol",
			"x": 1420.25,
			"y": 178,
			"object": "f71cfd9f-6a0f-4446-b3ad-5b3f428c688c"
		},
		"4be06e4a-d5e8-408b-b289-f425434e110b": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "1441.25,178.5 1441.25,132 1585,132",
			"sourceSymbol": "c30d7f53-4284-4310-8736-825bd8e0d531",
			"targetSymbol": "3aec1617-5ad1-48ad-8917-0bd3ddeedc64",
			"object": "4ce7918e-2632-4c54-b0e9-aa4f60a04bfe"
		},
		"796ab1ce-a8cc-4a94-838a-f3ac46c82b35": {
			"classDefinition": "com.sap.bpm.wfs.ui.EndEventSymbol",
			"x": 1704.25,
			"y": 305.5,
			"width": 35,
			"height": 35,
			"object": "7d0514a6-29f4-4883-bb2c-0c7381e4b142"
		},
		"7f8cfec9-7488-43ef-b52b-9fa3aaec1c05": {
			"classDefinition": "com.sap.bpm.wfs.ui.ExclusiveGatewaySymbol",
			"x": 27.25,
			"y": 162,
			"object": "0b45570e-6733-463f-9565-e8002273ce12"
		},
		"9fbc5c10-bcf1-4f31-a0bb-b5bcd55291b4": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "48.25,182.5 111,182.5",
			"sourceSymbol": "7f8cfec9-7488-43ef-b52b-9fa3aaec1c05",
			"targetSymbol": "d21c5e3e-e069-41df-a586-651b942104b4",
			"object": "90926328-e6be-42f7-b297-34e8b377a75e"
		},
		"62d7f4ed-4063-4c44-af8b-39050bd44926": {
			"classDefinition": "com.sap.bpm.wfs.LastIDs",
			"terminateeventdefinition": 4,
			"timereventdefinition": 1,
			"sequenceflow": 72,
			"startevent": 1,
			"intermediatetimerevent": 1,
			"endevent": 7,
			"usertask": 6,
			"servicetask": 6,
			"scripttask": 9,
			"mailtask": 1,
			"exclusivegateway": 17,
			"parallelgateway": 5
		},
		"245ec0a5-514d-4580-8ca0-cf8e30e942e9": {
			"classDefinition": "com.sap.bpm.wfs.ParallelGateway",
			"id": "parallelgateway5",
			"name": "ParallelGateway5"
		},
		"62588939-a80d-4e29-b522-0737b5a5040e": {
			"classDefinition": "com.sap.bpm.wfs.ui.ParallelGatewaySymbol",
			"x": 976.5,
			"y": 178,
			"object": "245ec0a5-514d-4580-8ca0-cf8e30e942e9"
		},
		"5de32aa3-f2ef-4ae4-883a-cdd490c54b4d": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow64",
			"name": "SequenceFlow64",
			"sourceRef": "2196e5b6-cf15-4967-9433-1fd8caa6ed4e",
			"targetRef": "245ec0a5-514d-4580-8ca0-cf8e30e942e9"
		},
		"62087cfa-fe81-4a02-8e95-cdbeb366dbe5": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "909,199 997.5,199",
			"sourceSymbol": "eef597a9-ebe1-4add-b941-1dc43a7f0ad5",
			"targetSymbol": "62588939-a80d-4e29-b522-0737b5a5040e",
			"object": "5de32aa3-f2ef-4ae4-883a-cdd490c54b4d"
		},
		"9e060501-2592-4a35-aaa5-e08c888d462d": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow65",
			"name": "SequenceFlow65",
			"sourceRef": "245ec0a5-514d-4580-8ca0-cf8e30e942e9",
			"targetRef": "f4322e19-9777-47fd-ba76-89e75eea7ffd"
		},
		"75833ffc-8cef-4f64-b6b9-34670d09fd88": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "997.5,199 1146,199",
			"sourceSymbol": "62588939-a80d-4e29-b522-0737b5a5040e",
			"targetSymbol": "aa5ab274-07de-43d5-a3be-561e9b9e3a59",
			"object": "9e060501-2592-4a35-aaa5-e08c888d462d"
		},
		"f4322e19-9777-47fd-ba76-89e75eea7ffd": {
			"classDefinition": "com.sap.bpm.wfs.ExclusiveGateway",
			"id": "exclusivegateway17",
			"name": "ExclusiveGateway17",
			"default": "d6926956-f048-4d74-a8b7-92abdf304823"
		},
		"aa5ab274-07de-43d5-a3be-561e9b9e3a59": {
			"classDefinition": "com.sap.bpm.wfs.ui.ExclusiveGatewaySymbol",
			"x": 1125,
			"y": 178,
			"object": "f4322e19-9777-47fd-ba76-89e75eea7ffd"
		},
		"620f7c74-1da7-495a-9622-85bc2a3bab4b": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"condition": "${context.nextManagersLength == 0 && context.currentManagersLength == 0}",
			"id": "sequenceflow66",
			"name": "SequenceFlow66",
			"sourceRef": "f4322e19-9777-47fd-ba76-89e75eea7ffd",
			"targetRef": "b35d8f7d-0dfc-4408-a8a7-59f29951cb24"
		},
		"d751a7e7-3ebb-438a-93bb-429b3d7007c6": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "1146,199 1324,199",
			"sourceSymbol": "aa5ab274-07de-43d5-a3be-561e9b9e3a59",
			"targetSymbol": "b6ab27c4-5a73-463a-bb67-93b16013db69",
			"object": "620f7c74-1da7-495a-9622-85bc2a3bab4b"
		},
		"d6926956-f048-4d74-a8b7-92abdf304823": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow67",
			"name": "SequenceFlow67",
			"sourceRef": "f4322e19-9777-47fd-ba76-89e75eea7ffd",
			"targetRef": "245ec0a5-514d-4580-8ca0-cf8e30e942e9"
		},
		"17c149bb-08f6-4419-838c-73bd018b3bba": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "1146,178.5 1146,128 997.5,128 997.5,178.5",
			"sourceSymbol": "aa5ab274-07de-43d5-a3be-561e9b9e3a59",
			"targetSymbol": "62588939-a80d-4e29-b522-0737b5a5040e",
			"object": "d6926956-f048-4d74-a8b7-92abdf304823"
		},
		"0b035e81-5023-4bb6-afcb-8b741fef8087": {
			"classDefinition": "com.sap.bpm.wfs.ServiceTask",
			"destination": "Reshuffle_Simulation_decline",
			"path": "",
			"httpMethod": "POST",
			"requestVariable": "${context.caseID}",
			"id": "servicetask4",
			"name": "denial(current manager)",
			"principalPropagationRef": "7042cf7c-7fc9-4ad3-a2ed-5a28c9858a66"
		},
		"b6a536cf-9cfa-4d9c-90b7-8025bce0375b": {
			"classDefinition": "com.sap.bpm.wfs.ui.ServiceTaskSymbol",
			"x": 663.25,
			"y": -4.5,
			"width": 100,
			"height": 60,
			"object": "0b035e81-5023-4bb6-afcb-8b741fef8087"
		},
		"91bc939d-8f35-44b7-b021-bc13a3acf456": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"condition": "${usertasks.usertask1.last.decision == \"decline\"}",
			"id": "sequenceflow68",
			"name": "SequenceFlow68",
			"sourceRef": "c3be53fe-c606-4dd7-a3a3-3b154d80f6e0",
			"targetRef": "0b035e81-5023-4bb6-afcb-8b741fef8087"
		},
		"e5c4e452-fa78-4ca4-9365-0fd9c6f719a7": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "711.875,101 711.875,55",
			"sourceSymbol": "512087a0-a046-4ef1-935e-5f2b41debf3a",
			"targetSymbol": "b6a536cf-9cfa-4d9c-90b7-8025bce0375b",
			"object": "91bc939d-8f35-44b7-b021-bc13a3acf456"
		},
		"f1d4306c-d559-42da-939f-c20e4a7202d5": {
			"classDefinition": "com.sap.bpm.wfs.ServiceTask",
			"destination": "Reshuffle_Simulation_decline",
			"path": "",
			"httpMethod": "POST",
			"requestVariable": "${context.caseID}",
			"id": "servicetask5",
			"name": "denial(next manager)",
			"principalPropagationRef": "ee98e134-e894-45e7-ae17-3217e777c3b1"
		},
		"eeeb07ab-e0da-4338-a1f5-9e836a207df5": {
			"classDefinition": "com.sap.bpm.wfs.ui.ServiceTaskSymbol",
			"x": 671.25,
			"y": 387.5,
			"width": 100,
			"height": 60,
			"object": "f1d4306c-d559-42da-939f-c20e4a7202d5"
		},
		"507eae6c-b11d-44f7-82df-60449466e60e": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"condition": "${usertasks.usertask2.last.decision == \"decline\"}",
			"id": "sequenceflow69",
			"name": "SequenceFlow69",
			"sourceRef": "fc70f644-29af-41b4-ab93-fa2b92c7a391",
			"targetRef": "f1d4306c-d559-42da-939f-c20e4a7202d5"
		},
		"e0335ee9-1ce4-4fc9-b296-30b4aadf891f": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "722.75,258.5 722.75,417.5",
			"sourceSymbol": "2cbe801c-9e58-4bfa-a874-8cef7b5d4325",
			"targetSymbol": "eeeb07ab-e0da-4338-a1f5-9e836a207df5",
			"object": "507eae6c-b11d-44f7-82df-60449466e60e"
		},
		"3b357805-3bc2-45b1-9ac7-e50250f8ed8f": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow70",
			"name": "SequenceFlow70",
			"sourceRef": "f1d4306c-d559-42da-939f-c20e4a7202d5",
			"targetRef": "747a49cb-681c-419a-a60d-71e0e655996f"
		},
		"fcd5f9d7-1ca4-4e21-9635-29960d83ad20": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "722.5,417.5 722.5,493.5",
			"sourceSymbol": "eeeb07ab-e0da-4338-a1f5-9e836a207df5",
			"targetSymbol": "5a816e56-7e0d-4c38-8201-27db5f5238a5",
			"object": "3b357805-3bc2-45b1-9ac7-e50250f8ed8f"
		},
		"75c527ec-b455-406d-b922-9fdd24c0f619": {
			"classDefinition": "com.sap.bpm.wfs.ServiceTask",
			"destination": "Reshuffle_Simulation_decline",
			"path": "",
			"httpMethod": "POST",
			"requestVariable": "${context.caseID}",
			"id": "servicetask6",
			"name": "denial_HR",
			"principalPropagationRef": "b35d8f7d-0dfc-4408-a8a7-59f29951cb24"
		},
		"9113f55b-8e0b-4287-ba17-b5c5ccbc6c82": {
			"classDefinition": "com.sap.bpm.wfs.ui.ServiceTaskSymbol",
			"x": 1535.25,
			"y": 291,
			"width": 100,
			"height": 64,
			"object": "75c527ec-b455-406d-b922-9fdd24c0f619"
		},
		"c86e0281-ea22-486b-8bb5-9fc81c06dc8b": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"condition": "${usertasks.usertask4.last.decision == \"deny\"}",
			"id": "sequenceflow71",
			"name": "SequenceFlow71",
			"sourceRef": "f71cfd9f-6a0f-4446-b3ad-5b3f428c688c",
			"targetRef": "75c527ec-b455-406d-b922-9fdd24c0f619"
		},
		"971b9d27-c889-4a09-a8a7-3d4101b30f8b": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "1441.25,199 1441.25,323 1535.75,323",
			"sourceSymbol": "c30d7f53-4284-4310-8736-825bd8e0d531",
			"targetSymbol": "9113f55b-8e0b-4287-ba17-b5c5ccbc6c82",
			"object": "c86e0281-ea22-486b-8bb5-9fc81c06dc8b"
		},
		"33d614b1-4b4e-4da8-b1df-59f399be0f2b": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow72",
			"name": "SequenceFlow72",
			"sourceRef": "75c527ec-b455-406d-b922-9fdd24c0f619",
			"targetRef": "7d0514a6-29f4-4883-bb2c-0c7381e4b142"
		},
		"c556b1f2-6260-4169-a78b-9e1595c65d15": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "1585.25,323 1704.75,323",
			"sourceSymbol": "9113f55b-8e0b-4287-ba17-b5c5ccbc6c82",
			"targetSymbol": "796ab1ce-a8cc-4a94-838a-f3ac46c82b35",
			"object": "33d614b1-4b4e-4da8-b1df-59f399be0f2b"
		}
	}
}