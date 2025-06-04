# How To Run the server

## 1. Create jar
### ./gredlew clean bootJar
## 2. Docker build
### docker build -t jtocean-app .
## 3. Docker save 
### docker save jtocean-app > jtocean-app.tar
## 4. Send to the server.
### scp -i "/c/Users/Min/Desktop/jtocean.pem" docker-compose.yml ubuntu@54.206.80.3:/home/ubuntu/
### scp -i "/c/Users/Min/Desktop/jtocean.pem" jtocean-app.tar ubuntu@54.206.80.3:/home/ubuntu/
### * Modify the PEM KEY path.
## 5. Run on the server.
### ./deploy-jtocean.sh

# 어플리케이션 구동 방법

## 1. jar 파일 생성
### ./gredlew clean bootJar
## 2. Docker 빌드
### docker build -t jtocean-app .
## 3. Docker 저장
### docker save jtocean-app > jtocean-app.tar
## 4. 서버에 전송(scp 사용)
### scp -i "/c/Users/Min/Desktop/jtocean.pem" docker-compose.yml ubuntu@54.206.80.3:/home/ubuntu/
### scp -i "/c/Users/Min/Desktop/jtocean.pem" jtocean-app.tar ubuntu@54.206.80.3:/home/ubuntu/
### * pem key 위치는 자신의 경로에 맞춰 수정
## 5. 서버에서 구동하는 방법 (AWS EC2 서버 내부)
### ./deploy-jtocean.sh

### deploy.sh 을 구동하면 위의 명령어를 일괄 수행함. (deploy.sh 안의 pem key 위치는 수정필요)
