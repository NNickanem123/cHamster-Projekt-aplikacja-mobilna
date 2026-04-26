package com.example.chamster.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chamster.R;
import com.example.chamster.data.SkinManager;
import com.example.chamster.data.model.HamsterItem;

import java.io.InputStream;

public class DetailsFragment extends Fragment {

    private HamsterItem item;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        item = (HamsterItem) getArguments().getSerializable("hamster_item");

        TextView title = view.findViewById(R.id.textViewTitle);
        TextView description = view.findViewById(R.id.textViewDescription);
        ImageView image = view.findViewById(R.id.imageViewHamster);
        Button btnSet = view.findViewById(R.id.btnSetSkin);

        title.setText(item.getName());
        description.setText(item.getDescription());

        try {
            InputStream is = requireContext().getAssets().open(item.getImagePath());
            Drawable d = Drawable.createFromStream(is, null);
            image.setImageDrawable(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isAccessory =
                item.getName().equals("Aureola") ||
                        item.getName().equals("Korona") ||
                        item.getName().equals("Czapka") ||
                        item.getName().equals("Okulary") ||
                        item.getName().equals("Peleryna") ||
                        item.getName().equals("Skrzydła");

        btnSet.setOnClickListener(v -> {

            if (isAccessory) {
                SkinManager.toggleAccessory(requireContext(), item.getImagePath());
            } else {
                SkinManager.saveBaseSkin(requireContext(), item.getImagePath());
            }

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new MainFragment())
                    .commit();
        });
    }
}