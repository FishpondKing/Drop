package com.fishpondking.android.drop.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.activity.DiaryContentActivity;
import com.fishpondking.android.drop.engine.Diary;
import com.fishpondking.android.drop.engine.MoneyEvent;
import com.fishpondking.android.drop.engine.MoneyEventLab;
import com.fishpondking.android.drop.engine.SingletonDormitory;
import com.github.clans.fab.FloatingActionButton;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Author: FishpondKing
 * Date: 2016/11/3:17:13
 * Email: song511653502@gmail.com
 * Description: 主界面中“财务”选项卡中对应的界面
 */

public class MoneyFragment extends Fragment {

    public static final int MONEY_EVENT_LIST_SIZE = 31;
    public static final int ONE_MONEY_EVENT_DOWNLOAD_SUCCESS = 32;
    public static final int REQUEST_ADD_MONEY_EVENT = 33;
    public static final int REQUEST_REFRESH_MONEY_EVENT_LIST = 34;
    public static final int EACH_PAGE_MONEY_EVENT_COUNT = 1000;

    private View mView;
    private View mViewMoneyOutDialog;
    private View mViewMoneyInDialog;
    private View mHeaderView;
    private View mFooterView;
    private TextView mTextViewFooter;
    private Activity mActivity;
    private Fragment mFragment;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFloatingActionButtonMoneyOut;
    private FloatingActionButton mFloatingActionButtonMoneyIn;
    private AlertDialog mDialogMoneyOut;
    private AlertDialog mDialogMoneyIn;
    private EditText mEditTextMoneyOutReason;
    private EditText mEditTextMoneyOutCount;
    private EditText mEditTextMoneyInReason;
    private EditText mEditTextMoneyInCount;
    private TextView mTextViewMoneyRemain;
    private TextView mTextViewMonthMoneyOut;
    private TextView mTextViewMonthMoneyIn;

    private SingletonDormitory mSingletonDormitory;
    private MoneyEventLab mMoneyEventLab;
    private ArrayList<MoneyEvent> mMoneyEvents;
    private MoneyEventAdapter mMoneyEventAdapter;
    private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat mDecimalFormat =new DecimalFormat("#.00");
    private Handler mHandler;
    private int mLoadMoneyEventTotalCount = 0;
    private int mLoadNewMoneyEventCount = 0;
    private int mPreviousLoadMoneyEventCount = 0;
    private int mLastVisibleItem;
    private boolean mLoadFinish = false;

