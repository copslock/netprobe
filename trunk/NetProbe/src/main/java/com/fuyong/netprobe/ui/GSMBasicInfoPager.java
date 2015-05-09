package com.fuyong.netprobe.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fuyong.netprobe.R;
import com.fuyong.netprobe.qualcomm.gsm.GSM5071Packet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by f00151473 on 2014/6/10.
 */
public class GSMBasicInfoPager extends Fragment {
    public static final int UPDATE = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE:
                    update();
                    break;
                default:
            }
        }
    };
    private TextView textView;
    private byte[] mPacketData;
    private Map<Integer, TextView> mTableCells = new HashMap<Integer, TextView>();

    public GSMBasicInfoPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.table, container, false);
        TableLayout tableLayout = ((TableLayout) rootView.findViewById(R.id.table));
        createTable(this.getActivity(), tableLayout, 15, 6);
        mTableCells.get(calcCellId(0, 0)).setText("No.");
        mTableCells.get(calcCellId(0, 1)).setText("ARFCN");
        mTableCells.get(calcCellId(0, 2)).setText("RxP(dBm)");
        mTableCells.get(calcCellId(0, 3)).setText("BSIC");
        mTableCells.get(calcCellId(0, 4)).setText("FN Lag");
        mTableCells.get(calcCellId(0, 5)).setText("Qbit Lag");
        return rootView;
    }

    private void createTable(Context context, TableLayout tableLayout, int rows, int cols) {
        for (int row = 0; row < rows; row++) {
            TableRow tr = new TableRow(context);
            if (row % 2 == 0) tr.setBackgroundColor(Color.LTGRAY);
            tr.setId(row);
            tr.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            for (int col = 0; col < cols; col++) {
                TextView textView = new TextView(context);
                textView.setId(calcCellId(row, col));
                textView.setPadding(8, 2, 8, 2);
                textView.setTextColor(Color.BLUE);
                textView.setGravity(Gravity.RIGHT);
                tr.addView(textView);
                mTableCells.put(textView.getId(), textView);
            }
            tableLayout.addView(tr, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private int calcCellId(int row, int col) {
        return (short) row << 16 | (short) col;
    }

    private void update() {
        GSM5071Packet gsm5071Packet = new GSM5071Packet(mPacketData);
        byte count = gsm5071Packet.getCellCount();
        byte ncc = 0;
        byte bcc = 0;
        for (int i = 0; i < count; i++) {
            mTableCells.get(calcCellId(i + 1, 0)).setText(String.valueOf(i + 1));
            mTableCells.get(calcCellId(i + 1, 1)).setText(String.valueOf(gsm5071Packet.getAEFCN(i)));
            mTableCells.get(calcCellId(i + 1, 2)).setText(String.format("%.2f", gsm5071Packet.getRxPower(i)));
            if (1 == gsm5071Packet.getBSICKnow(i)) {
                byte basic = gsm5071Packet.getBSIC(i);
                ncc = (byte) ((basic >> 3) & 0x07);
                bcc = (byte) (basic & 0x07);
                mTableCells.get(calcCellId(i + 1, 3)).setText(String.format("<%d,%d>", ncc, bcc));
            } else {
                mTableCells.get(calcCellId(i + 1, 3)).setText("<-,->");
            }
            mTableCells.get(calcCellId(i + 1, 4)).setText(String.valueOf(gsm5071Packet.getFrameNumberOffset(i)));
            mTableCells.get(calcCellId(i + 1, 5)).setText(String.valueOf(gsm5071Packet.getTimeOffset(i)));
        }
    }
}
