package com.custom.seekbar;

import java.util.Timer;
import java.util.TimerTask;

import com.custom.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * @Author: zwy
 * @E-mail: weiyazhang1987@gmail.com
 * @time Create Date: 2013-9-23下午4:48:35
 * @Message:重定义seekbar使显示更清晰
 **/
public class CustomSeekBar extends LinearLayout {

	public CustomSeekBar(Context context) {
		super(context);
		init();
	}

	public CustomSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@SuppressLint("NewApi")
	public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SeekBar mSeekBar;
	private TextView mTitle;
	private TextView mValue;
	private TextView mMinValue;
	private TextView mMaxValue;

	LayoutInflater mInflater;

	public void init() {
		mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(R.layout.custom_view_seekbar, this, true);

		mTitle = (TextView) findViewById(R.id.seekBarTitle);
		mValue = (TextView) findViewById(R.id.seekBarValue);
		mMinValue = (TextView) findViewById(R.id.seekBarMinValue);
		mMaxValue = (TextView) findViewById(R.id.seekBarMaxValue);
		mSeekBar = (SeekBar) findViewById(R.id.seekBar);
	}

	public void setTitle(CharSequence title) {
		mTitle.setText(title);
	}

	public void setTitle(int title, Object... formatArgs) {
		setTitle(getContext().getString(title, formatArgs));
	}

	public void setValue(CharSequence value) {
		mValue.setText(value);
		int seekBarWidth = mSeekBar.getWidth();
		int viewWidth = mValue.getWidth();
		int left = (int) (((seekBarWidth * mSeekBar.getProgress() + 49) / 100) - (viewWidth / 2));
		if (left < 0)
			left = 0;

		if (left > seekBarWidth - viewWidth)
			left = seekBarWidth - viewWidth;

		LinearLayout.LayoutParams lp = (LayoutParams) mValue.getLayoutParams();
		lp.setMargins(left, lp.topMargin, lp.rightMargin, lp.bottomMargin);
		mValue.setLayoutParams(lp);
	}

	public void setValue(int value, Object... formatArgs) {
		setValue(getContext().getString(value, formatArgs));
	}

	public void setMin(CharSequence value) {
		mMinValue.setText(value);
	}

	public void setMin(int value, Object... formatArgs) {
		setMin(getContext().getString(value, formatArgs));
	}

	public void setMax(CharSequence value) {
		mMaxValue.setText(value);
	}

	public void setMax(int value, Object... formatArgs) {
		setMax(getContext().getString(value, formatArgs));
	}

	public void setProgress(int progress) {
		mSeekBar.setProgress(progress);
	}

	public void setProgressTimer(final int progress) {
		Timer timer = new Timer("INIT_SEEKBAR_PROGRESS");
		final Handler handler = new Handler();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						mSeekBar.setProgress(progress);
					}
				});
			}
		}, 100);
	}

	public int getProgress() {
		return mSeekBar.getProgress();
	}

	public void setOnSeekBarChangeListener(final OnCustomSeekBarChangeListener listener) {
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				listener.onStopTrackingTouch(CustomSeekBar.this);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				listener.onStartTrackingTouch(CustomSeekBar.this);
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				listener.onProgressChanged(CustomSeekBar.this, progress, fromUser);
			}
		});
	}

	public interface OnCustomSeekBarChangeListener {
		/**
		 * Notification that the progress level has changed. Clients can use the
		 * fromUser parameter to distinguish user-initiated changes from those
		 * that occurred programmatically.
		 * 
		 * @param seekBar
		 *            The SeekBar whose progress has changed
		 * @param progress
		 *            The current progress level. This will be in the range
		 *            0..max where max was set by
		 *            {@link ProgressBar#setMax(int)}. (The default value for
		 *            max is 100.)
		 * @param fromUser
		 *            True if the progress change was initiated by the user.
		 */
		void onProgressChanged(CustomSeekBar seekBar, int progress, boolean fromUser);

		/**
		 * Notification that the user has started a touch gesture. Clients may
		 * want to use this to disable advancing the seekbar.
		 * 
		 * @param seekBar
		 *            The SeekBar in which the touch gesture began
		 */
		void onStartTrackingTouch(CustomSeekBar seekBar);

		/**
		 * Notification that the user has finished a touch gesture. Clients may
		 * want to use this to re-enable advancing the seekbar.
		 * 
		 * @param seekBar
		 *            The SeekBar in which the touch gesture began
		 */
		void onStopTrackingTouch(CustomSeekBar seekBar);
	}

}
