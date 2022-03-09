(ns bbg-reframe.model.sort-filter
  (:require [clojure.string :as s]
            [clojure.tools.reader.edn :refer [read-string]]
            [bbg-reframe.model.examples.data :refer [local-storage-db]]
            [clojure.spec.alpha :as spec]
            [clojure.pprint :as pp]))

;; 
;; Sorters
;; 
(declare rating-for-number-of-players)

(defn name-alpha?
  [g1 g2]
  (< (:name g1) (:name g2)))

(defn game-better?
  [g1 g2]
  (> (:rating g1) (:rating g2)))

(defn game-shorter?
  [g1 g2]
  (< (:playingtime g1) (:playingtime g2)))


(defn playability
  [game num-players]
  (/ (* (:rating game) (:rating game) (:rating game) (:rating game) (rating-for-number-of-players game num-players)) 1000))

(defn time-rating-fun
  [playing-time available-time]
  ;; (println (str "TIME avail: " time " game time " (:playingtime game)))
  ;; (/ 1 (Math/log10 (+ (Math/abs (- (:playingtime game) time)) 10)))    
  (if (>= available-time playing-time)
      ;; (/ game-time time)
    (- 1 (* 0.002 (- available-time playing-time)))
      ;; (/ 1 (- game-time time))
      ;; (/ time game-time)
    (- 1 (* 0.01 (- playing-time available-time)))))

(comment
  (time-rating-fun 90 60) ; 0.7
  (time-rating-fun 60 60) ; 1.0
  (time-rating-fun 30 60) ; 0.94
;
  )

(defn time-rating
  [game available-time]
  (time-rating-fun (:playingtime game) available-time))

(defn playability-time
  [game num-players time]
  (* (playability game num-players)
     (time-rating game time)))

(comment
  (Math/log10 (+ 60 10))
  (/ 1 (Math/log10 (+ (Math/abs (- 60 60)) 10)))
  ;
  )

(defn game-more-playable?
  [num-players _]
  (fn
    [g1 g2]
    (> (playability g1 num-players)
       (playability g2 num-players))))

(defn game-more-time-playable?
  [num-players time-limit]
  (fn
    [g1 g2]
    (> (playability-time g1 num-players time-limit)
       (playability-time g2 num-players time-limit))))

(defn rating-function
  [votes]
  ;; (double (/ (+  (* 1.5 (votes :best-votes)) (votes :recommended-votes))
  ;;            (+ 0.0001 (* 1.5 (votes :best-votes)) (votes :recommended-votes) (votes :not-recommended-votes))))
  (double (+ (* (:best-perc votes)) (* 0.7 (:recommended-perc votes)))))

(defn rating-for-number-of-players
  "Returns a rating based on the best and recommended percentages
   if votes are not availabe returns 1"
  [game num-players]
  (if (:votes game)
    (let [percentages (map rating-function (:votes game))]
    ;; Assuming all games have rating for 1 player
    ;; 1 2 3 4 4+ (5 perc)
      (if (> (count percentages) num-players)
        (nth percentages (dec num-players))
        (last percentages)))
    1.0))

(def sorting-fun
  {:rating (fn [_ _] game-better?)
   :time (fn [_ _] game-shorter?)
   :playability-time game-more-time-playable?
   :playability game-more-playable?})

(map name (keys sorting-fun))

;; 
;; Filters
;; 
(defn has-name? [substring]
  (fn
    [game]
    (let [name (:name game)]
      (s/includes? (s/upper-case name) (s/upper-case substring)))))

(defn should-show-expansions?
  [show-expansions?]
  {:pre [(spec/valid? boolean? show-expansions?)]}
  (fn [game]
    (if show-expansions?
      (= (:type game) :expansion)
      (not= (:type game) :expansion))))

(defn is-best-with-num-of-players
  [num-players]
  (fn [game]
    (let [best-string ((apply max-key :best-perc (:votes game)) :players)]
      (if (s/includes? best-string "+")
        ; remove '+' from the '4+'. 4+ means more than 4.
        (< num-players (read-string (s/join "" (drop-last best-string))))
        (= num-players (read-string best-string))))))

(comment
  ;;  for linter unused public keyword
  (is-best-with-num-of-players 5)
  ;
  )

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

(comment
  playingtime-between?
  ;
  )

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




(defn max-index
  ([coll]    (max-index coll 0 (apply max coll)))
  ([coll num max] (if (= max (nth coll num))
                    num
                    (max-index coll (inc num) max))))

(defn game->best-rec-not
  "For a game, returns 0 1 2 if best, recommended or not recommended for player-num.
   Returns nil if game has no votes (unfetched game)"
  [game player-num]
  (if (game :votes)
    (if (> player-num (count (game :votes)))
      ;; (function-name game (count (game :votes)))
      nil
      (let [votes-perc (filter number? (vals (nth (game :votes) (dec player-num))))]
        (max-index (map #(nth votes-perc %) [1 3 5]))))
    nil))


(comment

  local-storage-db

  (def wizard (local-storage-db "1465"))
  (def caverna (local-storage-db "102794"))
  (pp/pp)
  (game->best-rec-not caverna 1)
  (rating-for-number-of-players caverna 1)
  (rating-for-number-of-players wizard 10)

  (map #(game->best-rec-not wizard (inc %)) (range 10))
  (map #(rating-for-number-of-players wizard (inc %)) (range 10))

  (pp/pprint (map (fn [n]
                    [(game->best-rec-not caverna (inc n)) (rating-for-number-of-players caverna (inc n))])
                  (range 10)))

   ;
  )
