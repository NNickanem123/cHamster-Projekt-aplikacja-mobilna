package com.example.chamster.data;

import java.util.Random;

public class RaceEngine {

    private static final Random random = new Random();

    public static int runRace(boolean useMyHamster) {
        int numHamsters = useMyHamster ? 5 : 4;

        double[] speeds = new double[numHamsters];
        double[] positions = new double[numHamsters];

        for (int i = 0; i < numHamsters; i++) {
            double baseSpeed = random.nextDouble() * 3 + 1; // 1-4

            if (i == 3) {
                if (random.nextDouble() < 0.3) {
                    baseSpeed *= 1.5;
                }
            }

            if (i == 4 && useMyHamster) {
                baseSpeed *= 1.1;
            }

            speeds[i] = baseSpeed;
        }

        boolean finished = false;
        int winner = 0;

        while (!finished) {
            for (int i = 0; i < numHamsters; i++) {
                positions[i] += speeds[i] * (0.5 + random.nextDouble() * 0.5);
                if (positions[i] >= 100) {
                    winner = i;
                    finished = true;
                    break;
                }
            }
        }

        return winner;
    }

    public static int calculateWinnings(int bet, int winnerIndex, boolean useMyHamster) {
        double multiplier;

        if (winnerIndex == 3) {
            multiplier = 2.5;
        } else if (winnerIndex == 4 && useMyHamster) {
            multiplier = 3.5;
        } else {
            multiplier = 3.0;
        }

        return (int) (bet * multiplier);
    }
}