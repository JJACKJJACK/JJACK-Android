package com.mkm.hanium.jjack.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.mkm.hanium.jjack.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by MIN on 2017-06-03.
 */

public class ExtraUserPropertyLayout extends FrameLayout
    implements AdapterView.OnItemSelectedListener {

    /**
     * 추가 정보를 받기 위한 레이아웃을 설정하는 부분
     */

    // property key
    private static final String YEAR_KEY = "year";
    private static final String GENDER_KEY  = "gender";

    private Context context;
    private Spinner gender;
    private Spinner year;

    private int spin;
    private boolean[] flag;

    public ExtraUserPropertyLayout(Context context) {
        super(context);
        initView(context);
    }

    public ExtraUserPropertyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ExtraUserPropertyLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        final View view = inflate(getContext(), R.layout.layout_signup_extra_user_property, this);
        year = (Spinner) view.findViewById(R.id.spinner_year);
        gender = (Spinner) view.findViewById(R.id.spinner_gender);
        this.context = context;
        flag = new boolean[2];

        setSpinnerYear();
        setSpinnerGender();
    }

    private void setSpinnerYear() {
        ArrayList<String> list = new ArrayList<>();
        final int y = Calendar.getInstance().get(Calendar.YEAR); // 현재 연도를 받아옴

        list.add("태어난 해");
        for(int i = 1960; i <= y; i++)
            list.add(Integer.toString(i));

        ArrayAdapter adapter = new ArrayAdapter(getContext(),
                R.layout.simple_dropdown_item_1line,
                list);
        year.setSelection(0);
        year.setAdapter(adapter);
        year.setOnItemSelectedListener(this);
    }

    private void setSpinnerGender() {
        ArrayList<String> list = new ArrayList<>();
        list.add("성별");
        list.add("남자");
        list.add("여자");

        ArrayAdapter adapter = new ArrayAdapter(getContext(),
                R.layout.simple_dropdown_item_1line,
                list);
        gender.setSelection(0);
        gender.setAdapter(adapter);
        gender.setOnItemSelectedListener(this);
    }

    public HashMap<String, String> getProperties(){
        final String yearValue = String.valueOf(year.getSelectedItem());
        final String genderValue = String.valueOf(gender.getSelectedItem());

        HashMap<String, String> properties = new HashMap<>();

        if(yearValue != null)
            properties.put(YEAR_KEY, yearValue);

        if(genderValue != null)
            properties.put(GENDER_KEY, genderValue);

        return properties;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinner_year)
            spin = 0;
        else if(parent.getId() == R.id.spinner_gender)
            spin = 1;

        if(position == 0) {
            flag[spin] = false;
        } else {
            flag[spin] = true;
        }

        // 두 스피너 모두 정상적인 값이 선택되어야 버튼이 활성화됨
        ((SignupActivity) context).setSignupBtnEnable(flag[0] && flag[1]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
