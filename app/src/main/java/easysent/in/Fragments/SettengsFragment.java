package easysent.in.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.databinding.FragmentSettengsBinding;

public class SettengsFragment extends Fragment {

FragmentSettengsBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_settengs, container, false);
        binding  =FragmentSettengsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.enableFingireprint.setOnCheckedChangeListener(null);
        if (PreferenceFile.getFingirAuth()){
            binding.enableFingireprint.setChecked(true);
        }else{
            binding.enableFingireprint.setChecked(false);
        }
        binding.enableFingireprint.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                Enable();
            }else {
                PreferenceFile.setFingirAuth(false);
            }
        });
    }

    private void Enable() {
       /* Authenticate authenticate = new Authenticate(getContext(), getActivity(), "Add 2 Step Verification",
                "While Adding Two Step, You Should Know You Need to Verify Yourself EveryTime While Autologin", "Easysent");
        authenticate.setIssuccessListener(new Authenticate.isSuccess() {
            @Override
            public boolean isSuccess(boolean isS) {
                if (isS){
                    PreferenceFile.setFingirAuth(true);
                }else{
                    PreferenceFile.setFingirAuth(false);
                }
                return false;
            }
        });*/

    }
}