package com.fuyong.netprobe.qualcomm.wcdma;


import com.fuyong.netprobe.common.QID;
import com.fuyong.netprobe.qualcomm.PacketBase;

public final class WCDMA4179Packet extends PacketBase {
    public static final int CELL_INFO_LEN = 12;
    public static final int SRCHLOGTASK_INFO_LENGTH = 11;


    public static final int ASET = 0;
    public static final int INTRA_FREQ_MORNITORED_SET = 2;
    public static final int INTRA_FREQ_UNLISTED_SET = 4;
    public static final int INTER_FREQ_MORNITORED_SET = 1;
    public static final int INTER_FREQ_VIRTUAL_ACTIVE_SET = 5;
    public static final int INPUT_TASK_INFO_STAT_POS = LOG_RESPONSE_HEAD_LEN + 14;

    public WCDMA4179Packet(byte[] data) {
        super(data);
    }

    public WCDMA4179Packet() {

    }

    @Override
    public synchronized Number getNumber(QID.ID k) {
        switch (k) {
            case WCDMA_4179_TASK_NUM:
                return getTaskSpecNum();
            default:
                return super.getNumber(k);
        }
    }

    @Override
    public synchronized Number getNumber(QID.ID k, int i) {
        switch (k) {
            case WCDMA_4179_CARRIER:
                return getCarrierInfo(i);
            case WCDMA_4179_PSC:
                return getPSC(i);
            case WCDMA_4179_R99_SET:
                return getSet(i);
            case WCDMA_4179_RSCP:
                float EcIo = getEcIo1(i);
                if (EcIo != Float.NaN) {
                    return getRscp(EcIo, getRxAgc(i));
                } else {
                    return Float.NaN;
                }
            case WCDMA_4179_ECIO:
                return getEcIo1(i);
            case WCDMA_4179_PEAK_ECIO:
                return getPeakEcIo1(i);
            default:
                return super.getNumber(k);
        }
    }

