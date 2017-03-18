package com.fishpondking.android.drop.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.activity.PhotoPagerActivity;
import com.fishpondking.android.drop.engine.PhotoWallPhoto;
import com.fishpondking.android.drop.engine.PhotoWallPhotoLab;
import com.fishpondking.android.drop.engine.SingletonDormitory;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.ui.GridSpacingItemDecoration;
import com.fishpondking.android.drop.utils.GlideImageLoader;
import com.github.clans.fab.FloatingActionButton;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.fishpondking.android.drop.activity.WriteDiaryActivity
        .PERMISSIONS_REQUEST_READ_CONTACTS;

/**
 * Author: FishpondKing
 * Date: 2017/3/5:9:51
 * Email: song511653502@gmail.com
 * Description:
 */

public class PhotoWallFragment extends Fragment {

    public static final int PHOTO_LIST_SIZE = 21;
    public static final int ONE_PHOTO_DOWNLOAD_SUCCESS = 22;
    public static final int REQUEST_ADD_PHOTO = 23;
    public static final int REQUEST_REFRESH_PHOTO_WALL = 24;
    public static final int NO_PHOTO = 25;
    public static final int EACH_PAGE_PHOTO_COUNT = 15;

    private View mView;
    private Activity mActivity;
    private Fragment mFragment;
    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private FloatingActionButton mFloatingActionButtonSubmitPhoto;
    private ProgressDialog mProgressDialog;
    private TextView mTextViewFooter;
    private View mHeaderView;
    private View mFooterView;

    private Resources mResources;
    private Handler mHandler;
    private int mLoadPhotoCount;
    private int mLoadNewPhotoCount;
    private int mPreviousLoadPhotoCount;
    private PhotoAdapter mPhotoAdapter;
    private PhotoWallPhotoLab mPhotoWallPhotoLab;
    private ArrayList<PhotoWallPhoto> mPhotos;
    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;
    private ArrayList<String> photoPathList = new ArrayList<>();
    private int mSelectPhotoCount;
    private int mSubmitSuccessPhotoCount;
    private int mSubmittingPhotoCount;
    private DecimalFormat df = new DecimalFormat("000000");
    private int mOldPhotoCount;
    private int mOldUserPhotoCount;
    private int mOldDormitoryPhotoCount;
    private SingletonUser mSingletonUser;
    private SingletonDormitory mSingletonDormitory;
    private int selectedPhotoCount;
    private String strProgressDialogShow;
    private String photoId;
    private String photoName;
    private int mLastVisibleItem;
    private boolean mLoadFinish = false;

    public static PhotoWallFragment newInstance() {
        PhotoWallFragment photoWallFragment = new PhotoWallFragment();
        return photoWallFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_photo_wall, container, false);
        mActivity = getActivity();
        mResources = mActivity.getResources();

        mSingletonUser = SingletonUser.getInstance();
        mSingletonDormitory = SingletonDormitory.getInstance();

