package com.example.myhistorycleaner.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ScaleXSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;

import com.example.myhistorycleaner.R;

import java.util.ArrayList;


public class CustomButton extends AppCompatButton {

    public static final String FONT_PATH = "fonts/";
    private Context context;
    private int textFont;
    private int lineHeight = 1;
    private int lineSpacing = 1;
    private int letterSpacing = 1;
    private String textFontType;
    private boolean strike;

    /**
     * Constructor with 1 params context
     *
     * @param context
     */
    public CustomButton(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Constructor with 2 params context and attrs
     *
     * @param context
     * @param attrs
     */

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initCustomText(context, attrs);
    }

    /**
     * Constructor with 3 params context, attrs and defStyleAttr
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initCustomText(context, attrs);
    }

    /**
     * Initializes all the attributes and respective methods are called based on the attributes
     *
     * @param context
     * @param attrs
     */

    private void initCustomText(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        textFont = ta.getInt(R.styleable.CustomButton_custom_font, 0);
        lineSpacing = ta.getDimensionPixelSize(R.styleable.CustomButton_btn_line_spacing, lineSpacing);
        letterSpacing = ta.getDimensionPixelSize(R.styleable.CustomButton_btn_letter_spacing, letterSpacing);
        lineHeight = ta.getDimensionPixelSize(R.styleable.CustomButton_btn_line_height, lineHeight);
        strike = ta.getBoolean(R.styleable.CustomTextView_strike, false);

        /**
         * A custom view uses isInEditMode() to determine whether or not it is being rendered inside the editor
         * and if so then loads test data instead of real data.
         */
        if (!isInEditMode()) {


            switch (textFont) {

                case 1:
                    textFontType = "avenirLight.otf";
                    break;

                case 2:
                    textFontType = "avenirMedium.otf";
                    break;

                default:
                    textFontType = "avenirLight.otf";
                    break;
            }
            setTextFont(textFontType);
            if (strike) {
                setTextStriked();
            }
            ta.recycle();
        }
    }

    /**
     * Set the drawable background
     *
     * @param gradientDrawable
     */
    private void setTheDrawable(GradientDrawable gradientDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(gradientDrawable);
        } else {
            setBackgroundDrawable(gradientDrawable);
        }
    }

    /**
     * Sets the type of text font that is to be applied to the TextView
     *
     * @param fontType
     */
    public void setTextFont(String fontType) {
        setTypeface(Typeface.createFromAsset(context.getResources()
                .getAssets(), FONT_PATH + fontType));
    }


    /**
     * Changes the color of a single character in a text at an index
     *
     * @param text
     * @param index
     * @param color
     */
    public void setTextSpannableSingleChar(String text, int index, int color) {
        Spannable word = new SpannableString(text);
        word.setSpan(new ForegroundColorSpan(color), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(word);
    }

    /**
     * Changes the color of a section of characters or a group of characters in a text starting from start index to end index
     *
     * @param text
     * @param startIndex
     * @param endIndex
     * @param color
     */
    public void setTextSpannableSectionOfChars(String text, int startIndex, int endIndex, int color) {
        Spannable word = new SpannableString(text);
        word.setSpan(new ForegroundColorSpan(color), startIndex, endIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(word);
    }


    /**
     * Underlines the text from start index to end index
     *
     * @param text
     * @param startIndex
     * @param endIndex
     */
    public void setTextUnderlined(String text, int startIndex, int endIndex) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), startIndex, endIndex + 1, 0);
        setText(content);
    }

    /**
     * Sets the entire text underlined
     *
     * @param text
     */
    public void setEntireTextUnderlined(String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        setText(content);
    }

    /**
     * Strikes the text
     */
    public void setTextStriked() {
        setPaintFlags(getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * Set letter spacing for the text
     *
     * @param text
     * @param letterSpace
     */
    public void setTextLetterSpacing(String text, float letterSpace) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("letter spacing", "Lollipop");
            setLetterSpacing(letterSpace);
        } else {
            Log.e("letter spacing", "below Lollipop");
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

    /**
     * Returns a single String separated with comma while passing an array list of strings
     *
     * @param selectedItems
     * @return
     */
    private static String getStringFromArr(ArrayList<String> selectedItems) {
        StringBuilder finalSelectedText = new StringBuilder();
        for (int j = 0; j < selectedItems.size(); j++) {
            finalSelectedText.append(selectedItems.get(j));
            if (j + 1 != selectedItems.size()) {
                finalSelectedText.append(", ");
            }
        }
        return finalSelectedText.toString();
    }

}