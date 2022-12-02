;; # 🎄 Advent of Clerk: Day 2
(ns advent-of-clerk.day-02
  (:require [clojure.string :as str]
            [nextjournal.clerk :as clerk]
            [clojure.java.io :as io]))

;; Let's take a look at the example input

(def example
  "A Y
B X
C Z")

;; The first column is the opponent, the second is the player.
;; The opponent mapping: A is rock, B is Paper, C is Scissors
;; The player mapping: X is rock, Y for Paper, Z for Scissors

(defn parsed
  [input]
  (->> (str/split-lines input)
       (map #(str/split % #" "))
       (map (fn [[opponent player]]
              [({"A" :rock "B" :paper "C" :scissors} opponent)
               ({"X" :rock "Y" :paper "Z" :scissors} player)]))))

;; Let's try it on the example
(parsed example)

;; Looks good. Now for the scoring. To score a round you sum the score of each
;; of the chosen shape and outcome.

(defn score
  [[opponent player]]
  (let [shape ({:rock 1 :paper 2 :scissors 3} player)]
    (+ shape
       (cond (= opponent player) 3
             (= ({:rock :scissors :paper :rock :scissors :paper} player) opponent) 6
             :else 0))))

;; Let's score the example rounds

(map score (parsed example))

;; Looks good. The answer to part one is the _total_ score.

(defn part-one
  [input]
  (->> (parsed input)
       (map score)
       (reduce +)))

;; and on the example
(part-one example)

;; That's right, now on the input

(-> "02.input" io/resource slurp part-one)

;; Boom, got it.

;; For part two we interpret the input differently. X means lose, Y means draw,
;; Z means win. Let's change how we parse first.

(defn parsed2
  [input]
  (->> (str/split-lines input)
       (map #(str/split % #" "))
       (map (fn [[opponent outcome]]
              [({"A" :rock "B" :paper "C" :scissors} opponent)
               ({"X" :lose "Y" :draw "Z" :win} outcome)]))))

;; Funny thing is, we can re-use the scoring function as long as we can
;; translate this opponent/outcome pair into a round.

(defn round
  [[opponent outcome]]
  [opponent
   (case outcome
     :win ({:rock :paper :paper :scissors :scissors :rock} opponent)
     :lose ({:rock :scissors :paper :rock :scissors :paper} opponent)
     :draw opponent)])

;; Let's check that on the example

(map round (parsed2 example))

;; Looks good! Let's wrap it up

(defn part-two
  [input]
  (->> (parsed2 input)
       (map round)
       (map score)
       (reduce +)))

(-> example part-two)

(-> "02.input" io/resource slurp part-two)

;; Woo!
