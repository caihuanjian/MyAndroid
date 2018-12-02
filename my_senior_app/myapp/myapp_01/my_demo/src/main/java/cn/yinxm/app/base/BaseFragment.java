package cn.yinxm.app.base;

/**
 * 功能：fragment 懒加载
 * Created by yinxm on 2017/11/14.
 */

public abstract class BaseFragment extends BaseFragmentUtil {
    /** Fragment当前状态是否可见 */
    protected boolean isVisible;
    public  boolean isPrepared;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {

    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();





    /**
     * 延迟加载
     * 子类必须重写此方法
     * 注：懒加载先加载布局文件，接口调用在界面展示给用户的时候调用
     */
//    @Override
//    protected void lazyLoad() {
//
//        if (!isPrepared || !isVisible || mHasLoadedOnce) {
//            return;
//        }
//        loadhttpnet(page);
//        mHasLoadedOnce = true;
//        if (loadingLayout != null) {
//            loadingLayout.showLoading();
//        }
//    }
    /**
     * 需要延迟加载的子类在oncreateview里面加上这一句
     */
//    isPrepared = true;
}
