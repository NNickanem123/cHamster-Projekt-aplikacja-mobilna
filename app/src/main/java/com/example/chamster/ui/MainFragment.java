package com.example.chamster.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chamster.R;
import com.example.chamster.data.SkinManager;

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

        FrameLayout container = view.findViewById(R.id.mainContainer);
        Button btnAnimals = view.findViewById(R.id.btnAnimals);

        container.removeAllViews();

        ImageView base = new ImageView(requireContext());
        base.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

        try {
            InputStream is = requireContext().getAssets().open(SkinManager.loadBaseSkin(requireContext()));
            Drawable d = Drawable.createFromStream(is, null);
            base.setImageDrawable(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        container.addView(base);

        List<String> accessories = SkinManager.loadAccessories(requireContext());

        for (String acc : accessories) {
            ImageView layer = new ImageView(requireContext());
            layer.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            ));

            try {
                InputStream is = requireContext().getAssets().open(acc);
                Drawable d = Drawable.createFromStream(is, null);
                layer.setImageDrawable(d);
            } catch (Exception e) {
                e.printStackTrace();
            }

            container.addView(layer);
        }

        btnAnimals.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new ListFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}