(ns bbg-reframe.game-view.views
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.subs :as subs]
            [bbg-reframe.game-view.subs :as game-view-subs]
            [bbg-reframe.test-firebase.subs :as fb-subs]
            [bbg-reframe.test-firebase.events :as events]))

(defn game-view-panel []
  (let [route-params @(re-frame/subscribe [::subs/route-params 1])
        game @(re-frame/subscribe [::game-view-subs/game (:id route-params)])]
    [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
     [:h1 (:name game)]
     [:h2 (:rating game)]

     (let [id (:id game)
           available (re-frame/subscribe [::fb-subs/available id])
           in-box (re-frame/subscribe [::fb-subs/group-with id])]
       [:div
        (str id) " available: " (str @available)
    ;;  (if-not (nil? @available) (str @available) "null")
        " in box: "
        (if-not (nil? @in-box) (str @in-box) "null")
        [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/update-available id true])} "Make av"]
        [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/update-available id nil])} "Make non-av"]
        [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/update-group-with id (str (rand-int 999))])} "Random box"]
        [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/update-group-with id nil])} "Remove from box"]])]))