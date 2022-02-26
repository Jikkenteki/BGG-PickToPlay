(ns bbg-reframe.views
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.events :as events]
   [bbg-reframe.model.sort-filter :refer [rating-for-number-of-players sorting-fun]]
   [goog.string :as gstring]
   [goog.string.format]
   ["sax" :as sax]))


; required for tubax to work
(js/goog.exportSymbol "sax" sax)

(def SHOW_PLAYABILITY true)


(defn game-div
  [game players]
  (let [;_ (println game)
        playability (gstring/format "%.2f" (rating-for-number-of-players
                                            game players))]
    ^{:key (random-uuid)}
    [:p
     (when SHOW_PLAYABILITY (str playability " - " (gstring/format "%.2f" (* playability (:rating game)))  " - "))
     (:name game)]))

(defn result-div
  [result]
  (let [players @(re-frame/subscribe [::subs/form :players])]
    [:div.pl-6.flex-auto.overflow-auto
     (map
      (fn [game]
        (game-div game players))
      result)]))


(defn select
  [id label options]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div
     [:label label]
     [:select {:value @value
               :on-change #(re-frame/dispatch [::events/update-form
                                               id
                                               (-> % .-target .-value)])}
      (map (fn [o] [:option {:key o :value o} o]) options)]]))

(defn custom-select
  [id label options]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.flex
     [:p label]
     [:div.border-2.w-24.ml-2.pl-1.border-orange-900
      [:p @value]]]))

(defn slider
  [id label min max step]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.flex.justify-between.mb-1
     [:label label]
     [:div.flex
      [:input.range.my-auto.mr-2 {:type "range" :min min :max max :step step :value @value :id id
                                  :onChange #(re-frame/dispatch [::events/update-form
                                                                 id
                                                                 (-> % .-target .-value)])}]
      [:span.range-slider-ticket-value @value]]]))

;; (defn fn-queue
;;   []
;;   (let [result (re-frame/subscribe [::subs/result])]
;;     (re-frame/dispatch [::events/update-queue
;;                         @result])))

(defn main-panel []
  (let [result (re-frame/subscribe [::subs/result])
        loading (re-frame/subscribe [::subs/loading])
        error (re-frame/subscribe [::subs/error])]
    [:div.container.p-3.flex.flex-col.h-full.bg-stone-800.text-neutral-200
    ;;  (fn-queue)
     (when @error [:h1 @error])
     [:h1.text-3xl.font-bold.mb-2
      "HMPWTP "
      [:span.text-sm.font-normal "aka 'Help me pick what to play'"]]
     (slider :take "Take" 1 100 1)
     (slider :higher-than "Rating higher than" 0 10 0.1)
     (slider :players "For number of players" 1 10 1)
     (slider :threshold "Playability threshold" 0 0.95 0.05)
     (slider :time-limit "Time limit" 10 500 10)
     [:h3 "Games " (when @loading " loading...")]
     (result-div @result)
     (select :sort-id "Sort by " (map name (keys sorting-fun)))
     (custom-select :sort-id "Sort by " (map name (keys sorting-fun)))
    ;;  [:button {:on-click (re-frame/dispatch [::events/fetch-collection "ddmits"])} "Refetch collection"]
     ]))
