package com.strivacity.android.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.strivacity.android.demo.databinding.FragmentFirstBinding;
import com.strivacity.android.native_sdk.NativeSDK;
import com.strivacity.android.native_sdk.auth.IdTokenClaims;
import com.strivacity.android.native_sdk.auth.config.LoginParameters;

import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    MainActivity mainActivity;
    NativeSDK nativeSDK;
    FloatingActionButton cancelFlowButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = ((MainActivity) view.getContext());
        nativeSDK = mainActivity.nativeSDK;
        binding.buttonLogin.setOnClickListener(v -> {
            binding.appLayout.setVisibility(View.GONE);
            binding.appScreenLayoutContainer.setVisibility(View.VISIBLE);
            cancelFlowButton = mainActivity.findViewById(R.id.cancelFlowButton);
            cancelFlowButton.setVisibility(View.VISIBLE);

            nativeSDK.login(
                LoginParameters.builder().scopes(List.of("openid", "email")).build(),
                binding.appScreenLayout,
                this::showProfileScreen,
                throwable -> {
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    showLoginScreen();
                }
            );
        });

        binding.buttonLogout.setOnClickListener(v -> {
            nativeSDK.logout();
            showLoginScreen();
        });

        nativeSDK.isAuthenticated(authenticated -> {
            if (authenticated) {
                showProfileScreen(nativeSDK.getIdTokenClaims());
            } else {
                showLoginScreen();
            }
        });
    }

    private void showLoginScreen() {
        binding.appLayout.setVisibility(View.VISIBLE);
        binding.appScreenLayoutContainer.setVisibility(View.GONE);

        binding.buttonLogin.setVisibility(View.VISIBLE);
        binding.buttonLogout.setVisibility(View.GONE);

        binding.textviewWelcome.setText("Login required");
    }

    private void showProfileScreen(IdTokenClaims idTokenClaims) {
        if (cancelFlowButton != null) {
            cancelFlowButton.setVisibility(View.GONE);
        }

        binding.appLayout.setVisibility(View.VISIBLE);
        binding.appScreenLayoutContainer.setVisibility(View.GONE);

        binding.buttonLogin.setVisibility(View.GONE);
        binding.buttonLogout.setVisibility(View.VISIBLE);

        binding.textviewWelcome.setText(idTokenClaims.getString("email"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
