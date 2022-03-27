(ns bbg-reframe.model.xmlapi
  (:require [bbg-reframe.config :refer [xml-api]]
            [bbg-reframe.model.examples.game :refer [powergrid-xml]]
            [bbg-reframe.model.examples.plays :refer [plays-page-1]]
            [bbg-reframe.model.tag-helpers :refer [find-element-with-tag
                                                   has-attr-with-value? has-tag?]]
            [clojure.pprint :as pp]
            [clojure.tools.reader.edn :refer [read-string]]
            [re-frame-firebase-nine.example.forms.forms :refer [db-get-ref]]
            [tubax.core :refer [xml->clj]]))


;; 
;; Fields accessors from API XML
;; 
;; ;; id in the game XMLAPI2
;; (defn item-game-id [item]
;;   (get-in item [:attrs :id]))

;; id in the game XMLAPI
(defn item-game-id [item]
  (if (= xml-api 1)
    (get-in item [:attrs :objectid])
    (get-in item [:attrs :id])))

;; type in the game XMLAPI2
(defn item-game-type [item]
  (if (= xml-api 1)
    nil
    (let [type-string (get-in item [:attrs :type])]
      (case type-string
        "boardgame" :boardgame
        "boardgameexpansion" :expansion
        :boardgame))))


;; in in the collection
(defn game-id [game]
  (get-in game [:attrs :objectid]))


(defn game-stats-attributes [collection-game]
  (->> (find-element-with-tag :stats collection-game)
       :attrs))

(defn game-my-rating [collection-game]
  (let [my-rating (->> collection-game
                       (find-element-with-tag :rating)
                       :attrs
                       :value
                       read-string)]
    (if (number? my-rating) my-rating nil)))

(defn game-rating [collection-game]
  (let [rating (->> collection-game
                    (find-element-with-tag :rating)
                    (find-element-with-tag :average)
                    :attrs
                    :value
                    read-string)]
    (if (number? rating) rating nil)))


(defn game-name [collection-game]
  (-> collection-game
      :content
      first
      :content
      first))


(defn game-attribute [collection-game]
  (fn [key]
    (let [attr (game-stats-attributes collection-game)
          value (attr key)]
      (if value (read-string value) nil))))


(defn game-thumbnail
  [collection-game]
  (->> collection-game
       (find-element-with-tag :thumbnail)
       (:content)
       (first)))

(defn game-ranks
  [collection-game]
  (->> collection-game
       (find-element-with-tag :ranks)
       (:content)
       (map (fn [rank]
              {:friendlyname (get-in rank [:attrs :friendlyname])
               :value
               (let [value (get-in rank [:attrs :value])]
                 (if (= value "Not Ranked")
                   nil
                   (read-string value)))}))
       (into [])))

(defn game-yearpublished
  [collection-game]
  (->> collection-game
       (find-element-with-tag :yearpublished)
       (:content)
       (first)
       (read-string)))

;; 
;; Functions for numbers of players
;; 
(defn list-results-of-votes-per-playernum
  "Returns a list of results tagged elements, one for each number of players
   with the votes for the game."
  [game]
  (->> game
       :content
       (filter (has-tag? :poll))
       (filter (has-attr-with-value? :name "suggested_numplayers"))
       first
       :content))

(comment
  powergrid-xml
  (def powergrid (first (:content (xml->clj powergrid-xml))))
  (pp/pprint powergrid)

  (list-results-of-votes-per-playernum powergrid)
  (pp/pp)
  ;
  )

(defn create-votes-for-results
  "Creates a map with the votes and percentages for an element
   of the list of results"
  [poll-results]
  (let [votes (map #(read-string (get-in (nth (poll-results :content) %) [:attrs :numvotes])) (range 3))
        total-votes (apply + votes)
        [best-votes recommended-votes not-recommended-votes] votes
        [best-perc recommended-perc not-recommended-perc] (if (= 0 total-votes)
                                                            [0 0 0]
                                                            (map #(/ % total-votes) votes))]
    {:players (get-in poll-results [:attrs :numplayers])
     :best-votes best-votes :best-perc best-perc
     :recommended-votes recommended-votes :recommended-perc recommended-perc
     :not-recommended-votes not-recommended-votes :not-recommended-perc not-recommended-perc}))

;; 
;; Collection API 
;; 

;;
;; BGG XML response
;;
(defn xml->game
  [response]
  (->> response
       xml->clj
       :content
       first))

;;
;; BGG XML plays
;;
(defn clj->plays
  [clj]
  (clj :content))

(defn clj->total-games
  [clj]
  (->> clj :attrs :total))

(defn clj->page
  [clj]
  (->> clj :attrs :page read-string))

(defn play-date
  [a-play]
  (->> a-play :attrs :date))

(defn play-objectid
  [a-play]
  (->> a-play :content first :attrs :objectid))

(defn play-id
  [a-play]
  (->> a-play :attrs :id))

(defn- play-players
  [a-play]
  (->> a-play :content second :content))

(defn- play-player-username
  [a-player]
  (->> a-player :attrs :username))

(defn- play-player-name
  [a-player]
  (->> a-player :attrs :name))

(defn- play-player->player
  [a-player]
  {:name (->> a-player :attrs :name)
   :username (->> a-player :attrs :username)})

(defn play->play
  [a-play]
  {:id (play-id a-play)
   :game-id (play-objectid a-play)
   :date (play-date a-play)
   :players (into [] (map play-player->player (->> a-play play-players)))})

(comment



  (->> plays-page-1
       xml->clj
       clj->plays
       first
       play->play)



  ;
  )





