package com.fuyong.netprobe.qualcomm.gsm;


import com.fuyong.netprobe.common.QID;
import com.fuyong.netprobe.qualcomm.PacketBase;

public final class GSM5071Packet extends PacketBase {
    public static final int CELL_INFO_LEN = 12;

    public GSM5071Packet(byte[] data) {
        super(data);
    }

    public GSM5071Packet() {

    }

    @Override
    public Number getNumber(QID.ID k) {
        switch (k) {
            case GSM_5071_BA_COUNT:
                return getCellCount();
            default:
                return super.getNumber(k);
        }
    }

    @Override
    public Number getNumber(QID.ID k, int i) {
        switch (k) {
            case GSM_5071_ARFCN:
                return getAEFCN(i);
            case GSM_5071_RX_POWER:
                return getRxPower(i);
            case GSM_5071_BSIC_KNOW:
                return getBSICKnow(i);
            case GSM_5071_BSIC:
                return getBSIC(i);
            case GSM_5071_FN_LAG:
                return getFrameNumberOffset(i);
            case GSM_5071_QBIT_LAG:
                return getTimeOffset(i);
            case GSM_5071_BAND:
                return getBand(i);
            default:
                return super.getNumber(k, i);
        }
    }

    public byte getCellCount() {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN);
    }

    public short getAEFCN(int i) {
        return (short) (0x0FFF & mPacketData.getShort(LOG_RESPONSE_HEAD_LEN + i * CELL_INFO_LEN + 1));
    }

    public byte getBand(int i) {
        return (byte) ((0xF0 & mPacketData.get(LOG_RESPONSE_HEAD_LEN + i * CELL_INFO_LEN + 2)) >> 4);
    }

    public float getRxPower(int i) {
        return mPacketData.getShort(LOG_RESPONSE_HEAD_LEN + i * CELL_INFO_LEN + 3) / 16.0f;
    }

    public byte getBSICKnow(int i) {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN + i * CELL_INFO_LEN + 5);
    }

    public byte getBSIC(int i) {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN + i * CELL_INFO_LEN + 6);
    }

    public int getFrameNumberOffset(int i) {
        return mPacketData.getInt(LOG_RESPONSE_HEAD_LEN + i * CELL_INFO_LEN + 7);
    }

    public short getTimeOffset(int i) {
        return mPacketData.getShort(LOG_RESPONSE_HEAD_LEN + i * CELL_INFO_LEN + 11);
    }
}
