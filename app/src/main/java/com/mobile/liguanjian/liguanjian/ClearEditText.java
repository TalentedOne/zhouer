package com.mobile.liguanjian.liguanjian;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;

public class ClearEditText extends AppCompatEditText implements OnFocusChangeListener,
        TextWatcher {

    private String backUp = "";
    private RightIcon rightIcon = RightIcon.Gone;
    /**
     * 删除按钮的引用
     */
    private Drawable drawable_clear;

    private Drawable drawable_reDo;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        drawable_clear = getCompoundDrawables()[2];

        if (drawable_clear == null)
            drawable_clear = getResources().getDrawable(R.drawable.edtv_cancel);
        if (drawable_reDo == null)
            drawable_reDo = getResources().getDrawable(R.drawable.restore);
        // drawable_clear.getIntrinsicHeight()
        drawable_clear.setBounds(
                0,
                0,
                getEditTextDrawableSide(),
                getEditTextDrawableSide());
        drawable_reDo.setBounds(
                0,
                0,
                getEditTextDrawableSide(),
                getEditTextDrawableSide());
        setClearIconVisibility(RightIcon.Gone);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    private int getEditTextDrawableSide() {
        return (int) (20 * getResources().getDisplayMetrics().density);
    }

    public void setDrawableRight(int id) {
        drawable_clear = getResources().getDrawable(id);
        invalidate();
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null)
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean isX_AxisTouchable =
                        event.getX() > (getWidth() - getPaddingRight() - getEditTextDrawableSide()) &&
                                (event.getX() < ((getWidth() - getPaddingRight())));

                if (isX_AxisTouchable)
                    if (rightIcon == RightIcon.ReDo)
                        setText(backUp);
                    else if (rightIcon == RightIcon.Clear) {
                        backUp = getText().toString();
                        setText("");
                    }
            }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)
            setClearIconVisibility(getText().length() > 0 ? RightIcon.Clear : RightIcon.ReDo);
        else
            setClearIconVisibility(RightIcon.Gone);
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param rightIcon
     */
    protected void setClearIconVisibility(RightIcon rightIcon) {
        this.rightIcon = rightIcon;
        Drawable right = rightIcon == RightIcon.Clear ? drawable_clear :
                rightIcon == RightIcon.ReDo ? drawable_reDo : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        setClearIconVisibility(s.length() > 0 ? RightIcon.Clear : RightIcon.ReDo);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private enum RightIcon {
        Clear, ReDo, Gone
    }
}
