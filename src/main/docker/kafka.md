## Kafka

### 非 Docker 環境啟動 Kafka

通常集群中一台機器對應一個 Broker 實例，此例在本機部署三個 broker 以測試偽分布式集群

```bash
# https://kafka.apache.org/downloads
# 下載後解壓縮卡夫卡
tar -xzvf kafka_2.12-3.3.1.tgz
cd kafka_2.12-3.3.1
ls
# bin 目錄存放 Kafka Shell 腳本
# config 目錄存放 Kafka 配置文件
# libs 目錄存放 Kafka 依賴包(jar)

# Kafka 需要啟動 Zookeeper
# https://gitbook.cn/books/5ae1e77197c22f130e67ec4e/index.html
# 建立 etc 資料夾存放 zookeeper 配置文件
mkdir etc
cp config/zookeeper.properties etc
cd etc
vi zookeeper.properties
clientPort=2181 代表 zookeeper 的端口

# Kafka 搭建三個 Brokers 的偽分布式節點
# server.properties 是建立 Broker 的配置文件
# 拷貝三份分別命名為 server-0、1、2
cp config/server.properties etc/server-0.properties
cp config/server.properties etc/server-1.properties
cp config/server.properties etc/server-2.properties

cd etc
vi server-0.properties
# 更改broker.id 以進行區分 broker.id = 0
broker.id = 0
# 去掉注釋 listeners=PLAINTEXT://:9092
# 並更改端口以進行區分
listeners=PLAINTEXT://:9092
# 更改log file name 以進行區分 log.dirs=/temp/kafka-logs
log.dirs=/temp/kafka-logs-0

cd etc
vi server-1.properties
# 更改broker.id 以進行區分 broker.id = 0
broker.id = 1
# 去掉注釋 listeners=PLAINTEXT://:9092
# 並更改端口以進行區分
listeners=PLAINTEXT://:9093
# 更改log file name 以進行區分 log.dirs=/temp/kafka-logs
log.dirs=/temp/kafka-logs-1

cd etc
vi server-2.properties
# 更改broker.id 以進行區分 broker.id = 0
broker.id = 2
# 去掉注釋 listeners=PLAINTEXT://:9092
# 並更改端口以進行區分
listeners=PLAINTEXT://:9094
# 更改log file name 以進行區分 log.dirs=/temp/kafka-logs
log.dirs=/temp/kafka-logs-2

# 建立好 zookeeper 跟 broker 配置文件後就可以啟動集群
cd bin
bin ./zookeeper-server-start.sh ../etc/zookeeper.properties
bin ./kafka-server-start.sh ../etc/server-0.properties
bin ./kafka-server-start.sh ../etc/server-1.properties
bin ./kafka-server-start.sh ../etc/server-2.properties

# 建立 Kafka topics
# 主題名稱 test 有3個分區 副本因子2
cd bin
bin ./kafka-topics.sh --zookeeper localhost:2181 --create --topic test --partitions 3 --replication-factor 2
# 必要參數 --zookeeper
# 常用參數-創建主題 --create
# 常用參數-主題名字 --topic
# 常用參數-查看主題描述 --describe
# 常用參數-分區數量 --partitions
# 常用參數-創建多少副本 --replication-factor

# 查看主題狀態
cd bin
bin ./kafka-topics.sh --zookeeper localhost:2181 --describe --topic test
# 會看到有三個分區
# partion:0 有兩個副本，分別在 server 1 跟 2 進行維護，leader 是 server 1
# partion:1 有兩個副本，分別在 server 2 跟 0 進行維護，leader 是 server 2
# partion:2 有兩個副本，分別在 server 0 跟 1 進行維護，leader 是 server 0
# 所以 server 0 有 partition 1 partition 2
# 所以 server 1 有 partition 0 partition 2
# 所以 server 2 有 partition 0 partition 1

# 可以在主控台使用主控台生產者腳本
# 不需要使用 zookeeper
cd bin
bin ./kafka-console-producer.sh --broker-list localhost:9092,localhost:9093,localhost:9094 --topic test
# 發送訊息消費者可以收到
> message1
> hello

# 可以在主控台使用主控台消費者腳本
# 不需要使用 zookeeper
cd bin
bin ./kafka-console-consumer.sh --bootstrap-server localhost:9092,localhost:9093,localhost:9094 --topic test

## listeners 監聽器
# 指定 broker 啟動時本機的監聽名稱、端口，給服務器端使用
## advertised.listeners
# 是對外發布的訪問ip跟端口，註冊到 zookeeper 中，給客戶端使用
## 默認情況
# advertised.listeners 沒有配置則採用 listeners 配置

## Producer / Consumer 通過公網ip訪問內部cluster的各個broker
## broker
# (broker 與 broker 之間使用 9092 互相通信)
# (nginx 與 broker 通信使用 9093)
# listers internal: http://:9092
# listers external: http://0.0.0.0:9093
## zookeeper
# advertised.listeners  internal: http://kafka-0:9092
# advertised.listeners  external: http://公網ip:9093
## client
# (clinet 如果處於內網可以使用主機名稱:9092來訪問某個broker)
# (client 如果處於外網必須使用公網ip，進去 cluster 內前會經過 nginx)
# advertised.listeners  internal: http://kafka-0:9092
# advertised.listeners  external: http://公網ip:9093
```