    public static MoneyFragment newInstance() {
        MoneyFragment moneyFragment = new MoneyFragment();
        return moneyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_money, container, false);
        mActivity = getActivity();
        mFragment = MoneyFragment.this;
        mSingletonDormitory = SingletonDormitory.getInstance();

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_money);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //初始化mHandler
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MONEY_EVENT_LIST_SIZE:
                        mLoadMoneyEventTotalCount = msg.arg1;
                        mPreviousLoadMoneyEventCount = msg.arg2;
                        if (mLoadMoneyEventTotalCount == 0) {
                            //提示“该宿舍还没有记账”
                        } else {
                            //提示“加载中”
                        }
                        mLoadNewMoneyEventCount = 0;
                        break;
                    case ONE_MONEY_EVENT_DOWNLOAD_SUCCESS:
                        mLoadNewMoneyEventCount++;
                        mPreviousLoadMoneyEventCount++;
                        if (mLoadNewMoneyEventCount == EACH_PAGE_MONEY_EVENT_COUNT
                                || mPreviousLoadMoneyEventCount == mLoadMoneyEventTotalCount) {
//                        if (mPreviousLoadMoneyEventCount == mLoadMoneyEventTotalCount) {
                            mMoneyEventAdapter.notifyDataSetChanged();
//                            mTextViewFooter.setText("上拉加载更多");
                            if (mPreviousLoadMoneyEventCount == mLoadMoneyEventTotalCount) {
                                mTextViewFooter.setText("已经没有更多账目");
                                mLoadFinish = true;
                            }
                            //计算余额，本月支出，本月收入
                            calculateMoneyRemain();
                            calculateMonthMoneyOut();
                            calculateMonthMoneyIn();
                            //终止SwipeRefreshLayout的刷新动画
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        break;
                    case REQUEST_REFRESH_MONEY_EVENT_LIST:
                        mMoneyEventAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        };

        mMoneyEventLab = MoneyEventLab.get(getActivity());
        mMoneyEvents = new ArrayList<MoneyEvent>();
        mMoneyEvents = mMoneyEventLab.getMoneyEvents(mHandler);
        mMoneyEventAdapter = new MoneyEventAdapter(mMoneyEvents);
        mRecyclerView.setAdapter(mMoneyEventAdapter);

        //设置RecyclerView的Header和Footer
        setHeaderView(mRecyclerView);
        setFooterView(mRecyclerView);

        //配置上拉加载事件
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastVisibleItem + 1 == mMoneyEventAdapter.getItemCount()
                        && mLoadFinish == false) {
                    //加载更多Diary
                    mMoneyEventLab.pullUpRefreshMoneyEventLab(mHandler);
                    mTextViewFooter.setText("加载中...");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });

        //配置下拉刷新
        mSwipeRefreshLayout =
                (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_layout_money);
        mSwipeRefreshLayout.setProgressViewOffset(true, 50, 200);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue500);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新动画开始后回调此方法
                mLoadFinish = false;
                mMoneyEventLab.pullDownRefreshMoneyEventLab(mHandler);
            }
        });

        //创建支出对话框
        mViewMoneyOutDialog = inflater.inflate(R.layout.dialog_money_event_out, null);
        mDialogMoneyOut = new AlertDialog.Builder(mActivity).setTitle("支出")
                .setIcon(R.drawable.ic_bottom_bar_money)
                .setView(mViewMoneyOutDialog)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMoneyOutEventToDB();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialogMoneyOut.cancel();
                    }
                }).create();

        mEditTextMoneyOutReason = (EditText) mViewMoneyOutDialog.findViewById(R.id.edit_text_dialog_money_out_event);
        mEditTextMoneyOutCount = (EditText) mViewMoneyOutDialog.findViewById(R.id.edit_text_dialog_money_event_out_count);

        //创建收入对话框
        mViewMoneyInDialog = inflater.inflate(R.layout.dialog_money_event_in, null);
        mDialogMoneyIn = new AlertDialog.Builder(mActivity).setTitle("收入")
                .setIcon(R.drawable.ic_bottom_bar_money)
                .setView(mViewMoneyInDialog)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMoneyInEventToDB();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialogMoneyIn.cancel();
                    }
                }).create();

        mEditTextMoneyInReason = (EditText) mViewMoneyInDialog.findViewById(R.id.edit_text_dialog_money_in_event);
        mEditTextMoneyInCount = (EditText) mViewMoneyInDialog.findViewById(R.id.edit_text_dialog_money_event_in_count);

        //支出按钮
        mFloatingActionButtonMoneyOut = (FloatingActionButton) mView.findViewById(R.id.floating_action_button_money_out);
        mFloatingActionButtonMoneyOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogMoneyOut.show();
            }
        });

        //收入按钮
        mFloatingActionButtonMoneyIn = (FloatingActionButton) mView.findViewById(R.id.floating_action_button_money_in);
        mFloatingActionButtonMoneyIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogMoneyIn.show();
            }
        });

        return mView;

    }

    //自定义的ViewHolder
    private class MoneyEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int mPosition;

        private TextView mTextViewMoneyEvent;
        private TextView mTextViewDate;
        private TextView mTextViewMoneyEventCount;

        public MoneyEventViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            mTextViewMoneyEvent = (TextView) itemView.findViewById(R.id.text_view_money_dormitory_event);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_money_dormitory_event_time);
            mTextViewMoneyEventCount = (TextView) itemView.findViewById(R.id.text_view_money_dormitory_event_money);
        }

        public void bindMoneyEvent(MoneyEvent moneyEvent) {
            mTextViewDate.setText(mDateFormat.format(moneyEvent.getDate()));
            mTextViewMoneyEvent.setText(moneyEvent.getReason());
            //分别处理支出和收入
            if (moneyEvent.getEventType() == MoneyEvent.MONEY_OUT_EVENT) {
                mTextViewMoneyEventCount.setText("-" + String.valueOf(moneyEvent.getCount()));
                mTextViewMoneyEventCount.setTextColor(mActivity.getResources().getColor(R.color.red500));
            } else if (moneyEvent.getEventType() == MoneyEvent.MONEY_IN_EVENT) {
                mTextViewMoneyEventCount.setText("+" + String.valueOf(moneyEvent.getCount()));
                mTextViewMoneyEventCount.setTextColor(mActivity.getResources().getColor(R.color.green500));
            }

        }

        @Override
        public void onClick(View v) {

        }
    }


    private class MoneyEventAdapter extends RecyclerView.Adapter<MoneyFragment.MoneyEventViewHolder> {

        public static final int TYPE_HEADER = 0;
        public static final int TYPE_FOOTER = 1;
        public static final int TYPE_NORMAL = 2;

        private ArrayList<MoneyEvent> mMoneyEvents;

        public MoneyEventAdapter(ArrayList<MoneyEvent> moneyEvents) {
            mMoneyEvents = moneyEvents;
        }

        //HeaderView和FooterView的get和set函数
        public View getHeaderView() {
            return mHeaderView;
        }

        public void setHeaderView(View headerView) {
            mHeaderView = headerView;
            notifyItemInserted(0);
        }

        public View getFooterView() {
            return mFooterView;
        }

        public void setFooterView(View footerView) {
            mFooterView = footerView;
            notifyItemInserted(getItemCount() - 1);
        }


        //这个方法是加入Header和Footer的关键，通过判断item的类型，从而绑定不同的view
        @Override
        public int getItemViewType(int position) {
            if (mHeaderView == null && mFooterView == null) {
                return TYPE_NORMAL;
            }
            if (position == 0) {
                //第一个item应该加载Header
                return TYPE_HEADER;
            }
            if (position == getItemCount() - 1) {
                //最后一个,应该加载Footer
                return TYPE_FOOTER;
            }
            return TYPE_NORMAL;
        }

        //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
        @Override
        public MoneyFragment.MoneyEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if (mHeaderView != null && viewType == TYPE_HEADER) {
                return new MoneyFragment.MoneyEventViewHolder(mHeaderView);
            }
            if (mFooterView != null && viewType == TYPE_FOOTER) {
                return new MoneyFragment.MoneyEventViewHolder(mFooterView);
            }
            View layout = layoutInflater.inflate(R.layout.item_money_recyclerview, parent, false);

            return new MoneyFragment.MoneyEventViewHolder(layout);
        }

        //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，HeaderView和FooterView就不用绑定了
        @Override
        public void onBindViewHolder(MoneyFragment.MoneyEventViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_NORMAL) {
                if (holder instanceof MoneyFragment.MoneyEventViewHolder) {
                    //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                    MoneyEvent moneyEvent = mMoneyEvents.get(position - 1);
                    holder.bindMoneyEvent(moneyEvent);
                    return;
                }
                return;
            } else if (getItemViewType(position) == TYPE_HEADER) {
                return;
            } else if (getItemViewType(position) == TYPE_FOOTER) {
                return;
            }
        }

        //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
        @Override
        public int getItemCount() {
            if (mHeaderView == null && mFooterView == null) {
                return mMoneyEvents.size();
            } else if (mHeaderView == null && mFooterView != null) {
                return mMoneyEvents.size() + 1;
            } else if (mHeaderView != null && mFooterView == null) {
                return mMoneyEvents.size() + 1;
            } else {
                return mMoneyEvents.size() + 2;
            }
        }

    }

    //上传支出到数据库
    private void sendMoneyOutEventToDB() {
        if (mEditTextMoneyOutReason.getText().length() > 0 && mEditTextMoneyOutCount.getText().length() > 0
                && isDoubleTwoDigitsAfter(mEditTextMoneyOutCount.getText().toString())) {

            String moneyOutReason = mEditTextMoneyOutReason.getText().toString();
            String moneyOutCount = mEditTextMoneyOutCount.getText().toString();
            AVObject moneyOutEvent = new AVObject("MoneyEvent");
            moneyOutEvent.put("dormitoryId", mSingletonDormitory.getId());
            moneyOutEvent.put("eventType", MoneyEvent.MONEY_OUT_EVENT);
            moneyOutEvent.put("reason", moneyOutReason);
            moneyOutEvent.put("eventCount", moneyOutCount);
            moneyOutEvent.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        //刷新页面
                        mMoneyEventLab.pullDownRefreshMoneyEventLab(mHandler);
                    } else {
                        //网络连接失败
                        Toast.makeText(mActivity, R.string.link_network_fail,
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });

        } else {
            mDialogMoneyOut.cancel();
            Toast.makeText(mActivity, R.string.please_input_valid_data,
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //上传收入到数据库
    private void sendMoneyInEventToDB() {
        if (mEditTextMoneyInReason.getText().length() > 0 && mEditTextMoneyInCount.getText().length() > 0
                && isDoubleTwoDigitsAfter(mEditTextMoneyInCount.getText().toString())) {

            String moneyInReason = mEditTextMoneyInReason.getText().toString();
            String moneyInCount = mEditTextMoneyInCount.getText().toString();
            AVObject moneyInEvent = new AVObject("MoneyEvent");
            moneyInEvent.put("dormitoryId", mSingletonDormitory.getId());
            moneyInEvent.put("eventType", MoneyEvent.MONEY_IN_EVENT);
            moneyInEvent.put("reason", moneyInReason);
            moneyInEvent.put("eventCount", moneyInCount);
            moneyInEvent.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        //刷新页面
                        mMoneyEventLab.pullDownRefreshMoneyEventLab(mHandler);
                    } else {
                        //网络连接失败
                        Toast.makeText(mActivity, R.string.link_network_fail,
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });

        } else {
            mDialogMoneyIn.cancel();
            Toast.makeText(mActivity, R.string.please_input_valid_data,
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //判断字符串是否为double保留后两位
    public static boolean isDoubleTwoDigitsAfter(String str) {
        Pattern pattern = Pattern.compile("^([\\d]+)|([\\d]+[.][\\d]{1,2})$");
        return pattern.matcher(str).matches();
    }

    private void setHeaderView(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_money_header,
                view, false);
        mTextViewMoneyRemain = (TextView) header.findViewById(R.id.text_view_money_dormitory_remain);
        mTextViewMonthMoneyOut = (TextView) header.findViewById(R.id.text_view_month_money_out);
        mTextViewMonthMoneyIn = (TextView) header.findViewById(R.id.text_view_month_money_in);
        mMoneyEventAdapter.setHeaderView(header);
    }

    private void setFooterView(RecyclerView view) {
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.item_money_footer,
                view, false);
        mTextViewFooter = (TextView) footer.findViewById(R.id.text_view_money_footer);
        mMoneyEventAdapter.setFooterView(footer);
    }

    //计算舍费余额
    private void calculateMoneyRemain() {
        double moneyRemain = 0;
        Iterator it = mMoneyEvents.iterator();
        while (it.hasNext()){
            MoneyEvent moneyEvent = (MoneyEvent) it.next();
            if (moneyEvent.getEventType() == MoneyEvent.MONEY_OUT_EVENT){
                moneyRemain -= moneyEvent.getCount();
            }else if (moneyEvent.getEventType() == MoneyEvent.MONEY_IN_EVENT){
                moneyRemain += moneyEvent.getCount();
            }
        }
        mTextViewMoneyRemain.setText(String.valueOf(mDecimalFormat.format(moneyRemain)));
    }

    //计算本月支出
    private void calculateMonthMoneyOut(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Date nowDate = new Date();
        String nowDateString = dateFormat.format(nowDate);
        double monthMoneyOut = 0;
        Iterator it = mMoneyEvents.iterator();
        while (it.hasNext()){
            MoneyEvent moneyEvent = (MoneyEvent) it.next();
            if(dateFormat.format(moneyEvent.getDate()).equals(nowDateString)){
                if (moneyEvent.getEventType() == MoneyEvent.MONEY_OUT_EVENT){
                    monthMoneyOut -= moneyEvent.getCount();
                }
            }else {
                break;
            }
        }
        mTextViewMonthMoneyOut.setText(String.valueOf(mDecimalFormat.format(monthMoneyOut)));
    }

    //计算本月收入
    private void calculateMonthMoneyIn(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Date nowDate = new Date();
        String nowDateString = dateFormat.format(nowDate);
        double monthMoneyIn = 0;
        Iterator it = mMoneyEvents.iterator();
        while (it.hasNext()){
            MoneyEvent moneyEvent = (MoneyEvent) it.next();
            if(dateFormat.format(moneyEvent.getDate()).equals(nowDateString)){
                if (moneyEvent.getEventType() == MoneyEvent.MONEY_IN_EVENT){
                    monthMoneyIn += moneyEvent.getCount();
                }
            }else {
                break;
            }
        }
        mTextViewMonthMoneyIn.setText("+"+String.valueOf(mDecimalFormat.format(monthMoneyIn)));
    }
}
