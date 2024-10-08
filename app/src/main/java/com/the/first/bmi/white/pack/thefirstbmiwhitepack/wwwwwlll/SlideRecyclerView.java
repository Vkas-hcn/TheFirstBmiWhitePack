package com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll;


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SlideRecyclerView extends RecyclerView {

    private static final String TAG = "SlideRecyclerView";
    private static final int INVALID_POSITION = -1;
    private static final int INVALID_CHILD_WIDTH = -1;
    private static final int SNAP_VELOCITY = 600;

    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private Rect mTouchFrame;
    private Scroller mScroller;
    private float mLastX;
    private float mFirstX, mFirstY;
    private boolean mIsSlide;
    private ViewGroup mFlingView;
    private int mPosition;
    private int mMenuViewWidth;
    private List<Integer> slidOutPositions = new ArrayList<>();

    public SlideRecyclerView(Context context) {
        this(context, null);
    }

    public SlideRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        obtainVelocity(e);
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mFirstX = mLastX = x;
                mFirstY = y;
                mPosition = pointToPosition(x, y);
                if (mPosition != INVALID_POSITION) {
                    View view = mFlingView;
                    mFlingView = (ViewGroup) getChildAt(mPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition());
                    if (view != null && mFlingView != view && view.getScrollX() != 0) {
                        view.scrollTo(0, 0);
                        slidOutPositions.remove(Integer.valueOf(mPosition)); // Remove from list if it was slid out
                    }
                    if (mFlingView.getChildCount() == 2) {
                        mMenuViewWidth = mFlingView.getChildAt(1).getWidth();
                    } else {
                        mMenuViewWidth = INVALID_CHILD_WIDTH;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                float yVelocity = mVelocityTracker.getYVelocity();
                if (Math.abs(xVelocity) > SNAP_VELOCITY && Math.abs(xVelocity) > Math.abs(yVelocity)
                        || Math.abs(x - mFirstX) >= mTouchSlop
                        && Math.abs(x - mFirstX) > Math.abs(y - mFirstY)) {
                    mIsSlide = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                releaseVelocity();
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mIsSlide && mPosition != INVALID_POSITION) {
            float x = e.getX();
            obtainVelocity(e);
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mMenuViewWidth != INVALID_CHILD_WIDTH) {
                        float dx = mLastX - x;
                        if (mFlingView.getScrollX() + dx <= mMenuViewWidth
                                && mFlingView.getScrollX() + dx > 0) {
                            mFlingView.scrollBy((int) dx, 0);
                        }
                        mLastX = x;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mMenuViewWidth != INVALID_CHILD_WIDTH) {
                        int scrollX = mFlingView.getScrollX();
                        mVelocityTracker.computeCurrentVelocity(1000);
                        if (mVelocityTracker.getXVelocity() < -SNAP_VELOCITY) {
                            mScroller.startScroll(scrollX, 0, mMenuViewWidth - scrollX, 0, Math.abs(mMenuViewWidth - scrollX));
                            slidOutPositions.add(mPosition); // Add to list when slid out

                        } else if (mVelocityTracker.getXVelocity() >= SNAP_VELOCITY) {
                            mScroller.startScroll(scrollX, 0, -scrollX, 0, Math.abs(scrollX));
                            slidOutPositions.remove(Integer.valueOf(mPosition)); // Remove from list when slid back
                        } else if (scrollX >= mMenuViewWidth / 2) {
                            mScroller.startScroll(scrollX, 0, mMenuViewWidth - scrollX, 0, Math.abs(mMenuViewWidth - scrollX));
                            slidOutPositions.add(mPosition); // Add to list when slid out

                        } else {
                            mScroller.startScroll(scrollX, 0, -scrollX, 0, Math.abs(scrollX));
                            slidOutPositions.remove(Integer.valueOf(mPosition)); // Remove from list when slid back
                        }
                        invalidate();
                    }
                    mMenuViewWidth = INVALID_CHILD_WIDTH;
                    mIsSlide = false;
                    mPosition = INVALID_POSITION;
                    releaseVelocity();
                    break;
            }
            return true;
        } else {
            closeMenu();
            releaseVelocity();
        }
        return super.onTouchEvent(e);
    }
    public List<Integer> getSlidOutPositions() {
        return new ArrayList<>(slidOutPositions); // Return a copy to avoid external modification
    }
    private void releaseVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void obtainVelocity(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    public int pointToPosition(int x, int y) {
        int firstPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        Rect frame = mTouchFrame;
        if (frame == null) {
            mTouchFrame = new Rect();
            frame = mTouchFrame;
        }

        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return firstPosition + i;
                }
            }
        }
        return INVALID_POSITION;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mFlingView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
    public void closeMenu() {
        if (mFlingView != null && mFlingView.getScrollX() != 0) {
            mFlingView.scrollTo(0, 0);
            int position = pointToPosition(mFlingView.getLeft(), mFlingView.getTop());
            slidOutPositions.remove(Integer.valueOf(position)); // Remove from list if menu is closed

        }
    }
    public void closeAllMenus() {
        int firstVisiblePosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        int lastVisiblePosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

        for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {
            View itemView = getChildAt(i - firstVisiblePosition);
            if (itemView instanceof ViewGroup && itemView.getScrollX() != 0) {
                itemView.scrollTo(0, 0); // Scroll back to the original position
            }
        }

        // Clear the list of slid out positions
        slidOutPositions.clear();
    }
    public void slideAllItems() {
        int firstVisiblePosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        int lastVisiblePosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

        for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {
            View itemView = getChildAt(i - firstVisiblePosition);
            if (itemView instanceof ViewGroup && ((ViewGroup) itemView).getChildCount() == 2) {
                int menuWidth = ((ViewGroup) itemView).getChildAt(1).getWidth();
                Scroller scroller = new Scroller(getContext());
                scroller.startScroll(0, 0, menuWidth, 0, Math.abs(menuWidth));
                while (!scroller.isFinished()) {
                    scroller.computeScrollOffset();
                    itemView.scrollTo(scroller.getCurrX(), 0);
                }
                itemView.scrollTo(menuWidth, 0); // Ensure it's fully scrolled

                // Add the position to slidOutPositions list
                slidOutPositions.add(i);
            }
        }
    }
}
