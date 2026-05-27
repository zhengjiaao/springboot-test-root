# springboot-log-system

- [参考资料](https://juejin.cn/post/7295623585364082739)

日志收集系统：包括日志采集、日志处理、日志存储、日志分析、日志告警、日志可视化等功能

## 常见日志系统

- ELK日志系统：Elastic Stack架构为Elasticsearch + Logstash + Kibana + Beats的组合
- EFK日志系统：Elasticsearch + Fluentd + Kibana
- PLG日志系统：Prometheus + Loki + Grafana
- Loki日志系统：Loki + Grafana
- Fluentd日志系统：Fluentd + Elasticsearch + Kibana
- Splunk日志系统：Splunk + Elasticsearch + Kibana
- Logstash日志系统：Logstash + Elasticsearch + Kibana

## 日志系统对比

> 解译：Elasticsearch（ES）、Kubernetes（k8s）

| 日志系统                       | 采集         | 处理       | 存储 | 搜索 | 分析         | 告警         | 可视化        | 难度 | 备注 |
|----------------------------|------------|----------|----|----|------------|------------|------------|----|----|
| ELK（经典Elastic Stack架构）     | Beats      | Logstash | ES | ES |            |            | Kibana     |    |    |
| EFK（容器化Prometheus + k8s架构） | Prometheus |          |    |    |            |            | Kibana     |    |    |
| PLG（容器化Prometheus + k8s架构） | Prometheus | Loki     |    |    | Prometheus | Prometheus | Prometheus |    |    |
|                            |            |          |    |    |            |            |            |    |    |

## 链路追踪系统

- Zipkin
- Jaeger
- SkyWalking
- Datadog
- New Relic
- AppDynamics
- Splunk

