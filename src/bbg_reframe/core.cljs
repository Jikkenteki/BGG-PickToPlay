(ns bbg-reframe.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]

   [bbg-reframe.events :as events]
   [bbg-reframe.network-events :as network-events]

   [bbg-reframe.views :as views]
  ;;  [bbg-reframe.test-firebase.views :as views]

   [bbg-reframe.config :as config]
   [bbg-reframe.model.localstorage :refer [item-exists? get-item remove-item!]]
   [clojure.tools.reader.edn :refer [read-string]]
   [re-frame.loggers :refer [console]]

   [bbg-reframe.test-firebase.events :as fb-events]
   [bbg-reframe.test-firebase.firebase.firebase-app :refer [init-app]]
   [bbg-reframe.test-firebase.firebase.firebase-auth :refer [get-auth]]
   [bbg-reframe.test-firebase.firebase.fb-reframe :refer [set-browser-session-persistence fb-reframe-config]]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
    ;; at the beginning so that they are loaded first
  (init-app)
  (get-auth)
  (set-browser-session-persistence)
  ;; set the path in the db for the fb temp storage
  ;; and returning maps instead of lists
  (fb-reframe-config {:temp-path [::fb-events/fire-base-temp-storage]})



  (console :log "Deleting bbg-ui-settings from local storage (Remove me!)")
  (remove-item! "bgg-ui-settings")


  (re-frame/dispatch [::events/initialize-db])
  (re-frame/dispatch [::network-events/cors-check])
  (when (and (item-exists? "bgg-user") (item-exists? "bgg-games"))
    (console :log "Using local storage data")
    (re-frame/dispatch [::events/update-user (get-item "bgg-user")])
    (re-frame/dispatch [::events/update-games (read-string (get-item "bgg-games"))]))
  (when (item-exists? "bgg-ui-settings")
    (re-frame/dispatch [::events/update-ui-settings (read-string (get-item "bgg-ui-settings"))]))
  (re-frame/dispatch [::network-events/update-result])

    ;; poll for a signed-in user for 2 seconds
  ;; auth is not ready
  (re-frame/dispatch [::fb-events/poll-user 10000])

  (dev-setup)
  (mount-root))

(comment
  (remove-item! "bgg-ui-settings")
  (init))
