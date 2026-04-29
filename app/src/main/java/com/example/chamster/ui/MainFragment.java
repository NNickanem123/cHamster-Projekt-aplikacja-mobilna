package com.example.chamster.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chamster.R;
import com.example.chamster.data.DataManager;

import java.io.InputStream;
import java.util.List;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateBalanceDisplay(view);
        setupHamsterDisplay(view);
        setupButtons(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) updateBalanceDisplay(view);
    }

    private void updateBalanceDisplay(View view) {
        TextView tvBalance = view.findViewById(R.id.tvBalance);
        tvBalance.setText("Saldo: " + DataManager.getBalance() + " zł");
    }

    private void setupHamsterDisplay(View view) {
        FrameLayout container = view.findViewById(R.id.mainContainer);
        container.removeAllViews();

        ImageView base = new ImageView(requireContext());
        base.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        setImageViewDrawable(base, DataManager.getBaseSkin());
        container.addView(base);

        for (String acc : DataManager.getAccessories()) {
            ImageView layer = new ImageView(requireContext());
            layer.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            ));
            setImageViewDrawable(layer, acc);
            container.addView(layer);
        }
    }

    private void setupButtons(View view) {
        Button btnAnimals = view.findViewById(R.id.btnAnimals);
        Button btnRace = view.findViewById(R.id.btnRace);

        btnAnimals.setOnClickListener(v -> navigateTo(new ListFragment()));
        btnRace.setOnClickListener(v -> navigateTo(new RaceFragmentAnimated()));
    }

    private void setImageViewDrawable(ImageView iv, String assetPath) {
        try {
            InputStream is = requireContext().getAssets().open(assetPath);
            iv.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateTo(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .addToBackStack(null)
                .commit();
    }
}