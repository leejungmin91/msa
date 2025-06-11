package com.store.member.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * RedisService: provides atomic list operations to prevent duplicates
 */
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // --- Value operations ---
    public void save(String key, Object value, long timeoutInSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeoutInSeconds));
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // --- List operations with deduplication ---

    /**
     * Set element at specific index in the list.
     */
    public void setList(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * Atomically remove all existing instances of `value` and push it to the end of the list.
     * Prevents duplicates when multiple pushes occur.
     */
    @SuppressWarnings("unchecked")
    public Long pushList(String key, Object value) {
        RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
        RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
        byte[] rawKey = keySerializer.serialize(key);
        byte[] rawVal = valueSerializer.serialize(value);
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            connection.lRem(rawKey, 0, rawVal);
            return connection.rPush(rawKey, rawVal);
        });
    }

    /**
     * Retrieve all elements from the list.
     */
    public List<Object> rangeListAll(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * Remove all occurrences of `value` from the list.
     */
    public Long removeList(String key, Object value) {
        return redisTemplate.opsForList().remove(key, 0, value);
    }

    // --- Key management operations ---

    public void expire(String key, long timeoutInSeconds) {
        redisTemplate.expire(key, Duration.ofSeconds(timeoutInSeconds));
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
