(ns bbg-reframe.model.db
  (:require [clojure.pprint :as pp]
            [clojure.tools.reader.edn :refer [read-string]]
            [bbg-reframe.model.localstorage :refer [get-item]]
            [bbg-reframe.model.xmlapi :refer [game-attributes game-id
                                              game-name game-rating game-my-rating
                                              votes-best-rating-per-players polls-with-num-of-players-for-game]]
            [re-frame.loggers :refer [console]]
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
  (try {:id (game-id collection-item)
        :type nil
        :name (game-name collection-item)
        :rating (game-rating collection-item)
        :my-rating (game-my-rating collection-item)
        :minplayers ((game-attribute collection-item) :minplayers)
        :maxplayers ((game-attribute collection-item) :maxplayers)
        :playingtime ((game-attribute collection-item) :playingtime)}
       (catch js/Error e
         (console :error (str "Error:" e "\nOn collection-item: " collection-item))
         (throw e))))

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

(comment
  (sort (map read-string (keys (read-string (get-item "bgg-games")))))
  (def adranidis-games [1 3 11 12 13 42 50 71 93 118 171 188 215 234 278 372 432 478 555 760 822 1231 1465 2591 2651 2655 2993 3076 3230 4390 6249 6472 8051 8217 9217 9674 10630 10640 12333 12493 12543 14105 14996 15987 17133 18602 20551 21385 21763 21790 22038 22345 24181 24480 25292 25554 25613 27833 28143 28720 29714 29716 30549 31260 34219 34499 34585 34635 35677 36218 37111 39953 40210 40692 40834 43015 43018 43111 45315 51811 54138 63628 66589 70149 72287 73439 84876 96848 96913 102680 107529 109276 120677 121408 121921 122515 124361 126042 126163 128882 131357 144592 144733 146508 146886 148949 154203 154246 155426 156566 157354 157403 157969 160495 161417 161936 161970 163412 163967 164928 167791 169786 171131 171623 172386 175914 176396 176494 176920 181304 182874 183394 188834 193738 196340 210108 213893 216132 228341 229220 229853 236457 246784 247763 276025])
  (def ddmits-games [1 3 11 12 13 42 50 71 93 118 215 234 278 325 372 432 478 555 760 822 926 1465 2389 2448 2591 2651 2655 2993 3076 5405 6249 6472 8051 8217 9209 9216 9217 9625 9674 10630 12333 12493 14105 14996 15987 17133 18602 19600 20551 21385 21763 21790 22038 22279 22664 22766 22821 24181 24270 24480 25292 25554 25613 27833 28143 28720 29714 29715 29716 29717 29718 30549 31260 31784 34499 34585 34635 34887 35677 36218 37111 40210 40692 40834 43015 43018 43111 45315 51811 54138 55697 62219 63628 66188 66589 70149 72125 73439 77130 84876 86246 91873 94480 96613 96848 96913 97842 98778 102680 102794 104006 107529 109276 111426 117914 120677 121408 121921 122515 124361 126042 126163 128621 128882 133632 136223 144592 144733 145219 146508 146886 148949 154203 154246 155426 156061 156566 157096 157354 157403 159507 160495 161417 161936 161970 163967 164928 165022 166372 167355 167791 169786 171623 172386 173346 175914 176396 176494 176920 178044 181304 181796 182874 183394 184267 188834 192661 193738 196340 203624 210108 213893 216132 220308 228341 229220 229853 230802 236457 245655 247763 251247 266507 276025 277018 312484 324856])
  (defn in-ddmits [game] (some #(= game %) ddmits-games))
  (remove (fn [ag] (in-ddmits ag)) adranidis-games)
;
  )
