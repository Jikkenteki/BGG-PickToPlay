(ns bbg-reframe.panels.home.components.fetch-collection-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.network-events :as network-events]
   [bbg-reframe.events :as events]
   [bbg-reframe.panels.login.login-panel :refer [login-comp]]))

(defn fetch-collection-comp []
  (let [user @(re-frame/subscribe [::subs/user])]
    [:<>
     [:div.flex.items-center.mb-1;.h-full
      [:input.input-box.min-w-0.grow;.h-full
       {:type "text"
        :value user
        :placeholder "Insert BGG username"
        :on-change #(re-frame/dispatch [::events/update-user (-> % .-target .-value)])}]
      [:button.button
       {:disabled (empty? user)
        :on-click #(re-frame/dispatch [::network-events/fetch-collection])} "Fetch collection"]
      [:button.button
       {:disabled (empty? user)
        :on-click #(re-frame/dispatch [::network-events/fetch-plays 1])} "Fetch plays"]]
     [:div.flex.items-center.mb-1;.h-full 
      [login-comp]]]))