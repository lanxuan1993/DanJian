package com.fw.dangjian.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fw.dangjian.R;
import com.fw.dangjian.adapter.DjeAdapter;
import com.fw.dangjian.base.BaseActivity;
import com.fw.dangjian.bean.DjeBean;
import com.fw.dangjian.mvpView.DJEMvpView;
import com.fw.dangjian.presenter.DJEPresenter;
import com.fw.dangjian.util.ToastUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class DJEActivity extends BaseActivity implements DJEMvpView{

    @BindView(R.id.left)
    RelativeLayout left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recyclerview)
    XRecyclerView nrecycler;
    @BindView(R.id.no_content)
    LinearLayout linearLayout_no_content;
    @BindView(R.id.tv_kong)
    TextView tv_kong;
    @BindView(R.id.no_net)
    LinearLayout linearLayout_no_net;

    private DjeAdapter mAdapter;
    private ArrayList<DjeBean.ResultBean.PageInfoBean.ListBean> lists;
    private DJEPresenter djePresenter;
    int page = 1;
    private int refreshTime = 0;


    @Override
    protected int fillView() {
        return R.layout.activity_dje;
    }

    @Override
    protected void initUi() {
        left.setVisibility(View.VISIBLE);
        tv_title.setText("支部活动");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        nrecycler.setLayoutManager(layoutManager);

        djePresenter = new DJEPresenter();
    }

    @Override
    protected void initData() {

        lists = new ArrayList<>();
        requestServer(page);

        nrecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        requestServer(page);
                    }

                }, 200);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        requestServer(page);
                    }
                }, 200);

            }
        });

        mAdapter = new DjeAdapter(lists,this);
        nrecycler.setAdapter(mAdapter);

        initAdapterClike();
    }

    private void requestServer(int page) {

        djePresenter.getDJe(page,this);
    }

    private void initAdapterClike() {

        mAdapter.setonItemClickLitener(new DjeAdapter.onItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(DJEActivity.this,WorkInfoActivity.class);
                intent.putExtra("news_id",lists.get(position-1).id);
                intent.putExtra("title","支部活动");
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left:
                finish();
                break;
        }
    }



    @Override
    public void onGetDataNext(DjeBean djeBean) {

        if (djeBean.result_code != null && djeBean.result_code.equals("200")) {
            if (djeBean.result.pageInfo.list.size() > 0) {
                switch (page) {
                    case 1:
                        lists.clear();
                        lists.addAll(djeBean.result.pageInfo.list);
                        break;
                    default:
                        lists.addAll(djeBean.result.pageInfo.list);
                        break;
                }

                linearLayout_no_content.setVisibility(View.GONE);
                linearLayout_no_net.setVisibility(View.GONE);
                nrecycler.setVisibility(View.VISIBLE);
                nrecycler.loadMoreComplete();
                nrecycler.refreshComplete();
                mAdapter.notifyDataSetChanged();

            } else {
                switch (page) {
                    case 1:
                        linearLayout_no_content.setVisibility(View.VISIBLE);
                        linearLayout_no_net.setVisibility(View.GONE);
                        nrecycler.setVisibility(View.GONE);
                        nrecycler.loadMoreComplete();
                        nrecycler.refreshComplete();
                        break;
                    default:
                        ToastUtils.showShort(act, "没有更多数据");
                        page--;
                        linearLayout_no_content.setVisibility(View.GONE);
                        linearLayout_no_net.setVisibility(View.GONE);
                        nrecycler.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                        nrecycler.loadMoreComplete();
                        nrecycler.refreshComplete();
                        break;
                }
            }
        } else {
            switch (page) {
                case 1:
                    linearLayout_no_content.setVisibility(View.GONE);
                    linearLayout_no_net.setVisibility(View.VISIBLE);
                    nrecycler.setVisibility(View.GONE);
                    nrecycler.loadMoreComplete();
                    nrecycler.refreshComplete();
                    break;
                default:
                    ToastUtils.showShort(act, "网络错误");
                    page--;
                    linearLayout_no_content.setVisibility(View.GONE);
                    linearLayout_no_net.setVisibility(View.GONE);
                    nrecycler.setVisibility(View.VISIBLE);
                    nrecycler.loadMoreComplete();
                    nrecycler.refreshComplete();
                    break;
            }
        }
    }

    @Override
    public void onGetDataError(Throwable e) {
        super.onGetDataError(e);
        nrecycler.loadMoreComplete();
        nrecycler.refreshComplete();
    }
}
