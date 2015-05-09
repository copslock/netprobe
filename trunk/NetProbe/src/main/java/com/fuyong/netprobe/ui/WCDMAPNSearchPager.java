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
public class WCDMAPNSearchPager extends Fragment implements PacketDispatcher.PacketListener {
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
    public static final int CELL_MAX_COUNT = 18;
    private Table mCellInfo;
    private int i = 0;
    private long mLastTickcount;

    public WCDMAPNSearchPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.linear_layout_empty, container, false);
        mCellInfo = new Table(inflater, CELL_MAX_COUNT, 7);
        mCellInfo.setTitle("PN Search");
        mCellInfo.setValue(0, 0, "No.");
        mCellInfo.setValue(0, 1, "Carrier");
        mCellInfo.setValue(0, 2, "PSC");
        mCellInfo.setValue(0, 3, "R99 Set");
        mCellInfo.setValue(0, 4, "RSCP");
        mCellInfo.setValue(0, 5, "Ec/Io");
        mCellInfo.setValue(0, 6, "Peak Ec/Io");

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linear_layout);
        layout.addView(mCellInfo.getTableView());

        PacketDispatcher.getInstance().registerPacketListener(0x4179, this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PacketDispatcher.getInstance().unregisterPacketListener(0x4179, this);
    }

    private void updateView(PacketBase packet) {
        Number number = packet.getNumber(QID.ID.WCDMA_4179_TASK_NUM);
        if (null == number) {
            return;
        }
        byte taskSpecNum = number.byteValue();
        if (taskSpecNum > CELL_MAX_COUNT) {
            taskSpecNum = CELL_MAX_COUNT;
        }
        float ecIo;
        for (int i = 0; i < taskSpecNum; i++) {
            mCellInfo.setValue(i + 1, 0, String.valueOf(i + 1));
            String str = "Primary";
            Byte carrier = (Byte) (packet.getNumber(QID.ID.WCDMA_4179_CARRIER, i));
            if (0 != carrier) {
                str = "Secondary";
            }
            mCellInfo.setValue(i + 1, 1, str);
            mCellInfo.setValue(i + 1, 2, FormatUtil.formatNumber(packet.getNumber(QID.ID.WCDMA_4179_PSC, i)));

            byte set = (byte) (0x07 & (Byte) packet.getNumber(QID.ID.WCDMA_4179_R99_SET, i));
            str = "";
            if (0 == set) {
                str = "ASET";
            } else if (2 == set) {
                str = "IntraF MSET";
            }
            if (4 == set) {
                str = "IntraF USET";
            }
            if (1 == set) {
                str = "InterF MSET";
            }
            if (5 == set) {
                str = "InterF VASET";
            }

            mCellInfo.setValue(i + 1, 3, str);
            mCellInfo.setValue(i + 1, 4, FormatUtil.formatNumber(packet.getNumber(QID.ID.WCDMA_4179_RSCP, i)));
            mCellInfo.setValue(i + 1, 5, FormatUtil.formatNumber(packet.getNumber(QID.ID.WCDMA_4179_ECIO, i)));
            mCellInfo.setValue(i + 1, 6, FormatUtil.formatNumber(packet.getNumber(QID.ID.WCDMA_4179_PEAK_ECIO, i)));
        }

        mCellInfo.reset(taskSpecNum + 1, 0, CELL_MAX_COUNT, 7);
        long tickCount = packet.getNumber(QID.ID.TICK_COUNT).longValue();
        mCellInfo.setTitle("PN Search\n[" +
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
