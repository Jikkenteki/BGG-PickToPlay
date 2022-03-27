(ns bbg-reframe.model.db
  (:require [bbg-reframe.model.examples.collection2 :refer [collection-2]]
            [bbg-reframe.model.examples.game2 :refer [powergrid2]]
            [bbg-reframe.model.localstorage :refer [get-item]]
            [bbg-reframe.model.tag-helpers :refer [find-element-with-tag]]
            [bbg-reframe.model.xmlapi :refer [create-votes-for-results
                                              game-attribute game-id game-my-rating
                                              game-name game-ranks game-rating game-thumbnail game-yearpublished
                                              list-results-of-votes-per-playernum]]
            [bbg-reframe.spec.game-spec :as game-spec]
            [cljs.test :refer [is]]
            [clojure.pprint :as pp]
            [clojure.spec.alpha :as s]
            [clojure.string :refer [includes? upper-case]]
            [clojure.tools.reader.edn :refer [read-string]]
            [re-frame-firebase-nine.example.forms.forms :refer [db-get-ref]]
            [re-frame.loggers :refer [console]]
            [tubax.core :refer [xml->clj]]))

(comment
  (->> powergrid2
       (xml->clj)
       (:content)
       (first)
       (find-element-with-tag :averageweight)
       (:attrs)
       (:value)
       (read-string))
  (pp/pp)
  ;
  )

(comment
  (->> collection-2
       (xml->clj)
       (:content)
       (first)
       (find-element-with-tag :yearpublished)
       (:content)
       (first)
       (read-string))
  (pp/pp)

  (->> collection-2
       (xml->clj)
       (:content)
       (first)
       (game-yearpublished))
  (pp/pp)


  ;
  )

;; 
;; API - DB
;; 
(defn  create-game-from-collection-item
  [collection-item]
  {:post [(is (s/valid? ::game-spec/game %))]}
  (try {:id (game-id collection-item)
        :type nil
        :name (game-name collection-item)
        :rating (game-rating collection-item)
        :my-rating (game-my-rating collection-item)
        :minplayers ((game-attribute collection-item) :minplayers)
        :maxplayers ((game-attribute collection-item) :maxplayers)
        :playingtime ((game-attribute collection-item) :playingtime)
        :thumbnail (game-thumbnail collection-item)
        :ranks (game-ranks collection-item)
        :yearpublished (game-yearpublished collection-item)}
       (catch js/Error e
         (console :error (str "Error:" e "\nOn collection-item: " collection-item))
         (throw e))))

(comment
  (->> collection-2
       (xml->clj)
       (:content)
       (first)
       (create-game-from-collection-item))
  (pp/pp)
  ;
  )

(defn game-weight
  [game-item]
  {:post [(is (s/valid? game-spec/non-negative-number? %))]}
  (->> game-item
       (find-element-with-tag :averageweight)
       (:attrs)
       (:value)
       (read-string)))

(defn game-votes
  [game-item]
  {:post [(is (s/valid? ::game-spec/votes %))]}
  (into [] (map create-votes-for-results
                (list-results-of-votes-per-playernum game-item))))

(defn indexed-games
  [response-xml]
  (let [response-clj (xml->clj response-xml)]
    ;; tag is expected to be items; otherwise an error occured
    (if (= :items (:tag response-clj))
      (let [games (map create-game-from-collection-item (:content response-clj))
            indexed-games (reduce
                           #(assoc %1 (:id %2) %2)
                           {} games)]
        indexed-games)
      nil)))

(defn update-games-with-play
  [games play]
  ;; (println games play)
  (let [play-id (:id play)
        game-id (:game-id play)
        game (get games game-id)]
    ;; (println play-id game-id game)
    (if game
      (assoc-in games [game-id :plays play-id] play)
      games)))

