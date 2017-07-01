package com.mkm.hanium.jjack.ranking;

/**
 * Created by MIN on 2017-06-24.
 */

public class RankingItem {

    private int rank;
    private String name;

    public RankingItem() { }

    public RankingItem(int rank) { this.rank = rank; }

    public RankingItem(String name) { this.name = name; }

    public RankingItem(int rank, String name) {
        this.rank = rank;
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
