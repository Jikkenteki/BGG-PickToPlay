(ns bbg-reframe.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [bbg-reframe.events :as events]
   [bbg-reframe.views :as views]
   [bbg-reframe.config :as config]

   [bbg-reframe.model.db :refer [local-storage-db]]
   [bbg-reframe.model.localstorage :refer [set-item!]]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (set-item! "resources/db.clj" (str local-storage-db))
  (re-frame/dispatch [::events/initialize-db])
  (dev-setup)
  (mount-root))
