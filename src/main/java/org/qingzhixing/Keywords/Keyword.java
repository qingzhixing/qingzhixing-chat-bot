package org.qingzhixing.Keywords;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Keyword implements Comparable<Keyword> {
    private final InnerData keyword;
    private final PriorityQueue<InnerData> replies;

    public Keyword(InnerData keyword, List<InnerData> replies) {
        this.keyword = keyword;
        this.replies = new PriorityQueue<>(Comparator.reverseOrder());
        this.replies.addAll(
                //剔除空回答
                replies.parallelStream().filter(reply -> !(reply.value().equals(""))).collect(Collectors.toList())
        );
    }

    public Keyword(String keyword, List<String> replies) {
        this.keyword = new InnerData(keyword);
        this.replies = new PriorityQueue<>(Comparator.reverseOrder());
        this.replies.addAll(
                //剔除空回答
                replies.parallelStream().filter(reply -> !(reply.equals("")))
                        .map(InnerData::new).collect(Collectors.toList())
        );
    }

    private int CalculateRepliesWeightAmount() {
        AtomicInteger amount = new AtomicInteger();
        replies.forEach(reply -> amount.addAndGet(reply.weight()));
        return amount.intValue();
    }

    public String DoReply() {
        int weightAmount = CalculateRepliesWeightAmount();
        double randomIndex = Math.random() * weightAmount;
        for (InnerData currentReply : replies) {
            randomIndex -= currentReply.weight();
            if (randomIndex <= 0) {
                return currentReply.value();
            }
        }
        return "没有找到回答，怎么会事呢？";
    }

    @Override
    public int compareTo(@NotNull Keyword o) {
        return keyword().compareTo(o.keyword());
    }

    public InnerData keyword() {
        return keyword;
    }

    public PriorityQueue<InnerData> replies() {
        return replies;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "keyword=" + keyword +
                ", replies=" + replies +
                '}';
    }

    public static final class InnerData implements Comparable<InnerData> {

        private final int weight;
        private final String value;

        InnerData(String value, int weight) {
            if (weight <= 0) {
                weight = 1;
            }
            this.weight = weight;
            this.value = value;
        }

        InnerData(String value) {
            this.weight = 1;
            this.value = value;
        }

        public int weight() {
            return weight;
        }

        public String value() {
            return value;
        }

        @Override
        public int compareTo(@NotNull Keyword.InnerData o) {
            return (weight() - o.weight());
        }

        @Override
        public String toString() {
            return "InnerData{" +
                    "weight=" + weight +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
