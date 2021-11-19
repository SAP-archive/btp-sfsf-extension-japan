# DB Module

## Candidate Table
```SQL
column table candidate(
	 caseID nvarchar(50), 					--Case ID
	 candidateID nvarchar(50),              --候補者ID
	 candidateName nvarchar(50),            --候補者名
	 currentDivision nvarchar(10) ,         --候補者現事業部
	 currentDepartment nvarchar(10) ,       --候補者現部門
	 currentPosition nvarchar(10) ,         --候補者現ポジション
	 currentDivisionName nvarchar(50) ,     --候補者現事業部名
	 currentDepartmentName nvarchar(50) ,   --候補者現部門名
	 currentPositionName nvarchar(50) ,     --候補者現ポジション名
	 currentJobGrade nvarchar(5),           --候補者現等級
	 currentJobGradeName nvarchar(50),      --候補者現等級名
	 jobTenure int,                         --候補者在籍期間
	 transferTimes int,                     --候補者異動回数
	 rating1 nvarchar(5),					--評価履歴1
	 rating2 nvarchar(5),					--評価履歴2
	 rating3 nvarchar(5),					--評価履歴3
	 lastTransReason nvarchar(50) ,         --候補者前回異動理由
	 certification nvarchar(50),            --候補者資格
	 nextDivision nvarchar(10) ,            --異動先事業部
	 nextDepartment nvarchar(10) ,          --異動先部門
	 nextPosition nvarchar(10) ,            --異動先ポジション
	 nextDivisionName nvarchar(50) ,        --異動先事業部名
	 nextDepartmentName nvarchar(50) ,      --異動先部門名
	 nextPositionName nvarchar(50) ,        --異動先ポジション名
	 nextJobGrade nvarchar(5),              --異動先等級
	 nextJobGradeName nvarchar(50),         --異動先等級名
	 currentEmpID nvarchar(50),             --異動先現職者ID
	 currentEmpName nvarchar(50),           --異動先現職者名
	 currentEmpRetire nvarchar(5),          --異動先現職者退職希望フラグ
	 currentManager nvarchar(200),			--異動元マネジャー
	 nextManager nvarchar(200),				--移動先マネジャー
	 cost BIGINT,							--異動コスト
	 checkResult nvarchar(500),             --チェック結果詳細
	 checkStatus nvarchar(5),               --チェック結果ステータス
	 checkDateTime DateTime                 --チェックタイムスタンプ
	 mailSentFlg nvarchar(5),				--メール送信フラグ
	 mailSentAt Datetime					--メール送信日時
	 upsertFlg nvarchar(5),					--SFSF更新フラグ
	 upsertAt Datetime,						--SFSF更新日時
	 createdAt Datetime,
	 createdBy nvarchar(40)
)
```

## Config Table
```SQL
column table config (
	 startDateTime DateTime,         --発令日
	 span int,                       --次回発令日までの期間（月数）
	 competencyThreshold int         --コンピテンシー閾値
	 rateFormKey1 nvarchar(10),      --評価項目1
	 rateFormKey2 nvarchar(10),      --評価項目2
 	 rateFormKey3 nvarchar(10)       --評価項目3
 	 presidentName nvarchar(32)      --社長名（メールテンプレート内で使用）
 	 mailTemplate nvarchar(4112)     --メールテンプレート
)
```