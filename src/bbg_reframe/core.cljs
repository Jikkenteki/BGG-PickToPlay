(ns bbg-reframe.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]

   [bbg-reframe.events :as events]
   [bbg-reframe.views :as views]
   [bbg-reframe.config :as config]
   [bbg-reframe.model.localstorage :refer [item-exists? spit]]
   [bbg-reframe.model.data :refer [local-storage-db]]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (if (item-exists? "ls-games")
    (println "Using local storage data")
    (re-frame/dispatch [::events/fetch-collection "ddmits"]))
  (re-frame/dispatch [::events/initialize-db])
  (re-frame/dispatch [::events/update-form :sort-id "rating"])
  (dev-setup)
  (mount-root))

    ;;  [:button {:on-click #(re-frame/dispatch [::events/fetch-collection "ddmits"])} "Fetch collection and write to Local storage"]
