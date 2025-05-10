#!/bin/bash

# Javaアプリケーションをバックグラウンドで起動
java -jar /app/app.jar &

# Docker Hub push
# echo "$" | docker login -u "$" --password-stdin
# docker build -t vd3baeky/RenderOutput2:latest .
# docker push vd3baeky/RenderOutput2:latest

# テストを実行
mvn test

# (任意) アプリケーション終了まで待機
wait
