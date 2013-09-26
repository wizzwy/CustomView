package com.custom;

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

}
