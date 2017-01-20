package com.example.myhistorycleaner.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ScaleXSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;


import com.example.myhistorycleaner.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CustomTextView extends AppCompatTextView
{

    public static final String FONT_PATH = "fonts/";
    private Context context;
    private int textFont;
    private int lineHeight = 1;
    private int lineSpacing = 1;
    private int letterSpacing = 1;
    private String textFontType;
    private boolean strike;
    /*
     * Caches typefaces based on their file path and name, so that they don't have to be created
     * every time when they are referenced.
     */
    private static Map<String, Typeface> mTypefaces;

    /**
     * Constructor with 1 params context
     *
     * @param context
     */
    public CustomTextView(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Constructor with 2 params context and attrs
     *
     * @param context
     * @param attrs
     */
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (mTypefaces == null) {
            mTypefaces = new HashMap<String, Typeface>();
        }
        initCustomText(context, attrs);
    }

    /**
     * Constructor with 3 params context, attrs and defStyleAttr
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        textFont = ta.getInt(R.styleable.CustomTextView_custom_font, 0);
        lineSpacing = ta.getDimensionPixelSize(R.styleable.CustomTextView_line_spacing, lineSpacing);
        letterSpacing = ta.getDimensionPixelSize(R.styleable.CustomTextView_letter_spacing, letterSpacing);
        lineHeight = ta.getDimensionPixelSize(R.styleable.CustomTextView_line_height, lineHeight);
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
     * Sets two different colors to a string by passing a character. All the characters upto the passed Character set to
     * one color and the remaining characters after the passed character are set to a different color.
     *
     * @param text
     * @param character
     * @param color1
     * @param color2
     * @param firstColor
     */
    public void setTextSpannable(String text, String character, int color1, int color2, boolean firstColor) {
        int splitColor;
        String[] splitString = text.split(character);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        if (firstColor) {
            splitColor = color1;
        } else {
            splitColor = color2;
        }
        if (splitString.length == 2) {
            try {
                SpannableString firstSpannable = new SpannableString(splitString[0]);
                firstSpannable.setSpan(new ForegroundColorSpan(color1), 0, splitString[0].length(), 0);
                stringBuilder.append(firstSpannable);
                SpannableString splitChar = new SpannableString(character);
                splitChar.setSpan(new ForegroundColorSpan(splitColor), 0, character.length(), 0);
                stringBuilder.append(splitChar);
                SpannableString secondSpannable = new SpannableString(splitString[1]);
                secondSpannable.setSpan(new ForegroundColorSpan(color2), 0, splitString[1].length(), 0);
                stringBuilder.append(secondSpannable);
                setText(stringBuilder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets different colors to a string by passing a character. All the characters upto the passed Character set to
     * different colors.
     *
     * @param text
     * @param character
     * @param colors
     */
    public void setTextSpannable2(String text, String character, int[] colors) {
        String[] splitString = text.split(character);
        int color;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        try {
            for (int i = 0; i < splitString.length; i++) {
                if (splitString.length == colors.length) {
                    color = colors[i];
                } else if (colors.length >= 1) {
                    color = colors[0];
                } else {
                    color = Color.BLUE;
                }
                SpannableString firstSpannable = new SpannableString(splitString[i]);
                firstSpannable.setSpan(new ForegroundColorSpan(color), 0, splitString[i].length(), 0);
                stringBuilder.append(firstSpannable);
                if (splitString.length - 1 > i) {
                    SpannableString splitChar = new SpannableString(character);
                    stringBuilder.append(splitChar);
                }
                setText(stringBuilder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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