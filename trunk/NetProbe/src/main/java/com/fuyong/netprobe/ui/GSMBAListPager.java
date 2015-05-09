/**
 * Copyright (c) 2014 <a href="userfuy@163.com">fu yong</a>. All right is reserved.
 *
 * @author: <a href="userfuy@163.com">fu yong</a>
 * @version: 1.0
 * @className:
 * @description:
 * @date:
 */

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

public class GSMBAListPager extends Fragment implements PacketDispatcher.PacketListener {
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
    public static final int CELL_MAX_COUNT = 17;
    private Table mCellInfo;
    private int i = 0;
    private long mLastTickcount;

    public GSMBAListPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.linear_layout_empty, container, false);
        mCellInfo = new Table(inflater, CELL_MAX_COUNT, 7);
        mCellInfo.setTitle("GSM BA List");
        mCellInfo.setValue(0, 0, "No.");
        mCellInfo.setValue(0, 1, "ARFCN");
        mCellInfo.setValue(0, 2, "RxP(dBm)");
        mCellInfo.setValue(0, 3, "BSIC");
        mCellInfo.setValue(0, 4, "FN lag");
        mCellInfo.setValue(0, 5, "Qbit lag");
        mCellInfo.setValue(0, 6, "Band");

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linear_layout);
        layout.addView(mCellInfo.getTableView());

        PacketDispatcher.getInstance().registerPacketListener((short) 0x5071, this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PacketDispatcher.getInstance().unregisterPacketListener(0x5071, this);
    }

    private void updateView(PacketBase packet) {
        Number number = packet.getNumber(QID.ID.GSM_5071_BA_COUNT);
        if (null == number)
            return;
        int cellCount = number.shortValue();
        if (cellCount > CELL_MAX_COUNT) {
            cellCount = CELL_MAX_COUNT;
        }
        byte ncc = 0;
        byte bcc = 0;
        for (int i = 0; i < cellCount; i++) {
            mCellInfo.setValue(i + 1, 0, String.valueOf(i + 1));
            mCellInfo.setValue(i + 1, 1, FormatUtil.formatNumber(packet.getNumber(QID.ID.GSM_5071_ARFCN, i)));
            mCellInfo.setValue(i + 1, 2, FormatUtil.formatNumber(packet.getNumber(QID.ID.GSM_5071_RX_POWER, i)));
            if (1 == (Byte) packet.getNumber(QID.ID.GSM_5071_BSIC_KNOW, i)) {
                byte basic = (Byte) packet.getNumber(QID.ID.GSM_5071_BSIC, i);
                ncc = (byte) ((basic >> 3) & 0x07);
                bcc = (byte) (basic & 0x07);
                mCellInfo.setValue(i + 1, 3, String.format("<%d,%d>", ncc, bcc));
            } else {
                mCellInfo.setValue(i + 1, 3, "<-,->");
            }
            mCellInfo.setValue(i + 1, 4, FormatUtil.formatNumber(packet.getNumber(QID.ID.GSM_5071_FN_LAG, i)));
            mCellInfo.setValue(i + 1, 5, FormatUtil.formatNumber(packet.getNumber(QID.ID.GSM_5071_QBIT_LAG, i)));
            mCellInfo.setValue(i + 1, 6, FormatUtil.formatNumber(packet.getNumber(QID.ID.GSM_5071_BAND, i)));
        }

        mCellInfo.reset(cellCount + 1, 0, CELL_MAX_COUNT, 7);
        long tickCount = packet.getNumber(QID.ID.TICK_COUNT).longValue();
        mCellInfo.setTitle("GSM BA List\n[" +
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
