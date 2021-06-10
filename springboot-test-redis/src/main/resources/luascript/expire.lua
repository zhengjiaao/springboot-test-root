--获取KEY
local key1 = KEYS[1]

local expire = ARGV[1]

if redis.call("get", key1) then
    redis.call("expire", key1, expire)
    return true
else
    return false
end
