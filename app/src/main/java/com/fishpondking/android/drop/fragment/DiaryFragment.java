package com.fishpondking.android.drop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2016/11/30:9:36
 * Email: song511653502@gmail.com
 * Description:
 */

public class DiaryFragment extends Fragment{

    private View mView;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    public static DiaryFragment newInstance(){
        DiaryFragment diaryFragment = new DiaryFragment();
        return diaryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_diary, container, false);

        mToolbar = (Toolbar) mView.findViewById(R.id.toolbar_diary);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_diary);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return mView;
    }

    //自定义的ViewHolder
    private class DiaryViewHolder extends RecyclerView.ViewHolder{

        private ImageView mImageViewPhoto;
        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private TextView mTextViewAuthor;

        public DiaryViewHolder(View itemView){
            super(itemView);
            mImageViewPhoto = (ImageView) itemView.findViewById(R.id.image_view_diary_photo);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view_diary_title);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_diary_date);
            mTextViewAuthor = (TextView) itemView.findViewById(R.id.text_view_diary_author);

        }
    }


    private class DiaryAdapter extends RecyclerView.Adapter<DiaryViewHolder>{

        private List<Diary> mDiaries;

        public DiaryAdapter(List<Diary> diaries){
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
            Diary diary = mDiaries.get(position);
            holder.mImageViewPhoto.setImageBitmap();
            holder.mTextViewTitle.setText(diary.getTitle());
            holder.mTextViewDate.setText(diary.getDate().toString());
            holder.mTextViewAuthor.setText(diary.getAuthor());
        }

        @Override
        public int getItemCount() {
            return mDiaries.size();
        }
    }
}
