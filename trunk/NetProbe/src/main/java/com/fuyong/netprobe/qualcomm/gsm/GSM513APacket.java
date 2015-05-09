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

public final class GSM513APacket extends PacketBase {

    public static final int NCELL_DATA_LEN = 4;
    public static final int NCELL_DATA_START = LOG_RESPONSE_HEAD_LEN + 8;

    public GSM513APacket(byte[] data) {
        super(data);
    }

    public GSM513APacket() {
        super();
    }


    @Override
    public Number getNumber(QID.ID k) {
        switch (k) {
            case GSM_SERVING_ARFCN:
                return getServingBcchArfcn();
            case GSM_SERVING_BAND:
                return getServingBcchBand();
            case GSM_SERVING_RX_LEV_FULL:
                return getRxLevFull();
            case GSM_SERVING_RX_LEV_SUB:
                return getRxLevSub();
            case GSM_SERVING_RX_LEV:
                return getRxLevSub();
            case GSM_SERVING_RX_QUAL_FULL:
                return getRxQualFull();
            case GSM_SERVING_RX_QUAL_SUB:
                return getRxQualSub();
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
            case GSM_NEIGHBOUR_BAND:
                return getNCellBand(i);
            case GSM_NEIGHBOUR_RX_LEV:
                return getNCellRxLev(i);
            case GSM_NEIGHBOUR_BSIC:
                return getNCellBasic(i);
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

    public float getRxLevFull() {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN + 2) - 110.0f;
    }

    public float getRxLevSub() {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN + 3) - 110.0f;
    }

    public byte getRxQualFull() {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN + 4);
    }

    public byte getRxQualSub() {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN + 5);
    }

    public short getNCellCount() {
        return mPacketData.getShort(LOG_RESPONSE_HEAD_LEN + 6);
    }

    public short getNCellArfcn(int i) {
        return (short) (0x0FFF & mPacketData.getShort(NCELL_DATA_START + i * NCELL_DATA_LEN));
    }

    public byte getNCellBand(int i) {
        return (byte) ((0xF0 & mPacketData.get(NCELL_DATA_START + i * NCELL_DATA_LEN + 1)) >> 4);
    }

    public float getNCellRxLev(int i) {
        return mPacketData.get(NCELL_DATA_START + i * NCELL_DATA_LEN + 2) - 110.0f;
    }

    public byte getNCellBasic(int i) {
        return mPacketData.get(NCELL_DATA_START + i * NCELL_DATA_LEN + 3);
    }
}
