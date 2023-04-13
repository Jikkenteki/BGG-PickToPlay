(ns bbg-reframe.model.plays
  (:require [bbg-reframe.model.examples.game-plays :refer [plays-games]]
            [bbg-reframe.model.sort-filter :refer [sort-by-field]]))


(defn number-of-plays
  [game]
  (->> game :plays
       (reduce-kv (fn [num _ _] (inc num)) 0)))

(defn dates-of-plays
  [game]
  (->> game :plays
       (reduce-kv (fn [list _ v] (conj list (v :date))) [])))

(defn last-played
  [game]
  (last (sort (dates-of-plays game))))

(defn plays-stats
  [games]
  (filter (fn [entry] (:last-played entry))
          (map (fn [game] {:name (:name game)
                           :last-played (last-played game)
                           :number-of-plays (number-of-plays game)
                           :id (:id game)}) (vals games))))



(defn played-rank
  [games game]
  (inc (.indexOf (map :id (sort (sort-by-field :number-of-plays)
                                (plays-stats games)))
                 (:id game))))



(comment

  (def games plays-games)
  (def game (get plays-games "25613"))
  (->> game
       :plays
       (reduce-kv (fn [list _ v] (conj list (v :date))) []))

  (dates-of-plays (get plays-games "25613"))

  (sort (dates-of-plays (get plays-games "25613")))


  (map (fn [game] {:name (:name game)
                   :last-played (last-played game)
                   :number-of-plays (number-of-plays game)
                   :id (:id game)}) (vals plays-games))

  (sort (sort-by-field :last-played)
        (map (fn [game] {:name (:name game)
                         :last-played (last-played game)
                         :number-of-plays (number-of-plays game)
                         :id (:id game)}) (vals plays-games)))

  (sort (sort-by-field :number-of-plays)
        (plays-stats plays-games))


  (sort (sort-by-field :number-of-plays)
        (plays-stats games))

  (played-rank plays-games game)


  ;
  )


