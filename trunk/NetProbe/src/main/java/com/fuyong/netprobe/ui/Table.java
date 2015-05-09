package com.fuyong.netprobe.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fuyong.netprobe.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by f00151473 on 2014/6/11.
 */
public class Table {
    private final LayoutInflater mInflater;
    private Context mCtx;
    private Map<Integer, TextView> mTableCells = new HashMap<Integer, TextView>();
    private int mRows;
    private int mCols;
    private TableLayout mTableLayout;
    private View mTableView;
    private TextView mTitleView;

    public Table(LayoutInflater inflater, int rows, int cols) {
        mInflater = inflater;
        mCtx = inflater.getContext();
        mRows = rows;
        mCols = cols;
        createTable();
    }

    public View getTableView() {
        return mTableView;
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    private void createTable() {
        mTableView = mInflater.inflate(R.layout.table, null);
        mTitleView = (TextView) mTableView.findViewById(R.id.table_title);
        mTableLayout = (TableLayout) mTableView.findViewById(R.id.table);
        // 设置只可扩展，不可收缩
        mTableLayout.setStretchAllColumns(true);
        mTableLayout.setShrinkAllColumns(true);
        for (int row = 0; row < mRows; row++) {
            TableRow tr = new TableRow(mCtx);
            if (row % 2 == 0) tr.setBackgroundColor(Color.LTGRAY);
            tr.setId(row);
            tr.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            for (int col = 0; col < mCols; col++) {
                TextView textView = new TextView(mCtx);
                textView.setId(calcCellId(row, col));
                textView.setPadding(8, 2, 8, 2);
                if (0 == row) {
                    textView.setGravity(Gravity.CENTER);
                } else {
                    textView.setGravity(Gravity.RIGHT);
                }
                textView.setTextColor(Color.BLUE);
                textView.setBackgroundResource(R.drawable.textview_border);
//                textView.setLayoutParams(new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT));
                tr.addView(textView);
                mTableCells.put(textView.getId(), textView);
            }
            mTableLayout.addView(tr, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    private int calcCellId(int row, int col) {
        return (short) row << 16 | (short) col;
    }

    public void setValue(int row, int col, String value) {
        TextView textView = mTableCells.get(calcCellId(row, col));
        if (null != textView) {
            textView.setText(value);
        }
    }

    public void reset(int top, int left, int bottom, int right) {
        for (int row = top; row < bottom; row++) {
            for (int col = left; col < right; col++) {
                setValue(row, col, "");
            }
        }
    }
}
