
package com.example.myhistorycleaner.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;

import com.example.myhistorycleaner.R;

public class CustomEditText extends AppCompatEditText {

    public static final String FONT_PATH = "fonts/";
    private Context context;
    private int textFont;
    private int lineHeight = 1;
    private String textFontType;
    private int lineSpacing = 1;
    private int letterSpacing = 1;

    /**
     * Constructor with one param
     *
     * @param context
     */
    public CustomEditText(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Constructor with two params
     *
     * @param context
     * @param attrs
     */
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initCustomEditText(context, attrs);
    }

    /**
     * Constructor with three params
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initCustomEditText(context, attrs);
    }

    /**
     * @param context
     * @param attrs   This method initializes all the attributes and respective methods are called based on the attributes
     */
    private void initCustomEditText(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        textFont = ta.getInt(R.styleable.CustomEditText_custom_font, 0);
        lineHeight = ta.getDimensionPixelSize(R.styleable.CustomEditText_edt_txt_line_height, lineHeight);
        lineSpacing = ta.getDimensionPixelSize(R.styleable.CustomEditText_edt_txt_line_spacing, lineSpacing);
        letterSpacing = ta.getDimensionPixelSize(R.styleable.CustomEditText_edt_txt_letter_spacing, letterSpacing);
        /**
         * A custom view uses isInEditMode() to determine whether or not it is being rendered inside the editor
         * and if so then loads test data instead of real data.
         */
        if (!isInEditMode()) {

            switch (textFont) {

                case 1:
                    textFontType = "AvenirNextLTProCn.otf";
                    break;

                default:
                    textFontType = "AvenirNextLTProCn.otf";
                    break;
            }
            setTextFont(textFontType);
            ta.recycle();
        }
    }

    /**
     * Sets the type of text font that is to be applied to the TextView
     *
     * @param textFontType
     */
    public void setTextFont(String textFontType) {
        setTypeface(Typeface.createFromAsset(context.getResources()
                .getAssets(), FONT_PATH + textFontType));
    }

    private void setTheDrawable(GradientDrawable gradientDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            setBackground(gradientDrawable);
        else
            setBackgroundDrawable(gradientDrawable);
    }


    /**
     * Set letter spacing for the text
     *
     * @param text
     * @param letterSpace
     */
    public void setTextLetterSpacing(String text, float letterSpace) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setLetterSpacing(letterSpace);
        } else {
            if (context == null || text == null) return;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                String c = "" + text.charAt(i);
                builder.append(c.toLowerCase());
                if (i + 1 < text.length()) {
                    builder.append("\u00A0");
                }
            }
            SpannableString finalText = new SpannableString(builder.toString());
            if (builder.toString().length() > 1) {
                for (int i = 1; i < builder.toString().length(); i += 2) {
                    finalText.setSpan(new ScaleXSpan((letterSpace + 1)), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            super.setText(finalText, BufferType.SPANNABLE);
        }
    }

}