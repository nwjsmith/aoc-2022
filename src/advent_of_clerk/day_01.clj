;; # 🎄 Advent of Clerk: Day 1
(ns advent-of-clerk.day-01
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [nextjournal.clerk :as clerk]))

;; The following string is the example input. It is a contiguous,
;; newline-separated list of numbers of calories followed by a newline.

(def example
  "1000
2000
3000

4000

5000
6000

7000
8000
9000

10000")

;; We can parse this by splitting on newlines, and reducing over the list of
;; strings. Each time we see a newline we start an empty collection, otherwise
;; we push the number onto the end of the last group.

(defn parsed
  [input]
  (loop [lines (string/split input #"\n")
         group []
         groups []]
    (if-let [[line & remaining] (seq lines)]
      (if (empty? line)
        (recur remaining [] (conj groups group))
        (recur remaining (conj group (Long/parseLong line)) groups))
      (conj groups group))))

;; Let's try it on the example

(parsed example)

;; OK, now that we have the input parsed we can solve the first problem. We want
;; to know the greatest amount of calories being carried by an Elf. We can do
;; this by summing each group, sorting, and grabbing the greatest number.

(defn part-one
  [groups]
  (->> groups (map #(reduce + %)) sort last))

;; Let's try it

(-> example parsed part-one)

;; Great! that matches the example

;; Now to try it on the real input

(-> "01.input" io/resource slurp parsed part-one)

;; Hurray! Now on to part two. We now want to know the total of the top three
;; Elves. This is pretty similar to part one, but instead of grabbing the last
;; one we drop all but the last three totals and sum them up.

(defn part-two
  [groups]
  (->> groups
       (map #(reduce + %))
       sort
       (take-last 3)
       (reduce +)))

;; Let's try on the example

(-> example parsed part-two)

;; Looks good, now for the input

(-> "01.input" io/resource slurp parsed part-two)
