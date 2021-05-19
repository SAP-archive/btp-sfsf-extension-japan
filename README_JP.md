# BTPを用いたSAP SuccessFactors拡張：玉突き人事

![REUSE status](https://api.reuse.software/badge/github.com/SAP-samples/btp-sfsf-extentsion-japan)

## 概要

本プロジェクトはSAP Business Technology PlatformのCloud Foundry環境上に構築された玉突き人事を管理するためのアプリケーションセットである。このアプリケーションセットはSAP SuccessFactorsから人事情報を取得して玉突き人事計画をデータベース上に保存し、保存したデータに対して異動可能要件をチェックできる。このアプリケーションセットの技術的な構成要素は主にSAPUI5、SAP Cloud SDK（Java）およびSAP HANA Cloudである。

## プロジェクトリスト

| プロジェクト名          | 対応シナリオ                                                 |
| ----------------------- | ------------------------------------------------------------ |
| Reshuffle_Applicants    | 玉突き人事計画を作成するためのUI5およびJavaのアプリケーション |
| Reshuffle_Simulation    | 異動可能要件をチェックするためのUI5およびJavaのアプリケーション |
| Reshuffle_Configuration | **Reshuffle_Applicants**および**Reshuffle_Simulation**を設定するためのUI5およびJavaのアプリケーション. |
| Reshuffle_DB            | **Reshuffle_Applicants**、**Reshuffle_Simulation**および**Reshuffle_Configuration**で使用するデータを保存するためのSAP HANA Cloud用アーティファクトのプロジェクト |

## 機能一覧

### 各アプリケーションにおける主な機能

| 機能                 | 概要                                        | 機能詳細                                                     |
| -------------------- | ------------------------------------------- | ------------------------------------------------------------ |
| 異動案作成           | 異動候補者を一覧表示                        | 抽出条件はフィルタで指定可能                                 |
|                      | 異動候補者のプロファイル表示                | 一覧上の顔写真をクリックするとプロファイル情報をポップアップ表示 |
|                      | 異動候補者データのエクスポート（CSV出力）   | ZIP形式のファイルをエクスポート<br />解凍用パスワードを都度ランダム生成して表示 |
|                      | 異動先の空きポジションリスト表示            | 異動先の空きポジションを一覧表示<br />抽出条件はフィルタで指定可能 |
|                      | ドラッグ&ドロップによる候補者の割当         | 異動候補者一覧上の候補者をドラッグして空きポジションにドロップすることで異動候補者を割り当て |
|                      | 異動案の保存                                | 作成した異動案を保存 <br />異動案には任意の文字列を命名可能  |
|                      | 異動案のエクスポート（CSV出力）             | ZIP形式のファイルをエクスポート<br />解凍用パスワードを都度ランダム生成して表示 |
| 異動案チェック       | チェック処理実行                            | 作成された異動案が異動可能要件を満たすかどうかチェックする処理を実行<br />チェックされる異動可能要件は下記チェックロジック一覧を参照 |
|                      | チェック結果確認                            | エラーおよびワーニングを含む“チェック処理実行”の結果を表示   |
|                      | チェック結果データのエクスポート（CSV出力） | ZIP形式のファイルをエクスポート<br />解凍用パスワードを都度ランダム生成して表示 |
| 異動案確定           | 移動対象者の一覧表示                        | 確定した移動対象者の検索およびその表意<br />抽出条件はフィルタで指定可能 |
|                      | 人事情報確定                                | “アプリケーション設定”の機能を用いて設定された日付に異動発令処理を実施するフラグを立てる |
|                      | 辞令送信（メール）                          | 各異動対象者にメールで辞令を送信<br />事例送信のタイミングは即時もしくは未来の日付を設定可能 |
| アプリケーション設定 | アプリケーション設定                        | “異動案作成”、“異動案チェック”および“異動案確定”のそれぞれの機能内で利用される値を設定 |

### ”異動案チェック”におけるチェックロジック一覧

下記“チェック内容”を満たす場合、“チェック”の値が”判定”の値になる。

| チェック         | チェック内容                                                 | 判定    |
| ---------------- | ------------------------------------------------------------ | ------- |
| 異動回数チェック | 対象者の異動回数が5回以上の場合                              | Error   |
| 異動後在籍期間   | 直近の異動から経過期間が1年以内の場合                        | Error   |
| 滞留年数         | 直近の異動から経過期間が3年以上経過している場合              | Warning |
| 縁故者有無       | 異動先部署に縁故者（配偶者）がいる場合                       | Error   |
| 懲罰履歴         | 懲罰履歴がある場合                                           | Error   |
| ハラスメント有無 | 異動先部署の上司が過去に対象者に対してパワハラ加害を行っていた場合 | Error   |
| 休職中           | 対象者が休職中の場合                                         | Error   |
| 人事評価         | 直近3年間の人事評価結果が連続して良でない場合                | Error   |

## ソリューションダイアグラム

### ダイアグラム

![Diagram](./figs/solutionDiagram.png)

### サービスリスト

| **ダイアグラム内の番号** | **サービス名称**                            |
| ------------------------ | ------------------------------------------- |
| 1                        | Cloud Foundry Runtime                       |
| 2                        | HTML5 Application Repository  Service       |
| 3                        | Destination                                 |
| 4                        | SAP HANA Cloud                              |
| 5                        | Job Scheduling Service                      |
| 6                        | SAP Workflow Service                        |
| 7                        | SAP Business Application Studio             |
| 8                        | Authorization and Trust  Management Service |
| 9                        | Identity Authentication                     |
| 10                       | SAP SuccessFactors Extensibility  Service   |
| 11                       | Forms Service by Adobe                      |
| 12                       | Application Logging  Service                |
| 13                       | Alert Notification Service                  |

## 要件

1. SAP Business Technology Platform (トライアルアカウントもしくはプロダクションアカウント)
2. SAP SuccessFactors 
3. Active subscription or entitlement:
   1. Cloud Foundry Runtime: > 8 GB
   2. SAP HANA Cloud (plan hana)
   3. SAP HANA Schemas & HDI Containers (plan hdi-shared)
   4. SAP SuccessFactors Extensibility (plan api-access) 
   5. [Optional] SAP Business Application Studio

## ダウンロードおよびインストール方法

1. XSUAAのサービスインスタンス**uaa_Reshuffle**を作成
2. Job Schedulingのサービスインスタンス**my-job-scheduler**を作成
3. Reshuffle_ApplicantsのJavaアプリケーションとSAP SuccessFactorsを接続するためのDestinationを設定
   1. Name: SFSF
   2. Type: HTTP
   3. URL: Your SuccessFactors's endpoint
   4. Proxy Type: Internet
   5. Authentication: BasicAuthentication
   6. User: Your SaccuessFactors user
   7. Password: Your SaccuessFactors password
4. 本リポジトリをダウンロードもしくはクローン
5. **Reshuffle_DB**をビルドおよびデプロイ
6. テーブルに1行分の下記データを初期値として挿入
   1. startDateTime: any date
   2. span: any number
   3. rateFormKeyN: **formTemplateId** （ODataサービスの**FormTemplate**の値を参照）
7. **Reshuffle_Configuration**、**Reshuffle_Applicants**および**Reshuffle_Simulation**をそれぞれビルド、デプロイ

## サポート情報

[Create an issue](https://github.com/SAP-samples/btp-sfsf-extentsion-japan/issues) in this repository if you find a bug or have questions about the content.

For additional support, [ask a question in SAP Community](https://answers.sap.com/questions/ask.html).

## License

Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved. This project is licensed under the Apache Software License, version 2.0 except as noted otherwise in the [LICENSE](LICENSES/Apache-2.0.txt) file.
