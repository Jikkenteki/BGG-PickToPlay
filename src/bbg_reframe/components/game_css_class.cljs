(ns bbg-reframe.components.game-css-class
  (:require [goog.string :as gstring]
            [clojure.string :as string]
            [bbg-reframe.model.sort-filter :refer [game->best-rec-not]]))

(defn game-decoration
  [game players available-time]
  (case (game->best-rec-not game players)
    0 "underline decoration-green-500 decoration-2" ;"B"
    1 "";"R "
    2 "" ;"N "
    ""))

(defn game-color
  [game players available-time]
  (let [playing-time (:playingtime game)
        opacity (* (/ available-time playing-time))
        opac-string (gstring/format "%.2f" opacity)]
    (println opacity)
    (str "text-white/["
         (if (<= opacity 1)
        ;;    (core/subs
           opac-string
            ;; 1)
           "0.9")
         "]")
    "text-white/[.28]"
    "text-white"))


(defn game-css-class
  [game players available-time]
  (string/join " "
               [(game-color game players available-time)
                (game-decoration game players available-time)]))

(defn game-icon-players
  [game players available-time]
  (case (game->best-rec-not game players)
    0 [:i.mr-1 {:class "fa-solid fa-user text-green-600"}] ;"B"
    1 [:i.mr-1 {:class "fa-regular fa-user text-green-800"}] ;"R "
    2 [:i.mr-1 {:class "fa-solid fa-user-slash"  :style {:color "Red"}}] ;"N "
    ""))

(defn game-icon-time
  [game players time]
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
  [game players available-time]
  [:span.mr-2 [game-icon-players game players available-time] [game-icon-time game players available-time]] ;"B"
  )
