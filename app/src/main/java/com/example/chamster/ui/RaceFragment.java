package com.example.chamster.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chamster.R;
import com.example.chamster.data.BalanceManager;
import com.example.chamster.data.RaceEngine;
import com.example.chamster.data.SkinManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RaceFragment extends Fragment {

    private TextView tvBalance, tvResult;
    private EditText[] betInputs = new EditText[5];
    private int[] bets = new int[5];
    private boolean useMyHamster = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_race, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBalance = view.findViewById(R.id.tvRaceBalance);
        tvResult = view.findViewById(R.id.tvRaceResult);
        Button btnStart = view.findViewById(R.id.btnStartRace);

        List<String> accessories = SkinManager.loadAccessories(requireContext());
        String baseSkin = SkinManager.loadBaseSkin(requireContext());
        useMyHamster = !baseSkin.equals("hamster/chomik.png") || !accessories.isEmpty();

        LinearLayout llMyHamster = view.findViewById(R.id.llMyHamster);
        if (useMyHamster) {
            llMyHamster.setVisibility(View.VISIBLE);
            ImageView ivMyHamster = view.findViewById(R.id.ivMyHamster);
            loadImageToImageView(ivMyHamster, baseSkin);
        }

        betInputs[0] = view.findViewById(R.id.etRedBet);
        betInputs[1] = view.findViewById(R.id.etBlueBet);
        betInputs[2] = view.findViewById(R.id.etGreenBet);
        betInputs[3] = view.findViewById(R.id.etBlackBet);
        betInputs[4] = view.findViewById(R.id.etMyHamsterBet);

        loadImageToImageView(view.findViewById(R.id.ivRed), "hamster/czerwony_chomik.png");
        loadImageToImageView(view.findViewById(R.id.ivBlue), "hamster/niebieski_chomik.png");
        loadImageToImageView(view.findViewById(R.id.ivGreen), "hamster/zielony_chomik.png");
        loadImageToImageView(view.findViewById(R.id.ivBlack), "hamster/czarny_chomik.png");

        updateBalanceDisplay();

        btnStart.setOnClickListener(v -> startRace());
    }

    private void loadImageToImageView(ImageView iv, String assetPath) {
        try {
            InputStream is = requireContext().getAssets().open(assetPath);
            Drawable d = Drawable.createFromStream(is, null);
            iv.setImageDrawable(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBalanceDisplay() {
        int balance = BalanceManager.getBalance(requireContext());
        tvBalance.setText("Saldo: " + balance + " zł");
    }

    private void startRace() {
        int totalBet = 0;
        boolean hasBet = false;

        for (int i = 0; i < (useMyHamster ? 5 : 4); i++) {
            String betText = betInputs[i].getText().toString();
            if (!betText.isEmpty()) {
                int betAmount = Integer.parseInt(betText);
                if (betAmount > 0) {
                    bets[i] = betAmount;
                    totalBet += betAmount;
                    hasBet = true;
                } else {
                    bets[i] = 0;
                }
            } else {
                bets[i] = 0;
            }
        }

        if (!hasBet) {
            Toast.makeText(requireContext(), "Postaw przynajmniej jeden zakład!", Toast.LENGTH_SHORT).show();
            return;
        }

        int currentBalance = BalanceManager.getBalance(requireContext());
        if (totalBet > currentBalance) {
            Toast.makeText(requireContext(), "Nie masz tyle złota! Masz: " + currentBalance + " zł", Toast.LENGTH_SHORT).show();
            return;
        }

        BalanceManager.removeBalance(requireContext(), totalBet);
        updateBalanceDisplay();

        int winner = RaceEngine.runRace(useMyHamster);

        int winnings = 0;
        boolean won = false;

        if (bets[winner] > 0) {
            winnings = RaceEngine.calculateWinnings(bets[winner], winner, useMyHamster);
            BalanceManager.addBalance(requireContext(), winnings);
            won = true;
        }

        updateBalanceDisplay();

        String[] names = {"Czerwony", "Niebieski", "Zielony", "Czarny", "Twój Chomik"};
        String winnerName = names[winner];

        if (won) {
            tvResult.setText("🎉 WYGRANA! 🎉\nWygrał " + winnerName + "!\n+ " + winnings + " zł");
            tvResult.setBackgroundColor(0xFFC8E6C9);
        } else {
            tvResult.setText("😢 PRZEGRANA! 😢\nWygrał " + winnerName + ".\nTracona stawka: " + totalBet + " zł");
            tvResult.setBackgroundColor(0xFFFFCDD2);
        }

        for (int i = 0; i < betInputs.length; i++) {
            if (betInputs[i] != null) {
                betInputs[i].setText("");
            }
        }
    }
}