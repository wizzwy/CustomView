package com.custom;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;

/**
 * @Author: zwy
 * @E-mail: weiyazhang1987@gmail.com
 * @time Create Date: 2013-9-25下午4:45:07
 * @Message: 控件常用的一些操作接口
 **/
public class CustomGlobal {

	/**
	 * 因为是在构造函数里测量高度，应该先measure一下
	 * @param view
	 */
	@SuppressWarnings("deprecation")
	public static void measureView(View view) {
		ViewGroup.LayoutParams params = view.getLayoutParams();
		if (params == null)
			params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, params.width);
		int lpHeight = params.height;
		int childHeightSpec;
		if (lpHeight > 0)
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		else
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);

		view.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * 精准的方向
	 * @author zwy
	 */
	public enum GesttureDirection {
		LEFT, RIGHT, TOP, BOTTOM, LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, MIDDLE_LEFT_TOP, MIDDLE_LEFT_BOTTOM, MIDDLE_RIGHT_TOP, MIDDLE_RIGHT_BOTTOM, NULL;
	}

	/**
	 * 获取一个较为精准的方向，精确度为45读
	 */
	public static GesttureDirection getGesttureDirection(MotionEvent start, MotionEvent end) {
		float startX = start.getRawX();
		float startY = start.getRawY();
		float endX = end.getRawX();
		float endY = end.getRawY();
		float moveX = endX - startX;
		float moveY = endY - startY;
		float absMoveX = Math.abs(moveX);
		float absMoveY = Math.abs(moveY);

		if (absMoveX < 5 && absMoveY < 5) {
			return GesttureDirection.NULL;
		} else if (absMoveX < 5 && moveY < -5) {
			return GesttureDirection.TOP;
		} else if (absMoveX < 5 && moveY > 5) {
			return GesttureDirection.BOTTOM;
		} else if (absMoveY < 5 && moveX < -5) {
			return GesttureDirection.LEFT;
		} else if (absMoveY < 5 && moveX > 5) {
			return GesttureDirection.RIGHT;
		} else if (absMoveY == absMoveX && moveX < -5 && moveY < -5) {
			return GesttureDirection.MIDDLE_LEFT_TOP;
		} else if (absMoveY == absMoveX && moveX < -5 && moveY > 5) {
			return GesttureDirection.MIDDLE_LEFT_BOTTOM;
		} else if (absMoveY == absMoveX && moveX > 5 && moveY < -5) {
			return GesttureDirection.MIDDLE_RIGHT_TOP;
		} else if (absMoveY == absMoveX && moveX > 5 && moveY > 5) {
			return GesttureDirection.MIDDLE_RIGHT_BOTTOM;
		} else if (moveX < -5 && moveY < -5 && absMoveY > absMoveX) {
			return GesttureDirection.TOP_LEFT;
		} else if (moveX < -5 && moveY < -5 && absMoveY < absMoveX) {
			return GesttureDirection.LEFT_TOP;
		} else if (moveX < -5 && moveY > 5 && absMoveY > absMoveX) {
			return GesttureDirection.BOTTOM_LEFT;
		} else if (moveX < -5 && moveY > 5 && absMoveY < absMoveX) {
			return GesttureDirection.LEFT_BOTTOM;
		} else if (moveX > 5 && moveY < -5 && absMoveY > absMoveX) {
			return GesttureDirection.TOP_RIGHT;
		} else if (moveX > 5 && moveY < -5 && absMoveY < absMoveX) {
			return GesttureDirection.RIGHT_TOP;
		} else if (moveX > 5 && moveY > 5 && absMoveY > absMoveX) {
			return GesttureDirection.BOTTOM_RIGHT;
		} else if (moveX > 5 && moveY > 5 && absMoveY < absMoveX) {
			return GesttureDirection.RIGHT_BOTTOM;
		}
		return GesttureDirection.NULL;
	}

	/**
	 * 标准的方向，东南西北四个方向
	 * @author zwy
	 */
	public enum GesttureNormalDirection {
		LEFT, RIGHT, TOP, BOTTOM, NULL;
	}

	/**
	 * 获取一个较为标准的方向，把45度便宜方向归结为东南西北四个方向
	 */
	public static GesttureNormalDirection getGesttureNormalDirection(MotionEvent start, MotionEvent end) {
		switch (CustomGlobal.getGesttureDirection(start, end)) {
		case LEFT:
		case LEFT_TOP:
		case LEFT_BOTTOM:
			return GesttureNormalDirection.LEFT;
		case RIGHT:
		case RIGHT_TOP:
		case RIGHT_BOTTOM:
			return GesttureNormalDirection.RIGHT;
		case TOP:
		case TOP_LEFT:
		case TOP_RIGHT:
			return GesttureNormalDirection.TOP;
		case BOTTOM:
		case BOTTOM_LEFT:
		case BOTTOM_RIGHT:
			return GesttureNormalDirection.BOTTOM;
		default:
			return GesttureNormalDirection.NULL;
		}
	}

	/**
	 * 通过X方向移动距离判断是否跳过该次事件的判断
	 * @param oldEvent 上一次事件
	 * @param newEvent 当前事件
	 * @param criticalDistance 临界距离
	 * @return
	 */
	public static boolean skipCurrentMotionEventByRawX(MotionEvent oldEvent, MotionEvent newEvent, int criticalDistance){
		return Math.abs(oldEvent.getRawX() - newEvent.getRawX()) <= criticalDistance;
	}

	/**
	 * 通过Y方向移动距离判断是否跳过该次事件的判断
	 * @param oldEvent 上一次事件
	 * @param newEvent 当前事件
	 * @param criticalDistance 临界距离
	 * @return
	 */
	public static boolean skipCurrentMotionEventByRawY(MotionEvent oldEvent, MotionEvent newEvent, int criticalDistance){
		return Math.abs(oldEvent.getRawY() - newEvent.getRawY()) <= criticalDistance;
	}
}
