#!/usr/bin/env bash
set -euo pipefail

# 0) 스크립트 위치로 이동 (언제나 프로젝트 루트에서 실행되도록)
cd "$(dirname "$0")"

### 환경 설정 ###
JAR_TASK="bootJar"
IMAGE_NAME="jtocean-app"
TAR_NAME="jtocean-app.tar"
PEM_PATH="/c/Users/Min/Desktop/jtocean-seoul.pem"
REMOTE_USER="ubuntu"
REMOTE_HOST="3.35.20.58"
REMOTE_PATH="/home/ubuntu"

### 1. Gradle Build ###
echo "🛠️ 1. Gradle Clean & Build: $JAR_TASK"
./gradlew clean "$JAR_TASK" --no-daemon || { echo "❌ Gradle build failed"; exit 1; }

# -- 디버그: build/libs 안에 무엇이 있는지 찍어 봅니다.
echo "🔍 build/libs contents:"
ls -lh build/libs

### 2. Docker Build ###
echo "🐳 2. Docker Build: $IMAGE_NAME"
docker build -t "$IMAGE_NAME" . || { echo "❌ Docker build failed"; exit 1; }

### 3. Docker Save ###
echo "📦 3. Docker Save to TAR: $TAR_NAME"
docker save "$IMAGE_NAME" > "$TAR_NAME" || { echo "❌ Docker save failed"; exit 1; }

### 4. SCP to Server ###
echo "🚀 4. SCP Send to Server: $REMOTE_HOST"
scp -i "$PEM_PATH" -c aes128-ctr -o Compression=no "$TAR_NAME" "$REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH" || { echo "❌ SCP of TAR failed"; exit 1; }
echo "🚀 4-1. SCP Send docker-compose.yml to Server"
scp -i "$PEM_PATH" "docker-compose.yml" "$REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH" || { echo "❌ SCP of docker-compose.yml failed"; exit 1; }
echo "🚀 4-2. SCP Send nginx.conf to Server"
scp -i "$PEM_PATH" "nginx.conf" "$REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH" || { echo "❌ SCP of nginx.conf failed"; exit 1; }

echo "✅ 모든 작업 완료!"
