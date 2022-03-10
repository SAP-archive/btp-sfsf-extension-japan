# DB Module

## Candidate Table
```SQL
// 接頭辞の意味：
// - 異動先：作成された異動案における候補者の異動先ポジションに関するデータ
// - 候補者：候補者が現在所属するポジションに関するデータ
// - 現職者：上記“異動先”ポジションに現在割り当てられている人材
column table candidate(					--接頭辞：役割		キー項目		データ例
    	caseID nvarchar(50),				--その他：Case ID	x		2022ido_sales_dep
	positionID nvarchar(10),                        --異動先：ポジションID	x		0123456
	positionName nvarchar(50),                      --異動先：ポジション名			アカウント営業	
	divisionID nvarchar(10),                        --異動先：事業部ID			1234567
	divisionName nvarchar(50),                      --異動先：事業部名			営業本部
	departmentID nvarchar(10),                      --異動先：部門ID				2345678
	departmentName nvarchar(50),                    --異動先：部門名				営業（関西第一）
	jobGradeID nvarchar(5),                         --異動先：等級ID				GR-1
	jobGradeName nvarchar(50),                      --異動先：等級名				総合職研修員
	candidateID nvarchar(50),                       --候補者：異動候補者ID	x		syamada
	candidateName nvarchar(50),                     --候補者：異動候補者名			山田さわお
	candidateDivisionID nvarchar(10),               --候補者：事業部ID			3456789
	candidateDivisionName nvarchar(50),             --候補者：事業部名			営業本部
	candidateDepartmentID nvarchar(10),             --候補者：部門ID				4567890
	candidateDepartmentName nvarchar(50),           --候補者：部門名				営業（関東第二）
	candidatePositionID nvarchar(10),               --候補者：ポジションID			5678901
	candidatePositionName nvarchar(50),             --候補者：ポジション名			ソリューション営業
	candidateJobGradeID nvarchar(5),                --候補者：等級ID				GR-1
	candidateJobGradeName nvarchar(50),             --候補者：等級名				総合職研修員
	candidateJobTenure int,                         --候補者：在籍期間			1
	candidateLastRating1 nvarchar(5),               --候補者：今年度評価			2
	candidateLastRating2 nvarchar(5),               --候補者：昨年度評価			2
	candidateLastRating3 nvarchar(5),               --候補者：一昨年度評価			3
	candidateTransferTimes int,                     --候補者：異動回数			1
	candidateTransferReason nvarchar(50),           --候補者：前回の異動理由			定期異動
	candidateCertification nvarchar(50),            --候補者：保有する資格			DCプランナー
	candidateReshuffleCost BIGINT,                  --候補者：異動コスト			256
	candidateManagerID nvarchar(256),               --候補者：異動前のマネージャーID		pikumin
	incumbentEmpID nvarchar(50),                    --現職者：現職者ID			ryamanaka
	incumbentEmpName nvarchar(50),                  --現職者：現職者名			山中亮一
	incumbentEmpRetirementIntention nvarchar(5),    --現職者：退職希望の有無yes/no）		yes | no
	incumbentEmpManager nvarchar(256),              --現職者：現職者（異動先）のマネージャーID
	simulationCheckStatus nvarchar(5),              --その他：チェック結果ステータス		OK | NG | WARN | null
	simulationCheckDatetime Datetime,               --その他：チェック完了日時				
	wfStatus nvarchar(5),                           --その他：異動案の承認フローステータス	APPD | APPL | DENY | null
	mailSentFlg nvarchar(5),                        --その他：メール送信済みフラグ		Y | null
	mailSentAt Datetime,                            --その他：メール送信日時
	sfUpsertFlg nvarchar(5),                        --その他：SFSF更新フラグ			Y | null
	sfUpsertAt Datetime,                            --その他：SFSF更新日時
	createdAt Datetime,                             --その他：異動案作成日時
	createdBy nvarchar(50),                         --その他：異動案作成者			skuwa
	modifiedAt Datetime,                            --その他：異動案修正日時
	modifiedBy nvarchar(50)                         --その他：異動案修正者			skuwa
)
```

## Config Table
```SQL
column table config (
	startDateTime DateTime,         --発令日
	span int,                       --次回発令日までの期間（月数）
	competencyThreshold int         --コンピテンシー閾値
	rateFormKey1 nvarchar(10),      --今年度の評価項目
	rateFormKey2 nvarchar(10),      --昨年度の評価項目
	rateFormKey3 nvarchar(10)       --一昨年度の評価項目
	presidentName nvarchar(32)      --社長名（メールテンプレート内で使用）
	mailTemplate nvarchar(4112)     --メールテンプレート
)
```
