package com.jack.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhouhuansheng on 2016-08-18.
 */
public class IdGenerator {
    private final static Logger logger = LoggerFactory.getLogger(IdGenerator.class);

    private final static long twepoch = 1361753741828L;
    private long sequence = 0L;
    private final static long workerIdBits = 8L;
    private final static long maxWorkerId = -1L ^ -1L << workerIdBits;
    private final static long sequenceBits = 10L;
    private final static long workerIdShift = sequenceBits;
    private final static long timestampLeftShift = sequenceBits + workerIdBits;
    private final static long sequenceMask = -1L ^ -1L << sequenceBits;

    private long workerId = 0L;
    private long lastTimestamp = -1L;

    public IdGenerator() {

    }

    public IdGenerator(long moduleId) {
        if (moduleId > this.maxWorkerId || moduleId < 0) {
            throw new IllegalArgumentException(String.format("Worker id can't be greater than %d or less than 0",
                    this.maxWorkerId));
        }
        this.workerId = moduleId;
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & this.sequenceMask;
            if (this.sequence == 0) {
                logger.trace("###########" + sequenceMask);
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
            logger.warn("Clock moved backwards. Refusing to generate id for {} milliseconds", this.lastTimestamp - timestamp);
        }
        this.lastTimestamp = timestamp;
        long nextId = ((timestamp - twepoch << timestampLeftShift)) | (this.workerId << this.workerIdShift) | (this.sequence);
        logger.trace("timestamp:{},timestampLeftShift:{},nextId:{},workerId:{},sequence:{}", timestamp, timestampLeftShift,
                nextId, workerId, sequence);
        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
