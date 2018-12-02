package com.app.media.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.media.bean.base.MediaBean;
import com.app.media.manager.PlayControlManager;
import com.app.media.manager.PlayDataManager;
import com.app.media.sdk.IPlayBar;

import cn.yinxm.playsdk.test.R;


/**
 * 播放条
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/8
 */
public class PlayBar extends ConstraintLayout implements View.OnClickListener, IPlayBar {

    private View mLayoutView;

    private TextView mTitile;
    private TextView mArtist;
    private ImageView mCover;

    private CheckBox mPlayCheckbox;
    private SeekBar mSeekBar;
    private boolean isUserToucheSeek;


    public PlayBar(Context context) {
        this(context, null);
    }

    public PlayBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayoutView = View.inflate(context, getLayoutResID(), this);
        initView();
    }

    private void initView() {
        mTitile = mLayoutView.findViewById(R.id.title);
        mArtist = mLayoutView.findViewById(R.id.artist);
        mCover = mLayoutView.findViewById(R.id.cover);
        mPlayCheckbox = mLayoutView.findViewById(R.id.play_check);
        mSeekBar = mLayoutView.findViewById(R.id.seek_bar);

        mLayoutView.setOnClickListener(this);
        mLayoutView.findViewById(R.id.play).setOnClickListener(this);
        mLayoutView.findViewById(R.id.play_pre).setOnClickListener(this);
        mLayoutView.findViewById(R.id.play_next).setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserToucheSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserToucheSeek = false;
                PlayControlManager.getInstance().seekTo(seekBar.getProgress());
            }
        });
    }


    @Override
    public int getLayoutResID() {
        return R.layout.layout_play_bar;
    }

    @Override
    public void openPlayActivity() {
//        Context context = getContext();
//        Intent intent = new Intent(context, MusicPlayActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//        PlayerActivity.start(context);
    }

    @Override
    public void showBar() {
        // 只有有播放数据的时候才能显示
        if (PlayDataManager.getInstance().getPlayMediaBean() != null) {
            onResumeUpdatePlayInfo();
            mLayoutView.setVisibility(VISIBLE);
        } else {
            hideBar();
        }
    }

    @Override
    public void hideBar() {
        mLayoutView.setVisibility(GONE);
    }

    @Override
    public void onResumeUpdatePlayInfo() {
        onPlayStateChanged(PlayDataManager.getInstance().isPlaying());
        onPlayBeanChanged(PlayDataManager.getInstance().getPlayMediaBean());
    }

    @Override
    public void onPlayStateChanged(boolean isPlaying) {
        mPlayCheckbox.setChecked(!isPlaying);
    }

    @Override
    public void onPlayBeanChanged(MediaBean mediaBean) {
        if (mediaBean == null) {
            mediaBean = PlayDataManager.getInstance().getPlayMediaBean();
        }
        if (mediaBean == null) {
            hideBar();
            return;
        }
        mLayoutView.setVisibility(VISIBLE);
        mTitile.setText(mediaBean.getTitle());
        mArtist.setText(mediaBean.getArtist());

        String coverUrl = mediaBean.getCover();
        if (!TextUtils.isEmpty(coverUrl)) {
            // TODO: 2018/9/23
//            Object object = mCover.getTag(R.id.cover_tag);
//            if (object == null || !coverUrl.equals(object)) {
//                mCover.setTag(R.id.cover_tag, coverUrl);
//                // 重新加载图片
//                Glide.with(getContext()).load(coverUrl).crossFade(1000)
//                        .placeholder(R.mipmap.album).error(R.mipmap.album_s)
//                        .bitmapTransform(new GlideCircleTransform(getContext()),
//                                new AlbumTransformation(getContext())).into(mCover);
//
//            } else {
//                LogUtil.d("cover img reuse");
//            }
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.album_s);
            mCover.setImageDrawable(drawable);
        }
    }

    @Override
    public void onProgressChanged(long duration, long currentPlayPosition, long bufferedPosition) {
        if (!isUserToucheSeek) {
            mSeekBar.setMax((int) duration);
            mSeekBar.setProgress((int) currentPlayPosition);
            mSeekBar.setSecondaryProgress((int) bufferedPosition);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_bar:
                openPlayActivity();
                break;
            case R.id.play:
                mPlayCheckbox.setChecked(!mPlayCheckbox.isChecked());
                if (PlayControlManager.getInstance().isPlaying()) {
                    PlayControlManager.getInstance().pause();
                } else {
                    PlayControlManager.getInstance().play();
                }
                break;
            case R.id.play_next:
                PlayControlManager.getInstance().playNext();
                break;
            case R.id.play_pre:
                PlayControlManager.getInstance().playPrevious();
                break;
            default:
                break;
        }
    }
}
