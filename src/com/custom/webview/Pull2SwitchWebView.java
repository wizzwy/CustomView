package com.custom.webview;

import com.custom.CustomGlobal;
import com.custom.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
public class Pull2SwitchWebView extends LinearLayout {

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

		//计算控件的高度
		CustomGlobal.measureView(mTop);
		CustomGlobal.measureView(mBottom);
		mTopHeight = mTop.getMeasuredHeight();
		mBottomHeight = mBottom.getMeasuredHeight();

		//设置padding
		mTop.setPadding(mTop.getPaddingLeft(), -1 * mTopHeight, mTop.getPaddingRight(), 0);
		mTop.invalidate();
		mTop.setEnabled(false);
		mBottom.setPadding(mBottom.getPaddingLeft(), 0, mBottom.getPaddingRight(), -1 * mBottomHeight);
		mBottom.invalidate();
		mBottom.setEnabled(false);
		
		
	}

	private void initAttrs(AttributeSet attrs) {

		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.Pull2SwitchWebView);
		if (typedArray.hasValue(R.styleable.Pull2SwitchWebView_topBackgroundColor)) {
			setTopBackgroundColor(typedArray.getColor(R.styleable.Pull2SwitchWebView_topBackgroundColor, android.R.color.darker_gray));
		}
		setTopBackground(typedArray.getDrawable(R.styleable.Pull2SwitchWebView_topBackground));
		typedArray.recycle();
	}

	private enum Pull2SwitchWebViewState{
		RELEASE_TO_REFRESH, PULL_TO_REFRESH, DONE
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			if (mFirstItemIndex == 0 && !mIsRecored) {
//				mIsRecored = true;
//				mStartY = (int) event.getY();
//			}
//			break;
//
//		case MotionEvent.ACTION_UP:
//			if(mState != PullToRefreshListViewState.REFRESHING) {
//				switch (mState) {
//				case PULL_TO_REFRESH:
//					mState = PullToRefreshListViewState.DONE;
//					//由下拉刷新状态到done状态
//					changeHeaderViewByState();
//					break;
//				case RELEASE_TO_REFRESH:
//					mState = PullToRefreshListViewState.REFRESHING;
//					//由松开刷新状态到done状态
//					changeHeaderViewByState();
//					onRefresh();
//					break;
//				default:
//					break;
//				}
//			}
//			mIsRecored = false;
//			mIsBack = false;
//			break;
//		case MotionEvent.ACTION_MOVE:
//			int moveY = (int) event.getY();
//			if(!mIsRecored && mFirstItemIndex == 0){
//				mIsRecored = true;
//				mStartY = moveY;
//			}
//			int y = moveY - mStartY;
//			// &&  y> 100
//			if(mIsRecored && mState != PullToRefreshListViewState.REFRESHING){
//				//保证在设置padding的过程中，当前的位置一直实在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
//				//可以松手去刷新了
//				if(mState == PullToRefreshListViewState.RELEASE_TO_REFRESH) {
//					setSelection(0);//指定选定的位置 headView
//					
//					//往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
//					if((y / RATIO < mHeadContentHeight ) && y > 0){
//						//由松开刷新状态转变到下拉刷新状态
//						mState = PullToRefreshListViewState.PULL_TO_REFRESH;
//						changeHeaderViewByState();
//					}
//					//一下子推到顶了
//					else if(y <= 0){
//						//由松开刷新状态转变到done状态
//						mState = PullToRefreshListViewState.DONE;
//						changeHeaderViewByState();
//					}
//					//往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
//					else {
//						//不用进行特别的操作，只用更新paddingTop的值就行了
//						mHeadView.setPadding(0, y / RATIO - mHeadContentHeight, 0, 0);
//					}
//				}
//				
//				//还没有到达显示松开刷新的时候，DONE或者是PULL_TO_REFRESH状态
//				if(mState == PullToRefreshListViewState.PULL_TO_REFRESH){
//					setSelection(0);
//					
//					//下拉到可以进入RELEASE_TO_REFRESH的状体
//					if(y / RATIO >= mHeadContentHeight ){
//						//由DONE或者下拉刷新状态 转变到松开刷新
//						mState = PullToRefreshListViewState.RELEASE_TO_REFRESH;
//						mIsBack = true;
//						changeHeaderViewByState();
//					}
//					//上推到顶了
//					else if(y <= 0){
//						//由DONE或者下拉刷新状态转变到DONE状态
//						mState = PullToRefreshListViewState.DONE;
//						changeHeaderViewByState();
//					}
//				}
//				
//				//DONE状态下
//				if(mState == PullToRefreshListViewState.DONE) {
//					if(y > 0){
//						//由DONE状态转变到下拉刷新状态
//						mState = PullToRefreshListViewState.PULL_TO_REFRESH;
//						changeHeaderViewByState();
//					}
//				}
//				//更新headView的size
//				if(mState == PullToRefreshListViewState.PULL_TO_REFRESH) {
//					mHeadView.setPadding(0, -1 * mHeadContentHeight + y / RATIO, 0, 0);
//					
//				}
//			}
//			break;
//		}
		return super.onTouchEvent(event);
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
		mTopIcon.setImageResource(resId);
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

}
