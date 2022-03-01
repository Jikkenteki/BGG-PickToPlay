(ns bbg-reframe.model.db
  (:require [clojure.pprint :as pp]
            [clojure.tools.reader.edn :refer [read-string]]
            [bbg-reframe.model.localstorage :refer [get-item]]
            [bbg-reframe.model.xmlapi :refer [game-attributes game-id
                                              game-name game-rating game-my-rating
                                              votes-best-rating-per-players polls-with-num-of-players-for-game]]
            [tubax.core :refer [xml->clj]]))

(defn- game-attribute [collection-game]
  (fn [key]
    (let [attr (game-attributes collection-game)
          value (attr key)]
      (if value (read-string value) nil))))


;; 
;; API - DB
;; 
(defn  collection-item->game
  [collection-item]
  {:id (game-id collection-item)
   :name (game-name collection-item)
   :rating (game-rating collection-item)
   :my-rating (game-my-rating collection-item)
   :minplayers ((game-attribute collection-item) :minplayers)
   :maxplayers ((game-attribute collection-item) :maxplayers)
   :playingtime ((game-attribute collection-item) :playingtime)})

(defn game-votes
  [game]
  (into [] (map votes-best-rating-per-players
                (polls-with-num-of-players-for-game game))))



(comment
  (reduce #(assoc %1 (:id %2) %2) {} [{:id 1} {:id 2}])
  ;
  )


(defn read-db
  []
  (read-string (get-item "bgg-games")))

;; (def all-fields ["id" "name" "rating" "playability" "playingtime" "minplayers" "maxplayers"])




(comment
  ;; (def collection (g/read-collection-from-file))
  ;; (def games (g/read-games-from-file))
  ;; (make-db-from-collection-and-games collection games)

  (def db (read-db))
  (db "25613")
  (pp/pp)
  (def collection (vals db))

  (take 2 collection)
  ;
  )



(comment
  (-> "some text"
      :tag
      nil?)


  (def xml    "<?xml version= \"1.0\" encoding= \"utf-8\" standalone= \"yes\" ?>
  <errors>
    <error>
      <message>Invalid username specified</message>
    </error>
  </errors>")

  (xml->clj xml)

  (def xml "<?xml version= \"1.0\" encoding= \"utf-8\" standalone= \"yes\" ?>
<message>
  Your request for this collection has been accepted and will be processed.  Please try again later for access.
</message>")

  (xml->clj xml)

;;  status: 202 
;; <?xml version= \"1.0\" encoding= \"utf-8\" standalone= \"yes\" ?>
;; <message>
;;   Your request for this collection has been accepted and will be processed.  Please try again later for access.
;; </message>

  ;; collection's first tag is items
  (def collection-xml
    "<?xml version= \"1.0\" encoding= \"utf-8\" standalone= \"yes\" ?>
     <items totalitems= \"186\" termsofuse= \"https://boardgamegeek.com/xmlapi/termsofuse\" pubdate= \"Tue, 01 Mar 2022 16:12:18 +0000\" >
		    <item objecttype=\"thing\" objectid=\"432\" subtype=\"boardgame\" collid=\"10162387\">
        </item>
     </items>")
  (def response-clj (xml->clj collection-xml))
  (:tag response-clj)
  (:content response-clj)
  ;
  )

(defn indexed-games
  [response-xml]
  (let [response-clj (xml->clj response-xml)]
    (if (= :items (:tag response-clj))
      (let [games (map collection-item->game (:content response-clj))
            indexed-games (reduce
                           #(assoc %1 (:id %2) %2)
                           {} games)]
        indexed-games)
      nil)))

