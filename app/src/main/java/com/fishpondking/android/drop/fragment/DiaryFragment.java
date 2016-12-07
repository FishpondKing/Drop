package com.fishpondking.android.drop.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.engine.Diary;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/11/30:9:36
 * Email: song511653502@gmail.com
 * Description:
 */

public class DiaryFragment extends Fragment {

    private View mView;
    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    public static DiaryFragment newInstance() {
        DiaryFragment diaryFragment = new DiaryFragment();
        return diaryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_diary, container, false);

        mToolbar = (Toolbar) mView.findViewById(R.id.toolbar_diary);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_diary);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //测试数据
        List<Diary> diaries = new ArrayList<Diary>();
        for (int i = 0; i < 5; i++) {
            diaries.add(new Diary());
        }
        mRecyclerView.setAdapter(new DiaryAdapter(diaries));

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setProgressViewOffset(true, 50, 200);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue500);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.grey200);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新动画开始后回调此方法
            }
        });

        return mView;
    }

    //自定义的ViewHolder
    private class DiaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageViewPhoto;
        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private TextView mTextViewAuthor;

        public DiaryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageViewPhoto = (ImageView) itemView.findViewById(R.id.image_view_diary_photo);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view_diary_title);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_diary_date);
            mTextViewAuthor = (TextView) itemView.findViewById(R.id.text_view_diary_author);

        }

        @Override
        public void onClick(View v) {

        }
    }


    private class DiaryAdapter extends RecyclerView.Adapter<DiaryViewHolder> {

        private List<Diary> mDiaries;

        public DiaryAdapter(List<Diary> diaries) {
            mDiaries = diaries;
        }

        @Override
        public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_diary_recyclerview, parent, false);
            return new DiaryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DiaryViewHolder holder, int position) {

            //获取测试图片
            Drawable drawable = getActivity().getResources().getDrawable(R.drawable.ic_diary_photo);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();

            Diary diary = mDiaries.get(position);
            holder.mImageViewPhoto.setImageBitmap(bitmap);
            holder.mTextViewTitle.setText("这里是标题");
            holder.mTextViewDate.setText("2011.11.11");
            holder.mTextViewAuthor.setText("赵无敌");
        }

        @Override
        public int getItemCount() {
            return mDiaries.size();
        }
    }
}
