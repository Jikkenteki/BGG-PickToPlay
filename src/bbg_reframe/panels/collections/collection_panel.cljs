(ns bbg-reframe.panels.collections.collection-panel
  (:require [bbg-reframe.forms.forms :refer [input-element]]
            [bbg-reframe.panels.collections.collections-events :as collections-events]
            [bbg-reframe.panels.collections.collections-subs :as collection-subs]
            [bbg-reframe.panels.collections.components.game-in-collection :refer [game-in-collection]]
            [bbg-reframe.panels.home.components.search-comp :refer [search-games-query-comp search-games-results-comp]]
            [bbg-reframe.panels.home.components.search-comp-events :as search-comp-events]
            [bbg-reframe.subs :as subs]
            [re-frame-firebase-nine.example.forms.bind :refer [bind-form-to-value!]]
            [re-frame.core :as re-frame]
            [reagent.core :as r]))

(def state (r/atom {:collection-renaming? false
                    :games-searching? false}))

(defn collection-view-panel
  []
  (let [_ (re-frame/dispatch [::search-comp-events/reset-search])
        route-params @(re-frame/subscribe [::subs/route-params 1])
        id (:id route-params)
        collection (re-frame/subscribe [::collection-subs/collection id])
        collection-games (re-frame/subscribe [::collection-subs/collection-games (name id)])
        form-path (bind-form-to-value!
                   {:new-name (:name @collection)}
                   [:collection-form :edit-collection])]
    [:div.flex.flex-col.gap-2.p-3.h-full
     [:div.flex.justify-between.items-center.gap-2.pb-2
      (if (:collection-renaming? @state)
        [:div.flex.gap-2.min-w-0
         [input-element {:class "input-box"
                         :type :text
                         :placeholder "Collection name"
                         :path (into form-path [:new-name])
                         :auto-focus? true}]
         [:button.button {:on-click #(do (re-frame/dispatch
                                          [::collections-events/edit-collection-name [(keyword id) (into form-path [:new-name])]])
                                         (reset! state {:collection-renaming? false}))}
          [:i.fa-solid.fa-check]]]
        [:h1
         [:i.mr-1.font-normal.fa.fa-sm.fa-object-group] (:name @collection)])

      [:div.flex.gap-2
       [:button.button.text-slate-950.bg-orange-400 {:on-click
                                                     #(re-frame/dispatch
                                                       [::collections-events/delete-collection (keyword id)])}
        "Delete"]
       [:button.button {:on-click
                        #(swap! state assoc-in
                                [:collection-renaming?]
                                (not (:collection-renaming? @state)))}
        (if (:collection-renaming? @state) "Cancel rename" "Rename")]]]

     [:div.flex.justify-between.items-center
      [:h2 "Games in collection"]
      [:button.button {:on-click #(swap! state update-in [:games-searching?] not)}
       "Add Game to collection"]]

     [:div.flex-1.overflow-auto.relative
      (when (get @state :games-searching?)
        [:div.absolute.flex.flex-col.h-full.w-full
         [search-games-query-comp]
         [search-games-results-comp (fn [game-id] (re-frame/dispatch [::collections-events/add-game-to-collection [(keyword id) game-id]]))]])
      (map #(game-in-collection id %) @collection-games)]]))


(comment
  (def state (r/atom {:collection-renaming? false
                      :games-searching? false}))
  (swap! state update-in [:games-searching?] not)
  @state
  ;;
  )
