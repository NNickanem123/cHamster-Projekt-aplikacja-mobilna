package com.example.chamster.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chamster.R;
import com.example.chamster.data.DataManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RaceFragmentAnimated extends Fragment {

    private static final Random random = new Random();
    private static final String[] NAMES = {"Czerwony", "Niebieski", "Zielony", "Czarny", "Twój Chomik"};

    private TextView tvBalance, tvStatus;
    private EditText[] betInputs = new EditText[5];
    private int[] bets = new int[5];
    private boolean useMyHamster = false;
    private RaceTrackView raceTrackView;
    private Button btnStart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_race_animated, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBalance = view.findViewById(R.id.tvRaceBalance);
        tvStatus = view.findViewById(R.id.tvRaceStatus);
        raceTrackView = view.findViewById(R.id.raceTrackView);
        btnStart = view.findViewById(R.id.btnStartRace);

        setupCheckbox(view);
        setupBetInputs(view);
        loadThumbnails(view);
        updateBalanceDisplay();

        btnStart.setOnClickListener(v -> startRace());
    }

    private void setupCheckbox(View view) {
        CheckBox cbUseMyHamster = view.findViewById(R.id.cbUseMyHamster);
        String baseSkin = DataManager.getBaseSkin();
        boolean hasCustomSkin = !baseSkin.equals("hamster/chomik.png") || !DataManager.getAccessories().isEmpty();

        cbUseMyHamster.setVisibility(hasCustomSkin ? View.VISIBLE : View.GONE);
        cbUseMyHamster.setOnCheckedChangeListener((buttonView, isChecked) -> {
            useMyHamster = isChecked;
            updateMyHamsterVisibility(view);
        });
    }

    private void updateMyHamsterVisibility(View view) {
        LinearLayout llMyHamster = view.findViewById(R.id.llMyHamster);
        if (useMyHamster) {
            llMyHamster.setVisibility(View.VISIBLE);
            loadImageToImageView(view.findViewById(R.id.ivMyHamster), DataManager.getBaseSkin());
        } else {
            llMyHamster.setVisibility(View.GONE);
        }
    }

    private void setupBetInputs(View view) {
        betInputs[0] = view.findViewById(R.id.etRedBet);
        betInputs[1] = view.findViewById(R.id.etBlueBet);
        betInputs[2] = view.findViewById(R.id.etGreenBet);
        betInputs[3] = view.findViewById(R.id.etBlackBet);
        betInputs[4] = view.findViewById(R.id.etMyHamsterBet);
    }

    private void loadThumbnails(View view) {
        loadImageToImageView(view.findViewById(R.id.ivRed), "hamster/czerwony_chomik.png");
        loadImageToImageView(view.findViewById(R.id.ivBlue), "hamster/niebieski_chomik.png");
        loadImageToImageView(view.findViewById(R.id.ivGreen), "hamster/zielony_chomik.png");
        loadImageToImageView(view.findViewById(R.id.ivBlack), "hamster/czarny_chomik.png");
    }

    private void loadImageToImageView(ImageView iv, String assetPath) {
        try {
            InputStream is = requireContext().getAssets().open(assetPath);
            iv.setImageBitmap(BitmapFactory.decodeStream(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateBalanceDisplay() {
        tvBalance.setText("Saldo: " + DataManager.getBalance() + " zł");
    }

    private void startRace() {
        if (raceTrackView.isRacing()) return;

        int totalBet = collectBets();
        if (totalBet == 0) {
            Toast.makeText(requireContext(), "Postaw przynajmniej jeden zakład!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (totalBet > DataManager.getBalance()) {
            Toast.makeText(requireContext(), "Nie masz tyle złota!", Toast.LENGTH_SHORT).show();
            return;
        }

        DataManager.removeBalance(totalBet);
        updateBalanceDisplay();
        tvStatus.setText("🏁 Wyścig w toku... 🏁");
        btnStart.setEnabled(false);

        final int finalTotalBet = totalBet;
        int numHamsters = useMyHamster ? 5 : 4;
        double[] speeds = generateSpeeds(numHamsters);
        Bitmap[] combinedBitmaps = prepareBitmaps(numHamsters);

        List<RaceTrackView.RaceParticipant> participants = new ArrayList<>();
        for (int i = 0; i < numHamsters; i++) {
            participants.add(new RaceTrackView.RaceParticipant(NAMES[i], null, combinedBitmaps[i], (float) speeds[i]));
        }

        raceTrackView.setParticipants(participants, useMyHamster);
        raceTrackView.startRace(() -> finishRace(raceTrackView.getWinnerIndex(), finalTotalBet));
    }

    private int collectBets() {
        int total = 0;
        int limit = useMyHamster ? 5 : 4;
        for (int i = 0; i < limit; i++) {
            String text = betInputs[i].getText().toString();
            if (!text.isEmpty()) {
                int amount = Integer.parseInt(text);
                if (amount > 0) {
                    bets[i] = amount;
                    total += amount;
                }
            }
        }
        return total;
    }

    private double[] generateSpeeds(int count) {
        double[] speeds = new double[count];
        for (int i = 0; i < count; i++) {
            double base = random.nextDouble() * 3 + 1;
            if (i == 3 && random.nextDouble() < 0.3) base *= 1.5;
            if (i == 4 && useMyHamster) base *= 1.1;
            speeds[i] = base;
        }
        return speeds;
    }

    private Bitmap[] prepareBitmaps(int count) {
        Bitmap[] bitmaps = new Bitmap[count];
        for (int i = 0; i < count; i++) {
            if (i == 4 && useMyHamster) {
                bitmaps[i] = getFullHamsterBitmap();
            } else {
                bitmaps[i] = loadBitmap(getAssetPath(i));
            }
        }
        return bitmaps;
    }

    private void finishRace(int winner, int totalBet) {
        int winnings = 0;
        boolean won = false;

        if (bets[winner] > 0) {
            double multiplier = winner == 3 ? 2.5 : (winner == 4 && useMyHamster ? 3.5 : 3.0);
            winnings = (int) (bets[winner] * multiplier);
            DataManager.addBalance(winnings);
            won = true;
        }

        updateBalanceDisplay();

        if (won) {
            tvStatus.setText("🎉 WYGRANA! Wygrał " + NAMES[winner] + "! +" + winnings + " zł 🎉");
            tvStatus.setBackgroundColor(0xFFC8E6C9);
        } else {
            tvStatus.setText("😢 PRZEGRANA! Wygrał " + NAMES[winner] + ". Stracona stawka: " + totalBet + " zł 😢");
            tvStatus.setBackgroundColor(0xFFFFCDD2);
        }

        for (EditText input : betInputs) {
            if (input != null) input.setText("");
        }
        for (int i = 0; i < bets.length; i++) bets[i] = 0;

        btnStart.setEnabled(true);
    }

    private String getAssetPath(int index) {
        switch (index) {
            case 0: return "hamster/czerwony_chomik.png";
            case 1: return "hamster/niebieski_chomik.png";
            case 2: return "hamster/zielony_chomik.png";
            case 3: return "hamster/czarny_chomik.png";
            default: return DataManager.getBaseSkin();
        }
    }

    private Bitmap getFullHamsterBitmap() {
        Bitmap result = loadBitmap(DataManager.getBaseSkin());
        if (result == null) return null;

        List<String> accessories = DataManager.getAccessories();
        if (accessories.isEmpty()) return result;

        Bitmap combined = Bitmap.createBitmap(result.getWidth(), result.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combined);
        canvas.drawBitmap(result, 0, 0, null);

        for (String acc : accessories) {
            Bitmap accessory = loadBitmap(acc);
            if (accessory != null) canvas.drawBitmap(accessory, 0, 0, null);
        }
        return combined;
    }

    private Bitmap loadBitmap(String assetPath) {
        try {
            InputStream is = requireContext().getAssets().open(assetPath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            return BitmapFactory.decodeStream(is, null, options);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}