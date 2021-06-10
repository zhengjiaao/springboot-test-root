local key = KEYS[1]

if redis.call("get",key) then
    redis.call("del",key)
    return true
else
    return false
end
