(ns bbg-reframe.components.game-css-class
  (:require
   [clojure.string :as string]
   [bbg-reframe.model.sort-filter :refer [game->best-rec-not]]))

(defn game-color
  []
  "text-white")


(defn game-css-class
  [_ _ _]
  (string/join " " [(game-color)]))

(defn game-icon-players
  [game players]
  (case (game->best-rec-not game players)
    0 [:i.mr-2 {:class "fa-solid fa-user text-green-600"}] ;"B"
    1 [:i.mr-2 {:class "fa-regular fa-user text-green-800"}] ;"R "
    2 [:i.mr-2 {:class "fa-solid fa-user-slash text-red-600"}] ;"N "
    ""))

(defn game-icon-time
  [game  time]
  (let [game-time (:playingtime game)
        difference (.abs js/Math (- time game-time))]
    (cond
      (< difference 15)
      [:i.mr-1 {:class "fa-solid fa-clock text-green-600"}]
      (< difference 30)
      [:i.mr-1 {:class (str "fa-regular fa-clock " (if (> time game-time) "text-green-400" "text-orange-400"))}]
      :else
      [:i.mr-1 {:class (str "fa-regular fa-clock " (if (> time game-time) "text-green-900" "text-red-900"))}])))

(defn game-icon-span
  [game players available-time rating]
  [:span.mr-2 [:span {:class "mr-2 text-sm font-bold"} rating] [game-icon-players game players] [game-icon-time game available-time]] ;"B"
  )
