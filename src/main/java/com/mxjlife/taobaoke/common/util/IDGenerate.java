package com.mxjlife.web.webtemplate.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 类说明:
 *
 * @author mxj
 * @email mxjlife.com
 * @version 创建时间：2017年9月10日 上午11:07:08
 */
public class IDGenerate {

    private static final long startTimeStamp = 1534919378079L; //定义一个起始时间 new Date().getTime()

    private static final long workerIdBits = 6L;
    private static final long dataCenterIdBits = 6L;
    private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private static final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    private static final long sequenceBits = 14L;
    private static final long workerIdShift = sequenceBits;
    private static final long dataCenterIdShift = sequenceBits + workerIdBits;
    private static final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
    private static final long sequenceMask = -1L ^ (-1L << sequenceBits);
    private static final Random r = new Random();

    private final long workerId;
    private final long dataCenterId;
    private final long idEpoch;
    private long lastTimestamp = -1L;
    private long sequence = 0;

    public IDGenerate() {
        this(startTimeStamp);
    }

    public IDGenerate(long idEpoch) {
        this(r.nextInt((int) maxWorkerId), r.nextInt((int) maxDataCenterId), 0, idEpoch);
    }

    public IDGenerate(long workerId, long dataCenterId, long sequence) {
        this(workerId, dataCenterId, sequence, startTimeStamp);
    }

    public IDGenerate(long workerId, long dataCenterId, long sequence, long idEpoch) {
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.sequence = sequence;
        this.idEpoch = idEpoch;

        if (workerId < 0 || workerId > maxWorkerId) {
            throw new IllegalArgumentException("workerId is illegal: " + workerId);
        }
        if (dataCenterId < 0 || dataCenterId > maxDataCenterId) {
            throw new IllegalArgumentException("dataCenterId is illegal: " + dataCenterId);
        }

        if (idEpoch >= timeGen()) {
            throw new IllegalArgumentException("idEpoch is illegal: " + idEpoch);
        }
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getTime() {
        return timeGen();
    }

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            throw new IllegalArgumentException("Clock moved backwards.");
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        long id = ((timestamp - idEpoch) << timestampLeftShift) | (dataCenterId <<
                dataCenterIdShift) | (workerId << workerIdShift) | sequence;
        return id;
    }

    public long getIdTimestamp(long id) {
        return idEpoch + (id >> timestampLeftShift);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }

        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IdBitWorker{");
        sb.append("workerId=").append(workerId);
        sb.append(", dataCenterId=").append(dataCenterId);
        sb.append(", idEpoch=").append(idEpoch);
        sb.append(", lastTimestamp=").append(lastTimestamp);
        sb.append(", sequence=").append(sequence);
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        IDGenerate worker = new IDGenerate();
        List<Long> ids = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            ids.add(worker.nextId());
        }
        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(ids.size());
        System.out.println(new Date().getTime());
    }
}