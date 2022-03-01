(ns bbg-reframe.model.best-with
  (:require [bbg-reframe.model.data :refer [local-storage-db]]
            [clojure.pprint :as pprint]))


(comment

  local-storage-db

  (def games-list (vals local-storage-db))

  (def game (first games-list))
  game
  (pprint/pp)
  (game :votes)

  (def player-num 4)


  (defn function-name
    [game player-num]
    (let [x (filter number? (vals (nth (game :votes) (dec player-num))))
          l {:best (nth x 1) :rec (nth x 3) :not (nth x 5)}]
      l))

  (function-name game player-num)


   ;
  )
