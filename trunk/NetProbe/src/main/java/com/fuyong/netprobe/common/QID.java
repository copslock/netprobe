package com.fuyong.netprobe.common;

/**
 * Created by democrazy on 2014/6/14.
 */
public class QID {
    public enum ID {
        UNKNOWN,
        TICK_COUNT,
        LOG_CODE,
        GSM_SERVING_ARFCN,
        GSM_SERVING_BAND,
        GSM_SERVING_RX_LEV_FULL,
        GSM_SERVING_RX_LEV_SUB,
        GSM_SERVING_RX_LEV,
        GSM_SERVING_RX_QUAL_FULL,
        GSM_SERVING_RX_QUAL_SUB,

        GSM_SERVING_C1,
        GSM_SERVING_C2,
        GSM_SERVING_C31,
        GSM_SERVING_C32,

        GSM_NEIGHBOUR_COUNT,
        GSM_NEIGHBOUR_ARFCN,
        GSM_NEIGHBOUR_BAND,
        GSM_NEIGHBOUR_RX_LEV,
        GSM_NEIGHBOUR_BSIC,
        GSM_NEIGHBOUR_C1,
        GSM_NEIGHBOUR_C2,
        GSM_NEIGHBOUR_C31,
        GSM_NEIGHBOUR_C32,

        GSM_TA,
        GSM_TX_POWER,
        GSM_TX_LEV,

        // 0x5071
        GSM_5071_BA_COUNT,
        GSM_5071_ARFCN,
        GSM_5071_RX_POWER,
        GSM_5071_BSIC_KNOW,
        GSM_5071_BSIC,
        GSM_5071_FN_LAG,
        GSM_5071_QBIT_LAG,
        GSM_5071_BAND,

        // 0x4179
        WCDMA_4179_TASK_NUM,
        WCDMA_4179_CARRIER,
        WCDMA_4179_PSC,
        WCDMA_4179_R99_SET,
        WCDMA_4179_RSCP,
        WCDMA_4179_ECIO,
        WCDMA_4179_PEAK_ECIO,
    }
}
