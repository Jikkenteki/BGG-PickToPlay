(ns bbg-reframe.model.xmlapi
  (:require [clojure.tools.reader.edn :refer [read-string]]
            [tubax.core :refer [xml->clj]]
            [bbg-reframe.model.tag-helpers :refer [find-element-with-tag has-tag? has-attr-with-value?]]
            [bbg-reframe.config :refer [xml-api]]
            [bbg-reframe.model.examples.game :refer [powergrid-xml]]
            [clojure.pprint :as pp]))


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


;; in in the collection
(defn game-id [game]
  (get-in game [:attrs :objectid]))


(defn game-attributes [collection-game]
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
    (let [attr (game-attributes collection-game)
          value (attr key)]
      (if value (read-string value) nil))))

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