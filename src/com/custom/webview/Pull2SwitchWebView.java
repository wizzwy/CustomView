package com.custom.webview;

import com.custom.CustomGlobal;
import com.custom.CustomGlobal.GesttureDirection;
import com.custom.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Author: zwy
 * @E-mail: weiyazhang1987@gmail.com
 * @time Create Date: 2013-9-25上午10:34:46
 * @Message: 上下拖拽时切换笔记
 **/
public class Pull2SwitchWebView extends LinearLayout{

	public Pull2SwitchWebView(Context context) {
		super(context);
		init();
	}

	public Pull2SwitchWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		initAttrs(attrs);
	}

	@SuppressLint("NewApi")
	public Pull2SwitchWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		initAttrs(attrs);
	}

	private WebView mWebView;
	private View mTop;
	private ImageView mTopIcon;
	private TextView mTopAction;
	private TextView mTopTitle;
	private View mBottom;
	private ImageView mBottomIcon;
	private TextView mBottomAction;
	private TextView mBottomTitle;

	private int mTopHeight = 0;
	private int mBottomHeight = 0;

	private RotateAnimation mAnimation;
	private RotateAnimation mReverseAnimation;

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.custom_view_switch_webview, this, true);
		mTop = findViewById(R.id.switch_webview_top);
		mTopIcon = (ImageView) findViewById(R.id.switch_webview_top_icon);
		mTopAction = (TextView) findViewById(R.id.switch_webview_top_action);
		mTopTitle = (TextView) findViewById(R.id.switch_webview_top_title);
		mBottom = findViewById(R.id.switch_webview_bottom);
		mBottomIcon = (ImageView) findViewById(R.id.switch_webview_bottom_icon);
		mBottomAction = (TextView) findViewById(R.id.switch_webview_bottom_action);
		mBottomTitle = (TextView) findViewById(R.id.switch_webview_bottom_title);
		mWebView = (WebView) findViewById(R.id.switch_webview);
		//计算控件的高度
		CustomGlobal.measureView(mTop);
		CustomGlobal.measureView(mBottom);
		mTopHeight = mTop.getMeasuredHeight();
		mBottomHeight = mBottom.getMeasuredHeight();

		//设置padding
		initTopPaddingTop();
		mTop.invalidate();
		mTop.setEnabled(false);
		initBottomPaddingBottom();
		mBottom.invalidate();
		mBottom.setEnabled(false);

		mAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mAnimation.setInterpolator(new LinearInterpolator());
		mAnimation.setDuration(250);
		mAnimation.setFillAfter(true);
		mReverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseAnimation.setInterpolator(new LinearInterpolator());
		mReverseAnimation.setDuration(200);
		mReverseAnimation.setFillAfter(true);

		mState = Pull2SwitchWebViewState.DONE;//默认状态：完成
	}

	private void initTopPaddingTop() {
		setTopPaddingTop(-1 * mTopHeight);
	}

	private void setTopPaddingTop(int top) {
		mTop.setPadding(mTop.getPaddingLeft(), top, mTop.getPaddingRight(), 0);
	}

	private void initBottomPaddingBottom() {
		setBottomPaddingBottom(-1 * mBottomHeight);
	}

	private void setBottomPaddingBottom(int bottom) {
		mBottom.setPadding(mBottom.getPaddingLeft(), 0, mBottom.getPaddingRight(), bottom);
	}

	private void initAttrs(AttributeSet attrs) {

		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.Pull2SwitchWebView);
		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_topBackground)) {
			int resId = typedArray.getResourceId(R.styleable.Pull2SwitchWebView_topBackground, android.R.color.darker_gray);
			setTopBackground(resId);
		}
		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_bottomBackground)) {
			int resId = typedArray.getResourceId(R.styleable.Pull2SwitchWebView_bottomBackground, android.R.color.darker_gray);
			setBottomBackground(resId);
		}

		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_topActionText)) {
			String text = typedArray.getString(R.styleable.Pull2SwitchWebView_topActionText);
			if (!TextUtils.isEmpty(text))
				setTopActionText(text);
		}
		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_bottomActionText)) {
			String text = typedArray.getString(R.styleable.Pull2SwitchWebView_bottomActionText);
			if (!TextUtils.isEmpty(text))
				setBottomActionText(text);
		}

		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_topActionTextColor)) {
			int color = typedArray.getColor(R.styleable.Pull2SwitchWebView_topActionTextColor, android.R.color.black);
			setTopActionTextColor(color);
		}
		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_bottomActionTextColor)) {
			int color = typedArray.getColor(R.styleable.Pull2SwitchWebView_bottomActionTextColor, android.R.color.black);
			setBottomActionTextColor(color);
		}

		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_topTitleText)) {
			String title = typedArray.getString(R.styleable.Pull2SwitchWebView_topTitleText);
			if (!TextUtils.isEmpty(title))
				setTopTitleText(title);
		}
		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_bottomTitleText)) {
			String title = typedArray.getString(R.styleable.Pull2SwitchWebView_bottomTitleText);
			if (!TextUtils.isEmpty(title))
				setBottomTitleText(title);
		}

		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_topTitleTextColor)) {
			int color = typedArray.getColor(R.styleable.Pull2SwitchWebView_topTitleTextColor, android.R.color.black);
			setTopTitleTextColor(color);
		}
		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_bottomTitleTextColor)) {
			int color = typedArray.getColor(R.styleable.Pull2SwitchWebView_bottomTitleTextColor, android.R.color.black);
			setBottomTitleTextColor(color);
		}

		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_topIconSrc)) {
			int icon = typedArray.getResourceId(R.styleable.Pull2SwitchWebView_topIconSrc, R.drawable.icon_switch_webview_top);
			setTopIconImageResource(icon);
		}
		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_bottomIconSrc)) {
			int resId = typedArray.getResourceId(R.styleable.Pull2SwitchWebView_bottomIconSrc, R.drawable.icon_switch_webview_bottom);
			setBottomIconImageResource(resId);
		}

		typedArray.recycle();
	}

	/**
	 * DONE:完成，恢复状态；
	 * PULL_DOWN_2_LAST：向下拉切换上一篇
	 * RELEASE_2_LAST：松手切换到上一篇
	 * PULL_UP_2_NEXT：向上拉切换下一篇
	 * RELEASE_2_NEXT：松手切换到下一篇
	 */
	private Pull2SwitchWebViewState mState;//当前状态
	private enum Pull2SwitchWebViewState {
		DONE, PULL_DOWN_2_LAST, RELEASE_2_LAST, PULL_UP_2_NEXT, RELEASE_2_NEXT;
	}

	private float mStartY;//起始的y坐标
	private boolean mIsRecored;//用于保证startY的值在一个完整的touch事件中只被记录一次
	private final static int RATIO = 3;//实际的padding的距离与界面上手势偏移距离的比例
	private boolean mBack;//标识PULL_*_2_*是否从RELEASE_2_*返回的
	private MotionEvent mOldEvent;
	@SuppressLint("Recycle")
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mOldEvent = MotionEvent.obtain(event);
			break;

		case MotionEvent.ACTION_UP:
			switch (mState) {
			case PULL_DOWN_2_LAST:
				break;
			case RELEASE_2_LAST:
				mOnPullListener.onPullDownFromTop();
				break;
			case PULL_UP_2_NEXT:
				break;
			case RELEASE_2_NEXT:
				mOnPullListener.onPullUpFromBottom();
				break;
			default:
				break;
			}
			mBack = false;
			mIsRecored = false;
			mScrollDirection = ScrollDirection.NULL;
			if (mState == Pull2SwitchWebViewState.DONE)
				break;
			//切换状态到done，并刷新View
			mState = Pull2SwitchWebViewState.DONE;
			refreshAdditionalViewByState(mState);
			break;
		case MotionEvent.ACTION_MOVE:
			initEventStartData(event);
			if(!mIsRecored)
				break;
			float endY = event.getY();
			float moveY = endY - mStartY;

			//DONE状态下
			if(mState == Pull2SwitchWebViewState.DONE) {
				if(moveY > 5){//由DONE状态转变到下拉上一篇状态
					mState = Pull2SwitchWebViewState.PULL_DOWN_2_LAST;
				}else if (moveY < -5) {//由DONE状态转变到上拉下一篇状态
					mState = Pull2SwitchWebViewState.PULL_UP_2_NEXT;
				}
				refreshAdditionalView(mState, Pull2SwitchWebViewState.DONE);
			}

			if(mState == Pull2SwitchWebViewState.PULL_DOWN_2_LAST){

				if(moveY / RATIO >= mTopHeight){//下拉到可以进入RELEASE_2_LAST的状体，下拉上一篇状态转变到松开上一篇状态
					mState = Pull2SwitchWebViewState.RELEASE_2_LAST;
				} else if(moveY <= 0){//上推到顶了，下拉上一篇状态转变到DONE状态
					mState = Pull2SwitchWebViewState.DONE;
				}

				refreshAdditionalView(mState, Pull2SwitchWebViewState.PULL_DOWN_2_LAST);
			}

			//
			if(mState == Pull2SwitchWebViewState.RELEASE_2_LAST) {

				if(((moveY / RATIO) < mTopHeight) && moveY > 0){//往上推到了屏幕已掩盖部分head,松开上一篇状态转变到下拉上一篇状态
					mState = Pull2SwitchWebViewState.PULL_DOWN_2_LAST;
					mBack = true;
				} else if(moveY <= 0){//直接推到顶，松开上一篇状态转变到done状态
					mState = Pull2SwitchWebViewState.DONE;
				}

				refreshAdditionalView(mState, Pull2SwitchWebViewState.RELEASE_2_LAST);
			}

			// /////////////////////////////////////////////////////////////////////////////////////////////////////////
			float oppositeMoveY = -1 * moveY;
			if (mState == Pull2SwitchWebViewState.PULL_UP_2_NEXT) {

				if (oppositeMoveY / RATIO >= mBottomHeight) {// 上拉到可以进入RELEASE_2_NEXT的状体，上拉下一篇状态转变到松开下一篇状态
					mState = Pull2SwitchWebViewState.RELEASE_2_NEXT;
				} else if (oppositeMoveY <= 0) {// 下推至底，上拉下一篇状态转变到DONE状态
					mState = Pull2SwitchWebViewState.DONE;
				}

				refreshAdditionalView(mState, Pull2SwitchWebViewState.PULL_UP_2_NEXT);
			}

			//
			if (mState == Pull2SwitchWebViewState.RELEASE_2_NEXT) {

				if (((oppositeMoveY / RATIO) < mTopHeight) && oppositeMoveY > 0) {// 向下推到了屏幕已掩盖部分Bottom,松开下一篇状态转变到上拉下一篇状态
					mState = Pull2SwitchWebViewState.PULL_UP_2_NEXT;
					mBack = true;
				} else if (oppositeMoveY <= 0) {// 直接推到底，松开下一篇状态转变到done状态
					mState = Pull2SwitchWebViewState.DONE;
				}

				refreshAdditionalView(mState, Pull2SwitchWebViewState.RELEASE_2_NEXT);
			}

			//更新padding
			switch (mState) {
			case DONE:
				initTopPaddingTop();
				initBottomPaddingBottom();
				break;
			case PULL_DOWN_2_LAST:
			case RELEASE_2_LAST:
				int top = (int) ((-1 * mTopHeight) +  moveY / RATIO);
				setTopPaddingTop(top);
				initBottomPaddingBottom();
				break;
			case PULL_UP_2_NEXT:
			case RELEASE_2_NEXT:
				int bottom = (int) ((-1 * mBottomHeight) - (moveY / RATIO));
				initTopPaddingTop();
				setBottomPaddingBottom(bottom);
				break;

			default:
				break;
			}
			break;
		}
		return super.dispatchTouchEvent(event);
	}

	private void initEventStartData(MotionEvent event){
		refreshScrollDirection(mOldEvent, event);//判断滑动方向
		if (isReadyForPull() && !mIsRecored) {
			mStartY = event.getY();
			mIsRecored = true;
			mScrollDirection = ScrollDirection.NULL;
		}
	}

	private boolean isReadyForPull() {
		return isReadyForPullFromStart() || isReadyForPullFromEnd();
	}

	private boolean isReadyForPullFromStart() {
		if (mScrollDirection != ScrollDirection.TOP_2_BOTTOM)
			return false;
		float scrolly = mWebView.getScrollY();
		return scrolly <= 0;
	}

	@SuppressWarnings("deprecation")
	private boolean isReadyForPullFromEnd() {
		if (mScrollDirection != ScrollDirection.BOTTOM_2_TOP)
			return false;

		float scale = mWebView.getScale();
		float contentHeight = mWebView.getContentHeight();
		float height = mWebView.getHeight();
		float scrolly = mWebView.getScrollY();

		float exactContentHeight = FloatMath.floor(contentHeight * scale);
		return scrolly >= (exactContentHeight - height);
	}

	private ScrollDirection mScrollDirection;
	private enum ScrollDirection{
		NULL, TOP_2_BOTTOM, BOTTOM_2_TOP;
	}

	private void refreshScrollDirection(MotionEvent start, MotionEvent end) {
		if (Math.abs(end.getY() - start.getY()) < 5) {
			mScrollDirection = ScrollDirection.NULL;
			return;
		}

		GesttureDirection direction = CustomGlobal.getGesttureDirection(start, end);
		switch (direction) {
		case TOP:
		case TOP_LEFT:
		case TOP_RIGHT:
			mScrollDirection = ScrollDirection.BOTTOM_2_TOP;
			break;
		case BOTTOM:
		case BOTTOM_LEFT:
		case BOTTOM_RIGHT:
			mScrollDirection = ScrollDirection.TOP_2_BOTTOM;
			break;
		default:
			break;
		}
	}

	private void refreshAdditionalView(Pull2SwitchWebViewState state, Pull2SwitchWebViewState currentState){
		if (state == currentState) 
			return;
		refreshAdditionalViewByState(state);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void refreshAdditionalViewByState(Pull2SwitchWebViewState state) {
		switch (state) {
		case PULL_DOWN_2_LAST:
			if (mBack) {
				mBack = false;
				mTopIcon.clearAnimation();
				mTopIcon.startAnimation(mReverseAnimation);
			}
			break;
		case PULL_UP_2_NEXT:
			if (mBack) {
				mBack = false;
				mBottomIcon.clearAnimation();
				mBottomIcon.startAnimation(mReverseAnimation);
			}
			break;
		case RELEASE_2_LAST:
			mTopIcon.clearAnimation();
			mTopIcon.startAnimation(mAnimation);
			break;
		case RELEASE_2_NEXT:
			mBottomIcon.clearAnimation();
			mBottomIcon.startAnimation(mAnimation);
			break;
		case DONE://当前状态，done
			initTopPaddingTop();
			initBottomPaddingBottom();
			mTopIcon.clearAnimation();
			mBottomIcon.clearAnimation();
			reSetTopIconImageResource();
			reSetBottomIconImageResource();
			mTopIcon.setImageDrawable(mTopIcon.getDrawable());
			mBottomIcon.setImageDrawable(mBottomIcon.getDrawable());
			break;
		default:
			break;
		}
	}

	/**
	 * Sets the background color for this view.
	 * 
	 * @param color
	 *            the color of the background
	 */
	public void setTopBackgroundColor(int color) {
		mTop.setBackgroundColor(color);
	}

	/**
	 * Set the background to a given resource. The resource should refer to a
	 * Drawable object or 0 to remove the background.
	 * 
	 * @param resid
	 *            The identifier of the resource.
	 * @attr ref android.R.styleable#View_background
	 */
	public void setTopBackground(int resid) {
		mTop.setBackgroundResource(resid);
	}

	public void setTopBackground(Drawable background) {
		mTop.setBackground(background);
	}

	/**
	 * 设置top的图标标识
	 * 
	 * @param resId
	 */
	public void setTopIconImageResource(int resId) {
		mTopIconResourceId = resId;
		mTopIcon.setImageResource(resId);
	}

	private int mTopIconResourceId = 0;
	/**
	 * 重新设置top的图标标识
	 * 
	 * @param resId
	 */
	public void reSetTopIconImageResource() {
		if (mTopIconResourceId == 0)
			mTopIconResourceId = R.drawable.icon_switch_webview_top;
		setTopIconImageResource(mTopIconResourceId);
	}

	/**
	 * 设置Action的字体颜色
	 * 
	 * @param color
	 */
	public void setTopActionTextColor(int color) {
		mTopAction.setTextColor(color);
	}

	public void setTopActionTextColor(ColorStateList colors) {
		mTopAction.setTextColor(colors);
	}

	/**
	 * 设置title的字体颜色
	 * 
	 * @param color
	 */
	public void setTopTitleTextColor(int color) {
		mTopTitle.setTextColor(color);
	}

	public void setTopTitleTextColor(ColorStateList colors) {
		mTopTitle.setTextColor(colors);
	}

	public void setTopActionText(CharSequence text) {
		mTopAction.setText(text);
	}

	public void setTopActionText(int resid) {
		mTopAction.setText(resid);
	}

	/**
	 * 设置title的Text，String.xml中resid对应的string要有%n$d，并且n所指随着该替换字符的增多而增大(最小值为1)
	 * 
	 * @param resid
	 * @param formatArgs
	 */
	public void setTopActionText(int resid, Object... formatArgs) {
		setTopActionText(getContext().getString(resid, formatArgs));
	}

	public void setTopTitleText(CharSequence text) {
		mTopTitle.setText(text);
	}

	public void setTopTitleText(int resid) {
		mTopTitle.setText(resid);
	}

	/**
	 * 设置title的Text，String.xml中resid对应的string要有%n$d，并且n所指随着该替换字符的增多而增大(最小值为1)
	 * 
	 * @param resid
	 * @param formatArgs
	 */
	public void setTopTitleText(int resid, Object... formatArgs) {
		setTopTitleText(getContext().getString(resid, formatArgs));
	}

	/**
	 * Sets the background color for this view.
	 * 
	 * @param color
	 *            the color of the background
	 */
	public void setBottomBackgroundColor(int color) {
		mBottom.setBackgroundColor(color);
	}

	/**
	 * Set the background to a given resource. The resource should refer to a
	 * Drawable object or 0 to remove the background.
	 * 
	 * @param resid
	 *            The identifier of the resource.
	 * @attr ref android.R.styleable#View_background
	 */
	public void setBottomBackground(int resid) {
		mBottom.setBackgroundResource(resid);
	}

	public void setBottomBackground(Drawable background) {
		mBottom.setBackground(background);
	}

	/**
	 * 设置bottom的图标标识
	 * 
	 * @param resId
	 */
	public void setBottomIconImageResource(int resId) {
		mBottomIcon.setImageResource(resId);
	}

	private int mBottomIconResource = 0;
	/**
	 * 重新设置bottom的图标标识
	 * 
	 * @param resId
	 */
	public void reSetBottomIconImageResource() {
		if (mBottomIconResource == 0)
			mBottomIconResource = R.drawable.icon_switch_webview_bottom;
		setBottomIconImageResource(mBottomIconResource);
	}

	/**
	 * 设置Action的字体颜色
	 * 
	 * @param color
	 */
	public void setBottomActionTextColor(int color) {
		mBottomAction.setTextColor(color);
	}

	public void setBottomActionTextColor(ColorStateList colors) {
		mBottomAction.setTextColor(colors);
	}

	/**
	 * 设置title的字体颜色
	 * 
	 * @param color
	 */
	public void setBottomTitleTextColor(int color) {
		mBottomTitle.setTextColor(color);
	}

	public void setBottomTitleTextColor(ColorStateList colors) {
		mBottomTitle.setTextColor(colors);
	}

	public void setBottomActionText(CharSequence text) {
		mBottomAction.setText(text);
	}

	public void setBottomActionText(int resid) {
		mBottomAction.setText(resid);
	}

	/**
	 * 设置Action的Text，String.xml中resid对应的string要有%n$d，并且n所指随着该替换字符的增多而增大(最小值为1)
	 * 
	 * @param resid
	 * @param formatArgs
	 */
	public void setBottomActionText(int resid, Object... formatArgs) {
		setBottomActionText(getContext().getString(resid, formatArgs));
	}

	public void setBottomTitleText(CharSequence text) {
		mBottomTitle.setText(text);
	}

	public void setBottomTitleText(int resid) {
		mBottomTitle.setText(resid);
	}

	/**
	 * 设置title的Text，String.xml中resid对应的string要有%n$d，并且n所指随着该替换字符的增多而增大(最小值为1)
	 * 
	 * @param resid
	 * @param formatArgs
	 */
	public void setBottomTitleText(int resid, Object... formatArgs) {
		setBottomTitleText(getContext().getString(resid, formatArgs));
	}

	/**
	 * @return the mWebView
	 */
	public WebView getWebView() {
		return mWebView;
	}

	private OnPullListener mOnPullListener;
	public void setOnPullListener(OnPullListener listener){
		mOnPullListener = listener;
	}

	public static interface OnPullListener{
		public void onPullDownFromTop();
		public void onPullUpFromBottom();
	}
}
