#!/bin/bash

# Javaアプリケーションをバックグラウンドで起動
java -jar /app/app.jar &

# テストを実行
mvn test

# (任意) アプリケーション終了まで待機
wait
