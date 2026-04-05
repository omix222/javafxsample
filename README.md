# hellofx

JavaFX 製の天気予報アプリ。IPアドレスから現在地を自動取得し、今日の天気と明日の予報を表示します。

## 機能

- 現在地（都市名・国）の自動取得
- 今日の天気：天気状況・現在気温・風速
- 明日の予報：天気状況・最高/最低気温・最大風速
- 更新ボタンで再取得

## 使用API

| API | 用途 | 認証 |
|-----|------|------|
| [ip-api.com](http://ip-api.com) | IPアドレスから緯度経度・都市名を取得 | 不要 |
| [Open-Meteo](https://open-meteo.com) | 緯度経度から天気データを取得 | 不要 |

## 必要環境

- Java 21+
- Maven 3.6+

## 起動方法

```bash
mvn javafx:run
```

## ビルド

```bash
# コンパイルのみ
mvn compile

# クリーン＆起動
mvn clean javafx:run
```

## プロジェクト構成

```
hellofx/
├── pom.xml
└── src/main/
    ├── java/com/example/hellofx/
    │   ├── module-info.java         # Javaモジュール定義
    │   ├── HelloApplication.java    # エントリポイント
    │   ├── HelloController.java     # UIイベント処理
    │   └── WeatherService.java      # 天気API呼び出し
    └── resources/com/example/hellofx/
        └── hello-view.fxml          # UIレイアウト
```

## 使用技術

| 技術 | バージョン |
|------|-----------|
| JavaFX | 21.0.5 |
| Gson | 2.10.1 |
| javafx-maven-plugin | 0.0.8 |
| Java | 21 |
