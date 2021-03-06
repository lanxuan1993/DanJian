package com.fw.dangjian.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fw.dangjian.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qhc on 2017/9/22.
 */
public abstract class ResultDialog extends Dialog {

    public Context context;
    @BindView(R.id.tv_right_count)
    TextView tv_right_count;
    @BindView(R.id.tv_wrong_count)
    TextView tv_wrong_count;

    @BindView(R.id.sure)
    TextView sure;
    String right;
    String wrong;

    public ResultDialog(Context context, int themeResId,String right,String wrong) {
        super(context,themeResId);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.right = right;
        this.wrong = wrong;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_result);
        ButterKnife.bind(this);

        tv_right_count.setText(right);
        tv_wrong_count.setText(wrong);
    }

    @OnClick(R.id.sure)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sure:
                confirm();
                dismiss();
                break;
        }
    }
    public abstract void confirm();

}
