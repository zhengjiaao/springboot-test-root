--获取KEY
local key1 = KEYS[1]
--获取参数
local avg1 = ARGV[1]

if redis.call("set", key1, avg1) then
    return true
else
    return false
end
