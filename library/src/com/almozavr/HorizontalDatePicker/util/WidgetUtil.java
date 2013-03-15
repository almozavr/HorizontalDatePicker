package com.almozavr.HorizontalDatePicker.util;

import android.content.res.TypedArray;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WidgetUtil {

    /**
     * Use it for enum which defines custom view attr flags.
     */
    public interface AttrValue {

        public int getAttrIndex();
    }

    /**
     * Parse custom view attr valueIndexes (flags).<br>
     * E.g. you want your custom view with attr to work like that: <br>
     * <code>
     * <com.dating.sdk.ui.widget.UserActionsSection
     * android:layout_width="fill_parent"
     * android:layout_height="wrap_content"
     * <b>app:action="chat|mail|wink|icebreaker" /></b></code> <br>
     * E.g. You've got custom stylable with attr which works like
     * list of available flags. <br>
     * <code><declare-styleable title="UserActions">
     * <attr title="action">
     * <flag title="chat" value="1" />
     * <flag title="mail" value="2" />
     * <flag title="wink" value="4" />
     * <flag title="icebreaker" value="8" />
     * </attr>
     * </declare-styleable></code> <br>
     * <b>Caveat: flag valueIndexes should be power of 2 (2,4,8,16 etc)</b>
     * @param a
     *            attrs of view to search for attrName
     * @param attrName
     *            custom attr with flag valueIndexes
     * @param values
     *            enum's valueIndexes. enum should implement {@code AttrValue}
     * @return list of available (declared in xml) view's valueIndexes
     */
    public static <U extends AttrValue> Collection<U> readAttrValues(TypedArray a, int attrName, U[] values) {
        Set<U> availableValues = new HashSet<U>();
        int valuesSumIndex = a.getInteger(attrName, 0);
        if (valuesSumIndex != 0) {
            for (U action : values) {
                int actionIndex = action.getAttrIndex();
                if ((valuesSumIndex & actionIndex) == actionIndex)
                    availableValues.add(action);
            }
        }
        return availableValues;
    }
}
