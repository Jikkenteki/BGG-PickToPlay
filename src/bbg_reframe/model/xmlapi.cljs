(ns bbg-reframe.model.xmlapi
  (:require [clojure.tools.reader.edn :refer [read-string]]
            [tubax.core :refer [xml->clj]]
            [bbg-reframe.model.tag-helpers :refer [find-element-with-tag]]
            [bbg-reframe.config :refer [xml-api]]))

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
    (get-in item [:attrs :type])))


;; in in the collection
(defn game-id [game]
  (get-in game [:attrs :objectid]))


(defn game-attributes [collection-game]
  (->> (find-element-with-tag :stats collection-game)
       :attrs))

(defn game-my-rating [collection-game]
  (let [my-rating (->> (find-element-with-tag :rating collection-game)
                       :attrs
                       :value
                       read-string)]
    (if (number? my-rating) my-rating nil)))

(defn game-rating [collection-game]
  (let [rating (->> (find-element-with-tag :rating collection-game)
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


;; 
;; Functions for numbers of players
;; 
(defn polls-with-num-of-players-for-game [game]
  (let [tag-list (game :content)
        tag-poll (filter (fn [x] (= (x :tag) :poll)) tag-list)
        suggested-numplayers (filter (fn [x] (= (get-in x [:attrs :name]) "suggested_numplayers")) tag-poll)]
    ((first suggested-numplayers) :content)))

(defn votes-best-rating-per-players [poll-results]
  (let [players (get-in poll-results [:attrs :numplayers])
        total-votes (apply + (map (fn [x] (read-string (get-in x [:attrs :numvotes]))) (poll-results :content)))
        best-votes (read-string (get-in (first (poll-results :content)) [:attrs :numvotes]))
        best-perc (if (= 0 total-votes) 0 (/ best-votes total-votes))

        recommended-votes (read-string (get-in (second (poll-results :content)) [:attrs :numvotes]))
        recommended-perc (if (= 0 total-votes) 0 (/ recommended-votes total-votes))
        not-recommended-votes (read-string (get-in (last (poll-results :content)) [:attrs :numvotes]))
        not-recommended-perc (if (= 0 total-votes) 0 (/ not-recommended-votes total-votes))]
    {:players players
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