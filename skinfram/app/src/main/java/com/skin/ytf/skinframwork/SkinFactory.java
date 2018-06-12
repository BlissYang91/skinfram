package com.skin.ytf.skinframwork;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YTF
 * 布局监听器
 */
public class SkinFactory implements LayoutInflaterFactory {
    private List<SkinView> skinViewList = new ArrayList<>();
    private static final String TAG = "YTFSkin";
    private static final String[] prxfixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        Log.i(TAG, "onCreateView: YTFSkin" + name);
        View view = null;
        if (name.contains(".")) {
            view = creatView(context, attrs, name);
        }
        for (String pre : prxfixList) {
            view = creatView(context, attrs, pre + name);
            if (view != null) {
                break;
            }
        }
//        收集需要换肤的控件
        if (view != null) {
            parseView(context, attrs, view);
//            其中attrs是把xml文件中控件的属性和值以键值对的形式统一存于AttributeSet中
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void apply() {
        for (SkinView skinView : skinViewList) {
            skinView.apply();

        }
    }

    //需要换肤的view集合
    class SkinView {
        private View view;
        private List<SkinItem> list;

        public SkinView(View view, List<SkinItem> list) {
            this.view = view;
            this.list = list;
        }

        //        换肤开关
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void apply() {
            for (SkinItem skinItem : list) {
                if (skinItem.getAttrName().equals("background")) {
                    if ("color".equals(skinItem.getTypeName())) {
                        view.setBackgroundColor(SkinManger.getInstance().getColor(skinItem.getRefId()));

                    } else if ("drawable".equals(skinItem.getTypeName()) || "mipmap".equals(skinItem.getTypeName())) {
                        view.setBackground(SkinManger.getInstance().getDrawable(skinItem.getRefId()));
                    }
                }
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void parseView(Context context, AttributeSet attrs, View view) {
        List<SkinItem> list = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
//            获取每一个控件下属性的键和值
            String attrName = attrs.getAttributeName(i);
            String idValue = attrs.getAttributeValue(i);//@mipmap/ic_launcher @0x01214 引用属性获取的是16进制码的ID
            Log.i("ytf_idValue", "name: " + idValue + "idValue" + idValue);
            if (attrName.contains("background")) {
                int id = Integer.parseInt(idValue.substring(1));
                String typeName = context.getResources().getResourceTypeName(id);
                String entryName = context.getResources().getResourceEntryName(id);
//                注意此处APP中的typeName和entryName一定要与皮肤包中名字相同，不然在皮肤包中就会找不到对应名字的drawable图片
                SkinItem skinItem = new SkinItem(attrName, id, entryName, typeName);
                list.add(skinItem);

            }
        }
        if (!list.isEmpty()) {
            SkinView skinView = new SkinView(view, list);
            skinViewList.add(skinView);
            skinView.apply();
        }
    }

    //单个皮肤属性
    class SkinItem {
        String attrName;
        int refId;
        String entryName;
        String typeName;

        public SkinItem(String attrName, int refId, String entryName, String typeName) {
            this.attrName = attrName;
            this.refId = refId;
            this.entryName = entryName;
            this.typeName = typeName;
        }

        public String getAttrName() {
            return attrName;
        }

        public void setAttrName(String attrName) {
            this.attrName = attrName;
        }

        public int getRefId() {
            return refId;
        }

        public void setRefId(int refId) {
            this.refId = refId;
        }

        public String getEntryName() {
            return entryName;
        }

        public void setEntryName(String entryName) {
            this.entryName = entryName;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

    private View creatView(Context context, AttributeSet attrs, String className) {
        View view=null;
        try {
            Class viewClazz = context.getClassLoader().loadClass(className);
            Constructor<? extends View> constructor = viewClazz.getConstructor(new Class[]{
                    Context.class, AttributeSet.class});
            view=constructor.newInstance(context, attrs);
            return view;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}