    public byte getVersionNum() {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN);
    }

    public short getRxC0Agc0() {
        return mPacketData.getShort(LOG_RESPONSE_HEAD_LEN + 1);
    }

    public short getRxC0Agc1() {
        return mPacketData.getShort(LOG_RESPONSE_HEAD_LEN + 3);
    }

    public short getRxC1Agc0() {
        return mPacketData.getShort(LOG_RESPONSE_HEAD_LEN + 5);
    }

    public short getRxC1Agc1() {
        return mPacketData.getShort(LOG_RESPONSE_HEAD_LEN + 7);
    }

    public short getFreqHyp() {
        return mPacketData.getShort(LOG_RESPONSE_HEAD_LEN + 10);
    }

    public byte getTaskSpecNum() {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN + 12);
    }

    public int getNC(int i) {
        byte orgNC = mPacketData.get(INPUT_TASK_INFO_STAT_POS + i * SRCHLOGTASK_INFO_LENGTH);
        int nc;
        switch (orgNC) {
            case 31:
                nc = 2048;
                break;
            case 15:
                nc = 1024;
                break;
            case 7:
                nc = 512;
                break;
            case 3:
                nc = 256;
                break;
            case 1:
                nc = 128;
                break;
            default:
                nc = 0;
        }
        return nc;
    }

    public int getNN(int i) {
        byte orgNN = mPacketData.get(INPUT_TASK_INFO_STAT_POS + i * SRCHLOGTASK_INFO_LENGTH + 1);
        int nn = 0;
        switch (orgNN) {
            case 0:
                nn = 1;
                break;
            case 1:
                nn = 2;
                break;
            case 16:
                nn = 17;
                break;
            case 35:
                nn = 36;
                break;
            default:
        }
        return nn;
    }

    public byte getRxdMode(int i) {
        byte rxdInfo = mPacketData.get(INPUT_TASK_INFO_STAT_POS + i * SRCHLOGTASK_INFO_LENGTH + 2);
        return (byte) (0x03 & rxdInfo);
    }

    public byte getCarrierInfo(int i) {
        byte rxdInfo = mPacketData.get(INPUT_TASK_INFO_STAT_POS + i * SRCHLOGTASK_INFO_LENGTH + 2);
        return (byte) ((rxdInfo & 0x20) >> 5);
    }

    public byte getOVSF(int i) {
        return mPacketData.get(INPUT_TASK_INFO_STAT_POS + i * SRCHLOGTASK_INFO_LENGTH + 3);
    }

    public short getPNPos(int i) {
        return mPacketData.getShort(INPUT_TASK_INFO_STAT_POS + i * SRCHLOGTASK_INFO_LENGTH + 4);
    }

    public short getSrcCode(int i) {
        return mPacketData.getShort(INPUT_TASK_INFO_STAT_POS + i * SRCHLOGTASK_INFO_LENGTH + 6);
    }

    public byte getSet(int i) {
        return mPacketData.get(INPUT_TASK_INFO_STAT_POS + i * SRCHLOGTASK_INFO_LENGTH + 10);
    }

    public int getTaskInfoStartPos() {
        return INPUT_TASK_INFO_STAT_POS + SRCHLOGTASK_INFO_LENGTH * getTaskSpecNum();
    }

    public int getResultNum(int j) {
        if (3 == getRxdMode(j)) {
            return 2;
        }
        return 1;
    }

    public int getFiltEng1(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j);
    }

    public int getFiltEng2(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 4);
    }

    public int getFiltEng3(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 8);
    }

    public int getPOS1(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 12);
    }

    public int getPOS2(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 16);
    }

    public int getPOS3(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 20);
    }

    public int getPOS4(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 24);
    }

    public int getPOS5(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 28);
    }

    public int getPOS6(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 32);
    }

    public int getENG1(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 36);
    }

    public int getENG2(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 40);
    }

    public int getENG3(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 44);
    }

    public int getENG4(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 48);
    }


    public int getENG5(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 52);
    }

    public int getENG6(int j) {
        return mPacketData.getInt(getTaskInfoStartPos() + (3 * 4 + 48 * getResultNum(j)) * j + 56);
    }


    public int getPSC(int i) {
        return (getSrcCode(i) & 0x1fff) >> 4;
    }


    public float getEcIo1(int i) {
        int nc = getNC(i);
        int nn = getNN(i);
        int eng1 = getFiltEng1(i);
        int eng2 = getFiltEng2(i);
        int eng3 = getENG3(i);
        eng1 = eng1 > eng2 ? eng1 : eng2;
        eng1 = eng1 > eng3 ? eng1 : eng3;
        return calcEcIo(nc, nn, eng1);
    }

    public float getPeakEcIo1(int i) {
        int nc = getNC(i);
        int nn = getNN(i);
        int eng = getPOS1(i);
        return calcEcIo(nc, nn, eng);
    }

    public float getEcIo2(int i) {
        int nc = getNC(i);
        int nn = getNN(i);
        int eng = getENG2(i);
        return calcEcIo(nc, nn, eng);
    }

    public float getPeakEcIo2(int i) {
        int nc = getNC(i);
        int nn = getNN(i);
        int eng = getPOS2(i);
        return calcEcIo(nc, nn, eng);
    }

    public float getEcIo3(int i) {
        int nc = getNC(i);
        int nn = getNN(i);
        int eng = getENG3(i);
        return calcEcIo(nc, nn, eng);
    }

    public float getPeakEcIo3(int i) {
        int nc = getNC(i);
        int nn = getNN(i);
        int eng = getPOS3(i);
        return calcEcIo(nc, nn, eng);
    }

    public float getEcIo4(int i) {
        int nc = getNC(i);
        int nn = getNN(i);
        int eng = getENG4(i);
        return calcEcIo(nc, nn, eng);
    }

    public float getPeakEcIo4(int i) {
        int nc = getNC(i);
        int nn = getNN(i);
        int eng = getPOS4(i);
        return calcEcIo(nc, nn, eng);
    }

    public float getEcIo5(int i) {
        int nc = getNC(i);
        int nn = getNN(i);
        int eng = getENG5(i);
        return calcEcIo(nc, nn, eng);
    }

    public float getPeakEcIo5(int i) {
        int nc = getNC(i);
        int nn = getNN(i);
        int eng = getPOS5(i);
        return calcEcIo(nc, nn, eng);
    }

    public float getEcIo6(int i) {
        int nc = getNC(i);
        int nn = getNN(i);
        int eng = getENG6(i);
        return calcEcIo(nc, nn, eng);
    }

    public float getPeakEcIo6(int i) {
        int nc = getNC(i);
        int nn = getNN(i);
        int eng = getPOS6(i);
        return calcEcIo(nc, nn, eng);
    }

    public float getRscp(float ecio, int rxAgc) {
        return (float) (ecio - 106 + (rxAgc + 512) / 10.0);
    }

    public short getRxAgc(int i) {
        short rxAgc = getRxC0Agc0();
        if (0 != getCarrierInfo(i)) {
            rxAgc = getRxC1Agc0();
        }
        return rxAgc;
    }

    public float calcEcIo(int nc, int nn, int eng) {
        if (0 == eng) {
            return Float.NaN;
        }
        double ecio = 0;
        if (2048 == nc && 1 == nn) {
            ecio = 10 * Math.log(eng * 455.33 / (nc * nc * nn) - 1 / nc);
        } else if (2048 == nc && 2 == nn) {
            ecio = 10 * Math.log(eng * 910.67 / (nc * nc * nn) - 1 / nc);
        } else if (1024 == nc) {
            ecio = 10 * Math.log(eng * 113.89 / (nc * nc * nn) - 1 / nc);
        } else if (256 == nc && 16 == nn) {
            ecio = 10 * Math.log(eng * 114.22 / (nc * nc * nn) - 1 / nc);
        } else if (128 == nc) {
            ecio = 10 * Math.log(eng * 36.63 / (nc * nc * nn) - 0.64 / nc);
        }
        return (float) ecio;
    }
}
