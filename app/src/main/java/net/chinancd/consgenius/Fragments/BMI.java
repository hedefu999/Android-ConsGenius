package net.chinancd.consgenius.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.chinancd.consgenius.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hedefu
 * on 2016 0411 at 16:31 .
 * email:hedefu999@gmail.com
 */
public class BMI extends Fragment implements OnClickListener {
    private static final String TAG="BMI";
    private static int int_gender = 0;
    private TextView warning;
    private EditText et_ID;
    private EditText et_name;
    private EditText et_age;
    private EditText et_height;
    private EditText et_weight;
    private Button button_reset;
    private Button button_confirm;
    private RadioGroup rg_gender;
    Bundle bundle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View bmi_view = inflater.inflate(R.layout.bmi_frag_layout, null);
        button_reset = (Button) bmi_view.findViewById(R.id.bmi_bn_reset);
        button_reset.setOnClickListener(this);
        button_confirm = (Button) bmi_view.findViewById(R.id.bmi_bn_confirm);
        button_confirm.setOnClickListener(this);
        warning = (TextView) bmi_view.findViewById(R.id.warning);
        et_ID = (EditText) bmi_view.findViewById(R.id.bmi_id_et);
        et_name = (EditText) bmi_view.findViewById(R.id.bmi_name_et);
        et_age = (EditText) bmi_view.findViewById(R.id.bmi_age_et);
        et_height = (EditText) bmi_view.findViewById(R.id.bmi_height_et);
        et_weight = (EditText) bmi_view.findViewById(R.id.bmi_weight_et);
        rg_gender = (RadioGroup) bmi_view.findViewById(R.id.bmi_rg_gender);

        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    warning.setText(getString(R.string.bmi_info_name_hint));
                } else {
                    warning.setText("");
                }
            }
        });
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bmi_female:
                        int_gender = 1;
                        break;
                    case R.id.bmi_male:
                        int_gender = 2;
                        break;
                }

            }
        });
        return bmi_view;
    }

    private void showToast(int StringID) {
        Toast.makeText(getActivity(), getString(StringID), Toast.LENGTH_SHORT).show();
    }

    private int CheckInput(String ID, String name, int gender,
                           String age, String height, String weight) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        boolean name_check=true;
        for (int i=0;i<name.length();i++){
            String temp=name.charAt(i)+"";
            Matcher matcher=pattern.matcher(temp);
            if(!matcher.matches()){
               name_check=false;
            }
        }
        if (ID.equals("") || name.equals("") || ID.length() < 17 || name.length() > 4 ||
                name.length()<2|| !name_check) {
            return 0;
        } else if (gender == 0) {
            return 1;
        } else if (age.equals("")) {
            return 2;
        } else if (height.equals("")) {
            return 3;
        } else if (weight.equals("")) {
            return 4;
        } else {
            return 5;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bmi_bn_reset:
                et_ID.setText("");
                et_name.setText("");
                et_height.setText("");
                et_weight.setText("");
                et_age.setText("");
                rg_gender.clearCheck();
                break;
            case R.id.bmi_bn_confirm:
                String str_ID = et_ID.getText().toString();
                String str_name = et_name.getText().toString();
                String str_age = et_age.getText().toString();
                String str_height = et_height.getText().toString();
                String str_weight = et_weight.getText().toString();
                switch (CheckInput(str_ID, str_name, int_gender, str_age, str_height, str_weight)) {
                    case 0:
                        showToast(R.string.bmi_info_id_hint);
                        break;
                    case 1:
                        showToast(R.string.bmi_check_hint_1);
                        break;
                    case 2:
                        showToast(R.string.bmi_check_hint_2);
                        break;
                    case 3:
                        showToast(R.string.bmi_check_hint_3);
                        break;
                    case 4:
                        showToast(R.string.bmi_check_hint_4);
                        break;
                    case 5:
                    {
                        if (Integer.valueOf(str_age) > 85) {
                            showToast(R.string.olderthan100);
                        } else if (Integer.valueOf(str_age) < 7) {
                            showToast(R.string.toolight);
                        } else if (Double.valueOf(str_height) > 200) {
                            showToast(R.string.toohigh);
                        } else if (Double.valueOf(str_height) < 80) {
                            showToast(R.string.toolight);
                        } else if (Double.valueOf(str_weight) > 290) {
                            showToast(R.string.tooheavy);
                        } else if (Double.valueOf(str_weight) < 20) {
                            showToast(R.string.toolight);
                        } else {
                            bundle = new Bundle();
                            str_ID=str_ID.length()==17?str_ID+"X":str_ID;
                            bundle.putString("ID", str_ID);
                            bundle.putString("name", str_name);
                            bundle.putInt("gender", int_gender);
                            bundle.putString("age", str_age);
                            bundle.putString("height", str_height);
                            bundle.putString("weight", str_weight);
                            BMIResults bmiResult = new BMIResults();
                            bmiResult.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            bmiResult.show(fragmentTransaction, "tab?");
                        }
                    }
                    break;
                }
        }
    }
}