(comment

  (def games @(db-get-ref [:games]))
  (get games "276025")
  (:plays (get games "276025"))

  (def play {:date "2021-01-01",
             :game-id "276025",
             :id "48077780",
             :players [{:name "Dimitris Dranidis", :username "ddmits"}
                       {:name "Andreas", :username "adranidis"}]})

  (update-games-with-play games play)

  (def games {"0" {:id "0"}
              "1" {:id "1"}
              "2" {:id "2"}
              "3" {:id "3"}
              "4" {:id "4"}})

  (def play {:id "123"
             :date "2019-05-04",
             :game-id "3",
             :players '({:name "Dimitris Dranidis", :username "ddmits"}
                        {:name "Andreas", :username "adranidis"}
                        {:name "Kostas", :username "dimopoulosk"})})

  (update-games-with-play games play)

   ;
  )

(defn update-plays-in-games
  [games plays]
  (if (empty? plays)
    games
    (update-plays-in-games (update-games-with-play games (first plays)) (rest plays))))



(comment
  (sort (map read-string (keys (read-string (get-item "bgg-games")))))
  (def adranidis-games [1 3 11 12 13 42 50 71 93 118 171 188 215 234 278 372 432 478 555 760 822 1231 1465 2591 2651 2655 2993 3076 3230 4390 6249 6472 8051 8217 9217 9674 10630 10640 12333 12493 12543 14105 14996 15987 17133 18602 20551 21385 21763 21790 22038 22345 24181 24480 25292 25554 25613 27833 28143 28720 29714 29716 30549 31260 34219 34499 34585 34635 35677 36218 37111 39953 40210 40692 40834 43015 43018 43111 45315 51811 54138 63628 66589 70149 72287 73439 84876 96848 96913 102680 107529 109276 120677 121408 121921 122515 124361 126042 126163 128882 131357 144592 144733 146508 146886 148949 154203 154246 155426 156566 157354 157403 157969 160495 161417 161936 161970 163412 163967 164928 167791 169786 171131 171623 172386 175914 176396 176494 176920 181304 182874 183394 188834 193738 196340 210108 213893 216132 228341 229220 229853 236457 246784 247763 276025])
  (def ddmits-games [1 3 11 12 13 42 50 71 93 118 215 234 278 325 372 432 478 555 760 822 926 1465 2389 2448 2591 2651 2655 2993 3076 5405 6249 6472 8051 8217 9209 9216 9217 9625 9674 10630 12333 12493 14105 14996 15987 17133 18602 19600 20551 21385 21763 21790 22038 22279 22664 22766 22821 24181 24270 24480 25292 25554 25613 27833 28143 28720 29714 29715 29716 29717 29718 30549 31260 31784 34499 34585 34635 34887 35677 36218 37111 40210 40692 40834 43015 43018 43111 45315 51811 54138 55697 62219 63628 66188 66589 70149 72125 73439 77130 84876 86246 91873 94480 96613 96848 96913 97842 98778 102680 102794 104006 107529 109276 111426 117914 120677 121408 121921 122515 124361 126042 126163 128621 128882 133632 136223 144592 144733 145219 146508 146886 148949 154203 154246 155426 156061 156566 157096 157354 157403 159507 160495 161417 161936 161970 163967 164928 165022 166372 167355 167791 169786 171623 172386 173346 175914 176396 176494 176920 178044 181304 181796 182874 183394 184267 188834 192661 193738 196340 203624 210108 213893 216132 220308 228341 229220 229853 230802 236457 245655 247763 251247 266507 276025 277018 312484 324856])
  (defn in-ddmits [game] (some #(= game %) ddmits-games))
  (remove (fn [ag] (in-ddmits ag)) adranidis-games)

  (def game-names (map :name (vals (read-string (get-item "bgg-games")))))
  (filter #(includes? % "Pul") game-names)
  (includes? "Power" "owe")


  (def val "P")
  (apply :name (filter (fn [game] (includes? (:name game) val))
                       (vals (read-string (get-item "bgg-games")))))

  (map :name (vals (read-string (get-item "bgg-games"))))

  (->> (vals (read-string (get-item "bgg-games")))
       (mapv :name)
       (filterv (fn [name] (includes? (upper-case name) (upper-case "the")))))

  (upper-case "dssadas")
;
  )
