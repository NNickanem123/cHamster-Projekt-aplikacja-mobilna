package com.example.chamster.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chamster.R;
import com.example.chamster.data.DataManager;
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
        TextView price = view.findViewById(R.id.textViewPrice);
        ImageView image = view.findViewById(R.id.imageViewHamster);
        Button btnSet = view.findViewById(R.id.btnSetSkin);
        Button btnBuy = view.findViewById(R.id.btnBuySkin);

        title.setText(item.getName());
        description.setText(item.getDescription());

        if (item.getPrice() > 0) {
            price.setText("Cena: " + item.getPrice() + " zł");
            price.setVisibility(View.VISIBLE);
        } else {
            price.setVisibility(View.GONE);
        }

        try {
            InputStream is = requireContext().getAssets().open(item.getImagePath());
            Drawable d = Drawable.createFromStream(is, null);
            image.setImageDrawable(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isOwned = DataManager.isSkinOwned(item.getImagePath());
        boolean isAccessory = isAccessory(item.getName());

        if (isOwned || item.getPrice() == 0) {
            btnBuy.setVisibility(View.GONE);
            btnSet.setVisibility(View.VISIBLE);
        } else {
            btnBuy.setVisibility(View.VISIBLE);
            btnSet.setVisibility(View.GONE);
        }

        btnBuy.setOnClickListener(v -> {
            int balance = DataManager.getBalance();
            if (balance >= item.getPrice()) {
                DataManager.removeBalance(item.getPrice());
                DataManager.addOwnedSkin(item.getImagePath());
                Toast.makeText(requireContext(), "Kupiono " + item.getName() + "!", Toast.LENGTH_SHORT).show();

                btnBuy.setVisibility(View.GONE);
                btnSet.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(requireContext(), "Brak złota! Potrzebujesz: " + item.getPrice() + " zł", Toast.LENGTH_SHORT).show();
            }
        });

        btnSet.setOnClickListener(v -> {
            if (isAccessory) {
                DataManager.toggleAccessory(item.getImagePath());
            } else {
                DataManager.setBaseSkin(item.getImagePath());
            }

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new MainFragment())
                    .commit();
        });
    }

    private boolean isAccessory(String name) {
        return name.equals("Aureola") || name.equals("Korona") ||
                name.equals("Czapka") || name.equals("Okulary") ||
                name.equals("Peleryna") || name.equals("Skrzydła");
    }
}