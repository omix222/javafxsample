# hellofx

JavaFX サンプルアプリケーション。名前を入力してあいさつを表示するシンプルな GUI アプリです。

## 必要環境

- Java 21+
- Maven 3.6+

## 起動方法

```bash
mvn javafx:run
```

## ビルド

```bash
mvn compile
```

## プロジェクト構成

```
hellofx/
├── pom.xml
└── src/main/
    ├── java/com/example/hellofx/
    │   ├── module-info.java         # Javaモジュール定義
    │   ├── HelloApplication.java    # エントリポイント
    │   └── HelloController.java     # UIイベント処理
    └── resources/com/example/hellofx/
        └── hello-view.fxml          # UIレイアウト
```

## 使用技術

| 技術 | バージョン |
|------|-----------|
| JavaFX | 21.0.5 |
| javafx-maven-plugin | 0.0.8 |
| Java | 21 |
