;; # 🎄 Advent of Clerk: Day 3
(ns advent-of-clerk.day-03
  (:require [nextjournal.clerk :as clerk]
            [clojure.string :as str]
            [clojure.set :as set]
            [clojure.java.io :as io]))

;; Here's the example input

(def example
  "vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw")

;; Each line is a list of characters representing items. The lines are to be
;; split into halves representing each compartment of items.

(defn parsed
  [input]
  (->> (str/split-lines input)
       (map (fn [line] (str/split line #"")))
       (map (fn [items] (partition (/ (count items) 2) items)))))

;; Let's try it on the example

(parsed example)

;; OK, looking good. Now to answer part one.

;; First, we have to calculate an items priority. Items are ordered a-zA-Z, and
;; the priority is 1-indexed.

(def priority
  (into {}
        (map-indexed (fn [index item] [(str item) (inc index)]))
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"))

;; Let's check some priorities

(priority "a")
(priority "Z")

;; Second, we have to use this priority to calculate the sum priority of the
;; common items from each pair of compartments. We can do this with a set
;; intersection.

(defn part-one
  [input]
  (->> (parsed input)
       (map (fn [[fst snd]] (set/intersection (set fst) (set snd))))
       (map first)
       (map priority)
       (reduce +)))

;; The sum of the example is supposed to be 157, let's check

(= 157 (part-one example))

;; Great! Now on the real deal

(-> "03.input" io/resource slurp part-one)

;; 👏

;; For part two, we do the intersection on each triple of lines instead of the
;; halves of the line.

(defn parsed2
  [input]
  (partition 3
             (->> (str/split-lines input)
                  (map (fn [line] (str/split line #""))))))

;; Let's see if our parsing matches the example given

(parsed2 example)

;; Looks good, now to finish it

(defn part-two
  [input]
  (->> (parsed2 input)
       (map #(map set %))
       (map #(apply set/intersection %))
       (map first)
       (map priority)
       (reduce +)))

;; Check it against the example

(= 70 (part-two example))

;; And now the answer

(-> "03.input" io/resource slurp part-two)
