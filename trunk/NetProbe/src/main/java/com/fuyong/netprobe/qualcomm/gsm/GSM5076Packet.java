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

public final class GSM5076Packet extends PacketBase {
    private static byte[] txLevToPowerGSM = {39, 39, 38, 37, 35, 33, 31, 29, 27, 25, 23, 21, 19, 17, 15, 13, 11, 9, 7,
            5, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 0, 0};
    private static byte[] txLevToPowerDCS = {30, 28, 26, 24, 22, 20, 18, 16, 14, 12, 10, 8, 6, 4, 2, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 36, 34, 32};
    private static byte[] txLevToPowerPCS = {30, 28, 26, 25, 24, 22, 20, 18, 16, 14, 12, 10, 8, 6, 4, 2, 0, 0, 0, 0,
            0, 0, 33, 33, 33, 33, 33, 33, 33, 33, 33, 32};

    public GSM5076Packet(byte[] data) {
        super(data);
    }

    public GSM5076Packet() {

    }

    @Override
    public Number getNumber(QID.ID k) {
        switch (k) {
            case GSM_TA:
                return getTA();
            case GSM_TX_POWER:
                return getTxPower();
            case GSM_TX_LEV:
                return getTxLev();
            default:
                return super.getNumber(k);
        }
    }

    @Override
    public Number getNumber(QID.ID k, int i) {
        return super.getNumber(k, i);
    }

    public byte getTxLev() {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN + 2);
    }

    public byte getTA() {
        return mPacketData.get(LOG_RESPONSE_HEAD_LEN + 3);
    }

    public byte getTxPower() {
        byte txPower = -1;
        byte band = (byte) ((0xF0 & mPacketData.get(LOG_RESPONSE_HEAD_LEN + 1)) >> 4);
        byte txLev = getTxLev();
        if (txLev > 32) {
            return txPower;
        }
        if (GSMBand.DCS_1800 == band) {
            txPower = txLevToPowerDCS[txLev];
        } else if (GSMBand.PCS_1900 == band) {
            txPower = txLevToPowerPCS[txLev];
        } else if (GSMBand.GSM_850 == band || GSMBand.GSM_900 == band) {
            txPower = txLevToPowerGSM[txLev];
        }
        return txPower;
    }
}
