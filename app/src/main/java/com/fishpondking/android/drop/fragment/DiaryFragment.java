package com.fishpondking.android.drop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.activity.DiaryContentActivity;
import com.fishpondking.android.drop.activity.WriteDiaryActivity;
import com.fishpondking.android.drop.engine.Diary;
import com.fishpondking.android.drop.engine.DiaryLab;
import com.fishpondking.android.drop.engine.SingletonDormitory;
import com.github.clans.fab.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Author: FishpondKing
 * Date: 2016/11/30:9:36
 * Email: song511653502@gmail.com
 * Description:
 */

public class DiaryFragment extends Fragment {

    public static final int DIARY_LIST_SIZE = 1;
    public static final int ONE_DIARY_DOWNLOAD_SUCCESS = 2;
    public static final int REQUEST_ADD_DIARY = 3;
    public static final int REQUEST_REFRESH_DIARY_LIST = 4;
    public static final int EACH_PAGE_DIARY_COUNT = 5;

    private DiaryLab mDiaryLab;
    private ArrayList<Diary> mDiaries;
    private DiaryAdapter mDiaryAdapter;
    private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Handler mHandler;
    private SingletonDormitory mSingletonDormitory;
    private int mLoadDiaryTotalCount = 0;
    private int mLoadNewDiaryCount = 0;
    private int mPreviousLoadDiaryCount = 0;
    private int mLastVisibleItem;
    private boolean mLoadFinish = false;

    private View mView;
    private Fragment mFragment;
    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private View mHeaderView;
    private View mFooterView;
    private FloatingActionButton mFloatingActionButtonWriteDiary;
    private TextView mTextViewFooter;

    public static DiaryFragment newInstance() {
        DiaryFragment diaryFragment = new DiaryFragment();
        return diaryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_diary, container, false);
        mFragment = DiaryFragment.this;

