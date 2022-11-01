--获取KEY
local key1 = KEYS[1]

--获取参数
local avg1 = ARGV[1]
local avg2 = ARGV[2]

--打印日志到reids
--注意，这里的打印日志级别，需要和redis.conf配置文件中的日志设置级别一致才行
redis.log(redis.LOG_WARNING,"key1=" ..key1)
redis.log(redis.LOG_WARNING,"avg=" ..avg1, avg2)

--将参数String转为数字类型
--限流时间窗
local expire = tonumber(ARGV[1])
redis.log(redis.LOG_WARNING,"时间窗=" ..expire)

--限流阈值
local limit = tonumber(ARGV[2])
redis.log(redis.LOG_WARNING,"限流频次=" ..limit)

--当前并发数
local current = tonumber(redis.call('get', key1) or "0")
redis.log(redis.LOG_WARNING,"当前并发=" ..current)

if current + 1 > limit then
    return 0
else
    redis.call("INCRBY", key1, "1")
    redis.call("expire", key1, expire)
    return 1
end