        mToolbar = (Toolbar) mView.findViewById(R.id.toolbar_photo_wall);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_photo_wall);

        //初始化mHandler
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case NO_PHOTO:
                        mTextViewFooter.setText(mResources.getString(R.string.no_photo));
                        break;
                    case PHOTO_LIST_SIZE:
                        mLoadPhotoCount = msg.arg1;
                        mPreviousLoadPhotoCount = msg.arg2;
                        mLoadNewPhotoCount = 0;
                        break;
                    case ONE_PHOTO_DOWNLOAD_SUCCESS:
                        mLoadNewPhotoCount++;
                        mPreviousLoadPhotoCount++;
                        if (mLoadNewPhotoCount == EACH_PAGE_PHOTO_COUNT
                                || mPreviousLoadPhotoCount == mLoadPhotoCount) {
                            if (mPreviousLoadPhotoCount == EACH_PAGE_PHOTO_COUNT) {
                                mPhotoAdapter.notifyDataSetChanged();
                            } else {
                                mPhotoAdapter
                                        .notifyItemRangeInserted(mPreviousLoadPhotoCount
                                                - mLoadNewPhotoCount + 1, mLoadNewPhotoCount);
                            }
                            mTextViewFooter.setText(mResources.getString(R.string.pull_up_for_more));
                            if (mPreviousLoadPhotoCount == mLoadPhotoCount) {
                                mTextViewFooter
                                        .setText(mResources.getString(R.string.scrolled_to_bottom));
                                mLoadFinish = true;
                            }
                            //终止SwipeRefreshLayout的刷新动画
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        break;
                    case REQUEST_REFRESH_PHOTO_WALL:
                        mPhotoAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        };

        mPhotoWallPhotoLab = PhotoWallPhotoLab.get(getActivity());
        mPhotos = new ArrayList<PhotoWallPhoto>();
        mPhotos = mPhotoWallPhotoLab.getPhotos(mHandler);
        mPhotoAdapter = new PhotoAdapter(mPhotos);
        mRecyclerView.setAdapter(mPhotoAdapter);
        mRecyclerView.setHasFixedSize(true);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        //解决item左右切换的问题
        mStaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        //mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3,10,true));
        //mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));

        //解决Recycler滑到顶部出现的item移动问题
        mRecyclerView.setPadding(0, 0, 0, 0);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mStaggeredGridLayoutManager.invalidateSpanAssignments();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastVisibleItem + 1 == mPhotoAdapter.getItemCount()
                        && mLoadFinish == false) {
                    //加载更多Photo
                    mPhotoWallPhotoLab.pullUpRefreshPhotoWallPhotoLab(mHandler);
                    if (mLoadFinish == false){
                        mTextViewFooter.setText("加载中...");
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem =
                        getMax(mStaggeredGridLayoutManager
                                .findLastCompletelyVisibleItemPositions(new int[3]));
            }
        });

        //为RecyclerView设置Header和Footer
        setHeader(mRecyclerView);
        setFooter(mRecyclerView);

        mSwipeRefreshLayout =
                (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh_layout_photo_wall);
        mSwipeRefreshLayout.setProgressViewOffset(true, 50, 200);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue500);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新动画开始后回调此方法
                mLoadFinish = false;
                mPhotoWallPhotoLab.pullDownRefreshPhotoWallPhotoLab(mHandler);
            }
        });

        //传照片
        mFloatingActionButtonSubmitPhoto =
                (FloatingActionButton) mView.findViewById(R.id.floating_action_button_submit_photo);
        mFloatingActionButtonSubmitPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传照片
                chooseAndSubmitPhotos();
            }
        });

        //配置进度条
        strProgressDialogShow = mActivity.getResources().getString(R.string.photo_submitting);
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(String.format(strProgressDialogShow, 0, 0));

        return mView;
    }

    private class PhotoWallHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int mPosition;

        private ImageView mImageView;
        private TextView mTextView;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            mImageView = (ImageView) itemView.findViewById(R.id.image_view_photo_wall_photo);
        }

        public void bindPhoto(PhotoWallPhoto photoWallPhoto, int position) {
            mPosition = position;

            Glide.with(mActivity)
                    .load(photoWallPhoto.getPhotoUrl())
                    .placeholder(R.drawable.ic_bitmap)
                    .thumbnail(0.1f)
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            PhotoPagerActivity.activityStart(mActivity, mPosition);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

        public static final int TYPE_HEADER = 0;
        public static final int TYPE_FOOTER = 1;
        public static final int TYPE_NORMAL = 2;

        private ArrayList<PhotoWallPhoto> mPhotoWallPhotos;

        public PhotoAdapter(ArrayList<PhotoWallPhoto> photoWallPhotos) {
            mPhotoWallPhotos = photoWallPhotos;
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
            Log.d("setFooterView", "" + getItemCount());
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

        //获取Header和Footer的位置
        private boolean isHeaderViewPos(int position) {
            return position == 0;
        }

        private boolean isFooterViewPos(int position) {
            return position > mPhotos.size();
        }


        //为Header和Footer设置特殊宽度
        @Override
        public void onViewAttachedToWindow(PhotoViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            int position = holder.getLayoutPosition();
            if (isHeaderViewPos(position) || isFooterViewPos(position)) {
                Log.d("AttachedWindow函数", "" + position);
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

                if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                    StaggeredGridLayoutManager.LayoutParams p =
                            (StaggeredGridLayoutManager.LayoutParams) lp;

                    p.setFullSpan(true);
                }
            }
        }

        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if (mHeaderView != null && viewType == TYPE_HEADER) {
                return new PhotoViewHolder(mHeaderView);
            }
            if (mFooterView != null && viewType == TYPE_FOOTER) {
                return new PhotoViewHolder(mFooterView);
            }
            View view = layoutInflater.inflate(R.layout.item_photo_wall_recyclerview, parent
                    , false);
            return new PhotoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_NORMAL) {
                if (holder instanceof PhotoViewHolder) {
                    //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                    PhotoWallPhoto photoWallPhoto = mPhotos.get(position - 1);
                    holder.bindPhoto(photoWallPhoto, position - 1);
                    return;
                }
                return;
            } else if (getItemViewType(position) == TYPE_HEADER) {
                return;
            } else if (getItemViewType(position) == TYPE_FOOTER) {
                return;
            }
        }

        @Override
        public int getItemCount() {
            if (mHeaderView == null && mFooterView == null) {
                return mPhotos.size();
            } else if (mHeaderView == null && mFooterView != null) {
                return mPhotos.size() + 1;
            } else if (mHeaderView != null && mFooterView == null) {
                return mPhotos.size() + 1;
            } else {
                return mPhotos.size() + 2;
            }
        }
    }

    private void chooseAndSubmitPhotos() {
        //初始化变量
        photoPathList.clear();
        selectedPhotoCount = 0;
        mSubmitSuccessPhotoCount = 0;
        mSubmittingPhotoCount = 0;
        //配置图片选择器监听接口
        initGallery();
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .pathList(photoPathList)                         // 记录已选的图片
                .multiSelect(true, 9)                  // 配置是否多选的同时 配置多选数量
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Drop/Gallery/PhotoWall")     // 图片存放路径
                .build();
        //进行权限配置
        initPermissions();
    }

    //图片选择器监听接口
    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List<String> photoList) {

                for (String s : photoList) {
                    photoPathList.add(s);
                }

                //获取已选照片的数量
                mSelectPhotoCount = photoList.size();

                //获取photoId
                AVQuery<AVObject> query = new AVQuery<>("UtilData");
                query.whereEqualTo("scope", "all");
                query.getFirstInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (e == null) {
                            //获取已存在的照片数量
                            mOldPhotoCount = avObject.getInt("photoCount");
                            try {
                                submitPhoto(photoPathList);
                            } catch (FileNotFoundException e1) {
                                //失败，无法在本地找到该照片
                                Toast.makeText(mActivity, R.string.cant_find_local_photo,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            // 失败，请检查网络环境以及 SDK 配置是否正确
                            Toast.makeText(mActivity, R.string.submit_diary_top_photo_wrong,
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onError() {

            }
        };
    }

    //授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //需要授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //拒绝过了
                // 提示用户如果想要正常使用，要手动去设置中授权。
                Toast.makeText(mActivity, "请在 设置-应用管理 中开启此应用的储存授权。",
                        Toast.LENGTH_SHORT).show();
            } else {
                //进行授权
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            //不需要授权
            // 进行正常操作
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(mActivity);
        }
    }

    //此函数负责上传照片，并且递归调用
    private void submitPhoto(final ArrayList<String> photoPathList) throws FileNotFoundException {
        String photoPath;
        if (!photoPathList.isEmpty()) {
            //不断地读取数组中第一章照片并上传remove(0)
            photoPath = photoPathList.get(0);
            mOldPhotoCount++;
            photoId = df.format(mOldPhotoCount);

            //取得该图片的后缀名
            String[] location = photoPath.split("\\.");
            String suffix = location[location.length - 1];
            photoName = "photoWallPhoto" + photoId + "." + suffix;

            //上传照片
            AVFile AVPhoto = AVFile.withAbsoluteLocalPath(photoName, photoPath);
            mSubmittingPhotoCount++;
            mProgressDialog.setMessage(String.format(strProgressDialogShow, mSubmittingPhotoCount,
                    mSelectPhotoCount));
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            AVPhoto.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        //成功
                        mSubmitSuccessPhotoCount++;
                        updateUserPhotoCountToDB();
                        updateDormitoryPhotoCountToDB();

                        //保存照片其他信息
                        AVObject photo = new AVObject("Photo");
                        photo.put("photoId", photoId);
                        photo.put("photoName", photoName);
                        photo.put("authorId", mSingletonUser.getId());
                        photo.put("author", mSingletonUser.getUserName());
                        photo.put("dormitoryId", mSingletonDormitory.getId());
                        photo.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                photoPathList.remove(0);
                                updateUtilDataPhotoCountToDB();
                                try {
                                    submitPhoto(photoPathList);
                                } catch (FileNotFoundException e1) {
                                    //失败，无法在本地找到DiaryTopPhoto
                                    Toast.makeText(mActivity, R.string.cant_find_local_photo,
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });

                    } else {
                        // 失败，请检查网络环境以及 SDK 配置是否正确
                        Toast.makeText(mActivity, R.string.submit_diary_top_photo_wrong,
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {

                }
            });

        } else {
            //所有照片上传完毕，关闭进度条，提示上传成功，刷新照片墙
            mProgressDialog.cancel();
            Toast.makeText(mActivity, R.string.submit_photo_success,
                    Toast.LENGTH_SHORT)
                    .show();
            //刷新照片墙
            mPhotoWallPhotoLab.pullDownRefreshPhotoWallPhotoLab(mHandler);
            return;
        }

    }

    //更新UtilData表
    private void updateUtilDataPhotoCountToDB() {
        AVQuery<AVObject> query = new AVQuery<>("UtilData");
        query.whereEqualTo("scope", "all");
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                avObject.put("photoCount", mOldDormitoryPhotoCount);
                avObject.saveInBackground();
            }
        });
    }

    //更新_User表photoCount
    private void updateUserPhotoCountToDB() {
        mOldUserPhotoCount = AVUser.getCurrentUser().getInt("photoCount");
        mOldUserPhotoCount++;
        AVUser.getCurrentUser().put("photoCount", mOldUserPhotoCount);
        AVUser.getCurrentUser().saveInBackground();
    }

    //更新Dormitory表photoCount
    private void updateDormitoryPhotoCountToDB() {
        AVQuery<AVObject> query = new AVQuery<>("Dormitory");
        query.whereEqualTo("dormitoryId", mSingletonDormitory.getId());
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                mOldDormitoryPhotoCount = avObject.getInt("photoCount");
                mOldDormitoryPhotoCount++;
                avObject.put("photoCount", mOldDormitoryPhotoCount);
                avObject.saveInBackground();
            }
        });
    }

    //为RecyclerView设置Header
    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_photo_wall_header,
                view, false);
        mPhotoAdapter.setHeaderView(header);
    }

    //为RecyclerView设置Footer
    private void setFooter(RecyclerView view) {
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.item_photo_wall_footer,
                view, false);
        mTextViewFooter = (TextView) footer.findViewById(R.id.text_view_photo_wall_footer);
        mPhotoAdapter.setFooterView(footer);
    }

    //获取int数组中的最大值
    private int getMax(int[] arr) {
        int max = arr[0];

        for (int x = 1; x < arr.length; x++) {
            if (arr[x] > max)
                max = arr[x];

        }
        return max;
    }
}