        mToolbar = (Toolbar) mView.findViewById(R.id.toolbar_diary);
        mSingletonDormitory = SingletonDormitory.getInstance();
        mToolbar.setTitle("欢迎来到" + mSingletonDormitory.getName());
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_diary);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //初始化mHandler
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DIARY_LIST_SIZE:
                        mLoadDiaryTotalCount = msg.arg1;
                        mPreviousLoadDiaryCount = msg.arg2;
                        if (mLoadDiaryTotalCount == 0) {
                            //提示“该宿舍还没有日志”
                        } else {
                            //提示“加载中”
                        }
                        mLoadNewDiaryCount = 0;
                        break;
                    case ONE_DIARY_DOWNLOAD_SUCCESS:
                        mLoadNewDiaryCount++;
                        mPreviousLoadDiaryCount++;
                        if (mLoadNewDiaryCount == EACH_PAGE_DIARY_COUNT
                                || mPreviousLoadDiaryCount == mLoadDiaryTotalCount) {
//                            mDiaryAdapter
//                                    .notifyItemRangeInserted(mPreviousLoadDiaryCount
//                                            - mLoadNewDiaryCount + 1, mLoadNewDiaryCount);
                            mDiaryAdapter.notifyDataSetChanged();
                            mTextViewFooter.setText("上拉加载更多");
                            if (mPreviousLoadDiaryCount == mLoadDiaryTotalCount){
                                mTextViewFooter.setText("已经没有更多日志");
                                mLoadFinish = true;
                            }
                            //终止SwipeRefreshLayout的刷新动画
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        break;
                    case REQUEST_REFRESH_DIARY_LIST:
                        mDiaryAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        };

        mDiaryLab = DiaryLab.get(getActivity());
        mDiaries = new ArrayList<Diary>();
        mDiaries = mDiaryLab.getDiaries(mHandler);
        mDiaryAdapter = new DiaryAdapter(mDiaries);
        mRecyclerView.setAdapter(mDiaryAdapter);

        //设置RecyclerView的Header和Footer
        setHeaderView(mRecyclerView);
        setFooterView(mRecyclerView);

        //配置上拉加载事件
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastVisibleItem + 1 == mDiaryAdapter.getItemCount()
                        && mLoadFinish == false){
                    //加载更多Diary
                    mDiaryLab.pullUpRefreshDiaryLab(mHandler);
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
                (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_layout_diary);
        mSwipeRefreshLayout.setProgressViewOffset(true, 50, 200);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue500);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新动画开始后回调此方法
                mLoadFinish = false;
                updateDiaryListUI(mHandler);
            }
        });

        //写日志
        mFloatingActionButtonWriteDiary =
                (FloatingActionButton) mView.findViewById(R.id.floating_action_button_write_diary);
        mFloatingActionButtonWriteDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteDiaryActivity.activityStart(getActivity(), mFragment);
            }
        });

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取日志列表
        //updateDiaryListUI(mHandler);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case WriteDiaryActivity.RESULT_REFRESH:
                updateDiaryListUI(mHandler);
                break;
            default:
                break;
        }
    }

    //自定义的ViewHolder
    private class DiaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int mPosition;

        private ImageView mImageViewPhoto;
        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private TextView mTextViewAuthor;

        public DiaryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            mImageViewPhoto = (ImageView) itemView.findViewById(R.id.image_view_diary_photo);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view_diary_title);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_diary_date);
            mTextViewAuthor = (TextView) itemView.findViewById(R.id.text_view_diary_author);

        }

        public void bindDiary(Diary diary, int position) {
            mPosition = position;
            Glide.with(getActivity())
                    .load(diary.getTopPhotoUrl())
                    .into(mImageViewPhoto);
            mTextViewTitle.setText(diary.getTitle());
            mTextViewDate.setText(mDateFormat.format(diary.getDate()));
            mTextViewAuthor.setText(diary.getAuthor());
        }

        @Override
        public void onClick(View v) {
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            DiaryContentActivity.activityStart(getActivity(), mPosition);
        }
    }


    private class DiaryAdapter extends RecyclerView.Adapter<DiaryViewHolder> {

        public static final int TYPE_HEADER = 0;
        public static final int TYPE_FOOTER = 1;
        public static final int TYPE_NORMAL = 2;

        private ArrayList<Diary> mDiaries;

        public DiaryAdapter(ArrayList<Diary> diaries) {
            mDiaries = diaries;
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
        public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if (mHeaderView != null && viewType == TYPE_HEADER) {
                return new DiaryViewHolder(mHeaderView);
            }
            if (mFooterView != null && viewType == TYPE_FOOTER) {
                return new DiaryViewHolder(mFooterView);
            }
            View layout = layoutInflater.inflate(R.layout.item_diary_recyclerview, parent, false);
            return new DiaryViewHolder(layout);
        }

        //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，HeaderView和FooterView就不用绑定了
        @Override
        public void onBindViewHolder(DiaryViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_NORMAL) {
                if (holder instanceof DiaryViewHolder) {
                    //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                    Diary diary = mDiaries.get(position - 1);
                    holder.bindDiary(diary, position - 1);
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
                return mDiaries.size();
            } else if (mHeaderView == null && mFooterView != null) {
                return mDiaries.size() + 1;
            } else if (mHeaderView != null && mFooterView == null) {
                return mDiaries.size() + 1;
            } else {
                return mDiaries.size() + 2;
            }
        }

    }

    private void updateDiaryListUI(Handler handler) {
        mDiaryLab = DiaryLab.get(getActivity());
        mDiaryLab.pullDownRefreshDiaryLab(handler);
    }

    private void setHeaderView(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_diary_header_view,
                view, false);
        mDiaryAdapter.setHeaderView(header);
    }

    private void setFooterView(RecyclerView view) {
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.item_diary_footer_view,
                view, false);
        mTextViewFooter = (TextView) footer.findViewById(R.id.text_view_diary_footer);
        mDiaryAdapter.setFooterView(footer);
    }

}
