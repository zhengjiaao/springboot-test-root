--获取KEY
local key1 = KEYS[1]

if redis.call("get", key1) then
    return true
else
    return false
end
