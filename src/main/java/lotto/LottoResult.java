package lotto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LottoResult {
    private static final int ONE_LOTTO_PRICE = 1000;
    private static final int DEFAULT_VALUE = 0;
    private static final int ADD_COUNT = 1;
    private static final int DECIMAL_CALCULATION_NUMBER = 100;

    private final Map<LottoRank, Integer> lottoResults;

    public LottoResult(List<Lotto> lottos, WinningNumber winningNumber) {
        Map<LottoRank, Integer> results = new HashMap<>();
        for (Lotto lotto : lottos) {
            LottoRank lottoRank = LottoRank.valueOf(winningNumber.winningCount(lotto));
            results.put(lottoRank, results.getOrDefault(lottoRank,DEFAULT_VALUE) + ADD_COUNT);
        }
        this.lottoResults = results;
    }

    public int getResult(LottoRank key) {
        return lottoResults.getOrDefault(key, DEFAULT_VALUE);
    }

    public double rewardRatio() {
        Set<LottoRank> lottoRanks = lottoResults.keySet();
        return rateCalculation(getTotalReward(lottoRanks), getPurchaseAmount(lottoRanks));
    }

    public static double rateCalculation(long totalReward, double purchaseAmount) {
        return Math.floor((totalReward / purchaseAmount) * DECIMAL_CALCULATION_NUMBER) / DECIMAL_CALCULATION_NUMBER;
    }

    private long getPurchaseAmount(Set<LottoRank> lottoRanks) {
        return lottoRanks.stream()
                .mapToLong(lottoResults::get)
                .sum() * ONE_LOTTO_PRICE;
    }

    private long getTotalReward(Set<LottoRank> lottoRanks) {
        return lottoRanks.stream()
                .mapToLong(lottoRank -> lottoRank.getTotal(lottoResults.get(lottoRank)))
                .sum();
    }
}
