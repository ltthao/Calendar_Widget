package com.example.lttha.a14110180_lethithao_calendarwidget.View;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.example.lttha.a14110180_lethithao_calendarwidget.Untils.CalendarUtils;
import com.example.lttha.a14110180_lethithao_calendarwidget.Cursor.EventCursor;


public class CalendarView extends ViewPager {

    private final MonthView.OnDateChangeListener mDateChangeListener =
            new MonthView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(long dayMillis) {
                    // this should come from a page, only notify its neighbors
                    mPagerAdapter.setSelectedDay(getCurrentItem(), dayMillis, false);
                    notifyDayChange(dayMillis);
                }
            };
    private MonthViewPagerAdapter mPagerAdapter;
    private OnChangeListener mListener;
    private CalendarAdapter mCalendarAdapter;


    public interface OnChangeListener {

        void onSelectedDayChange(long dayMillis);
    }


    public static abstract class CalendarAdapter {
        private CalendarView mCalendarView;

        void setCalendarView(CalendarView calendarView) {
            mCalendarView = calendarView;
        }


        protected void loadEvents(long monthMillis) {
        }


        public final void bindEvents(long monthMillis, EventCursor cursor) {
            mCalendarView.swapCursor(monthMillis, cursor);
        }
    }

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // make this ViewPager's height WRAP_CONTENT
        View child = mPagerAdapter.mViews.get(getCurrentItem());
        if (child != null) {
            child.measure(widthMeasureSpec, heightMeasureSpec);
            int height = child.getMeasuredHeight();
            setMeasuredDimension(getMeasuredWidth(), height);
        }
    }


    public void setOnChangeListener(OnChangeListener listener) {
        mListener = listener;
    }

    //set khi chọn 1 ngày thì tự động tháng cũng sẽ chuyển theo
    public void setSelectedDay(long dayMillis) {
        // notify active page and its neighbors
        int position = getCurrentItem();
        if (CalendarUtils.monthBefore(dayMillis, mPagerAdapter.mSelectedDayMillis)) {
            mPagerAdapter.setSelectedDay(position - 1, dayMillis, true);
            setCurrentItem(position - 1, true);
        } else if (CalendarUtils.monthAfter(dayMillis, mPagerAdapter.mSelectedDayMillis)) {
            mPagerAdapter.setSelectedDay(position + 1, dayMillis, true);
            setCurrentItem(position + 1, true);
        } else {
            mPagerAdapter.setSelectedDay(position, dayMillis, true);
        }
    }


    public void setCalendarAdapter(@NonNull CalendarAdapter adapter) {
        mCalendarAdapter = adapter;
        mCalendarAdapter.setCalendarView(this);
        loadEvents(getCurrentItem());
    }


    public void deactivate() {
        mPagerAdapter.deactivate();
    }

//    Xoá bất kỳ ràng buộc dữ liệu hoạt động nào từ bộ điều hợp,
//    Nhưng giữ trạng thái của trình xem và kích hoạt dữ liệu lại
    public void invalidateData() {
        mPagerAdapter.invalidate();
        loadEvents(getCurrentItem());
    }

    /**
     * Clears any active data bindings from adapter,
     * resets view state to initial state and triggers rebinding data
     */
    public void reset() {
        deactivate();
        init();
        loadEvents(getCurrentItem());
    }

    private void init() {
        mPagerAdapter = new MonthViewPagerAdapter(mDateChangeListener);
        setAdapter(mPagerAdapter);
        setCurrentItem(mPagerAdapter.getCount() / 2);
        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            public boolean mDragging = false; // indicate if page change is from user

            @Override
            public void onPageSelected(int position) {
                if (mDragging) {
                    // sequence: IDLE -> (DRAGGING) -> SETTLING -> onPageSelected -> IDLE
                    // ensures that this will always be triggered before syncPages() for position
                    toFirstDay(position);
                    notifyDayChange(mPagerAdapter.getMonth(position));
                }
                mDragging = false;
                // trigger same scroll state changed logic, which would not be fired if not visible
                if (getVisibility() != VISIBLE) {
                    onPageScrollStateChanged(SCROLL_STATE_IDLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    syncPages(getCurrentItem());
                    loadEvents(getCurrentItem());
                } else if (state == SCROLL_STATE_DRAGGING) {
                    mDragging = true;
                }
            }
        });
    }

    private void toFirstDay(int position) {
        mPagerAdapter.setSelectedDay(position,
                CalendarUtils.monthFirstDay(mPagerAdapter.getMonth(position)), true);
    }

    private void notifyDayChange(long dayMillis) {
        if (mListener != null) {
            mListener.onSelectedDayChange(dayMillis);
        }
    }

    /**
     * shift and recycle pages if we are currently at last or first,
     * ensure that users can peek hidden pages on 2 sides
     * @param position  current item position
     */
    private void syncPages(int position) {
        int first = 0, last = mPagerAdapter.getCount() - 1;
        if (position == last) {
            mPagerAdapter.shiftLeft();
            setCurrentItem(first + 1, false);
        } else if (position == 0) {
            mPagerAdapter.shiftRight();
            setCurrentItem(last - 1, false);
        } else {
            // rebind neighbours due to shifting
            if (position > 0) {
                mPagerAdapter.bind(position - 1);
            }
            if (position < mPagerAdapter.getCount() - 1) {
                mPagerAdapter.bind(position + 1);
            }
        }
    }

    private void loadEvents(int position) {
        if (mCalendarAdapter != null && mPagerAdapter.getCursor(position) == null) {
            mCalendarAdapter.loadEvents(mPagerAdapter.getMonth(position));
        }
    }

    private void swapCursor(long monthMillis, EventCursor cursor) {
        mPagerAdapter.swapCursor(monthMillis, cursor, new PagerContentObserver(monthMillis));
    }

    class PagerContentObserver extends ContentObserver {

        private final long monthMillis;

        public PagerContentObserver(long monthMillis) {
            super(new Handler());
            this.monthMillis = monthMillis;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            // invalidate previous cursor for given month
            mPagerAdapter.swapCursor(monthMillis, null, null);
            // reload events if given month is active month
            // hidden months will be reloaded upon being swiped to
            if (CalendarUtils.sameMonth(monthMillis, mPagerAdapter.getMonth(getCurrentItem()))) {
                loadEvents(getCurrentItem());
            }
        }
    }
}
