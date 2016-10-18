package pervacio.com.signalstrength.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import pervacio.com.signalstrength.R;
import pervacio.com.signalstrength.ui.fragment.PlaceholderFragment;
import pervacio.com.signalstrength.ui.fragment.MainContentFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private int[] imageResId;
    private Context mContext;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        TypedArray icons = mContext.getResources().obtainTypedArray(R.array.tab_icons);
        int tabsCount = icons.length();
        imageResId = new int[tabsCount];
        for (int i = 0; i < tabsCount; i++) {
            imageResId[i] = icons.getResourceId(i, 0);
        }
        icons.recycle();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return MainContentFragment.newInstance(0);
        }
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return imageResId.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        Drawable image = mContext.getResources().getDrawable(imageResId[position]);
        DrawableCompat.setTint(image, mContext.getResources().getColor(R.color.tab_color));
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   ");// + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}