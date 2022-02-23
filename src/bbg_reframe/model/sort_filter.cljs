(ns bbg-reframe.model.sort-filter
  (:require [bbg-reframe.model.db :as db]
            [clojure.string :as s]
            [clojure.pprint :as pp]
            [clojure.tools.reader.edn :refer [read-string]]))

(defn all-games
  []
  (vals (db/read-db)))

;; 
;; Sorters
;; 
(defn game-better? [g1 g2]
  (> (:rating g1) (:rating g2)))

(defn game-shorter? [g1 g2]
  (< (:playingtime g1) (:playingtime g2)))

(defn game-shorter-and-better? [g1 g2]
  (> (/ (:rating g1) (:playingtime g1))
     (/ (:rating g2) (:playingtime g2))))

(defn rating-for-number-of-players
  "Returns a rating based on the best and recommended percentages
   if votes are not availabe returns 1"
  [game num-players]
  (if (:votes game)
   (let [;_ (println (:name game) num-players)
        percentages
        (map
         (fn [pl]
           (double (/ (+  (* 2 (pl :best-votes)) (pl :recommended-votes))
                      (+ 0.0001 (* 2 (pl :best-votes)) (pl :recommended-votes) (pl :not-recommended-votes)))))
         (:votes game))
        ;_ (println percentages)
        ]
    ;; Assuming all games have rating for 1 player
    ;; 1 2 3 4 4+ (5 perc)
    (if (> (count percentages) num-players)
      (nth percentages (dec num-players))
      (last percentages)))
    1.0))

(def sorting-fun
  {:rating game-better?
   :time game-shorter?
   :rating-time game-shorter-and-better?})

(map name (keys sorting-fun))

;; 
;; Filters
;; 
(defn has-name [name]
  (fn [game]
    (s/includes? (:name game) name)))

(defn is-best-with-num-of-players
  [num-players]
  (fn [game]
    (let [best-string ((apply max-key :best-perc (:votes game)) :players)]
      (if (s/includes? best-string "+")
        ; remove '+' from the '4+'. 4+ means more than 4.
        (< num-players (read-string (s/join "" (drop-last best-string))))
        (= num-players (read-string best-string))))))

(defn is-playable-with-num-of-players
  [num threshold]
  (fn [game]
    (>= (rating-for-number-of-players game num) threshold)))

(defn playingtime-between? [min max]
  (fn [game]
    (let [time (:playingtime game)]
      (and
       (number? time)
       (>= time min)
       (<= time max)))))

(defn with-number-of-players? [num]
  (fn [game] (and
              (>= (:maxplayers game) num)
              (<= (:minplayers game) num))))

(defn rating-higher-than? [rating]
  (fn [game]
    (let [game-rating (:rating game)]
      (and (not (nil? game-rating)) (>= game-rating rating)))))

;; (defn and-filters
;;   "Composes filters into one."
;;   [filter1 filter2]
;;   (fn [game] (and
;;               (filter1 game)
;;               (filter2 game))))

(defn and-filters
  "Composes filters into one."
  [& filters]
  (fn [game] (reduce
              #(and %1 %2)
              ((apply juxt filters) game))))

(comment

  (db/make-db)
  (def db-mem (all-games))
  db-mem
  (def game (last db-mem))
  game
  (pp/pp)

  ((fn [sorter]
     (take 15 (sort sorter db-mem)))
   (get sorting-fun :time))

  (take 5 (vals data/local-storage-db))
  (keys data/local-storage-db)
  data/local-storage-db

  data/local-storage-db
  (take 15 (sort game-better? db-mem))

  (map
   (juxt :name :rating)
   (sort game-better?
         (filter
          (and-filters
           (with-number-of-players? 3)
           (is-best-with-num-of-players 3)
           (playingtime-between? 0 120)
           (rating-higher-than? 6))
          db-mem)))

  (map (juxt :name :id)
       (sort game-better?
             (filter
              (and-filters
               (with-number-of-players? 5)
               (is-playable-with-num-of-players 5 0.50)
               (playingtime-between? 0 240))
              db-mem)))

  (rating-for-number-of-players game 6)
  (:votes game)
  (:name game)
  (pp/pp)
  ;
  )