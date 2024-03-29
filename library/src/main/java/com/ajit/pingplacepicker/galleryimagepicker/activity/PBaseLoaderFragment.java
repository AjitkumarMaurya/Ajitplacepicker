package com.ajit.pingplacepicker.galleryimagepicker.activity;

import static com.ajit.pingplacepicker.galleryimagepicker.ImagePicker.REQ_CAMERA;
import static com.ajit.pingplacepicker.galleryimagepicker.ImagePicker.REQ_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ajit.pingplacepicker.galleryimagepicker.ImagePicker;
import com.ajit.pingplacepicker.R;
import com.ajit.pingplacepicker.galleryimagepicker.bean.ImageItem;
import com.ajit.pingplacepicker.galleryimagepicker.bean.ImageSet;
import com.ajit.pingplacepicker.galleryimagepicker.bean.PickerItemDisableCode;
import com.ajit.pingplacepicker.galleryimagepicker.bean.selectconfig.BaseSelectConfig;
import com.ajit.pingplacepicker.galleryimagepicker.data.ICameraExecutor;
import com.ajit.pingplacepicker.galleryimagepicker.data.MediaItemsDataSource;
import com.ajit.pingplacepicker.galleryimagepicker.data.MediaSetsDataSource;
import com.ajit.pingplacepicker.galleryimagepicker.data.OnImagePickCompleteListener;
import com.ajit.pingplacepicker.galleryimagepicker.data.ProgressSceneEnum;
import com.ajit.pingplacepicker.galleryimagepicker.presenter.IPickerPresenter;
import com.ajit.pingplacepicker.galleryimagepicker.utils.PPermissionUtils;
import com.ajit.pingplacepicker.galleryimagepicker.utils.PStatusBarUtil;
import com.ajit.pingplacepicker.galleryimagepicker.views.PickerUiConfig;
import com.ajit.pingplacepicker.galleryimagepicker.views.PickerUiProvider;
import com.ajit.pingplacepicker.galleryimagepicker.views.base.PickerControllerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Description: 选择器加载基类，主要处理媒体文件的加载和权限管理
 * <p>
 * Author: peixing.yang
 * Date: 2019/2/21
 * 使用文档 ：https://github.com/yangpeixing/YImagePicker/wiki/Documentation_3.x
 */
public abstract class PBaseLoaderFragment extends Fragment implements ICameraExecutor {
    //选中图片列表
    protected ArrayList<ImageItem> selectList = new ArrayList<>();

    @NonNull
    protected abstract BaseSelectConfig getSelectConfig();


    @NonNull
    protected abstract IPickerPresenter getPresenter();


    @NonNull
    protected abstract PickerUiConfig getUiConfig();


    protected abstract void notifyPickerComplete();


    protected abstract void toggleFolderList();


    protected abstract void intentPreview(boolean isClickItem, int index);


    protected abstract void loadMediaSetsComplete(@Nullable List<ImageSet> imageSetList);


    protected abstract void loadMediaItemsComplete(@Nullable ImageSet set);


    protected abstract void refreshAllVideoSet(@Nullable ImageSet allVideoSet);


    public boolean onBackPressed() {
        return false;
    }



    protected void notifyOnSingleImagePickComplete(ImageItem imageItem) {
        selectList.clear();
        selectList.add(imageItem);
        notifyPickerComplete();
    }


    private boolean isOverMaxCount() {
        if (selectList.size() >= getSelectConfig().getMaxCount()) {
            getPresenter().overMaxCountTip(getContext(), getSelectConfig().getMaxCount());
            return true;
        }
        return false;
    }

    protected void checkTakePhotoOrVideo() {
        if (getSelectConfig().isShowVideo() && !getSelectConfig().isShowImage()) {
            takeVideo();
        } else {
            takePhoto();
        }
    }


