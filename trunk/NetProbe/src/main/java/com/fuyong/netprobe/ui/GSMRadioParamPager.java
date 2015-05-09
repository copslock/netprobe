package com.fuyong.netprobe.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fuyong.netprobe.R;
import com.fuyong.netprobe.common.DateSyncUtil;
import com.fuyong.netprobe.common.FormatUtil;
import com.fuyong.netprobe.common.QID;
import com.fuyong.netprobe.qualcomm.PacketBase;
import com.fuyong.netprobe.qualcomm.PacketDispatcher;


/**
 * Created by f00151473 on 2014/6/10.
 */
public class GSMRadioParamPager extends Fragment implements PacketDispatcher.PacketListener {
    public static final int UPDATE = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE:
                    updateView((PacketBase) msg.obj);
                    break;
                default:
            }
        }
    };
    public static final int NCELL_MAX_COUNT = 10;
    private Table mServingCellInfo;
    private Table mNCellInfo;

    public GSMRadioParamPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.linear_layout_empty, container, false);
        mServingCellInfo = new Table(inflater, 2, 5);
        mServingCellInfo.setTitle("Serving Cell Info");
        mServingCellInfo.setValue(0, 0, "ARFCN");
        mServingCellInfo.setValue(0, 1, "RxLev Full");
        mServingCellInfo.setValue(0, 2, "RxLev Sub");
        mServingCellInfo.setValue(0, 3, "RxQual Full");
        mServingCellInfo.setValue(0, 4, "RxQual Sub");
        mNCellInfo = new Table(inflater, NCELL_MAX_COUNT, 4);
        mNCellInfo.setTitle("Neighbour Cell Info");
        mNCellInfo.setValue(0, 0, "No.");
        mNCellInfo.setValue(0, 1, "ARFCN");
        mNCellInfo.setValue(0, 2, "RxLev");
        mNCellInfo.setValue(0, 3, "BSIC");

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linear_layout);
        layout.addView(mServingCellInfo.getTableView());
        layout.addView(mNCellInfo.getTableView());

        PacketDispatcher.getInstance().registerPacketListener(0x513A, this);
        PacketDispatcher.getInstance().registerPacketListener(0x51FC, this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PacketDispatcher.getInstance().unregisterPacketListener(0x513A, this);
        PacketDispatcher.getInstance().unregisterPacketListener(0x51FC, this);
    }

    private void updateView(PacketBase packet) {
        mServingCellInfo.setValue(1, 0, FormatUtil.formatNumber(packet.getNumber(QID.ID.GSM_SERVING_ARFCN)));
        mServingCellInfo.setValue(1, 1, FormatUtil.formatNumber(packet.getNumber(QID.ID.GSM_SERVING_RX_LEV_FULL)));
        mServingCellInfo.setValue(1, 2, FormatUtil.formatNumber(packet.getNumber(QID.ID.GSM_SERVING_RX_LEV_SUB)));
        mServingCellInfo.setValue(1, 3, FormatUtil.formatNumber(packet.getNumber(QID.ID.GSM_SERVING_RX_QUAL_FULL)));
        mServingCellInfo.setValue(1, 4, FormatUtil.formatNumber(packet.getNumber(QID.ID.GSM_SERVING_RX_QUAL_SUB)));

        Number number = packet.getNumber(QID.ID.GSM_NEIGHBOUR_COUNT);
        if (null == number) {
            return;
        }
        int cellCount = number.shortValue();
        if (cellCount > NCELL_MAX_COUNT) {
            cellCount = NCELL_MAX_COUNT;
        }
        byte ncc = 0;
        byte bcc = 0;
        for (int i = 0; i < cellCount; i++) {
            mNCellInfo.setValue(i + 1, 0, String.valueOf(i + 1));
            mNCellInfo.setValue(i + 1, 1, FormatUtil.formatNumber(packet.getNumber(QID.ID.GSM_NEIGHBOUR_ARFCN, i)));
            mNCellInfo.setValue(i + 1, 2, FormatUtil.formatNumber(packet.getNumber(QID.ID.GSM_NEIGHBOUR_RX_LEV, i)));
            Byte basic = (Byte) packet.getNumber(QID.ID.GSM_NEIGHBOUR_BSIC, i);
            if (null != basic) {
                ncc = (byte) ((basic >> 3) & 0x07);
                bcc = (byte) (basic & 0x07);
                mNCellInfo.setValue(i + 1, 3, String.format("<%d,%d>", ncc, bcc));
            } else {
                mNCellInfo.setValue(i + 1, 3, "<-,->");
            }
        }

        mNCellInfo.reset(cellCount + 1, 0, NCELL_MAX_COUNT, 4);

        long tickCount = packet.getNumber(QID.ID.TICK_COUNT).longValue();
        mServingCellInfo.setTitle("Serving Cell Info\n[" +
                DateSyncUtil.formatDate(tickCount) + "]");
        packet = null;
    }

    @Override
    public void update(PacketBase packet) {
        Message msg = handler.obtainMessage();
        msg.what = UPDATE;
        msg.obj = packet;
        handler.sendMessage(msg);
    }
}
