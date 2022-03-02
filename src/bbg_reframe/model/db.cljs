(ns bbg-reframe.model.db
  (:require [bbg-reframe.model.xmlapi :refer [game-attribute game-id game-name game-rating game-my-rating create-votes-for-results list-results-of-votes-per-playernum]]
            [tubax.core :refer [xml->clj]]))

;; 
;; API - DB
;; 
(defn  create-game-from-collection-item
  [collection-item]
  {:id (game-id collection-item)
   :name (game-name collection-item)
   :rating (game-rating collection-item)
   :my-rating (game-my-rating collection-item)
   :minplayers ((game-attribute collection-item) :minplayers)
   :maxplayers ((game-attribute collection-item) :maxplayers)
   :playingtime ((game-attribute collection-item) :playingtime)})

(defn game-votes
  [game-item]
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