    @Override
    public void takePhoto() {
        if (getActivity() == null || isOverMaxCount()) {
            return;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQ_CAMERA);
        } else {
            ImagePicker.takePhoto(getActivity(), null,
                    true, new OnImagePickCompleteListener() {
                        @Override
                        public void onImagePickComplete(ArrayList<ImageItem> items) {
                            if (items != null && items.size() > 0 && items.get(0) != null) {
                                onTakePhotoResult(items.get(0));
                            }
                        }
                    });
        }
    }

    @Override
    public void takeVideo() {
        if (getActivity() == null || isOverMaxCount()) {
            return;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQ_CAMERA);
        } else {
            ImagePicker.takeVideo(getActivity(), null, getSelectConfig().getMaxVideoDuration(),
                    true, new OnImagePickCompleteListener() {
                        @Override
                        public void onImagePickComplete(ArrayList<ImageItem> items) {
                            if (items != null && items.size() > 0 && items.get(0) != null) {
                                onTakePhotoResult(items.get(0));
                            }
                        }
                    });
        }
    }

    protected void loadMediaSets() {
        if (getActivity() == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {


            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQ_STORAGE);
            } else {
                ImagePicker.provideMediaSets(getActivity(), getSelectConfig().getMimeTypes(), new MediaSetsDataSource.MediaSetProvider() {
                    @Override
                    public void providerMediaSets(ArrayList<ImageSet> imageSets) {
                        loadMediaSetsComplete(imageSets);
                    }
                });
            }
        }else {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_STORAGE);
            } else {
                ImagePicker.provideMediaSets(getActivity(), getSelectConfig().getMimeTypes(), new MediaSetsDataSource.MediaSetProvider() {
                    @Override
                    public void providerMediaSets(ArrayList<ImageSet> imageSets) {
                        loadMediaSetsComplete(imageSets);
                    }
                });
            }
        }
    }


    protected void loadMediaItemsFromSet(final @NonNull ImageSet set) {
        if (set.imageItems == null || set.imageItems.size() == 0) {
            DialogInterface dialogInterface = null;
            if (!set.isAllMedia() && set.count > 1000) {
                dialogInterface = getPresenter().
                        showProgressDialog(getWeakActivity(), ProgressSceneEnum.loadMediaItem);
            }
            final BaseSelectConfig selectConfig = getSelectConfig();
            final DialogInterface finalDialogInterface = dialogInterface;
            ImagePicker.provideMediaItemsFromSetWithPreload(getActivity(), set, selectConfig.getMimeTypes(),
                    40, new MediaItemsDataSource.MediaItemPreloadProvider() {
                        @Override
                        public void providerMediaItems(ArrayList<ImageItem> imageItems) {
                            if (finalDialogInterface != null) {
                                finalDialogInterface.dismiss();
                            }
                            set.imageItems = imageItems;
                            loadMediaItemsComplete(set);
                        }
                    }, new MediaItemsDataSource.MediaItemProvider() {
                        @Override
                        public void providerMediaItems(ArrayList<ImageItem> imageItems, ImageSet allVideoSet) {
                            if (finalDialogInterface != null) {
                                finalDialogInterface.dismiss();
                            }
                            set.imageItems = imageItems;
                            loadMediaItemsComplete(set);
                            if (selectConfig.isShowImage() && selectConfig.isShowVideo()) {
                                refreshAllVideoSet(allVideoSet);
                            }
                        }
                    });
        } else {
            loadMediaItemsComplete(set);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQ_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                PPermissionUtils.create(getContext()).showSetPermissionDialog(
                        getString(R.string.picker_str_camera_permission));
            }
        } else if (requestCode == REQ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMediaSets();
            } else {
                PPermissionUtils.create(getContext()).
                        showSetPermissionDialog(getString(R.string.picker_str_storage_permission));
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    protected PickerControllerView titleBar;
    protected PickerControllerView bottomBar;


    protected PickerControllerView inflateControllerView(ViewGroup container, boolean isTitle, PickerUiConfig uiConfig) {
        final BaseSelectConfig selectConfig = getSelectConfig();
        PickerUiProvider uiProvider = uiConfig.getPickerUiProvider();
        PickerControllerView view = isTitle ? uiProvider.getTitleBar(getWeakActivity()) :
                uiProvider.getBottomBar(getWeakActivity());
        if (view != null && view.isAddInParent()) {
            container.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            if (selectConfig.isShowVideo() && selectConfig.isShowImage()) {
                view.setTitle(getString(R.string.picker_str_title_all));
            } else if (selectConfig.isShowVideo()) {
                view.setTitle(getString(R.string.picker_str_title_video));
            } else {
                view.setTitle(getString(R.string.picker_str_title_image));
            }
            final PickerControllerView finalView = view;

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == finalView.getCanClickToCompleteView()) {
                        notifyPickerComplete();
                    } else if (v == finalView.getCanClickToToggleFolderListView()) {
                        toggleFolderList();
                    } else {
                        intentPreview(false, 0);
                    }
                }
            };

            if (view.getCanClickToCompleteView() != null) {
                view.getCanClickToCompleteView().setOnClickListener(clickListener);
            }

            if (view.getCanClickToToggleFolderListView() != null) {
                view.getCanClickToToggleFolderListView().setOnClickListener(clickListener);
            }

            if (view.getCanClickToIntentPreviewView() != null) {
                view.getCanClickToIntentPreviewView().setOnClickListener(clickListener);
            }
        }

        return view;
    }


    protected void controllerViewOnTransitImageSet(boolean isOpen) {
        if (titleBar != null) {
            titleBar.onTransitImageSet(isOpen);
        }
        if (bottomBar != null) {
            bottomBar.onTransitImageSet(isOpen);
        }
    }


    protected void controllerViewOnImageSetSelected(ImageSet set) {
        if (titleBar != null) {
            titleBar.onImageSetSelected(set);
        }
        if (bottomBar != null) {
            bottomBar.onImageSetSelected(set);
        }
    }

    protected void refreshCompleteState() {
        if (titleBar != null) {
            titleBar.refreshCompleteViewState(selectList, getSelectConfig());
        }

        if (bottomBar != null) {
            bottomBar.refreshCompleteViewState(selectList, getSelectConfig());
        }
    }


    protected void setFolderListHeight(RecyclerView mFolderListRecyclerView, View mImageSetMask, boolean isCrop) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mFolderListRecyclerView.getLayoutParams();
        RelativeLayout.LayoutParams maskParams = (RelativeLayout.LayoutParams) mImageSetMask.getLayoutParams();
        PickerUiConfig uiConfig = getUiConfig();
        int height = uiConfig.getFolderListOpenMaxMargin();
        if (uiConfig.getFolderListOpenDirection() == PickerUiConfig.DIRECTION_BOTTOM) {
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            if (isCrop) {
                params.bottomMargin = bottomBar != null ? bottomBar.getViewHeight() : 0;
                params.topMargin = (titleBar != null ? titleBar.getViewHeight() : 0) + height;
                maskParams.topMargin = (titleBar != null ? titleBar.getViewHeight() : 0);
                maskParams.bottomMargin = bottomBar != null ? bottomBar.getViewHeight() : 0;
            } else {
                params.bottomMargin = 0;
                params.topMargin = height;
            }
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            if (isCrop) {
                params.bottomMargin = height + (bottomBar != null ? bottomBar.getViewHeight() : 0);
                params.topMargin = titleBar != null ? titleBar.getViewHeight() : 0;
                maskParams.topMargin = (titleBar != null ? titleBar.getViewHeight() : 0);
                maskParams.bottomMargin = bottomBar != null ? bottomBar.getViewHeight() : 0;
            } else {
                params.bottomMargin = height;
                params.topMargin = 0;
            }
        }
        mFolderListRecyclerView.setLayoutParams(params);
        mImageSetMask.setLayoutParams(maskParams);
    }


    protected boolean interceptClickDisableItem(int disableItemCode, boolean isCheckOverMaxCount) {
        if (disableItemCode != PickerItemDisableCode.NORMAL) {
            if (!isCheckOverMaxCount && disableItemCode == PickerItemDisableCode.DISABLE_OVER_MAX_COUNT) {
                return false;
            }
            String message = PickerItemDisableCode.getMessageFormCode(getActivity(), disableItemCode, getPresenter(), getSelectConfig());
            if (message.length() > 0) {
                getPresenter().tip(getWeakActivity(), message);
            }
            return true;
        }
        return false;
    }



    protected void addItemInImageSets(@NonNull List<ImageSet> imageSets,
                                      @NonNull List<ImageItem> imageItems,
                                      @NonNull ImageItem imageItem) {
        imageItems.add(0, imageItem);
        if (imageSets.size() == 0) {
            String firstImageSetName;
            if (imageItem.isVideo()) {
                firstImageSetName = getActivity().getString(R.string.picker_str_folder_item_video);
            } else {
                firstImageSetName = getActivity().getString(R.string.picker_str_folder_item_image);
            }
            ImageSet imageSet = ImageSet.allImageSet(firstImageSetName);
            imageSet.cover = imageItem;
            imageSet.coverPath = imageItem.path;
            imageSet.imageItems = (ArrayList<ImageItem>) imageItems;
            imageSet.count = imageSet.imageItems.size();
            imageSets.add(imageSet);
        } else {
            imageSets.get(0).imageItems = (ArrayList<ImageItem>) imageItems;
            imageSets.get(0).cover = imageItem;
            imageSets.get(0).coverPath = imageItem.path;
            imageSets.get(0).count = imageItems.size();
        }
    }

    private WeakReference<Activity> weakReference;

    protected Activity getWeakActivity() {
        if (getActivity() != null) {
            if (weakReference == null) {
                weakReference = new WeakReference<Activity>(getActivity());
            }
            return weakReference.get();
        }
        return null;
    }

    protected void tip(String msg) {
        getPresenter().tip(getWeakActivity(), msg);
    }

    final public int dp(float dp) {
        if (getActivity() == null || getContext() == null) {
            return 0;
        }
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    private long lastTime = 0L;

    protected boolean onDoubleClick() {
        boolean flag = false;
        long time = System.currentTimeMillis() - lastTime;

        if (time > 300) {
            flag = true;
        }
        lastTime = System.currentTimeMillis();
        return !flag;
    }

    /**
     * 设置是否显示状态栏
     */
    protected void setStatusBar() {
        if (getActivity() != null) {
            //刘海屏幕需要适配状态栏颜色
            if (getUiConfig().isShowStatusBar() || PStatusBarUtil.hasNotchInScreen(getActivity())) {
                PStatusBarUtil.setStatusBar(getActivity(), getUiConfig().getStatusBarColor(),
                        false, PStatusBarUtil.isDarkColor(getUiConfig().getStatusBarColor()));
            } else {
                PStatusBarUtil.fullScreen(getActivity());
            }
        }
    }
}
