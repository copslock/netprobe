package com.fuyong.netprobe.qualcomm;

import com.fuyong.netprobe.DataCache;
import com.fuyong.netprobe.common.QID;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

/**
 * Created by f00151473 on 2014/6/9.
 */
public class PacketBase {
    public final static byte CMD_CODE_LOG_RSP = 0x10;
    public final static int LOG_RESPONSE_HEAD_LEN = 16;
    public final static short INVALID_LOGCODE = (short) 0xFFFF;
    protected final static long PACKET_TYPE_DIAG_MASK = 0x0100000000000000L;
    protected final static long PACKET_TYPE_USER_MASK = 0x0200000000000000L;
    protected final static long PACKET_TYPE_HEART_MASK = 0x0300000000000000L;
    protected final static long DIRECTION_OUT = 0x0001000000000000L;
    protected final static long DIRECTION_IN = 0x0002000000000000L;
    private static final long TIME_OFF_SET = new Date(80, 0, 6, 0, 0, 0)
            .getTime() - new Date(70, 0, 1, 0, 0, 0).getTime();
    private final double TIMESTAMP_TO_TIMETICK_UNIT = 1.25;
    protected ByteBuffer mPacketData;
    private int mLength;
    private int mLogCode;
    private long mTimeStamp;
    private long mTickCount;

    public PacketBase() {

    }

    public PacketBase(byte[] data) {
        setPacketData(data);
    }

    public void setPacketData(byte[] packetData) {
        mLength = packetData.length;
        mPacketData = ByteBuffer.wrap(packetData);
        mPacketData.order(ByteOrder.LITTLE_ENDIAN);
    }

    private void calcTickCount() {
        mTimeStamp = mPacketData.getLong(8);
        long tmp125MsCount = (mTimeStamp & 0xFFFFFFFFFFFF0000L) >> 16;
        long tmp125Ms40Count = (mTimeStamp & 0xFC00L) >> 10;

        mTickCount = (long) (tmp125MsCount * TIMESTAMP_TO_TIMETICK_UNIT
                + tmp125Ms40Count * TIMESTAMP_TO_TIMETICK_UNIT / 40)
                + TIME_OFF_SET;
    }

    public int getLogCode() {
        mLogCode = INVALID_LOGCODE;
        if (CMD_CODE_LOG_RSP == mPacketData.get(0)) {
            mLogCode = mPacketData.getShort(6);
        } else if (mPacketData.limit() >= 4) {
//            mLogCode = mPacketData.getInt(0);
        }
        return mLogCode;
    }

    public long getTickCount() {
        calcTickCount();
        return mTickCount;
    }

    public boolean isValid() {
        if (CMD_CODE_LOG_RSP == mPacketData.get(0)) {
            int len = mPacketData.getShort(2);
            return mLength == (len + 4);
        }
        return false;
    }

    public void updateToCache(DataCache cache) {
        cache.put(QID.ID.LOG_CODE, getLogCode());
        cache.put(QID.ID.TICK_COUNT, getTickCount());
    }

    synchronized public Object get(QID.ID k) {
        return new Object();
    }

    synchronized public Object get(QID.ID k, int i) {
        return new Object();
    }

    public Number getNumber(QID.ID k) {
        switch (k) {
            case LOG_CODE:
                return getLogCode();
            case TICK_COUNT:
                return getTickCount();
            default:
                return new Integer(-1);
        }
    }

    public Number getNumber(QID.ID k, int i) {
        return new Integer(-1);
    }
}
