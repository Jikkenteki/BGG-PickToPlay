(ns bbg-reframe.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [bbg-reframe.events :as events]
   [bbg-reframe.localstorage.localstorage :refer [init-localstorage]]
   [bbg-reframe.model.localstorage :refer [remove-item!]]
   [bbg-reframe.network-events :as network-events]
   [bbg-reframe.routes :as views]
   [bbg-reframe.router :as routes]
   [bbg-reframe.config :as config]
   [re-frame.loggers :refer [console]]
   [bbg-reframe.panels.login.login-events :as login-events]
   [re-frame-firebase-nine.fb-reframe :refer [set-browser-session-persistence fb-reframe-config connect-emulator]]
   [re-frame-firebase-nine.firebase-auth :refer [get-auth on-auth-state-changed on-auth-state-changed-callback]]))


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
  (fb-reframe-config config/fb-reframe-config-map)

  (connect-emulator)

  (get-auth)
  (set-browser-session-persistence)

  (console :log "Deleting bbg-ui-settings from local storage (Remove me!)")
  (remove-item! "bgg-ui-settings")

  (re-frame/dispatch-sync [::events/initialize-db])
  (routes/start!)

  (re-frame/dispatch [::network-events/cors-check])

  (init-localstorage)

  (re-frame/dispatch [::network-events/update-result])

  ;; auth is not ready
  (on-auth-state-changed (fn [x]
                           (re-frame/dispatch [::login-events/auth-state-changed])
                           (on-auth-state-changed-callback x)))
  (dev-setup)
  (mount-root))

(comment
  (init))
