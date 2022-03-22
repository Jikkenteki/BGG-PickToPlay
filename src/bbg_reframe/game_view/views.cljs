(ns bbg-reframe.game-view.views
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.subs :as subs]
            [bbg-reframe.game-view.subs :as game-view-subs]
            [bbg-reframe.game-view.events :as game-view-events]
            [bbg-reframe.forms.forms :refer [input dropdown-search   db-get-ref]]
            [bbg-reframe.components.nav-bar-comp :refer [naive-nav-bar]]))

(defn game-view-panel
  []
  (let [route-params @(re-frame/subscribe [::subs/route-params 1])
        game @(re-frame/subscribe [::game-view-subs/game (:id route-params)])
        id (:id game)
        _ @(re-frame/subscribe [::game-view-subs/available id])
        _ @(re-frame/subscribe [::game-view-subs/group-with id])]

    [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
     [naive-nav-bar]
     [:h1 (:name game)]
     [:h1 [:b (:id game)]]
     [:h2 (:rating game)]

     [input {:label "Available" :type :checkbox :path [:game-form :available  (str id)]}]
     [:div
      [:label "Group Item with"]
      (dropdown-search {:db-path [:game-form :group-with (str id)]
                        :options (vals @(db-get-ref [:games]))
                        :id-keyword :id
                        :display-keyword :name
                        :button-text-empty "Click to select a game"
                        :input-placeholder "Type to find a game"
                        :select-nothing-text "(no selection)"
                        :sort? true})]

     [:button.button.min-w-fit.px-2.ml-1 {:on-click (fn [_]
                                                      ;;  (db-set-value! [:game-form :add-to-collection (str id)] nil)
                                                      (re-frame/dispatch [::game-view-events/save-game id]))} "Save"]]))
