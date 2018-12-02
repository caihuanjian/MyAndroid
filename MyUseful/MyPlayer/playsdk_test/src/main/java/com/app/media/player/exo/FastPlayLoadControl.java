package com.app.media.player.exo;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by yinxm on 2017/1/15.
 * 功能: 快速播放模式
 */

public class FastPlayLoadControl implements LoadControl {

    /**
     * The default minimum duration of media that the player will attempt to ensure is buffered at all
     * times, in milliseconds.
     */
    public static final int DEFAULT_MIN_BUFFER_MS = 5000; // 15s

    /**
     * The default maximum duration of media that the player will attempt to buffer, in milliseconds.
     */
    public static final int DEFAULT_MAX_BUFFER_MS = 30000;

    /**
     * The default duration of media that must be buffered for playback to start or resume following a
     * user action such as a seek, in milliseconds.
     */
    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_MS = 2500;

    /**
     * The default duration of media that must be buffered for playback to resume after a rebuffer,
     * in milliseconds. A rebuffer is defined to be caused by buffer depletion rather than a user
     * action.
     */
    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 5000; // default 5s

    private static final int ABOVE_HIGH_WATERMARK = 0;
    private static final int BETWEEN_WATERMARKS = 1;
    private static final int BELOW_LOW_WATERMARK = 2;

    private final DefaultAllocator allocator;

    private final long minBufferUs;
    private final long maxBufferUs;
    private final long bufferForPlaybackUs;
    private final long bufferForPlaybackAfterRebufferUs;

    private int targetBufferSize;
    private boolean isBuffering;

    /**
     * Constructs a new instance, using the {@code DEFAULT_*} constants defined in this class.
     */
    public FastPlayLoadControl() {
        this(new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE));
    }

    /**
     * Constructs a new instance, using the {@code DEFAULT_*} constants defined in this class.
     *
     * @param allocator The {@link DefaultAllocator} used by the loader.
     */
    public FastPlayLoadControl(DefaultAllocator allocator) {
        this(allocator, DEFAULT_MIN_BUFFER_MS, DEFAULT_MAX_BUFFER_MS, DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS);
    }

    /**
     * Constructs a new instance.
     *
     * @param allocator                        The {@link DefaultAllocator} used by the loader.
     * @param minBufferMs                      The minimum duration of media that the player will attempt to ensure is
     *                                         buffered at all times, in milliseconds.
     * @param maxBufferMs                      The maximum duration of media that the player will attempt buffer, in
     *                                         milliseconds.
     * @param bufferForPlaybackMs              The duration of media that must be buffered for playback to start or
     *                                         resume following a user action such as a seek, in milliseconds.
     * @param bufferForPlaybackAfterRebufferMs The default duration of media that must be buffered for
     *                                         playback to resume after a rebuffer, in milliseconds. A rebuffer is defined to be caused by
     *                                         buffer depletion rather than a user action.
     */
    public FastPlayLoadControl(DefaultAllocator allocator, int minBufferMs, int maxBufferMs,
                               long bufferForPlaybackMs, long bufferForPlaybackAfterRebufferMs) {
        this.allocator = allocator;
        minBufferUs = minBufferMs * 1000L;
        maxBufferUs = maxBufferMs * 1000L;
        bufferForPlaybackUs = bufferForPlaybackMs * 1000L;
        bufferForPlaybackAfterRebufferUs = bufferForPlaybackAfterRebufferMs * 1000L;
    }

    @Override
    public void onPrepared() {
        reset(false);
    }

    @Override
    public void onTracksSelected(Renderer[] renderers, TrackGroupArray trackGroups,
                                 TrackSelectionArray trackSelections) {
        targetBufferSize = 0;
        for (int i = 0; i < renderers.length; i++) {
            if (trackSelections.get(i) != null) {
                targetBufferSize += Util.getDefaultBufferSize(renderers[i].getTrackType());
            }
        }
        allocator.setTargetBufferSize(targetBufferSize);
    }

    @Override
    public void onStopped() {
        reset(true);
    }

    @Override
    public void onReleased() {
        reset(true);
    }

    @Override
    public Allocator getAllocator() {
        return allocator;
    }

    @Override
    public boolean shouldStartPlayback(long bufferedDurationUs, boolean rebuffering) {
        long minBufferDurationUs = rebuffering ? bufferForPlaybackAfterRebufferUs : bufferForPlaybackUs;
        return minBufferDurationUs <= 0 || bufferedDurationUs >= minBufferDurationUs;
    }

    @Override
    public boolean shouldContinueLoading(long bufferedDurationUs) {
        // 缓存pos处于区间(LOW_WATERMARK, HIGH_WATERMARK)的3种状态：2-1-0
        int bufferTimeState = getBufferTimeState(bufferedDurationUs);
        // 当前已缓存空间是否大于目标缓存空间
        boolean targetBufferSizeReached = allocator.getTotalBytesAllocated() >= targetBufferSize;
        // 当前换成状态未满（小于最小）|| 当前缓存状态位于最小-最大缓存区间 && 缓存中 && 目标缓存未满
        isBuffering = bufferTimeState == BELOW_LOW_WATERMARK
                || (bufferTimeState == BETWEEN_WATERMARKS && isBuffering && !targetBufferSizeReached);

        // TODO: 2018/10/3 当当前缓存值 < duration时，都返回true，简单粗暴方法，直接返回true，不确定会不会有问题，依赖于播放器LoadControl去容错处理
//        return isBuffering;
        return true;
    }

    private int getBufferTimeState(long bufferedDurationUs) {
        return bufferedDurationUs > maxBufferUs ? ABOVE_HIGH_WATERMARK :
                (bufferedDurationUs < minBufferUs ? BELOW_LOW_WATERMARK : BETWEEN_WATERMARKS);
    }

    private void reset(boolean resetAllocator) {
        targetBufferSize = 0;
        isBuffering = false;
        if (resetAllocator) {
            allocator.reset();
        }
    }
}