### Docker 環境啟動 Kafka (單節點)

```yaml
# 使用 bitnami 發行的 kafka 鏡像
# https://hub.docker.com/r/bitnami/kafka
# https://blog.csdn.net/qq_35939417/article/details/120554902
# 不要使用  docker pull bitnami/kafka:latest
# 通过这种方式只能获取到kafka镜像，
# 由于kafka运行依赖zookeeper，所以你还得拉取一个zookeeper镜像。
# 官方推荐使用docker compose同时管理kafka容器和zookeeper容器，
# 所以单独拉取kafka镜像这一步可以跳过了，直接进入以下流程。

# 單節點 broker 的 docker-compose.yml 下載
# https://github.com/bitnami/containers/blob/main/bitnami/kafka/docker-compose.yml

# 以下對原先 yaml 檔案做修改

version: "2"

services:
  zookeeper:
    container_name: zookeeper
    host_name: zookeeper
    image: docker.io/bitnami/zookeeper:3.8
    ports:
      - "2181:2181"
    volumes:
      - "zookeeper_data:/bitnami"
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
  kafka:
    container_name: kafka_library
    host_name: kafka_library
    image: docker.io/bitnami/kafka:3.3
    ports:
      - "9092:9092"
    volumes:
      - "kafka_data:/bitnami"
    environment:
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      - ALLOW_PLAINTEXT_LISTENER=yes
    depends_on:
      - zookeeper

volumes:
  zookeeper_data:
    driver: local
  kafka_data:
    driver: local
```

```bash
# 修改好 docker-compose.yml 後
# cd 至放置 docker-compose.yml 位置，然後輸入
docker-compose up -d
# up 會啟動 docker-compose.yml 中所有 containers (zookeepers、kafka)
# d 代表背景執行
docker-compose stop
# 關閉所有 containers 但不會移除
dokcer-compose start
# 啟動所有 containers
dokcer-compose down
# 關閉所有 containers 移除所有 containers 以及所有網路設定
# https://blog.csdn.net/liuzehn/article/details/122879756
```

```bash
# 進入 kafka
# https://blog.csdn.net/qq_29116427/article/details/80202392
# https://blog.csdn.net/qq_45558737/article/details/121237504

docker exec -it kafka_library /bin/bash
# kafka 放在這個資料夾
cd opt/bitnami/kafka/bin
# 新版 kafka 不使用 zookeeper 參數改使用 bootstrap-server
# https://blog.csdn.net/qq_29116427/article/details/80202392
# Kafka 从 2.2 版本开始将 kafka-topic.sh 脚本中的 −−zookeeper 参数标注为 “过时”
# 推荐使用 −−bootstrap-server 参数。

# 注意 kafka_library 跟 zookeeper_library 屬於同一個 docker 網路
# docker network ls
# docker inspect docker_default
# 因此 kafka_library 跟 zookeeper_library 無法直接跟 redis mysql 的 bridge 網路通信
# 除非 docker-compose 中使用 networks 參數指定要連到 mysql 網路
# networks:
#    - bridge
# 或者 mysql redis 連接至此 docker_default 網路
# docker network connect docker_default mysql_library
# docker network connect docker_default redis_library

# 因為 kafka_library 跟 zookeeper_library 屬於 docker_default 網路，屬於自定義網路
# 自定義橋接模式，可以以容器名稱進行ping通，使用 容器名+端口號 在同一個網路內互相通信

# --bootstrap-server kafka_library:9092
# 連線到容器 kafka_library 的9092端口
# --replication-factor 1
# 因為只有一個 broker 因此最多只有一個副本因子
kafka-topics.sh --bootstrap-server kafka_library:9092 --create --topic test --partitions 3 --replication-factor 1

kafka-topics.sh --bootstrap-server kafka_library:9092 --describe --topic test

# 啟動主控台使用主控台生產者腳本
kafka-console-producer.sh --broker-list kafka_library:9092 --topic test

# 為了測試主控台使用主控台消費者腳本
# 另外開啟一個主控台
# 輸入
docker exec -it kafka_library /bin/bash
cd opt/bitnami/kafka/bin
kafka-console-consumer.sh --bootstrap-server kafka_library:9092 --topic test

# 在 主控台生產者腳本 所在主控台輸入文字
# 則 主控台消費者腳本 所在主控台會顯示文字

```
