/*********************************************************
 * ��Ȩ��Ϣ:  ��Ϊ�������޹�˾
 * �� Ʒ ��:  GENEX SEA
 * �� �� ��:  Gsm5076Packet.java
 * �汾˵��:  GENEX SEA V100R005
 * @author s66105
 * ����ʱ��:  2010-8-24-����10:09:04
 * ������Ϣ: 
 * �޸ļ�¼: 
 * �޸���		��  ��				�޸�����
 * s66105		2010-8-24-����10:09:04		
 *********************************************************/
package com.fuyong.netprobe.qualcomm.gsm;

import com.fuyong.netprobe.common.QID;
import com.fuyong.netprobe.qualcomm.PacketBase;

public final class GSM51FCPacket extends PacketBase {

    public static final int NCELL_DATA_LEN = 25;
    public static final int NCELL_DATA_START = LOG_RESPONSE_HEAD_LEN + 8;

    public GSM51FCPacket() {
        super();
    }

    public GSM51FCPacket(byte[] data) {
        super(data);
    }

    @Override
    public Number getNumber(QID.ID k) {
        switch (k) {
            case GSM_SERVING_ARFCN:
                return getServingBcchArfcn();
            case GSM_SERVING_BAND:
                return getServingBcchBand();
            case GSM_SERVING_RX_LEV:
                return getRxLev();
            case GSM_SERVING_C1:
                return getServingC1();
            case GSM_SERVING_C2:
                return getServingC2();
            case GSM_SERVING_C31:
                return getServingC31();
            case GSM_SERVING_C32:
                return getServingC32();
            case GSM_NEIGHBOUR_COUNT:
                return getNCellCount();
            default:
                return super.getNumber(k);
        }
    }

    @Override
    public Number getNumber(QID.ID k, int i) {
        switch (k) {
            case GSM_NEIGHBOUR_ARFCN:
                return getNCellArfcn(i);
            case GSM_NEIGHBOUR_RX_LEV:
                return getNCellRxLev(i);
            case GSM_NEIGHBOUR_C1:
                return getNCellC1(i);
            case GSM_NEIGHBOUR_C2:
                return getNCellC2(i);
            case GSM_NEIGHBOUR_C31:
                return getNCellC31(i);
            case GSM_NEIGHBOUR_C32:
                return getNCellC32(i);
            default:
                return super.getNumber(k, i);
        }
    }

    public short getServingBcchArfcn() {
        return (short) (0x0FFF & mPacketData.getShort(LOG_RESPONSE_HEAD_LEN));
    }

    public byte getServingBcchBand() {
        return (byte) ((0xF0 & mPacketData.get(LOG_RESPONSE_HEAD_LEN + 1)) >> 4);
    }

    public float getRxLev() {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN + 5) - 110.0f;
    }

    public int getServingC1() {
        return mPacketData.getInt(LOG_RESPONSE_HEAD_LEN + 6);
    }

    public int getServingC2() {
        return mPacketData.getInt(LOG_RESPONSE_HEAD_LEN + 10);
    }

    public int getServingC31() {
        return mPacketData.getInt(LOG_RESPONSE_HEAD_LEN + 14);
    }

    public int getServingC32() {
        return mPacketData.getInt(LOG_RESPONSE_HEAD_LEN + 18);
    }

    public byte isRecentReselected() {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN + 24);
    }

    public short getNCellCount() {
        return mPacketData.getShort(LOG_RESPONSE_HEAD_LEN + 25);
    }

    public short getNCellArfcn(int i) {
        return (short) (0x0FFF & mPacketData.getShort(NCELL_DATA_START + i * NCELL_DATA_LEN));
    }

    public byte getNCellBand(int i) {
        return (byte) ((0xF0 & mPacketData.get(NCELL_DATA_START + i * NCELL_DATA_LEN + 1)) >> 4);
    }

    public float getNCellRxLev(int i) {
        return mPacketData.get(NCELL_DATA_START + i * NCELL_DATA_LEN + 5) - 110.0f;
    }

    public int getNCellC1(int i) {
        return mPacketData.getInt(NCELL_DATA_START + i * NCELL_DATA_LEN + 6);
    }

    public int getNCellC2(int i) {
        return mPacketData.getInt(NCELL_DATA_START + i * NCELL_DATA_LEN + 10);
    }

    public int getNCellC31(int i) {
        return mPacketData.getInt(NCELL_DATA_START + i * NCELL_DATA_LEN + 14);
    }

    public int getNCellC32(int i) {
        return mPacketData.getInt(NCELL_DATA_START + i * NCELL_DATA_LEN + 18);
    }
}
