local queue = KEYS[1]
local waiting = KEYS[2]
local user = ARGV[1]

-- if user already waiting do nothing
if redis.call("SISMEMBER", waiting, user) == 1 then
    return nil
end

-- try pop partner
local partner = redis.call("LPOP", queue)

-- if no partner → enqueue user
if not partner then
    redis.call("RPUSH", queue, user)
    redis.call("SADD", waiting, user)
    return nil
end

-- avoid self match
if partner == user then
    redis.call("RPUSH", queue, user)
    redis.call("SADD", waiting, user)
    return nil
end

-- remove partner from waiting set
redis.call("SREM", waiting, partner)

return partner